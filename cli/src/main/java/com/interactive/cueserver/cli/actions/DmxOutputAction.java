package com.interactive.cueserver.cli.actions;

import com.interactive.cueserver.CueServerClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Example for retrieving the DMX output values.
 * <p>
 * author: Chris Reising
 */
public class DmxOutputAction implements Action
{
    /** Client to retrieve data from. */
    private final CueServerClient client;

    /**
     * Creates a new {@code PlaybackStatusAction}.
     *
     * @param client client to retrieve from.
     */
    public DmxOutputAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Retrieve the current DMX output from the CueServer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        Integer[] values = client.getOutputLevels();

        for(int i = 0 ; i < values.length ; i++)
        {
            System.out.printf("Ch: %d Value: %d |", i +1, values[i]);
            if(i % 20 == 0)
            {
                System.out.println();
            }
        }
    }
}
