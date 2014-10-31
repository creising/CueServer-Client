package com.interactive.cueserver.data.cue;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests {@link Cue}
 *
 * author: Chris Reising
 */
public class CueTest
{
    /** Test cue number. */
    private final double num = 0.1;

    /** Test name. */
    private final String name = "name";

    /**
     * Test creating the object.
     */
    @Test
    public void testObject()
    {
        Cue cue = new Cue(num, name);

        assertThat(cue.getNumber(), is(num));
        assertThat(cue.getName(), is(name));
    }

    /**
     * Test minimal constructor
     */
    @Test
    public void nullName()
    {
        Cue cue = new Cue(num);

        assertThat(cue.getNumber(), is(num));
        assertThat(cue.getName(), nullValue());
    }

    /**
     * A non-positive cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nonPositiveNum()
    {
        new Cue(0, name);
    }
}
