package org.concurrency.locks.consistentlockingpolicy;

public class ThreadSafeGlobalCounter
{
    private static int counter;
    private static final Object lock = new Object(); // make sure lock is private static and final


    // lock static data with static locks other different locks for different objects
    public void increment()
    {
        synchronized (lock)
        {
            counter++;
        }
    }

    public int getCounter()
    {
        return counter;
    }

}
