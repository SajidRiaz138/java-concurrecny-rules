package org.concurrency.locks.singleton;

public class EagerInitialization
{
    private static final EagerInitialization INSTANCE = new EagerInitialization();

    public EagerInitialization getInstance()
    {
        return INSTANCE;
    }
}
