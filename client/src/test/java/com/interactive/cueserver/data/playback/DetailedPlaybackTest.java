package com.interactive.cueserver.data.playback;

import com.interactive.cueserver.data.cue.Cue;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertSame;

/**
 * Test {@link DetailedPlaybackStatus}.
 *
 * author: Chris Reising
 */
public class DetailedPlaybackTest
{
    /** Current cue used in tests. */
    private final Cue cc = new Cue(1);

    /** Next cue used in tests. */
    private final Cue nc = new Cue(2);

    /** Linked cue used in tests. */
    private final Cue linkedCue = new Cue(3);

    /** Tested playback status. */
    private final boolean stopped = true;

    /** Tested combineMode. */
    private final CombineMode combineMode = CombineMode.MERGE;

    /** Tested playback value. */
    private final int masterLevel = 255;

    /** Playback used in tests. */
    private final Playback pb = Playback.PLAYBACK_1;

    /**
     * Test creation of object with all fields.
     */
    @Test
    public void testConstructionAllFields()
    {
        DetailedPlaybackStatus playbackInfo =
                new DetailedPlaybackStatus.Builder()
                        .setCurrentCue(cc)
                        .setNextCue(nc)
                        .setLinkedCue(linkedCue)
                        .setTimingDisabled(stopped)
                        .setCombinedMode(combineMode)
                        .setMasterLevel(masterLevel)
                        .setPlayback(pb).build();

        assertSame(playbackInfo.getCurrentCue(), cc);
        assertSame(playbackInfo.getNextCue(), nc);
        assertSame(playbackInfo.getLinkedCue(), linkedCue);
        assertThat(playbackInfo.getCombineMode(), is(combineMode));
        assertThat(playbackInfo.isTimingDisabled(), is(stopped));
        assertThat(playbackInfo.getMasterLevel(), is(masterLevel));
        assertThat(playbackInfo.getPlayback(), is(pb));
        assertThat(playbackInfo.toString(),
                containsString("DetailedPlaybackInfo"));
    }

    /**
     * Test creation of object with master at its minimum value.
     */
    @Test
    public void testConstructionAllFieldsMasterMin()
    {
        int masterMin = 0;
        DetailedPlaybackStatus playbackInfo =
                new DetailedPlaybackStatus.Builder()
                        .setCurrentCue(cc)
                        .setNextCue(nc)
                        .setLinkedCue(linkedCue)
                        .setTimingDisabled(stopped)
                        .setCombinedMode(combineMode)
                        .setMasterLevel(masterMin)
                        .setPlayback(pb).build();

        assertSame(playbackInfo.getCurrentCue(), cc);
        assertSame(playbackInfo.getNextCue(), nc);
        assertSame(playbackInfo.getLinkedCue(), linkedCue);
        assertThat(playbackInfo.getCombineMode(), is(combineMode));
        assertThat(playbackInfo.isTimingDisabled(), is(stopped));
        assertThat(playbackInfo.getMasterLevel(), is(masterMin));
        assertThat(playbackInfo.getPlayback(), is(pb));
    }

    /**
     * Test creation of the object with optional fields.
     */
    @Test
    public void testConstructionOptionalFields()
    {
        DetailedPlaybackStatus playbackInfo =
                new DetailedPlaybackStatus.Builder()
                        .setTimingDisabled(stopped)
                        .setCombinedMode(combineMode)
                        .setMasterLevel(masterLevel)
                        .setPlayback(pb).build();

        assertThat(playbackInfo.getCurrentCue(), nullValue());
        assertThat(playbackInfo.getNextCue(), nullValue());
        assertThat(playbackInfo.getLinkedCue(), nullValue());
        assertThat(playbackInfo.getCombineMode(), is(combineMode));
        assertThat(playbackInfo.isTimingDisabled(), is(stopped));
        assertThat(playbackInfo.getMasterLevel(), is(masterLevel));
        assertThat(playbackInfo.getPlayback(), is(pb));
    }

    /**
     * A {@code null} playback will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void nullPlaybackValue()
    {
        new DetailedPlaybackStatus.Builder()
                .setCurrentCue(cc)
                .setNextCue(nc)
                .setLinkedCue(linkedCue)
                .setTimingDisabled(stopped)
                .setCombinedMode(combineMode)
                .setMasterLevel(masterLevel)
                .build();
    }

    /**
     * A {@code null} combine mode will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void nullCombineMode()
    {
        new DetailedPlaybackStatus.Builder()
                .setCurrentCue(cc)
                .setNextCue(nc)
                .setLinkedCue(linkedCue)
                .setTimingDisabled(stopped)
                .setMasterLevel(masterLevel)
                .build();
    }

    /**
     * A master value below its minimum value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidMasterValMin()
    {
        new DetailedPlaybackStatus.Builder()
                .setPlayback(Playback.PLAYBACK_1)
                .setCurrentCue(cc)
                .setNextCue(nc)
                .setLinkedCue(linkedCue)
                .setTimingDisabled(stopped)
                .setCombinedMode(combineMode)
                .setMasterLevel(-1)
                .build();
    }

    /**
     * A master value above its maximum value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidMasterValMax()
    {
        new DetailedPlaybackStatus.Builder()
                .setPlayback(Playback.PLAYBACK_1)
                .setCurrentCue(cc)
                .setNextCue(nc)
                .setLinkedCue(linkedCue)
                .setTimingDisabled(stopped)
                .setCombinedMode(combineMode)
                .setMasterLevel(256)
                .build();
    }
}
