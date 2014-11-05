package org.urbanbyte.cueserver.data.playback;

import org.urbanbyte.cueserver.data.cue.Cue;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link PlaybackInfo}.
 * <p>
 * author: Chris Reising
 */
public class PlaybackInfoTest
{
    /** Current cue used in tests. */
    private final Cue cc = new Cue(1);

    /** Next cue used in tests. */
    private final Cue nc = new Cue(2);

    /** Playback used in tests. */
    private final Playback pb = Playback.PLAYBACK_1;

    /**
     * Test with valid arguments.
     */
    @Test
    public void testConstructorAllArguments()
    {
        PlaybackInfo playbackInfo = new PlaybackInfo.Builder()
                .setPlayback(pb)
                .setCurrentCue(cc)
                .setNextCue(nc)
                .build();

        assertThat(playbackInfo.getPlayback(), is(pb));
        assertThat(playbackInfo.getCurrentCue(), is(cc));
        assertThat(playbackInfo.getNextCue(), is(nc));

        assertThat(playbackInfo.toString(), containsString("PlaybackInfo"));
    }

    /**
     * Check with optional arguments.
     */
    @Test
    public void testConstructorOptionalArguments()
    {

        PlaybackInfo playbackInfo = new PlaybackInfo.Builder()
                .setPlayback(pb)
                .build();

        assertThat(playbackInfo.getPlayback(), is(pb));
        assertThat(playbackInfo.getCurrentCue(), nullValue());
        assertThat(playbackInfo.getNextCue(), nullValue());

        assertThat(playbackInfo.toString(), containsString("PlaybackInfo"));
    }

    /**
     * A null playback will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void nullPlayback()
    {
        new PlaybackInfo.Builder().build();
    }
}
