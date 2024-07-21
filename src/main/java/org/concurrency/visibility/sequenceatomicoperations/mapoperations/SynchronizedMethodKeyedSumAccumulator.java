package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A thread-safe implementation of KeyedSumAccumulator using synchronized methods.
 * This ensures that the add and get operations are atomic and thread-safe.
 */
public final class SynchronizedMethodKeyedSumAccumulator implements KeyedSumAccumulator
{
    private final Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());

    @Override
    public synchronized void add(String key, int value)
    {
        Integer currentSum = map.get(key);
        long newSum = KeyedAccumulatorUtils.getValidSum(value, currentSum);
        map.put(key, (int) newSum);
    }

    @Override
    public synchronized Integer getSum(String key)
    {
        return map.get(key);
    }

    @Override
    public synchronized List<Map.Entry<String, Integer>> getState()
    {
        return new ArrayList<>(map.entrySet());
    }
}
