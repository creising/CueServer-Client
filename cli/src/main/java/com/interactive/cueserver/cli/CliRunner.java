package com.interactive.cueserver.cli;

import com.interactive.cueserver.CueServerClient;
import com.interactive.cueserver.cli.actions.DetailedPlaybackStatusAction;
import com.interactive.cueserver.cli.actions.DmxOutputAction;
import com.interactive.cueserver.cli.actions.PlaybackStatusAction;
import com.interactive.cueserver.http.HttpCueServerClient;
import com.interactive.cueserver.cli.actions.Action;
import com.interactive.cueserver.cli.actions.SystemInfoAction;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple command line interface for accessing a CueSever using the java
 * client library.
 * <p>
 * author: Chris Reising
 */
public class CliRunner
{
    /** CS actions. */
    private final List<Action> actions;

    /** For reading inputs from the user. */
    private final InputParser parser;

    /**
     * Creates a new {@code CliRunner}.
     *
     * @param actions actions available to the user.
     */
    public CliRunner(List<Action> actions)
    {
        this.actions = actions;
        this.parser = new InputParser();
    }

    /**
     * Runs the CLI.
     */
    public void run()
    {
        while(true)
        {
            printList();
            Integer option = parser.readInt("Enter option ");
            if(option != null && option > 0 && option <= actions.size())
            {
                System.out.println("Running action " + option);
                actions.get(option - 1).executeAction();
            }
            else
            {
                System.out.printf("%d is not a valid option.\n", option);
            }
        }
    }

    /**
     * Prints all of the available options to the user.
     */
    private void printList()
    {
        System.out.println("------------------------------");

        for(int index = 0 ; index < actions.size() ; index++)
        {
            System.out.printf("%d: %s\n", index + 1,
                    actions.get(index).getDescription());
        }

        System.out.println("------------------------------");

    }

    /**
     * Main method for the CLI.
     *
     * @param args the user can pass in an optional URL for a CueServer. If
     *             the user does not provide a URL, the CLI will use a well
     *             known test server.
     */
    public static void main(String[] args)
    {
        String url;
        if(args.length == 0)
        {
            System.out.println("Using http://cueserver.dnsalias.com for the " +
                    "URL.");
            url = "http://cueserver.dnsalias.com";
        }
        else
        {
            url = args[0];
        }

        CueServerClient client = new HttpCueServerClient(url);

        List<Action> csActions = new ArrayList<Action>();
        csActions.add(new SystemInfoAction(client));
        csActions.add(new PlaybackStatusAction(client));
        csActions.add(new DetailedPlaybackStatusAction(client));
        csActions.add(new DmxOutputAction(client));

        CliRunner runner = new CliRunner(csActions);
        runner.run();
    }
}
