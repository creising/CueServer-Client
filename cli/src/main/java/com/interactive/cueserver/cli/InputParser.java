package com.interactive.cueserver.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple class for parsing input from the console.
 *
 * author: creising
 * Date: 7/25/14
 * Time: 10:47 PM
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
}
