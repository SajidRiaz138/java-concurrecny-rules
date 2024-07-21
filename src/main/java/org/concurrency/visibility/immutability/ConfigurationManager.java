package org.concurrency.visibility.immutability;

public interface ConfigurationManager
{
    /**
     * Gets the current Configuration instance.
     *
     * @return the current Configuration instance.
     */
    Configuration getConfiguration();

    /**
     * Updates the Configuration instance.
     *
     * @param newConfig the new Configuration instance.
     */
    void updateConfiguration(Configuration newConfig);
}
