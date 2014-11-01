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

    /**
     * Executes the given cue number on {@link Playback#PLAYBACK_1}.
     *
     * @param cueNumber the cue number to execute.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void playCue(double cueNumber);

    /**
     * Executes the given cue number on the given playback.
     *
     * @param cueNumber the cue number to execute.
     * @param playback the playback to execute the cue on.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void playCue(double cueNumber, Playback playback);

    /**
     * Clear the given playback.
     *
     * @param playback the playback to clear.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void clearPlayback(Playback playback);

    /**
     * Sets the given channel to a level on playback 1 using a time of 0.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value. Must be within [0, 255].
     * @throws IllegalArgumentException if any parameters is out of bounds.
     */
    void setChannel(int channel, int value);

    /**
     * Sets the given channel to a level on playback 1 using the provided time.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must be positive.
     * @throws IllegalArgumentException if any argument is out of bounds.
     */
    void setChannel(int channel, int value, int timeSeconds);

    /**
     * Sets the given channel to a level on the provided playback and using the
     * provided time.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must &ge; 0.
     * @param playback the playback controlling the channel.
     * @throws IllegalArgumentException if any argument is out of its bounds.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void setChannel(int channel, int value, int timeSeconds, Playback playback);

    /**
     * Sets the range of channels to a level on playback 1 using a time of 0.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value to set the channel to. Must be within [0, 255].
     * @throws IllegalArgumentException if the end if greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     */
    void setChannelRange(int startChannel, int endChannel, int value);

    /**
     * Sets the range of channels to a level on playback 1 using the provied
     * time.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value to set the channel to. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must &ge; 0.
     * @throws IllegalArgumentException if the end if greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     */
    void setChannelRange(int startChannel, int endChannel, int value,
                         int timeSeconds);

    /**
     * Sets the range of channels to a level on playback 1 using the provied
     * time.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value to set the channel to. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must &ge; 0.
     @param playback the playback controlling the channel.
     * @throws IllegalArgumentException if the end if greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     * @throws NullPointerException if {@code playback} is {@code null}
     */
    void setChannelRange(int startChannel, int endChannel, int value,
                         int timeSeconds, Playback playback);

    /**
     * Records a cue.
     *
     * @param cueNumber the cue number to record.
     * @param uptimeSecs the fade's uptime in seconds.
     * @param downtimeSecs the fade's downtime in seconds.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive, or
     *                                  if {@code fadeTimeSeconds} is negative.
     */
    void recordCue(double cueNumber, int uptimeSecs, int downtimeSecs);

    /**
     * Deletes a cue.
     *
     * @param cueNumber the cue number to delete.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void deleteCue(double cueNumber);

    /**
     * Updates a cue.
     *
     * @param cueNumber the cue number to delete.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void updateCue(double cueNumber);

}
