package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;
import org.urbanbyte.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for setting a level for a range of channels.
 * <p>
 * author: Chris Reising
 */
public class SetChannelRangeAction implements Action
{
    /** Client to fire cue */
    private final CueServerClient client;

    /** For reading input from the user. */
    private final InputParser parser = new InputParser();

    /**
     * Creates a new {@code DetailedPlaybackStatusAction}.
     *
     * @param client client to retrieve from.
     */
    public SetChannelRangeAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Set the level for a range of channels";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        int startCh = parser.readInt("Enter the start channel number: ");
        int endCh = parser.readInt("Enter the end channel number: ");
        int value = parser.readInt("Enter a level within [0, 255]: ");
        double time = parser.readDouble("Enter a fade time: ");
        Playback playback = parser.readPlayback(
                "Enter the playback number [1, 4]: ");
        client.setChannelRange(startCh, endCh, value, time, playback);
    }
}
