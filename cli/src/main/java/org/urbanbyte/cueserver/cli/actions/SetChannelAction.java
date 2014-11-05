package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;
import org.urbanbyte.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for setting the level of a channel.
 * <p>
 * author: Chris Reising
 */
public class SetChannelAction implements Action
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
    public SetChannelAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Set the level of a channel";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        int channel = parser.readInt("Enter a channel number: ");
        int value = parser.readInt("Enter a level within [0, 255]: ");
        double time = parser.readDouble("Enter a fade time: ");
        Playback playback = parser.readPlayback(
                "Enter the playback number [1, 4]: ");
        client.setChannel(channel, value, time, playback);
    }
}
