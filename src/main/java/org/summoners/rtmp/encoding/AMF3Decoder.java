package org.summoners.rtmp.encoding;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLSocket;
import javax.xml.bind.DatatypeConverter;

import org.summoners.rtmp.*;

/**
 * Decodes AMF3 data and packets from RTMP
 * 
 * @author Gabriel Van Eyck
 */
public class AMF3Decoder {
    /** Stores the data to be consumed while decoding */
    private byte[] dataBuffer;
    private int dataPos;

    /** Lists of references and class definitions seen so far */
    private List<String> stringReferences = new ArrayList<>();
    private List<Object> objectReferences = new ArrayList<>();
    private List<ClassDefinition> classDefinitions = new ArrayList<>();

    /**
     * Reads RTMP packets from a stream
     */
   static class RTMPPacketReader {
        /** The stream to read from */
        private BufferedInputStream in;

        /** The AMF3 decoder */
        private final AMF3Decoder adc = new AMF3Decoder();

        /**
         * Starts a packet reader on the given stream
         * 
         * @param stream The stream to read packets from
         */
        public RTMPPacketReader(InputStream stream) {
            this.in = new BufferedInputStream(stream, 16384);

            Thread curThread = new Thread() {
                @Override
				public void run() {
                    parsePackets(this);
                }
            };
            curThread.setName("RTMPSClient (PacketReader)");
            curThread.setDaemon(true);
            curThread.start();
        }

        private byte readByte(InputStream in) throws IOException {
            byte ret = (byte)in.read();
            // System.out.println(String.format("%02X", ret));
            return ret;
        }

