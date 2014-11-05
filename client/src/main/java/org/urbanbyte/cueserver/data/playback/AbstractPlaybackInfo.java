package org.urbanbyte.cueserver.data.playback;

import org.urbanbyte.cueserver.data.cue.Cue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains common information for a CueServer's playback.
 *  <p>
 * author: Chris Reising
 */
public abstract class AbstractPlaybackInfo
{
    /** The playback being reported. */
    protected final Playback playback;

    /** The current cue. */
    protected final Cue currentCue;

    /** The next cue. */
    protected final Cue nextCue;

    /**
     * Creates a new {@code AbstractPlaybackInfo}.
     *
     * @param builder the builder used to create the object.
     * @throws NullPointerException if {@code playback} is {@code null}.
     */
    @SuppressWarnings("rawtypes")
    protected AbstractPlaybackInfo(Builder builder)
    {
        this.playback = checkNotNull(builder.getPlayback(),
                "playback cannot be null");
        this.currentCue = builder.getCurrentCue();
        this.nextCue = builder.getNextCue();
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

    /**
     * Builder for {@link AbstractPlaybackInfo}.
     * @param <T> The builder type.
     * @param <U> The type being constructed by the builder.
     */
    @SuppressWarnings("rawtypes")
    public abstract static class Builder
            <T extends Builder, U extends AbstractPlaybackInfo>
    {
        /** The playback being reported. */
        private Playback playback;

        /** The current cue. */
        private Cue currentCue;

        /** The next cue. */
        private Cue nextCue;

        /**
         * Gets the current value for playback.
         *
         * @return Can be {@code null}.
         */
        public Playback getPlayback()
        {
            return playback;
        }

        /**
         * Sets the value for the playback.
         *
         * @param playback The value for the playback.
         * @return {@code this} builder.
         */
        @SuppressWarnings("unchecked")
        public T setPlayback(Playback playback)
        {
            this.playback = playback;
            return (T) this;
        }

        /**
         * Gets the current value for current cue.
         *
         * @return Can be {@code null}.
         */
        public Cue getCurrentCue()
        {
            return currentCue;
        }

        /**
         * Sets the value for the current cue.
         *
         * @param currentCue The value for the current cue.
         * @return {@code this} builder.
         */
        @SuppressWarnings("unchecked")
        public T setCurrentCue(Cue currentCue)
        {
            this.currentCue = currentCue;
            return (T) this;
        }

        /**
         * Gets the current value for next cue.
         *
         * @return Can be {@code null}.
         */
        public Cue getNextCue()
        {
            return nextCue;
        }

        /**
         * Sets the value for the next cue.
         *
         * @param nextCue The value for the next cue.
         * @return {@code this} builder.
         */
        @SuppressWarnings("unchecked")
        public T setNextCue(Cue nextCue)
        {
            this.nextCue = nextCue;
            return (T) this;
        }

        /**
         * Build an object from the values in the builder.
         *
         * @return Never {@code null}
         */
        public abstract U build();
    }
}
