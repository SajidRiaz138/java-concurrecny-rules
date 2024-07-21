package org.concurrency.locks.privatelocks;

/**
 * Use private final lock objects to synchronize classes that may interact with untrusted code
 *
 */

public class Vehicle
{
    private final Object lock = new Object();
    private double price;

    public Vehicle(final double price)
    {
        this.price = price;
    }

    public void updatePrice(final double price)
    {
        synchronized (lock)
        {
            this.price = price;
        }
    }

    public double getPrice()
    {
        return price;
    }
}
