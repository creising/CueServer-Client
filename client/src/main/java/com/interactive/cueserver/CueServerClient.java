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
     * @return {@code null} if there was an error communicating to the
     *         CueServer.
     */
    SystemInfo getSystemInfo();

    /**
     * Gets the playback status from a CueServer.
     *
     * @return {@code null} if there was an error communicating to the
     *         CueServer.
     */
    PlaybackStatus getPlaybackStatus();
}
