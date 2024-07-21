package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import static org.concurrency.visibility.sequenceatomicoperations.mapoperations.KeyedAccumulatorUtils.getValidSum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe implementation of KeyedSumAccumulator using ConcurrentHashMap.
 * This ensures that the add and get operations are atomic and thread-safe without explicit synchronization.
 */
public final class ConcurrentHashMapKeyedSumAccumulator implements KeyedSumAccumulator
{
    private final Map<String, Integer> map = new ConcurrentHashMap<>();

    @Override
    public void add(String key, int value)
    {
        map.merge(key, value, (oldValue, newValue) -> (int) getValidSum(newValue, oldValue));
    }

    @Override
    public Integer getSum(String key)
    {
        return map.get(key);
    }

    @Override
    public List<Map.Entry<String, Integer>> getState()
    {
        return new ArrayList<>(map.entrySet());
    }
}
