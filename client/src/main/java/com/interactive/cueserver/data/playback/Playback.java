package com.interactive.cueserver.data.playback;

/**
 * Identifies a playback.
 *
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

    public static Playback getPlaybackForId(int id)
    {
        Playback playback;
        switch (id)
        {
            case 1:
                playback =  PLAYBACK_1;
                break;
            case 2:
                playback = PLAYBACK_2;
                break;
            case 3:
                playback = PLAYBACK_3;
                break;
            case 4:
                playback = PLAYBACK_4;
                break;
            default:
                throw new IllegalArgumentException(
                        "Playback ID unknown: " + id);
        }

        return playback;
    }
}
