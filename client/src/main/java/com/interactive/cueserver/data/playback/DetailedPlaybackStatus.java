package com.interactive.cueserver.data.playback;

import com.google.common.base.Preconditions;
import com.interactive.cueserver.data.cue.Cue;

/**
 * Contains detailed information for a CueServer's playback.
 *
 * author: Chris Reising
 */
public class DetailedPlaybackStatus extends AbstractPlaybackInfo
{
    /** {@code true} if the playback's timing is enabled. */
    private final boolean timingDisabled;

    /** The playback master level between [0, 255]. */
    private final int masterLevel;

    /** The combine mode of the playback. */
    private final CombineMode combineMode;

    /** The cue the current cue is linked to  if any. */
    private final Cue linkedCue;

    /**
     * Creates a new {@code DetailedPlaybackStatus} from the given builder.
     *
     * @param builder the builder to construct the object from.
     * @throws NullPointerException if the {@link #playback} or
     *                              {@link #combineMode} field is {@code null}.
     * @throws IllegalArgumentException if {@link #masterLevel} is not within
     *                                  [0, 255].
     */
    protected DetailedPlaybackStatus(Builder builder)
    {
        super(builder);

        timingDisabled = builder.isTimingDisabled();
        masterLevel = builder.getMasterLevel();

        if(masterLevel < 0 || masterLevel > 255)
        {
            throw new IllegalArgumentException("masterLevel must be within" +
                    "[0, 255].");
        }

        combineMode = Preconditions.checkNotNull(builder.getCombineMode(),
                "combineMode cannot be null.");
        linkedCue = builder.getLinkedCue();
    }

    /**
     * Gets the state of the playback.
     *
     * @return {@code true} if the playback is timingDisabled, {@code false} if it is
     *         normal.
     */
    public boolean isTimingDisabled()
    {
        return timingDisabled;
    }

    /**
     * Gets the playback's master level.
     *
     * @return a value within [0, 255].
     */
    public int getMasterLevel()
    {
        return masterLevel;
    }

    /**
     * The combine mode of the playback.
     *
     * @return Never {@code null}.
     */
    public CombineMode getCombineMode()
    {
        return combineMode;
    }

    /**
     * The cue linked with the cue currently running.
     *
     * @return the linked cue, or {@code null} if no cue is linked.
     */
    public Cue getLinkedCue()
    {
        return linkedCue;
    }

    @Override
    public String toString()
    {
        return "DetailedPlaybackInfo{" +
                "timingDisabled=" + timingDisabled +
                ", masterLevel=" + masterLevel +
                ", combineMode=" + combineMode +
                ", linkedCue=" + linkedCue +
                "} " + super.toString();
    }

    /**
     * Builder for a {@link DetailedPlaybackStatus}.
     */
    public static class Builder
            extends AbstractPlaybackInfo.Builder<Builder, DetailedPlaybackStatus>
    {
        /** {@code true} if the playback's timing is enabled. */
        private boolean timingDisabled;

        /** The playback master level between [0, 255]. */
        private int masterLevel;

        /** The combine mode of the playback. */
        private CombineMode combineMode;

        /** The cue the current cue is linked to  if any. */
        private Cue linkedCue;

        /**
         * Gets the current value of the {@link #timingDisabled} field.
         *
         * @return the {@link #timingDisabled} value.
         */
        public boolean isTimingDisabled()
        {
            return timingDisabled;
        }

        /**
         * Sets the value of the {@link #timingDisabled} field.
         *
         * @return This builder.
         */
        public Builder setTimingDisabled(boolean timingDisabled)
        {
            this.timingDisabled = timingDisabled;
            return this;
        }

        /**
         * Gets the current value of the {@link #masterLevel} field.
         *
         * @return the {@link #masterLevel} value.
         */
        public int getMasterLevel()
        {
            return masterLevel;
        }

        /**
         * Sets the value of the {@link #masterLevel} field.
         *
         * @return This builder.
         */
        public Builder setMasterLevel(int masterLevel)
        {
            this.masterLevel = masterLevel;
            return this;
        }

        /**
         * Gets the current value of the {@link #combineMode} field.
         *
         * @return the {@link #combineMode} value.
         */
        public CombineMode getCombineMode()
        {
            return combineMode;
        }

        /**
         * Sets the value of the {@link #combineMode} field.
         *
         * @return This builder.
         */
        public Builder setCombinedMode(CombineMode combineMode)
        {
            this.combineMode = combineMode;
            return this;
        }

        /**
         * Gets the current value of the {@link #linkedCue} field.
         *
         * @return the {@link #linkedCue} value.
         */
        public Cue getLinkedCue()
        {
            return linkedCue;
        }

        /**
         * Sets the value of the {@link #timingDisabled} field.
         *
         * @return This builder.
         */
        public Builder setLinkedCue(Cue linkedCue)
        {
            this.linkedCue = linkedCue;
            return this;
        }

        @Override
        public DetailedPlaybackStatus build()
        {
            return new DetailedPlaybackStatus(this);
        }
    }

}
