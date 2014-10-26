package com.interactive.cueserver.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link PlaybackInfo}.
 *
 * author: Chris Reising
 */
public class PlaybackInfoTest
{
    /**
     * Test with valid arguments.
     */
    @Test
    public void testConstructorAllArguments()
    {
        int pb = 1;
        double cc = 2;
        double nc = 3;

        PlaybackInfo playbackInfo = new PlaybackInfo(pb, cc, nc);

        assertThat(playbackInfo.getPlaybackNumber(), is(pb));
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
        int pb = 1;

        PlaybackInfo playbackInfo = new PlaybackInfo(pb, null, null);

        assertThat(playbackInfo.getPlaybackNumber(), is(pb));
        assertThat(playbackInfo.getCurrentCue(), nullValue());
        assertThat(playbackInfo.getNextCue(), nullValue());

        assertThat(playbackInfo.toString(), containsString("PlaybackInfo"));
    }

    /**
     * Invalid playback number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidPbNum()
    {
        new PlaybackInfo(0, null, null);
    }

    /**
     * Invalid current cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCcNum()
    {
        new PlaybackInfo(1, 0d, null);
    }

    /**
     * Invalid next cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidNcNum()
    {
        new PlaybackInfo(1, 1d, 0d);
    }
}
