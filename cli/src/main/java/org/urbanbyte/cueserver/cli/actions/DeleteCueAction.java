package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for updating a cue.
 * <p>
 * author: Chris Reising
 */
public class DeleteCueAction implements Action
{
    /** CueServer client */
    private final CueServerClient client;

    /** For reading input from the user. */
    private final InputParser parser = new InputParser();

    /**
     * Creates a new {@code DetailedPlaybackStatusAction}.
     *
     * @param client client to retrieve from.
     */
    public DeleteCueAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Delete a cue";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        double cueNumber =  parser.readDouble("Enter cue number: ");
        client.deleteCue(cueNumber);
    }
}
