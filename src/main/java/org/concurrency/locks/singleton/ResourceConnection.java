package org.concurrency.locks.singleton;

/**
 * ResourceConnection represents a connection to an external resource.
 */
public final class ResourceConnection
{
    private final String connectionInfo;

    public ResourceConnection(String connectionInfo)
    {
        this.connectionInfo = connectionInfo;
        // Simulate time-consuming connection setup
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public String getConnectionInfo()
    {
        return connectionInfo;
    }

    // Other methods related to resource operations
}
