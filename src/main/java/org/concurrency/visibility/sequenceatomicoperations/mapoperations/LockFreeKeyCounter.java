package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeKeyCounter
{
    private final Map<String, AtomicInteger> map = new ConcurrentHashMap<>();

    public void increment(String key)
    {
        map.compute(key, (k, value) ->
        {
            if (value == null)
            {
                value = new AtomicInteger();
            }
            while (true)
            {
                int current = value.get();
                if (current == Integer.MAX_VALUE)
                {
                    throw new ArithmeticException("Out of range");
                }
                int newValue = current + 1;
                if (value.compareAndSet(current, newValue))
                {
                    return value;
                }
            }
        });
    }

    public Integer getCount(String key)
    {
        AtomicInteger value = map.get(key);
        return (value == null) ? null : value.get();
    }

    public static void main(String[] args)
    {
        LockFreeKeyCounter counter = new LockFreeKeyCounter();
        for (int i = 1; i <= 10; i++)
        {
            counter.increment("key1");
        }

        System.out.println(counter.getCount("key1"));
    }
}
