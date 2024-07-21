package org.concurrency.locks.singleton;

/**
 * The Singleton class implements a singleton design pattern using double-checked locking.
 * This ensures that the singleton instance is created in a thread-safe manner with minimal synchronization overhead.
 */
public final class DoubleCheckSingleton
{

    // Volatile variable to ensure visibility of changes across threads
    private static volatile DoubleCheckSingleton instance;

    // Private constructor to prevent instantiation
    private DoubleCheckSingleton()
    {
        // Initialization code here
    }

    /**
     * Returns the singleton instance of the Singleton class.
     * This method uses double-checked locking to minimize synchronization overhead
     * and ensure thread safety.
     *
     * @return the singleton instance
     */
    public static DoubleCheckSingleton getInstance()
    {
        if (instance == null)
        { // First null check (without synchronization)
            synchronized (DoubleCheckSingleton.class)
            {
                if (instance == null)
                { // Second null check (with synchronization)
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

    // Other methods and members of the Singleton class
}
