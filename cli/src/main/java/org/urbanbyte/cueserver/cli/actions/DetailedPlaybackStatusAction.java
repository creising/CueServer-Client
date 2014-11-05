package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;
import org.urbanbyte.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for retrieving the playback status from a CueServer.
 * <p>
 * author: Chris Reising
 */
public class DetailedPlaybackStatusAction implements Action
{
    /** Client to retrieve data from. */
    private final CueServerClient client;

    /** For reading input from the user. */
    private final InputParser parser = new InputParser();

    /**
     * Creates a new {@code DetailedPlaybackStatusAction}.
     *
     * @param client client to retrieve from.
     */
    public DetailedPlaybackStatusAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Retrieve the detailed playback status from the CueServer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        Playback playback = parser.readPlayback("Enter playback number" +
                "(1-4): ");
        System.out.println(client.getDetailedPlaybackInfo(playback));
    }
}
