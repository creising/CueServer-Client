package com.interactive.cueserver.cli.actions;

import com.interactive.cueserver.CueServerClient;
import com.interactive.cueserver.cli.InputParser;
import com.interactive.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for recording a cue.
 * <p>
 * author: Chris Reising
 */
public class RecordCueAction implements Action
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
    public RecordCueAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Record a cue";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        double cueNumber =  parser.readDouble("Enter cue number: ");
        int uptime = parser.readInt("Enter an uptime: ");
        int downtime = parser.readInt("Enter a downtime: ");
        client.recordCue(cueNumber, uptime, downtime);
    }
}
