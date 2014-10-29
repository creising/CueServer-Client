package com.interactive.cueserver;

import com.interactive.cueserver.data.PlaybackStatus;
import com.interactive.cueserver.data.SystemInfo;

/**
 * Allows client to both retrieve and send commands to a CueServer.
 *
 * author: Chris Reising
 */
public interface CueServerClient
{
    /**
     * Gets the current system information from a CueServer.
     *
     * @return {@code null} if there was an error communicating with the
     *         CueServer.
     */
    SystemInfo getSystemInfo();

    /**
     * Gets the playback status from a CueServer.
     *
     * @return {@code null} if there was an error communicating with the
     *         CueServer.
     */
    PlaybackStatus getPlaybackStatus();

    /**
     * Gets the current output levels.
     *
     * @return the current output levels between [0, 255]. The array will always
     *         contain a full DMX universe. This method will return
     *         {@code null} if there was an error communicating with the
     *         CueServer.
     */
    Integer[] getOutputLevels();
}
