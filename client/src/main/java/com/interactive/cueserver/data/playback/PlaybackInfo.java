package com.interactive.cueserver.data.playback;

import com.interactive.cueserver.data.cue.Cue;

import static com.google.common.base.Preconditions.*;

/**
 * Contains the status of a playback.
 *
 * author: Chris Reising
 */
public class PlaybackInfo
{
    /** The playback being reported. */
    private final Playback playback;

    /** The current cue. */
    private final Cue currentCue;

    /** The next cue. */
    private final Cue nextCue;

    /**
     * Creates a new {@code PlaybackInfo}.
     *
     * @param playback the playback being reported.
     * @param currentCue the current cue, or {@code null} if the playback does
     *                   not have a current cue.
     * @param nextCue the next cue, or {@code null} if the playback does not
     *                 have a next cue.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    public PlaybackInfo(Playback playback,
                        Cue currentCue,
                        Cue nextCue)
    {
        this.playback = checkNotNull(playback,
                "playback cannot be null");
        this.currentCue = currentCue;
        this.nextCue = nextCue;
    }

    /**
     * Gets the playback being reported.
     *
     * @return Never {@code null}.
     */
    public Playback getPlayback()
    {
        return playback;
    }

    /**
     * Gets the current cue.
     *
     * @return {@code null} if the playback does not have a cue loaded.
     */
    public Cue getCurrentCue()
    {
        return currentCue;
    }

    /**
     * Gets the next cue.
     *
     * @return {@code null} if the playback does not have a next cue.
     */
    public Cue getNextCue()
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
                "playback=" + playback +
                ", currentCue=" + currentCue +
                ", nextCue=" + nextCue +
                '}';
    }
}
