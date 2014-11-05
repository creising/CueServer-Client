package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;
import org.urbanbyte.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for clearing a playback
 * <p>
 * author: Chris Reising
 */
public class ClearPlaybackAction implements Action
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
    public ClearPlaybackAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Clear a playback";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        Playback playback = parser.readPlayback("Enter the playback to " +
                "clear:  ");
        client.clearPlayback(playback);
    }
}
