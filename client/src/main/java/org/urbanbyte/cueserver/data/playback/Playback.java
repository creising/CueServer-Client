package org.urbanbyte.cueserver.data.playback;

/**
 * Identifies a playback.
 * <p>
 * author: Chris Reising
 */
public enum Playback
{
    PLAYBACK_1(1),
    PLAYBACK_2(2),
    PLAYBACK_3(3),
    PLAYBACK_4(4);

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
