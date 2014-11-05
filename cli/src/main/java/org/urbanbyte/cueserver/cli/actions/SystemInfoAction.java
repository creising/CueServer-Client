package org.urbanbyte.cueserver.cli.actions;

import org.urbanbyte.cueserver.CueServerClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Get the system info from a CueServer.
 * <p>
 * author: Chris Reising
 */
public class SystemInfoAction implements Action
{
    /** Client to retrieve data from. */
    private final CueServerClient client;

    /**
     * Creates a new {@code SystemInfoAction}.
     *
     * @param client client to retrieve from.
     */
    public SystemInfoAction(CueServerClient client)
    {
        this.client = checkNotNull(client, "client cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "Gets a CueServer's system information";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAction()
    {
        System.out.println(client.getSystemInfo());
    }
}
