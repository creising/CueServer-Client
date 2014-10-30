package com.interactive.cueserver.data.playback;

/**
 * Identifies a playback.
 *
 * author: Chris Reising
 */
public enum Playback
{
    PLAYBACK_1(0),
    PLAYBACK_2(1),
    PLAYBACK_3(2),
    PLAYBACK_4(3);

    /** The numerical ID of the playback. */
    private final int playbackId;

    /**
     * Creates a new {@code Playback}.
     *
     * @param playbackId the id.
     */
    Playback(int playbackId)
    {
        this.playbackId = playbackId;
    }

    /**
     * Gets the playback ID.
     *
     * @return the ID.
     */
    public int getPlaybackId()
    {
        return playbackId;
    }
}
