package com.interactive.cueserver.data.playback;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests {@link Playback}.
 * <p>
 * author: Chris Reising
 */
public class PlaybackTest
{
    /**
     * Test values of the playbacks
     */
    @Test
    public void playbackValues()
    {
        assertThat(Playback.PLAYBACK_1.getPlaybackId(), is(1));
        assertThat(Playback.PLAYBACK_2.getPlaybackId(), is(2));
        assertThat(Playback.PLAYBACK_3.getPlaybackId(), is(3));
        assertThat(Playback.PLAYBACK_4.getPlaybackId(), is(4));
    }
}
