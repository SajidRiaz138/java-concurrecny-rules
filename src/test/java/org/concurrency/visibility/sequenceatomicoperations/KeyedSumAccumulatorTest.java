package org.concurrency.visibility.sequenceatomicoperations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.concurrency.visibility.sequenceatomicoperations.mapoperations.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class KeyedSumAccumulatorTest
{

    // Method source to provide instances of all implementations
    private Stream<Arguments> accumulatorProvider()
    {
        return Stream.of(
                Arguments.of(new SynchronizedMethodKeyedSumAccumulator()),
                Arguments.of(new SynchronizedBlockKeyedSumAccumulator()),
                Arguments.of(new ConcurrentHashMapKeyedSumAccumulator()),
                Arguments.of(new LockBasedKeyedSumAccumulator()));
    }

    @ParameterizedTest
    @MethodSource ("accumulatorProvider")
    void testOverflow(KeyedSumAccumulator accumulator)
    {
        // Assuming Integer.MAX_VALUE is 2,147,483,647
        accumulator.add("key1", Integer.MAX_VALUE);

        // This should cause an overflow
        assertThrows(ArithmeticException.class, () -> accumulator.add("key1", 1));
    }

    @ParameterizedTest
    @MethodSource ("accumulatorProvider")
    void testAddAndGetSum(KeyedSumAccumulator accumulator)
    {
        accumulator.add("key1", 5);
        accumulator.add("key1", 10);
        accumulator.add("key2", 20);
        assertEquals(15, accumulator.getSum("key1"));
        assertEquals(20, accumulator.getSum("key2"));
        assertNull(accumulator.getSum("key3")); // Assuming getSum returns null for non-existing keys
    }

    @ParameterizedTest
    @MethodSource ("accumulatorProvider")
    void testConcurrency(KeyedSumAccumulator accumulator) throws InterruptedException
    {
        int threadCount = 10;
        int itemsPerThread = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Create and start threads
        IntStream.range(0, threadCount).forEach(i -> executor.submit(() ->
        {
            try
            {
                IntStream.range(0, itemsPerThread).forEach(j -> accumulator.add("key1", 1));
            }
            finally
            {
                latch.countDown();
            }
        }));

        // Wait for all threads to finish
        latch.await(1, TimeUnit.MINUTES);
        executor.shutdown();

        // Verify the final sum
        int expectedSum = threadCount * itemsPerThread;
        assertEquals(expectedSum, accumulator.getSum("key1"));
    }

    @ParameterizedTest
    @MethodSource ("accumulatorProvider")
    void testGetState(KeyedSumAccumulator accumulator)
    {
        accumulator.add("key1", 5);
        accumulator.add("key2", 10);
        List<Map.Entry<String, Integer>> state = accumulator.getState();

        // Convert state to a map for easier assertions
        Map<String, Integer> stateMap = state.stream()
                .collect(
                        java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        assertEquals(5, (int) stateMap.get("key1"));
        assertEquals(10, (int) stateMap.get("key2"));
    }
}