        /**
         * The main loop for the packet reader
         */
        private void parsePackets(Thread thread) {
            try {
                Map<Integer, Packet> packets = new HashMap<>();

                while (true) {
                    // Parse the basic header
                	byte basicHeader = 0;
                	try {
                    basicHeader = readByte(in);
                	} catch(Exception e) {
                		e.printStackTrace();
                	}
                    System.out.println(basicHeader);

                    int channel = basicHeader & 0x2F;
                    int headerType = basicHeader & 0xC0;

                    int headerSize = 0;
                    if (headerType == 0x00)
                        headerSize = 12;
                    else if (headerType == 0x40)
                        headerSize = 8;
                    else if (headerType == 0x80)
                        headerSize = 4;
                    else if (headerType == 0xC0)
                        headerSize = 1;

                    // Retrieve the packet or make a new one
                    if (!packets.containsKey(channel))
                        packets.put(channel, new Packet());
                    Packet p = packets.get(channel);

                    // Parse the full header
                    if (headerSize > 1) {
                        byte[] header = new byte[headerSize - 1];
                        for (int i = 0; i < header.length; i++)
                            header[i] = readByte(in);

                        if (headerSize >= 8) {
                            int size = 0;
                            for (int i = 3; i < 6; i++)
                                size = size * 256 + (header[i] & 0xFF);
                            p.setSize(size);

                            p.setType(header[6]);
                        }
                    }

                    // Read rest of packet
                    for (int i = 0; i < 128; i++) {
                        byte b = readByte(in);
                        p.add(b);

                        if (p.isComplete())
                            break;
                    }

                    // Continue reading if we didn't complete a packet
                    if (!p.isComplete())
                        continue;

                    // Remove the read packet
                    packets.remove(channel);

                    // Decode result
                    final TypedObject result;
                    if (p.getType() == 0x14) // Connect
                        result = adc.decodeConnect(p.getData());
                    else if (p.getType() == 0x11) // Invoke
                        result = adc.decodeInvoke(p.getData());
                    else if (p.getType() == 0x06) // Set peer bandwidth
                    {
                        byte[] data = p.getData();
                        int windowSize = 0;
                        for (int i = 0; i < 4; i++)
                            windowSize = windowSize * 256 + (data[i] & 0xFF);
                        int type = data[4];
                        continue;
                    }
                    else if (p.getType() == 0x03) // Ack
                    {
                        byte[] data = p.getData();
                        int ackSize = 0;
                        for (int i = 0; i < 4; i++)
                            ackSize = ackSize * 256 + (data[i] & 0xFF);
                        continue;
                    }
                    else
                    // Skip most messages
                    {
                        System.out.println("Unrecognized message type");
                        System.out.print(String.format("%02X ", p.getType()));
                        for (byte b : p.getData())
                            System.out.print(String.format("%02X", b & 0xff));
                        System.out.println();
                        continue;
                    }

                    // Store result
                    Integer id = result.getInt("invokeId");
                }
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }
   protected static Random rand = new Random();
    
    public static void main(String[] args) throws EncodingException, NotImplementedException {
        SSLSocket sslsocket;
		/*try {
			sslsocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("216.133.234.22", 2099);
			BufferedInputStream in = new BufferedInputStream(sslsocket.getInputStream());
			DataOutputStream out = new DataOutputStream(sslsocket.getOutputStream());
	        byte C0 = 0x03;
	        out.write(C0);

	        // C1
	        long timestampC1 = System.currentTimeMillis();
	        byte[] randC1 = new byte[1528];
	        rand.nextBytes(randC1);

	        out.writeInt((int)timestampC1);
	        out.writeInt(0);
	        out.write(randC1, 0, 1528);
	        out.flush();

	        // S0
	        byte S0 = (byte)in.read();
	        if (S0 != 0x03)
	            throw new IOException("Server returned incorrect version in handshake: " + S0);

	        // S1
	        byte[] S1 = new byte[1536];
	        in.read(S1, 0, 1536);

	        // C2
	        long timestampS1 = System.currentTimeMillis();
	        out.write(S1, 0, 4);
	        out.writeInt((int)timestampS1);
	        out.write(S1, 8, 1528);
	        out.flush();

	        // S2
	        byte[] S2 = new byte[1536];
	        for (int i = 0; i < S2.length; i++)
	            S2[i] = (byte)in.read();
	        // in.read(S2, 0, 1536);

	        // Validate handshake
	        boolean valid = true;
	        for (int i = 8; i < 1536; i++) {
	            if (randC1[i - 8] != S2[i]) {
	                valid = false;
	                break;
	            }
	        }

	        if (!valid)
	            throw new IOException("Server returned invalid handshake");
			RTMPPacketReader reader = new RTMPPacketReader(in);
			new InputStream() {
				
				@Override
				public int read() throws IOException {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			while(true) {
				Thread.sleep(10);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	AMF3Decoder dec = new AMF3Decoder();
    	String todec = "";
    	//bc5ff455a3407444016d767308004500058c1efa400037069c1fd885ea16c0a8000e0833cd08d9277a0e2647a745501001f66c2f0000fac3d4c8be6cc9fe2ae4bfce2a30400a729d1d36e248dff69b637bf56837dd4082fdbd20f6637dfb0ff8a76c0b8bfc03334c564d72cc91709f21b4a70c4dd1373952da58df095466bfd1c1c4e7d5d66b2301334060692ab3b7b9de4e3a40bb9bf7a92705134c2cb9f0db1a7c717c3b88110c6b59d1c84111c9fd314c0f85368204b42f60be8d2334320a5381575d42ae21557eb5f228f6f6953234c62fc65836ed695f7a615c6eee5b56f7be997f2954bf8c6567ba93b3e1b311ef0b96a186455c5d614c6f20aeb99fa0a22261abef9baad2aa6f095ab788564eb1b9c25021855ebd7cbfefcfe660f6024149e68daa5569a344e7bce5e26180bec1c89cba054df55b914eccffbaa5b54c21c8102959bd621daaebe77cc5ee6852b27a48270bd4760a553c05d26e0bc0283753c885a79c40c407648de7e6281f629c48e81a04b11497aaaa355686e53b44def9dd943bdb9805a0ec44a1278aaacbc7a606f88332c141a0cec81f829beebb35e6c34f54d275f2ef35d790ea81ac3c6225c0b7480d0623ba0667f978d647615329d0329c1be5face680a7f48e8b166ac3213955c39e719a778e5aaebc3ca1386f0ac009bab20adcc093d32537287134a54db63746a499b61f8118d30398e4e7d5bdb68ff15402618b62f3a37defa4d66e573f3eabea185e3461dbe629ec619f6e5839d02e3a824ec0e8d72a6aff4de38d1590ceeb6d5ebaf0bc47ea50f8f57580975d78114d501da1678b4a68c7adfa5544715f631a5f18dcf11974296f5aaf01a4389335b7e538694595db07d852d71982a4d2d233f59e380272329e540babdda6d7c1ac3c66a7e56052ef8d329c9448d9f65ffe0265114de66d5c5e0045ab56d1722a64731f46bd63e72e6aac22faaf098b9df4453fa19bc91b21d8cf7595f23e0524d5023e07b36a4179ec275aac69c5d2e50a62a3112cb104ef61136279a59ccb9a1a97cb66f3aa52d863b4f85f4f0972ab7e88464b281bfa4d0890280b7f6c946a3758f5699593a25193b9e22a4bcbb7a7cc8b5847e0c347cb5f5dc68b73349c3055e2e4660f72ac0309146c72ee3d77d8bc6526d84cabb1a9afc1643317aae43a8997d83bc8382a789e0b46400b6f9460974a16717f898a838fc5a0cba2efb7980a40eccbac59f66e8b789d4eee26b8adc32880473c807fe36fa233c2ec6f1b5ddb50e4b8807217b70e4f8ca2b73a105dfbba994ed5686f1b3ae51f9c0dfacaa0e4844ceebc604a43b92d35c706f71548a71465ec268138eaa50108ecb3843c8f7c9210f2ccf8a6c4f9ab45341bee8adec1173680dffec4e3d95cda8ea4ed902dde7c2e57dc714963a2d1ec3d4c631afc9ebe16b2f7f36365348c36aa8a704e3f27bea8e4f3484bd96ee36ca8a13812e624f9ea44f74d20259db989ba438d29e8b6151bd07df1a6910e1c407b7691757b9ffc02731c478903b44a0f36fcfaa29dea169aa5d7208ceaa1ccbc2909597826df2fb61cd0894816a95bc5365c8b363dd2b442d8ec9f6a82e58bc6eac023b78f8f80ebe9fb6c952f09110fce6696adb61a7769ff238dd000f0adef79e77859cf5f57944a969c0194806dc09996a55b6f29e9f98c88fa2039d1447ee2dfcc943d95d4099c90af5304b9ba7a58b4c9a116a06cc51c304b0ded0f9a8f6c597950f403190d3dc2b512488fcab8284d819b57b469656238df8af3b9ab86958edd0eb8666d31e7566be7c080c16f89297cbc5d214a2533c5fcdb88f40ba679cf2e14d55f458ff2935b179da32ba9a1020c61c62280e5cea896f3bf25dfe1262abe3b43224e64e0c23e3ddbd989e93886902993979e85f32b9a7507a6cbca35c76c92ef38b5edf7871472910a0c5742b7ed33cfdeada3e702d0849966062ce7934dbd74b73c8ac3f031c405ddf43e708b81e8145a2958d014aa8e0a6a8494043a062b17d55a5e48548

        // Parse the basic header
    	byte[] pack = DatatypeConverter.parseHexBinary(todec);
    	ByteBuffer buffer = ByteBuffer.wrap(pack);
        byte basicHeader = buffer.get();
        System.out.println(basicHeader);
        int channel = basicHeader & 0x2F;
        System.out.println(channel);
        int headerType = basicHeader & 0xC0;
        System.out.println(headerType);

        int headerSize = 0;
        if (headerType == 0x00)
            headerSize = 12;
        else if (headerType == 0x40)
            headerSize = 8;
        else if (headerType == 0x80)
            headerSize = 4;
        else if (headerType == 0xC0)
            headerSize = 1;
        System.out.println("headerSize " + headerSize);

        HashMap<Integer, Packet> packets = new HashMap<Integer, Packet>();
		// Retrieve the packet or make a new one
        while(true) {
	        if (!packets.containsKey(channel))
	            packets.put(channel, new Packet());
	        Packet p = packets.get(channel);
	
	        // Parse the full header
	        if (headerSize > 1) {
	            byte[] header = new byte[headerSize - 1];
	            for (int i = 0; i < header.length; i++) {
	                header[i] = buffer.get();
	            	System.out.println(header[i]);
	            }
	
	            if (headerSize >= 8) {
	                int size = 0;
	                for (int i = 3; i < 6; i++)
	                    size = size * 256 + (header[i] & 0xFF);
	                p.setSize(size);
	
	                p.setType(header[6]);
	            }
	        }
	
	        // Read rest of packet
	        for (int i = 0; i < 128; i++) {
	            byte b = buffer.get();
	            p.add(b);
	
	            if (p.isComplete())
	                break;
	        }
            if (!p.isComplete())
                continue;

            System.out.println("headerSize22 " + headerSize);
	
	        // Remove the read packet
	        packets.remove(channel);
	
	        // Decode result
	        final TypedObject result;
	        if (p.getType() == 0x14) // Connect
	            result = dec.decodeConnect(p.getData());
	        else if (p.getType() == 0x11) // Invoke
	            result = dec.decodeInvoke(p.getData());
	        else if (p.getType() == 0x06) // Set peer bandwidth
	        {
	            byte[] data = p.getData();
	            int windowSize = 0;
	            for (int i = 0; i < 4; i++)
	                windowSize = windowSize * 256 + (data[i] & 0xFF);
	            int type = data[4];
                continue;
	        }
	        else if (p.getType() == 0x03) // Ack
	        {
	            byte[] data = p.getData();
	            int ackSize = 0;
	            for (int i = 0; i < 4; i++)
	                ackSize = ackSize * 256 + (data[i] & 0xFF);
                continue;
	        }
	        else
	        // Skip most messages
	        {
	            System.out.println("Unrecognized message type");
	            System.out.print(String.format("%02X ", p.getType()));
	            for (byte b : p.getData())
	                System.out.print(String.format("%02X", b & 0xff));
	            System.out.println();
                continue;
	        }
        }
    }
    
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    /**
     * Resets all the reference lists
     */
    public void reset() {
        stringReferences.clear();
        objectReferences.clear();
        classDefinitions.clear();
    }

    /**
     * Decodes the result of a connect call
     * 
     * @param data The connect result
     * @return The decoded object
     * @throws EncodingException
     * @throws NotImplementedException
     */
    public TypedObject decodeConnect(byte[] data) throws NotImplementedException, EncodingException {
        reset();

        dataBuffer = data;
        dataPos = 0;

        TypedObject result = new TypedObject("Invoke");
        result.put("result", decodeAMF0());
        result.put("invokeId", decodeAMF0());
        result.put("serviceCall", decodeAMF0());
        result.put("data", decodeAMF0());

        if (dataPos != dataBuffer.length)
            throw new EncodingException("Did not consume entire buffer: " + dataPos + " of " + dataBuffer.length);

        return result;
    }

    /**
     * Decodes the result of a invoke call
     * 
     * @param data The invoke result
     * @return The decoded object
     * @throws EncodingException
     * @throws NotImplementedException
     */
    public TypedObject decodeInvoke(byte[] data) throws NotImplementedException, EncodingException {
        reset();

        dataBuffer = data;
        dataPos = 0;

        TypedObject result = new TypedObject("Invoke");
        if (dataBuffer[0] == 0x00) {
            dataPos++;
            result.put("version", 0x00);
        }
        result.put("result", decodeAMF0());
        result.put("invokeId", decodeAMF0());
        result.put("serviceCall", decodeAMF0());
        result.put("data", decodeAMF0());

        if (dataPos != dataBuffer.length)
            throw new EncodingException("Did not consume entire buffer: " + dataPos + " of " + dataBuffer.length);

        return result;
    }

    /**
     * Decodes data according to AMF3
     * 
     * @param data The data to decode
     * @return The decoded object
     * @throws NotImplementedException
     * @throws EncodingException
     */
    public Object decode(byte[] data) throws EncodingException, NotImplementedException {
        dataBuffer = data;
        dataPos = 0;

        Object result = decode();

        if (dataPos != dataBuffer.length)
            throw new EncodingException("Did not consume entire buffer: " + dataPos + " of " + dataBuffer.length);

        return result;
    }

    /**
     * Decodes AMF3 data in the buffer
     * 
     * @return The decoded object
     * @throws EncodingException
     * @throws NotImplementedException
     */
    private Object decode() throws EncodingException, NotImplementedException {
        byte type = readByte();
        switch (type) {
        case 0x00:
            throw new EncodingException("Undefined data type");

        case 0x01:
            return null;

        case 0x02:
            return false;

        case 0x03:
            return true;

        case 0x04:
            return readInt();

        case 0x05:
            return readDouble();

        case 0x06:
            return readString();

        case 0x07:
            return readXML();

        case 0x08:
            return readDate();

        case 0x09:
            return readArray();

        case 0x0A:
            return readObject();

        case 0x0B:
            return readXMLString();

        case 0x0C:
            return readByteArray();
        }

        throw new EncodingException("Unexpected AMF3 data type: " + type);
    }

    /**
     * Removes a single byte from the data buffer
     * 
     * @return The next byte in the data buffer
     */
    private byte readByte() {
        byte ret = dataBuffer[dataPos];
        dataPos++;
        return ret;
    }

    /**
     * Removes a single byte from the data buffer as an unsigned integer
     * 
     * @return The next byte in the data buffer as an unsigned integer
     */
    private int readByteAsInt() {
        int ret = readByte();
        if (ret < 0)
            ret += 256;
        return ret;
    }

    /**
     * Removes the next 'length' bytes from the data buffer
     * 
     * @param length The number of bytes to retrieve
     * @return The next 'length' bytes in the data buffer
     */
    private byte[] readBytes(int length) {
        byte[] ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = dataBuffer[dataPos];
            dataPos++;
        }
        return ret;
    }

    /**
     * Decodes an AMF3 integer
     * 
     * @return The decoded integer
     * @author FluorineFX
     */
    private int readInt() {
        int ret = readByteAsInt();
        int tmp;

        if (ret < 128) {
            return ret;
        }
        else {
            ret = (ret & 0x7f) << 7;
            tmp = readByteAsInt();
            if (tmp < 128) {
                ret = ret | tmp;
            }
            else {
                ret = (ret | tmp & 0x7f) << 7;
                tmp = readByteAsInt();
                if (tmp < 128) {
                    ret = ret | tmp;
                }
                else {
                    ret = (ret | tmp & 0x7f) << 8;
                    tmp = readByteAsInt();
                    ret = ret | tmp;
                }
            }
        }

        // Sign extend
        int mask = 1 << 28;
        int r = -(ret & mask) | ret;
        return r;
    }

    /**
     * Decodes an AMF3 double
     * 
     * @return The decoded double
     */
    private double readDouble() {
        long value = 0;
        for (int i = 0; i < 8; i++)
            value = (value << 8) + readByteAsInt();

        return Double.longBitsToDouble(value);
    }

    /**
     * Decodes an AMF3 string
     * 
     * @return The decoded string
     * @throws EncodingException
     */
    private String readString() throws EncodingException {
        int handle = readInt();
        boolean inline = ((handle & 1) != 0);
        handle = handle >> 1;

        if (inline) {
            if (handle == 0)
                return "";

            byte[] data = readBytes(handle);

            String str;
            try {
                str = new String(data, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                throw new EncodingException("Error parsing AMF3 string from " + data);
            }

            stringReferences.add(str);

            return str;
        }
        else {
            return stringReferences.get(handle);
        }
    }

    /**
     * Not implemented
     * 
     * @return
     * @throws NotImplementedException
     */
    private String readXML() throws NotImplementedException {
        throw new NotImplementedException("Reading of XML is not implemented");
    }

    /**
     * Decodes an AMF3 date
     * 
     * @return The decoded date
     */
    private Date readDate() {
        int handle = readInt();
        boolean inline = ((handle & 1) != 0);
        handle = handle >> 1;

        if (inline) {
            long ms = (long)readDouble();
            Date d = new Date(ms);

            objectReferences.add(d);

            return d;
        }
        else {
            return (Date)objectReferences.get(handle);
        }
    }

    /**
     * Decodes an AMF3 (non-associative) array
     * 
     * @return The decoded array
     * @throws EncodingException
     * @throws NotImplementedException
     */
    private Object[] readArray() throws EncodingException, NotImplementedException {
        int handle = readInt();
        boolean inline = ((handle & 1) != 0);
        handle = handle >> 1;

        if (inline) {
            String key = readString();
            if (key != null && !key.equals(""))
                throw new NotImplementedException("Associative arrays are not supported");

            Object[] ret = new Object[handle];
            objectReferences.add(ret);

            for (int i = 0; i < handle; i++)
                ret[i] = decode();

            return ret;
        }
        else {
            return (Object[])objectReferences.get(handle);
        }
    }

    /**
     * Decodes an AMF3 object
     * 
     * @return The decoded object
     * @throws EncodingException
     * @throws NotImplementedException
     */
    private Object readObject() throws EncodingException, NotImplementedException {
        int handle = readInt();
        boolean inline = ((handle & 1) != 0);
        handle = handle >> 1;

        if (inline) {
            boolean inlineDefine = ((handle & 1) != 0);
            handle = handle >> 1;

            ClassDefinition cd;
            if (inlineDefine) {
                cd = new ClassDefinition();
                cd.type = readString();

                cd.externalizable = ((handle & 1) != 0);
                handle = handle >> 1;
                cd.dynamic = ((handle & 1) != 0);
                handle = handle >> 1;

                for (int i = 0; i < handle; i++)
                    cd.members.add(readString());

                classDefinitions.add(cd);
            }
            else {
                cd = classDefinitions.get(handle);
            }

            TypedObject ret = new TypedObject(cd.type);

            // Need to add reference here due to circular references
            objectReferences.add(ret);

            if (cd.externalizable) {
                if (cd.type.equals("DSK"))
                    ret = readDSK();
                else if (cd.type.equals("DSA"))
                    ret = readDSA();
                else if (cd.type.equals("flex.messaging.io.ArrayCollection")) {
                    Object obj = decode();
                    ret = TypedObject.makeArrayCollection((Object[])obj);
                }
                else if (cd.type.equals("com.riotgames.platform.systemstate.ClientSystemStatesNotification") || cd.type.equals("com.riotgames.platform.broadcast.BroadcastNotification")) {
                    int size = 0;
                    for (int i = 0; i < 4; i++)
                        size = size * 256 + readByteAsInt();

                    String json;
                    try {
                        json = new String(readBytes(size), "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        throw new EncodingException(e.toString());
                    }
                    ret = new TypedObject((ObjectMap)JSON.parse(json));
                    ret.type = cd.type;
                }
                else {
                    for (int i = dataPos; i < dataBuffer.length; i++)
                        System.out.print(String.format("%02X", dataBuffer[i]));
                    System.out.println();
                    throw new NotImplementedException("Externalizable not handled for " + cd.type);
                }
            }
            else {
                for (int i = 0; i < cd.members.size(); i++) {
                    String key = cd.members.get(i);
                    Object value = decode();
                    ret.put(key, value);
                }

                if (cd.dynamic) {
                    String key;
                    while ((key = readString()).length() != 0) {
                        Object value = decode();
                        ret.put(key, value);
                    }
                }
            }

            return ret;
        }
        else {
            return objectReferences.get(handle);
        }
    }

    /**
     * Not implemented
     * 
     * @return
     * @throws NotImplementedException
     */
    private String readXMLString() throws NotImplementedException {
        throw new NotImplementedException("Reading of XML strings is not implemented");
    }

    /**
     * Decodes an AMF3 byte array
     * 
     * @return The decoded byte array
     */
    private byte[] readByteArray() {
        int handle = readInt();
        boolean inline = ((handle & 1) != 0);
        handle = handle >> 1;

        if (inline) {
            byte[] ret = readBytes(handle);
            objectReferences.add(ret);
            return ret;
        }
        else {
            return (byte[])objectReferences.get(handle);
        }
    }

    /**
     * Decodes a DSA
     * 
     * @return The decoded DSA
     * @throws NotImplementedException
     * @throws EncodingException
     */
    private TypedObject readDSA() throws EncodingException, NotImplementedException {
        TypedObject ret = new TypedObject("DSA");

        int flag;
        List<Integer> flags = readFlags();
        for (int i = 0; i < flags.size(); i++) {
            flag = flags.get(i);
            int bits = 0;
            if (i == 0) {
                if ((flag & 0x01) != 0)
                    ret.put("body", decode());
                if ((flag & 0x02) != 0)
                    ret.put("clientId", decode());
                if ((flag & 0x04) != 0)
                    ret.put("destination", decode());
                if ((flag & 0x08) != 0)
                    ret.put("headers", decode());
                if ((flag & 0x10) != 0)
                    ret.put("messageId", decode());
                if ((flag & 0x20) != 0)
                    ret.put("timeStamp", decode());
                if ((flag & 0x40) != 0)
                    ret.put("timeToLive", decode());
                bits = 7;
            }
            else if (i == 1) {
                if ((flag & 0x01) != 0) {
                    readByte();
                    byte[] temp = readByteArray();
                    ret.put("clientIdBytes", temp);
                    ret.put("clientId", byteArrayToID(temp));
                }
                if ((flag & 0x02) != 0) {
                    readByte();
                    byte[] temp = readByteArray();
                    ret.put("messageIdBytes", temp);
                    ret.put("messageId", byteArrayToID(temp));
                }
                bits = 2;
            }

            readRemaining(flag, bits);
        }

        flags = readFlags();
        for (int i = 0; i < flags.size(); i++) {
            flag = flags.get(i);
            int bits = 0;

            if (i == 0) {
                if ((flag & 0x01) != 0)
                    ret.put("correlationId", decode());
                if ((flag & 0x02) != 0) {
                    readByte();
                    byte[] temp = readByteArray();
                    ret.put("correlationIdBytes", temp);
                    ret.put("correlationId", byteArrayToID(temp));
                }
                bits = 2;
            }

            readRemaining(flag, bits);
        }

        return ret;
    }

    /**
     * Decodes a DSK
     * 
     * @return The decoded DSK
     * @throws NotImplementedException
     * @throws EncodingException
     */
    private TypedObject readDSK() throws EncodingException, NotImplementedException {
        // DSK is just a DSA + extra set of flags/objects
        TypedObject ret = readDSA();
        ret.type = "DSK";

        List<Integer> flags = readFlags();
        for (int i = 0; i < flags.size(); i++)
            readRemaining(flags.get(i), 0);

        return ret;
    }

    private List<Integer> readFlags() {
        List<Integer> flags = new ArrayList<Integer>();
        int flag;
        do {
            flag = readByteAsInt();
            flags.add(flag);
        } while ((flag & 0x80) != 0);

        return flags;
    }

    private void readRemaining(int flag, int bits) throws EncodingException, NotImplementedException {
        // For forwards compatibility, read in any other flagged objects to
        // preserve the integrity of the input stream...
        if ((flag >> bits) != 0) {
            for (int o = bits; o < 6; o++) {
                if (((flag >> o) & 1) != 0)
                    decode();
            }
        }
    }

    /**
     * Converts an array of bytes into an ID string
     * 
     * @return The ID string
     */
    private String byteArrayToID(byte[] data) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i == 4 || i == 6 || i == 8 || i == 10)
                ret.append('-');
            ret.append(String.format("%02x", data[i]));
        }

        return ret.toString();
    }

