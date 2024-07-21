package org.concurrency.visibility.compoundoperation;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FeatureToggle implementation using AtomicBoolean.
 *
 * Atomic Updates: AtomicBoolean ensures that updates to the featureEnabled state 
 * are atomic and visible to all threads.
 *
 * Lock-Free: This approach avoids the overhead of synchronization, providing 
 * better performance under high contention.
 */
public final class AtomicBooleanFeatureToggle implements FeatureToggle
{
    private final AtomicBoolean featureEnabled = new AtomicBoolean(true);

    @Override
    public void toggle()
    {
        boolean temp;
        do
        {
            temp = featureEnabled.get();
        }
        while (!featureEnabled.compareAndSet(temp, !temp));
    }

    @Override
    public boolean isFeatureEnabled()
    {
        return featureEnabled.get();
    }
}
