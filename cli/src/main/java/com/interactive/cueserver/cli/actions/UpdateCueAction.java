package com.interactive.cueserver.cli.actions;

import com.interactive.cueserver.CueServerClient;
import com.interactive.cueserver.cli.InputParser;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for updating a cue.
 * <p>
 * author: Chris Reising
 */
public class UpdateCueAction implements Action
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
    public UpdateCueAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Update a cue";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        double cueNumber =  parser.readDouble("Enter cue number: ");
        client.updateCue(cueNumber);
    }
}
