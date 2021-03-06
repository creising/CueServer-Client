package org.urbanbyte.cueserver.cli;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.actions.ClearPlaybackAction;
import org.urbanbyte.cueserver.cli.actions.DeleteCueAction;
import org.urbanbyte.cueserver.cli.actions.DetailedPlaybackStatusAction;
import org.urbanbyte.cueserver.cli.actions.DmxOutputAction;
import org.urbanbyte.cueserver.cli.actions.PlayCueAction;
import org.urbanbyte.cueserver.cli.actions.PlaybackStatusAction;
import org.urbanbyte.cueserver.cli.actions.RecordCueAction;
import org.urbanbyte.cueserver.cli.actions.SetChannelAction;
import org.urbanbyte.cueserver.cli.actions.SetChannelRangeAction;
import org.urbanbyte.cueserver.cli.actions.UpdateCueAction;
import org.urbanbyte.cueserver.http.HttpCueServerClient;
import org.urbanbyte.cueserver.cli.actions.Action;
import org.urbanbyte.cueserver.cli.actions.SystemInfoAction;

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
        int port;
        if(args.length == 0)
        {
            System.out.println("Using http://demo1.cueserver.com for the " +
                    "URL.");
            url = "http://demo1.cueserver.com";
            port = 100;
        }
        else
        {
            url = args[0];
            port = Integer.parseInt(args[1]);
        }
        CueServerClient client = new HttpCueServerClient(url, port);

        List<Action> csActions = new ArrayList<Action>();
        csActions.add(new SystemInfoAction(client));
        csActions.add(new PlaybackStatusAction(client));
        csActions.add(new DetailedPlaybackStatusAction(client));
        csActions.add(new DmxOutputAction(client));
        csActions.add(new PlayCueAction(client));
        csActions.add(new ClearPlaybackAction(client));
        csActions.add(new SetChannelAction(client));
        csActions.add(new SetChannelRangeAction(client));
        csActions.add(new RecordCueAction(client));
        csActions.add(new UpdateCueAction(client));
        csActions.add(new DeleteCueAction(client));

        CliRunner runner = new CliRunner(csActions);
        runner.run();
    }
}
