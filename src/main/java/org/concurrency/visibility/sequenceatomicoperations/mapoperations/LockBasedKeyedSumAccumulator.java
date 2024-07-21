package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import static org.concurrency.visibility.sequenceatomicoperations.mapoperations.KeyedAccumulatorUtils.getValidSum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe implementation of KeyedSumAccumulator using explicit locks.
 * This ensures that the add and get operations are atomic and thread-safe.
 */
public final class LockBasedKeyedSumAccumulator implements KeyedSumAccumulator
{
    private final Map<String, Integer> map = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    @Override
    public void add(String key, int value)
    {
        lock.lock();
        try
        {
            map.compute(key, (k, currentSum) -> (int) getValidSum(value, currentSum));
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Integer getSum(String key)
    {
        lock.lock();
        try
        {
            return map.get(key);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public List<Map.Entry<String, Integer>> getState()
    {
        lock.lock();
        try
        {
            return new ArrayList<>(map.entrySet());
        }
        finally
        {
            lock.unlock();
        }
    }
}
