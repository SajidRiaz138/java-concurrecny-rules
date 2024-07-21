package org.concurrency.locks.singleton;

/**
 * The ResourceManager class lazily initializes a ResourceConnection instance in a thread-safe manner
 * using double-checked locking and a local variable.
 * This ensures minimal synchronization overhead while maintaining thread safety.
 */
public final class ResourceManager
{
    private volatile ResourceConnection resourceConnection;

    public ResourceManager()
    {
        // Initialization code if necessary
    }

    /**
     * Returns the ResourceConnection instance.
     * This method uses double-checked locking to minimize synchronization overhead
     * and ensure thread safety.
     *
     * @return the ResourceConnection instance
     */
    public ResourceConnection getResourceConnection()
    {
        ResourceConnection localConnection = resourceConnection;
        if (localConnection == null)
        {
            synchronized (this)
            {
                localConnection = resourceConnection;
                if (localConnection == null)
                {
                    localConnection = new ResourceConnection("Resource Connection Info");
                    resourceConnection = localConnection;
                }
            }
        }
        return localConnection;
    }

    // Other methods and members of the ResourceManager class
}
