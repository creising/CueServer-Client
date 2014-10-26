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
    private final Double currentCue;

    /** The next cue. */
    private final Double nextCue;

    /**
     * Creates a new {@code PlaybackInfo}.
     *
     * @param playbackNumber the number of the playback.
     * @param currentCue the current cue, or {@code null} if the playback does
     *                   not have a current cue.
     * @param nextCue the next cue, or {@code null} if the playback does not
     *                 have a next cue.
     * @throws IllegalArgumentException if any value is not positive.
     */
    public PlaybackInfo(int playbackNumber,
                        Double currentCue,
                        Double nextCue)
    {
        this.playbackNumber = throwIfNotPositive(playbackNumber);
        this.currentCue = throwIfNotPositive(currentCue);
        this.nextCue = throwIfNotPositive(nextCue);
    }

    /**
     * Gets the playback number.
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
     * @return {@code null} if the playback does not have a cue loaded.
     */
    public Double getCurrentCue()
    {
        return currentCue;
    }

    /**
     * Gets the next cue.
     *
     * @return {@code null} if the playback does not have a next cue.
     */
    public Double getNextCue()
    {
        return nextCue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "PlaybackInfo{" +
                "playbackNumber=" + playbackNumber +
                ", currentCue=" + currentCue +
                ", nextCue=" + nextCue +
                '}';
    }

    /**
     * Throws an exception is the given number is not positive.
     *
     * @param number the number to check.
     * @return the original number.
     * @throws IllegalArgumentException if the given number is not positive.
     */
    private static<T extends Number> T throwIfNotPositive(T number)
    {
        if(number != null && number.doubleValue() <= 0)
        {
            throw new IllegalArgumentException("number must be positive");
        }

        return number;
    }
}
