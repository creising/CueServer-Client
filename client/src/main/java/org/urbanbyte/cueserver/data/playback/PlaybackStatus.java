package org.urbanbyte.cueserver.data.playback;

import com.google.common.base.Preconditions;

/**
 * Contains the playback status for each playback available in the CueServer.
 * <p>
 * author: Chris Reising
 */
public class PlaybackStatus
{
    /** The state of playback #1. */
    private final PlaybackInfo playbackInfo1;

    /** The state of playback #2. */
    private final PlaybackInfo playbackInfo2;

    /** The state of playback #3. */
    private final PlaybackInfo playbackInfo3;

    /** The state of playback #4. */
    private final PlaybackInfo playbackInfo4;

    /**
     * Creates a new {@code PlaybackStatus} from the builder.
     *
     * @param builder the builder.
     * @throws NullPointerException if {@code builder} is {@code null}.
     */
    private PlaybackStatus(Builder builder)
    {
        this.playbackInfo1 = Preconditions.checkNotNull(builder.getPlayback1(),
                "playback1 cannot be null");
        this.playbackInfo2 = Preconditions.checkNotNull(builder.getPlayback2(),
                "playback2 cannot be null");
        this.playbackInfo3 = Preconditions.checkNotNull(builder.getPlayback3(),
                "playback3 cannot be null");
        this.playbackInfo4 = Preconditions.checkNotNull(builder.getPlayback4(),
                "playback4 cannot be null");
    }

    /**
     * Gets the {@link PlaybackInfo} for playback 1.
     *
     * @return Never {@code null}.
     */
    public PlaybackInfo getPlayback1()
    {
        return playbackInfo1;
    }

    /**
     * Gets the {@link PlaybackInfo} for playback 2.
     *
     * @return Never {@code null}.
     */
    public PlaybackInfo getPlayback2()
    {
        return playbackInfo2;
    }

    /**
     * Gets the {@link PlaybackInfo} for playback 3.
     *
     * @return Never {@code null}.
     */
    public PlaybackInfo getPlayback3()
    {
        return playbackInfo3;
    }

    /**
     * Gets the {@link PlaybackInfo} for playback 4.
     *
     * @return Never {@code null}.
     */
    public PlaybackInfo getPlayback4()
    {
        return playbackInfo4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "PlaybackStatus{" +
                "playbackInfo1=" + playbackInfo1 +
                ", playbackInfo2=" + playbackInfo2 +
                ", playbackInfo3=" + playbackInfo3 +
                ", playbackInfo4=" + playbackInfo4 +
                '}';
    }

    /**
     * Builder for the {@link PlaybackStatus} object.
     */
    public static class Builder
    {
        /** The state of playback #1. */
        private PlaybackInfo playbackInfo1;

        /** The state of playback #2. */
        private PlaybackInfo playbackInfo2;

        /** The state of playback #3. */
        private PlaybackInfo playbackInfo3;

        /** The state of playback #4. */
        private PlaybackInfo playbackInfo4;

        /**
         * Gets the value currently set for playback 1.
         *
         * @return Can be {@code null}.
         */
        public PlaybackInfo getPlayback1()
        {
            return playbackInfo1;
        }

        /**
         * Sets the value for playback 1.
         *
         * @param playbackInfo1 the value for playback 1.
         * @return a reference to {@code this} builder.
         */
        public Builder setPlayback1(PlaybackInfo playbackInfo1)
        {
            this.playbackInfo1 = playbackInfo1;
            return this;
        }

        /**
         * Gets the value currently set for playback 2.
         *
         * @return Can be {@code null}.
         */
        public PlaybackInfo getPlayback2()
        {
            return playbackInfo2;
        }

        /**
         * Sets the value for playback 2.
         *
         * @param playbackInfo2 the value for playback 2.
         * @return a reference to {@code this} builder.
         */
        public Builder setPlayback2(PlaybackInfo playbackInfo2)
        {
            this.playbackInfo2 = playbackInfo2;
            return this;
        }

        /**
         * Gets the value currently set for playback 3.
         *
         * @return Can be {@code null}.
         */
        public PlaybackInfo getPlayback3()
        {
            return playbackInfo3;
        }

        /**
         * Sets the value for playback 3.
         *
         * @param playbackInfo3 the value for playback 3.
         * @return a reference to {@code this} builder.
         */
        public Builder setPlayback3(PlaybackInfo playbackInfo3)
        {
            this.playbackInfo3 = playbackInfo3;
            return this;
        }

        /**
         * Gets the value currently set for playback 4.
         *
         * @return Can be {@code null}.
         */
        public PlaybackInfo getPlayback4()
        {
            return playbackInfo4;
        }

        /**
         * Sets the value for playback 4.
         *
         * @param playbackInfo4 the value for playback 4.
         * @return a reference to {@code this} builder.
         */
        public Builder setPlayback4(PlaybackInfo playbackInfo4)
        {
            this.playbackInfo4 = playbackInfo4;
            return this;
        }

        /**
         * Builds a {@link PlaybackInfo} from the values in the builder.
         *
         * @return Never {@code null}.
         * @throws NullPointerException if any of the playbacks are null.
         */
        public PlaybackStatus build()
        {
            return new PlaybackStatus(this);
        }
    }
}
