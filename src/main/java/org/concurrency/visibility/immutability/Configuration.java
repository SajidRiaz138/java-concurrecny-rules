package org.concurrency.visibility.immutability;

public final class Configuration
{
    private final String databaseUrl;
    private final int maxConnections;

    public Configuration(String databaseUrl, int maxConnections)
    {
        this.databaseUrl = databaseUrl;
        this.maxConnections = maxConnections;
    }

    public String getDatabaseUrl()
    {
        return databaseUrl;
    }

    public int getMaxConnections()
    {
        return maxConnections;
    }
}
