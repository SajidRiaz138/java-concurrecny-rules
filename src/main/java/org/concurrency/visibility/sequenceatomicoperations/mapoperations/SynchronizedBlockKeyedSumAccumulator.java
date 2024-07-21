package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A thread-safe implementation of KeyedSumAccumulator using synchronized blocks.
 * This ensures that the add and get operations are atomic and thread-safe.
 */
public final class SynchronizedBlockKeyedSumAccumulator implements KeyedSumAccumulator
{
    private final Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void add(String key, int value)
    {
        synchronized (map)
        {
            Integer currentSum = map.get(key);
            long newSum = KeyedAccumulatorUtils.getValidSum(value,currentSum);
            map.put(key, (int) newSum);
        }
    }

    @Override
    public Integer getSum(String key)
    {
        synchronized (map)
        {
            return map.get(key);
        }
    }

    @Override
    public List<Map.Entry<String, Integer>> getState()
    {
        synchronized (map)
        {
            return new ArrayList<>(map.entrySet());
        }
    }
}
