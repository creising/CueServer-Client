package com.interactive.cueserver;

import com.interactive.cueserver.data.playback.DetailedPlaybackStatus;
import com.interactive.cueserver.data.playback.Playback;
import com.interactive.cueserver.data.playback.PlaybackStatus;
import com.interactive.cueserver.data.system.SystemInfo;

/**
 * Allows client to both retrieve and send commands to a CueServer.
 * <p>
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
     * Gets detailed information for the given playback.
     *
     * @param playback the playback to retrieve information for.
     * @return {@code null} if there was an error communicating with the
     *         CueServer.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    DetailedPlaybackStatus getDetailedPlaybackInfo(Playback playback);

    /**
     * Gets the current output levels.
     *
     * @return The current output levels between [0, 255]. The array will always
     *         contain a full DMX universe. This method will return
     *         {@code null} if there was an error communicating with the
     *         CueServer.
     */
    Integer[] getOutputLevels();
}
