package org.concurrency.visibility.immutability;

/**
 * ConfigurationManager implementation using volatile.
 * Visibility: The volatile keyword ensures that updates to the config field
 * are immediately visible to all threads.
 * <p>
 * No Synchronization: This approach does not prevent race conditions if multiple
 * threads call updateConfiguration concurrently, but it ensures visibility of 
 * the latest configuration.
 */
public class VolatileConfigurationManager implements ConfigurationManager
{
    private volatile Configuration config;

    @Override
    public Configuration getConfiguration()
    {
        return config;
    }

    @Override
    public void updateConfiguration(Configuration newConfig)
    {
        config = newConfig;
    }
}
