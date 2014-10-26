package com.interactive.cueserver.data;

/**
 * Contains the status of a playback.
 *
 * author: Chris Reising
 */
public class PlaybackInfo
{
    /** The playback number being reported. */
    private final int playbackNumber;

    /** The current cue. */
    private final Integer currentCue;

    /** The next cue. */
    private final Integer nextCue;

    /**
     * Creates a new {@code Playback}.
     *
     * @param playbackNumber the number of the playback.
     * @param currentCue the current cue, or {@code null} if there is no
     *                   current cue.
     * @param nextCue the next cue, or {@code null} if there is no current cue.
     * @throws IllegalArgumentException if any value is not positive.
     */
    public PlaybackInfo(int playbackNumber,
                        Integer currentCue,
                        Integer nextCue)
    {
        this.playbackNumber = throwIfNotPositive(playbackNumber);
        this.currentCue = throwIfNotPositive(currentCue);
        this.nextCue = throwIfNotPositive(nextCue);
    }

    /**
     * Get the playback number.
     *
     * @return Never negative.
     */
    public int getPlaybackNumber()
    {
        return playbackNumber;
    }

    /**
     * Gets the current cue.
     *
     * @return {@code null} if there is no current cue.
     */
    public Integer getCurrentCue()
    {
        return currentCue;
    }

    /**
     * Gets the next cue.
     *
     * @return {@code null} if there is no next cue.
     */
    public Integer getNextCue()
    {
        return nextCue;
    }

    private static Integer throwIfNotPositive(Integer number)
    {
        if(number != null && number <= 0)
        {
            throw new IllegalArgumentException("number cannot be negative");
        }

        return number;
    }
}
