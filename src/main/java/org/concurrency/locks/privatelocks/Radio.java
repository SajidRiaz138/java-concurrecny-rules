package org.concurrency.locks.privatelocks;

/**
 * Thread-safe public classes that both use intrinsic synchronization over the class object and may interact
 * with untrusted code must use a static private final lock object and block synchronization.
 */

public final class Radio
{
    private static final Object lock = new Object();
    private int volume;

    public Radio(final int volume)
    {
        this.volume = volume;
    }

    // increaseVolume() obtains a lock on a private static Object that is inaccessible to the caller.
    public void increaseVolume()
    {
        synchronized (lock)
        {
            volume++;
        }
    }

    public int getVolume()
    {
        return volume;
    }

}
