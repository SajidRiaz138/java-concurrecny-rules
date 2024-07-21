package org.concurrency.visibility.immutability;

/**
 * ConfigurationManager implementation using synchronized methods.
 * <p>
 * Atomicity and Visibility: The synchronized methods ensure that updates to 
 * the config field are atomic and visible to all threads.
 * <p>
 * Thread Safety: This approach guarantees that only one thread can read or 
 * update the configuration at a time, preventing race conditions.
 */
public class SynchronizedConfigurationManager implements ConfigurationManager
{
    private Configuration config;

    @Override
    public synchronized Configuration getConfiguration()
    {
        return config;
    }

    @Override
    public synchronized void updateConfiguration(Configuration newConfig)
    {
        config = newConfig;
    }
}
