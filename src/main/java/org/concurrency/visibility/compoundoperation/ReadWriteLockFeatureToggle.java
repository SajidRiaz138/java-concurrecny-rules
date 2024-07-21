package org.concurrency.visibility.compoundoperation;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

/**
 * FeatureToggle implementation using ReadWriteLock.
 * <p>
 * Fine-Grained Locking: ReadWriteLock allows multiple threads to read the feature 
 * state concurrently but only one thread to update it at a time.
 * <p>
 * Read Performance: This approach improves performance for read-heavy workloads 
 * by allowing multiple concurrent reads.
 */
public final class ReadWriteLockFeatureToggle implements FeatureToggle
{
    private boolean featureEnabled = true;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    @Override
    public void toggle()
    {
        writeLock.lock();
        try
        {
            featureEnabled = !featureEnabled;
        }
        finally
        {
            writeLock.unlock();
        }
    }

    @Override
    public boolean isFeatureEnabled()
    {
        readLock.lock();
        try
        {
            return featureEnabled;
        }
        finally
        {
            readLock.unlock();
        }
    }
}
