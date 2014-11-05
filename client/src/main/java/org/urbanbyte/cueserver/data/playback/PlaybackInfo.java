package org.urbanbyte.cueserver.data.playback;

/**
 * Contains the status of a playback.
 * <p>
 * author: Chris Reising
 */
public class PlaybackInfo extends AbstractPlaybackInfo
{
    /**
     * Creates a new {@code PlaybackInfo}.
     *
     * @param builder the builder used to create the object.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    protected PlaybackInfo(Builder builder)
    {
        super(builder);
    }

    /**
     * Builder for {@code PlaybackInfo}.
     */
    public static class Builder
        extends AbstractPlaybackInfo.Builder<Builder, PlaybackInfo>
    {
        /**
         * Build a {@code PlaybackInfo} object from the values in the builder.
         *
         * @return Never {@code null}
         */
        @Override
        public PlaybackInfo build()
        {
            return new PlaybackInfo(this);
        }
    }
}
