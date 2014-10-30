package com.interactive.cueserver.data.playback;

import com.interactive.cueserver.data.playback.PlaybackInfo;
import com.interactive.cueserver.data.playback.PlaybackStatus;
import org.junit.Test;

import static junit.framework.TestCase.assertSame;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests the {@link PlaybackStatus}.
 *
 * author: Chris Reising
 */
public class PlaybackStatusTest
{
    /** Test value for playback 1. */
    private final PlaybackInfo pb1 = mock(PlaybackInfo.class);

    /** Test value for playback 2. */
    private final PlaybackInfo pb2 = mock(PlaybackInfo.class);

    /** Test value for playback 3. */
    private final PlaybackInfo pb3 = mock(PlaybackInfo.class);

    /** Test value for playback 4. */
    private final PlaybackInfo pb4 = mock(PlaybackInfo.class);

    /**
     * Sets the values of the builder and assert the built object.
     */
    @Test
    public void buildInfo()
    {
        PlaybackStatus.PlaybackStatusBuilder builder =
                new PlaybackStatus.PlaybackStatusBuilder();

        builder.setPlayback1(pb1);
        builder.setPlayback2(pb2);
        builder.setPlayback3(pb3);
        builder.setPlayback4(pb4);

        PlaybackStatus status = builder.build();

        assertSame(status.getPlayback1(), pb1);
        assertSame(status.getPlayback2(), pb2);
        assertSame(status.getPlayback3(), pb3);
        assertSame(status.getPlayback4(), pb4);

        assertThat(status.toString(), containsString("playback"));
    }

    /**
     * Playback 1 not being set will result in an exception.
     */
    @Test(expected = NullPointerException.class)
    public void missingPb1()
    {
        PlaybackStatus.PlaybackStatusBuilder builder =
                new PlaybackStatus.PlaybackStatusBuilder();

        builder.setPlayback2(pb2);
        builder.setPlayback3(pb3);
        builder.setPlayback4(pb4);

        builder.build();
    }

    /**
     * Playback 2 not being set will result in an exception.
     */
    @Test(expected = NullPointerException.class)
    public void missingPb2()
    {
        PlaybackStatus.PlaybackStatusBuilder builder =
                new PlaybackStatus.PlaybackStatusBuilder();

        builder.setPlayback1(pb1);
        builder.setPlayback3(pb3);
        builder.setPlayback4(pb4);

        builder.build();
    }

    /**
     * Playback 3 not being set will result in an exception.
     */
    @Test(expected = NullPointerException.class)
    public void missingPb3()
    {
        PlaybackStatus.PlaybackStatusBuilder builder =
                new PlaybackStatus.PlaybackStatusBuilder();

        builder.setPlayback1(pb1);
        builder.setPlayback2(pb2);
        builder.setPlayback4(pb4);

        builder.build();
    }

    /**
     * Playback 4 not being set will result in an exception.
     */
    @Test(expected = NullPointerException.class)
    public void missingPb4()
    {
        PlaybackStatus.PlaybackStatusBuilder builder =
                new PlaybackStatus.PlaybackStatusBuilder();

        builder.setPlayback1(pb1);
        builder.setPlayback2(pb2);
        builder.setPlayback3(pb3);

        builder.build();
    }
}
