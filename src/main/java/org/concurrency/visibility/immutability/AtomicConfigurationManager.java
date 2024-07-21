package org.concurrency.visibility.immutability;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ConfigurationManager implementation using AtomicReference.
 * <p>
 * Atomic Updates: AtomicReference ensures that updates to the configRef are 
 * atomic and visible to all threads.
 * <p>
 * Lock-Free: This approach avoids the overhead of synchronization, potentially 
 * providing better performance under high contention.
 */
public class AtomicConfigurationManager implements ConfigurationManager
{
    private final AtomicReference<Configuration> configRef = new AtomicReference<>();

    @Override
    public Configuration getConfiguration()
    {
        return configRef.get();
    }

    @Override
    public void updateConfiguration(Configuration newConfig)
    {
        configRef.set(newConfig);
    }
}
