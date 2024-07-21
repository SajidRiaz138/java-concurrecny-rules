package org.concurrency.locks.singleton;

public class HolderIdiomSingleton
{
    // Lazy initialization
    static class Holder
    {
        static HolderIdiomSingleton singleton = new HolderIdiomSingleton();
    }

    public static HolderIdiomSingleton getInstance()
    {
        return Holder.singleton;
    }
}
