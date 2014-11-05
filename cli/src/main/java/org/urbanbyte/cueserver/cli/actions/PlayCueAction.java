package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.cli.InputParser;
import org.urbanbyte.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for playing a cue on a playback
 * <p>
 * author: Chris Reising
 */
public class PlayCueAction implements Action
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
    public PlayCueAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Play a cue on a playback";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        double cueNumber =  parser.readDouble("Enter cue number: ");
        Playback playback = parser.readPlayback("Enter cue number to play the" +
                " cue on: ");
        client.playCue(cueNumber, playback);
    }
}