    /**
     * Decodes the next AMF0 object from the buffer
     * 
     * @return The decoded object
     * @throws NotImplementedException
     * @throws EncodingException
     */
    private Object decodeAMF0() throws NotImplementedException, EncodingException {
        int type = readByte();
        switch (type) {
        case 0x00:
            return readIntAMF0();

        case 0x02:
            return readStringAMF0();

        case 0x03:
            return readObjectAMF0();

        case 0x05:
            return null;

        case 0x11: // AMF3
            return decode();
        }

        throw new NotImplementedException("AMF0 type not supported: " + type);
    }

    /**
     * Decodes an AMF0 string
     * 
     * @return The decoded string
     * @throws EncodingException
     */
    private String readStringAMF0() throws EncodingException {
        int length = (readByteAsInt() << 8) + readByteAsInt();
        if (length == 0)
            return "";

        byte[] data = readBytes(length);

        // UTF-8 applicable?
        String str;
        try {
            str = new String(data, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new EncodingException("Error parsing AMF0 string from " + data);
        }

        return str;
    }

    /**
     * Decodes an AMF0 integer
     * 
     * @return The decoded integer
     */
    private int readIntAMF0() {
        return (int)readDouble();
    }

    /**
     * Decodes an AMF0 object
     * 
     * @return The decoded object
     * @throws EncodingException
     * @throws NotImplementedException
     */
    private TypedObject readObjectAMF0() throws EncodingException, NotImplementedException {
        TypedObject body = new TypedObject("Body");
        String key;
        while (!(key = readStringAMF0()).equals("")) {
            byte b = readByte();
            if (b == 0x00)
                body.put(key, readDouble());
            else if (b == 0x02)
                body.put(key, readStringAMF0());
            else if (b == 0x05)
                body.put(key, null);
            else
                throw new NotImplementedException("AMF0 type not supported: " + b);
        }
        readByte(); // Skip object end marker

        return body;
    }
}
