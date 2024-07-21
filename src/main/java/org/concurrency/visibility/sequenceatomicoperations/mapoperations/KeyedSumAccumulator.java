package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

import java.util.List;
import java.util.Map;

/**
 * Interface for an accumulator that maintains sums associated with keys.
 * Implementations of this interface should ensure thread safety.
 */
public interface KeyedSumAccumulator
{
    /**
     * Adds a value to the sum associated with the given key.
     * Implementations should ensure that this operation is thread-safe.
     *
     * @param key the key whose associated sum is to be incremented
     * @param value the value to add to the sum
     * @throws ArithmeticException if the sum exceeds Integer.MAX_VALUE
     */
    void add(String key, int value);

    /**
     * Returns the sum associated with the given key.
     * Implementations should ensure that this operation is thread-safe.
     *
     * @param key the key whose associated sum is to be returned
     * @return the sum associated with the given key
     */
    Integer getSum(String key);

    /**
     * Returns a copy of the current state of the accumulator.
     * The copy should include all keys and their associated sums.
     *
     * @return a copy of the current state of the accumulator
     */
    List<Map.Entry<String, Integer>> getState();
}
