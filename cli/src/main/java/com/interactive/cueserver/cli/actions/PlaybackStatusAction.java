package com.interactive.cueserver.cli.actions;

import com.interactive.cueserver.CueServerClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for retrieving the playback status from a CueServer.
 * <p>
 * author: Chris Reising
 */
public class PlaybackStatusAction implements Action
{
    /** Client to retrieve data from. */
    private final CueServerClient client;

    /**
     * Creates a new {@code PlaybackStatusAction}.
     *
     * @param client client to retrieve from.
     */
    public PlaybackStatusAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Retrieve the current playback status from the CueServer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        System.out.println(client.getPlaybackStatus());
    }
}
