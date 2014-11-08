package org.urbanbyte.cueserver;

import org.urbanbyte.cueserver.data.playback.DetailedPlaybackStatus;
import org.urbanbyte.cueserver.data.playback.Playback;
import org.urbanbyte.cueserver.data.playback.PlaybackStatus;
import org.urbanbyte.cueserver.data.system.SystemInfo;

/**
 * The {@code CueServerClient} facilitates interaction with a CueServer. Clients
 * have the ability to retrieve state related information, and send commands to
 * a CueServer.
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
     * @param cueNumber The cue number to execute. The CueServer only supports
     *                  up to one decimal place for cue number (e.g., 10.1). Any
     *                  values greater than one decimal place will be truncated
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void playCue(double cueNumber);

    /**
     * Executes the given cue number on the given playback.
     *
     * @param cueNumber The cue number to execute. The CueServer only supports
     *                  up to one decimal place for cue number (e.g., 10.1). Any
     *                  values greater than one decimal place will be truncated
     * @param playback the playback to execute the cue on.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void playCue(double cueNumber, Playback playback);

    /**
     * Clears the given playback.
     *
     * @param playback the playback to clear.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void clearPlayback(Playback playback);

    /**
     * Sets the given channel to a level on {@link Playback#PLAYBACK_1} using a
     * time of 0.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @throws IllegalArgumentException if any argument is out of bounds.
     */
    void setChannel(int channel, int value);

    /**
     * Sets the given channel to a level on {@link Playback#PLAYBACK_1} using
     * the provided time.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must be within [0, 65000]. The precision
     *                    is up to a tenth of a second.
     * @throws IllegalArgumentException if any argument is out of bounds.
     */
    void setChannel(int channel, int value, double timeSeconds);

    /**
     * Sets the given channel to a level on the provided playback and using the
     * provided time.
     *
     * @param channel The channel to set. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must be within [0, 65000]. The precision
     *                    is up to a tenth of a second.
     * @param playback the playback controlling the channel.
     * @throws IllegalArgumentException if any argument is out of its bounds.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    void setChannel(int channel, int value, double timeSeconds,
                    Playback playback);

    /**
     * Sets the range of channels to a level on {@link Playback#PLAYBACK_1}
     * using a time of 0.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @throws IllegalArgumentException if the end if greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     */
    void setChannelRange(int startChannel, int endChannel, int value);

    /**
     * Sets the range of channels to a level on {@link Playback#PLAYBACK_1}
     * using the provided time.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must be within [0, 65000].
     * @throws IllegalArgumentException if the end if greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     */
    void setChannelRange(int startChannel, int endChannel, int value,
                         double timeSeconds);

    /**
     * Sets the range of channels to a level on {@link Playback#PLAYBACK_1}
     * using the provided time.
     *
     * @param startChannel The beginning of the range. Must be within [1, 512].
     * @param endChannel The end of the range. Must be within [1, 512].
     * @param value The value of the channel level. Must be within [0, 255].
     * @param timeSeconds The time in seconds for the channel to complete its
     *                    transition. Must be within [0, 65000]. The precision
     *                    is up to a tenth of a second.
     *@param playback the playback controlling the channel.
     * @throws IllegalArgumentException if the end is greater than the start
     *                                  range, or if any value is outside of its
     *                                  bounds.
     * @throws NullPointerException if {@code playback} is {@code null}
     */
    void setChannelRange(int startChannel, int endChannel, int value,
                         double timeSeconds, Playback playback);

    /**
     * Records a cue.
     *
     * @param cueNumber the cue number to record. Must be positive. The
     *                  CueServer only supports up to one decimal place for cue
     *                  number (e.g., 10.1). The value passed in will be
     *                  truncated to accommodate this.
     * @param uptimeSecs the fade's uptime in seconds. Must be within
     *                   [0, 65000]. The precision is up to a tenth of a
     *                     second.
     * @param downtimeSecs the fade's downtime in seconds. Must be within
     *                     [0, 65000]. The precision is up to a tenth of a
     *                     second.
     * @throws IllegalArgumentException if any argument is out of bounds.
     */
    void recordCue(double cueNumber, double uptimeSecs, double downtimeSecs);

    /**
     * Deletes a cue.
     *
     * @param cueNumber the cue number to delete. The CueServer only supports up
     *                  to one decimal place for cue number (e.g., 10.1). The
     *                  value passed in will be truncated to accommodate this.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void deleteCue(double cueNumber);

    /**
     * Updates a cue.
     *
     * @param cueNumber the cue number to delete. The CueServer only supports up
     *                  to one decimal place for cue number (e.g., 10.1). The
     *                  value passed in will be truncated to accommodate this.
     * @throws IllegalArgumentException if {@code cueNumber} is not positive.
     */
    void updateCue(double cueNumber);

}
