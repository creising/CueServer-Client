package com.interactive.cueserver.data.playback;

/**
 * The different modes of a CueServer's playback.
 * <p>
 * author: Chris Reising
 */
public enum CombineMode
{
    /** Merge mode. */
    MERGE,
    /** Combine mode. */
    OVERRIDE,
    /** Scale mode. */
    SCALE,
    /** Mode is unknown. */
    UNKNOWN
}
