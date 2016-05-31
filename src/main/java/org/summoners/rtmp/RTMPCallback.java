package org.summoners.rtmp;

import org.summoners.rtmp.encoding.*;

/**
 * Provides callback functionality
 * 
 * @author Gabriel Van Eyck
 */
public interface RTMPCallback {
    /**
     * The function to call after the result has been read
     * 
     * @param result The result for this callback
     */
    public void callback(TypedObject result);
}
