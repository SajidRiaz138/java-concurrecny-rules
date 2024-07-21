package org.concurrency.visibility.compoundoperation;

/**
 * FeatureToggle implementation using synchronized methods.
 *<p>
 * Atomicity and Visibility: The synchronized methods ensure that updates to 
 * the featureEnabled field are atomic and visible to all threads.
 * <p>
 * Thread Safety: This approach guarantees that only one thread can read or 
 * update the feature state at a time, preventing race conditions.
 * </p>
 */
public final class SynchronizedFeatureToggle implements FeatureToggle
{
    private boolean featureEnabled = true;

    @Override
    public synchronized void toggle()
    {
        featureEnabled = !featureEnabled;
        System.out.println(Thread.currentThread().getName() + " Status " + featureEnabled);
    }

    @Override
    public synchronized boolean isFeatureEnabled()
    {
        return featureEnabled;
    }
}
