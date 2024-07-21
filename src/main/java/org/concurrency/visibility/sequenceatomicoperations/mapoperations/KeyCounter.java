package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class KeyCounter
{
    private final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>();
    private final Object lock = new Object();

    public void increment(String key)
    {
        AtomicInteger value = new AtomicInteger();
        AtomicInteger old = map.putIfAbsent(key, value);

        if (old != null)
        {
            value = old;
        }

        synchronized (lock)
        {
            if (value.get() == Integer.MAX_VALUE)
            {
                throw new ArithmeticException("Out of range");
            }
            value.incrementAndGet(); // Increment the value atomically
        }
    }

    public Integer getCount(String key)
    {
        AtomicInteger value = map.get(key);
        return (value == null) ? null : value.get();
    }

    public static void main(String[] args)
    {
        KeyCounter counter = new KeyCounter();
        for (int i = 1; i <= 10; i++)
        {
            counter.increment("key1");
        }

        System.out.println(counter.getCount("key1"));
    }
}
