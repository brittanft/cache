package org.summoners.cache.data.model;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.summoners.nio.*;

public class AnimationDefinition {
	
	public AnimationDefinition(ByteBuffer buf) throws IOException {
		id = BufferUtil.getString(buf, ID_LENGTH);
		
		version = buf.getInt() & 0xFFFFFFFFL;
		if (version == 0 || version == 1 || version == 2 || version == 3) {
			/*
			 * Parse the header.
			 */
			magic = buf.getInt() & 0xFFFFFFFFL;
			boneCount = buf.getInt() & 0xFFFFFFFFL;
			frameCount = buf.getInt() & 0xFFFFFFFFL;
			playbackFps = buf.getInt() & 0xFFFFFFFFL;
			
			for (int boneIndex = 0; boneIndex != boneCount; ++boneIndex) {
				AnimationBone bone = new AnimationBone();
				bone.setName(BufferUtil.getString(buf, AnimationBone.BONE_NAME_LENGTH));
				
				buf.getInt();
				for (int frameIndex = 0; frameIndex != frameCount; ++frameIndex) {
					AnimationFrame frame = new AnimationFrame();
					frame.getOrientation()[0] = buf.getFloat();
					frame.getOrientation()[1] = buf.getFloat();
					frame.getOrientation()[2] = buf.getFloat();
					frame.getOrientation()[3] = buf.getFloat();
					
					frame.getPosition()[0] = buf.getFloat();
					frame.getPosition()[1] = buf.getFloat();
					frame.getPosition()[2] = buf.getFloat();
					
					bone.getFrames().add(frame);
				}
				
				getBones().add(bone);
			}
		} else if (version == 4) {
			magic = buf.getInt() & 0xFFFFFFFFL;
			
			for (int i = 0; i != 3; ++i)
				buf.getFloat();
			
			boneCount = buf.getInt() & 0xFFFFFFFFL;
			frameCount = buf.getInt() & 0xFFFFFFFFL;
			playbackFps = buf.getInt() & 0xFFFFFFFFL;
			
			for (int i = 0; i != 3; ++i)
				buf.getInt();
			
			long positionOffset = buf.getInt() & 0xFFFFFFFFL;
			long orientationOffset = buf.getInt() & 0xFFFFFFFFL;
			long indexOffset = buf.getInt() & 0xFFFFFFFFL;
			
			for (int i = 0; i != 3; ++i)
				buf.getInt();
			
			LinkedList<Float> positions = new LinkedList<>();
			long positionCount = (orientationOffset - positionOffset) / 4L;
			for (int i = 0; i != positionCount; ++i)
				positions.add(buf.getFloat());
			
			LinkedList<Float> orientations = new LinkedList<>();
			long orientationCount = (indexOffset - orientationOffset) / 4L;
			for (int i = 0; i != orientationCount; ++i)
				orientations.add(buf.getFloat());
			
			HashMap<Long, AnimationBone> boneMap = new HashMap<>();
			for (int i = 0; i != boneCount; ++i) {
				long boneId = buf.getInt() & 0xFFFFFFFFL;
				int positionId = buf.getShort() & 0xFFFF;
				buf.getShort();
				int orientationId = buf.getShort() & 0xFFFF;
				buf.getShort();
				
				AnimationBone bone = new AnimationBone();
				bone.setId(boneId);
				
				for (int frameIndex = 0; frameIndex != frameCount; ++frameIndex)
					bone.getFrames().add(new AnimationFrame());
				
				AnimationFrame frame = bone.getFrames().getFirst();
				frame.setPosition(getVector(positionId, positions));
				frame.setOrientation(getQuaternion(orientationId, orientations));
				
				boneMap.put(boneId, bone);
			}
			
			int currentFrame = 1, currentBone = 0;
			long lookupCount = (frameCount - 1) * boneCount;
			for (int lookup = 0; lookup != lookupCount; ++lookup) {
				long boneId = buf.getInt() & 0xFFFFFFFFL;
				int positionId = buf.getShort() & 0xFFFF;
				buf.getShort();
				int orientationId = buf.getShort() & 0xFFFF;
				buf.getShort();
				
				AnimationBone bone = boneMap.get(boneId);
				AnimationFrame frame = bone.getFrames().get(currentFrame);
				frame.setPosition(getVector(positionId, positions));
				frame.setOrientation(getQuaternion(orientationId, orientations));
				
				currentBone++;
				if (currentBone >= boneCount) {
					currentBone = 0;
					currentFrame++;
				}
			}
			
			boneMap.forEach((id, b) -> bones.add(b));
		} else 
			throw new IOException("Animation version not supported: " + version);
		
		
	}

    private static float[] getVector(int id, LinkedList<Float> vectors) {
        float[] result = new float[3];
        
        int startingPosition = id * result.length;
        for (int i = 0; i != result.length; ++i)
            result[i] = vectors.get(startingPosition + i);
        
        return result;
    }

    private static float[] getQuaternion(int id, LinkedList<Float> quaternions) {
        float[] result = new float[4];
        
        int startingPosition = id * result.length;
        for (int i = 0; i != result.length; ++i)
            result[i] = quaternions.get(startingPosition + i);
        
        return result;
    }

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private long version;

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	private long magic;

	public long getMagic() {
		return magic;
	}

	public void setMagic(long magic) {
		this.magic = magic;
	}
	
	private long boneCount;

	public long getBoneCount() {
		return boneCount;
	}

	public void setBoneCount(long boneCount) {
		this.boneCount = boneCount;
	}
	
	private long frameCount;

	public long getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(long frameCount) {
		this.frameCount = frameCount;
	}

	private long playbackFps;
	
	public long getPlaybackFps() {
		return playbackFps;
	}

	public void setPlaybackFps(long playbackFps) {
		this.playbackFps = playbackFps;
	}
	
	private LinkedList<AnimationBone> bones = new LinkedList<>();
	
	public LinkedList<AnimationBone> getBones() {
		return bones;
	}

	public void setBones(LinkedList<AnimationBone> bones) {
		this.bones = bones;
	}
	
	public static final int ID_LENGTH = 8;
	
	public static class AnimationBone {
		
		private String name;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		private long id;
		
		public long getId() {
			return id;
		}
		
		public void setId(long id) {
			this.id = id;
		}
		
		private LinkedList<AnimationFrame> frames = new LinkedList<>();
		
		public LinkedList<AnimationFrame> getFrames() {
			return frames;
		}
		
		public static final int BONE_NAME_LENGTH = 32;
	}
	
	public static class AnimationFrame {
		
		private float[] orientation = new float[4];
		
		public float[] getOrientation() {
			return orientation;
		}
		
		public void setOrientation(float[] orientation) {
			this.orientation = orientation;
		}
		
		private float[] position = new float[3];
		
		public float[] getPosition() {
			return position;
		}
		
		public void setPosition(float[] position) {
			this.position = position;
		}
	}
}
