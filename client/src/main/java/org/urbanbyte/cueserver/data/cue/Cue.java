package org.urbanbyte.cueserver.data.cue;


import static com.google.common.base.Preconditions.*;

/**
 * Contains the name and number of a cue.
 * <p>
 * author: Chris Reising
 */
public class Cue
{
    /** The cue number. */
    private final double number;

    /** The name of the cue. */
    private final String name;

    /**
     * Creates a new {@code Cue} with no name.
     *
     * @param number the cue number. Must be greater than 0.
     * @throws IllegalArgumentException if {@code number} is not positive.
     */
    public Cue(double number)
    {
        this(number, null);
    }

    /**
     * Creates a new {@code Cue}.
     *
     * @param number the cue number. Must be greater than 0.
     * @param name the name of the cue. Cannot be {@code null}.
     * @throws IllegalArgumentException if {@code number} is not positive.
     */
    public Cue(double number, String name)
    {
        checkArgument(number > 0, "the cue number must be >= 0");
        this.number = number;
        this.name = name;
    }

    /**
     * Gets the cue number.
     *
     * @return always positive.
     */
    public double getNumber()
    {
        return number;
    }

    /**
     * Gets the name of the cue.
     *
     * @return Can be {@code null}.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "Cue{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}

