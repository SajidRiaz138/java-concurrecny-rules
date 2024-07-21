package org.concurrency.visibility.compoundoperation;

/**
 * FeatureToggle implementation using volatile with synchronized toggle.
 * <p>
 * Visibility: The volatile keyword ensures that updates to the featureEnabled 
 * field are immediately visible to all threads.
 * <p>
 * Atomicity: The synchronized toggle method ensures that the toggling operation 
 * is atomic, preventing race conditions during the update.
 */
public final class VolatileFeatureToggle implements FeatureToggle
{
    private volatile boolean featureEnabled = true;

    @Override
    public synchronized void toggle()
    {
        featureEnabled = !featureEnabled;
    }

    @Override
    public boolean isFeatureEnabled()
    {
        return featureEnabled;
    }
}
