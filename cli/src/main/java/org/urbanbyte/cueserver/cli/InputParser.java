package org.urbanbyte.cueserver.cli;

import org.urbanbyte.cueserver.data.playback.Playback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple class for parsing input from the console.
 * <p>
 * author: Chris Reising
 */
public class InputParser
{
    /** For reading input. */
    private final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads the input from the user and attempts to parse it into an integer.
     *
     * @param prompt displayed to the user before parsing.
     * @return the integer value of the input, or {@code null} if the input
     *          provided could not be parsed.
     */
    public Integer readInt(String prompt)
    {
        System.out.print(prompt);
        Integer integer = null;

        try
        {
            String input = reader.readLine();
            integer = Integer.parseInt(input);
        }
        catch (IOException e)
        {
            System.out.println("You must enter a number");
        }
        return integer;
    }

    /**
     * Reads the input from the user and attempts to parse it into an double.
     *
     * @param prompt displayed to the user before parsing.
     * @return the double value of the input, or {@code null} if the input
     *          provided could not be parsed.
     */
    public Double readDouble(String prompt)
    {
        System.out.print(prompt);
        Double doubleValue = null;

        try
        {
            String input = reader.readLine();
            doubleValue = Double.parseDouble(input);
        }
        catch (IOException e)
        {
            System.out.println("You must enter a number");
        }
        return doubleValue;
    }

    /**
     * Gets a playback based on the input from the user.
     *
     * @param prompt displayed to the user before parsing.
     * @return The playback selected by the user.
     * @throws IllegalArgumentException if the playback could not be found for
     *                                  the provided input.
     */
    public Playback readPlayback(String prompt)
    {
        return getPlaybackForId(readInt(prompt));
    }

    /**
     * Converts the given int to a playback.
     *
     * @param id the id.
     * @return Never {@code null}.
     * @throws IllegalArgumentException if the ID cannot be mapped.
     */
    public static Playback getPlaybackForId(int id)
    {
        Playback playback;
        switch (id)
        {
            case 1:
                playback =  Playback.PLAYBACK_1;
                break;
            case 2:
                playback = Playback.PLAYBACK_2;
                break;
            case 3:
                playback = Playback.PLAYBACK_3;
                break;
            case 4:
                playback = Playback.PLAYBACK_4;
                break;
            default:
                throw new IllegalArgumentException(
                        "Playback ID unknown: " + id);
        }
        return playback;
    }
}
