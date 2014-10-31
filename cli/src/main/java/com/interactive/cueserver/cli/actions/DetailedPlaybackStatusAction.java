package com.interactive.cueserver.cli.actions;

import com.interactive.cueserver.CueServerClient;
import com.interactive.cueserver.cli.InputParser;
import com.interactive.cueserver.data.playback.Playback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for retrieving the playback status from a CueServer.
 *
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
        int playbackNum = parser.readInt("Enter playback number: ");
        System.out.println(client.getDetailedPlaybackInfo(
                getPlaybackForId(playbackNum)));
    }

    /**
     * Converts the given int to a playback.
     *
     * @param id the id.
     * @return Never {@code null}.
     * @throws IllegalArgumentException if the ID cannot be mapped.
     */
    public static Playback getPlaybackForId(int id)
    {
        Playback playback;
        switch (id)
        {
            case 1:
                playback =  Playback.PLAYBACK_1;
                break;
            case 2:
                playback = Playback.PLAYBACK_2;
                break;
            case 3:
                playback = Playback.PLAYBACK_3;
                break;
            case 4:
                playback = Playback.PLAYBACK_4;
                break;
            default:
                throw new IllegalArgumentException(
                        "Playback ID unknown: " + id);
        }
        return playback;
    }
}
