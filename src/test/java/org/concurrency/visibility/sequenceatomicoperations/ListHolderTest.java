package org.concurrency.visibility.sequenceatomicoperations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.concurrency.visibility.sequenceatomicoperations.listholder.ListHolder;
import org.concurrency.visibility.sequenceatomicoperations.listholder.SynchronizedBlockListHolder;
import org.concurrency.visibility.sequenceatomicoperations.listholder.SynchronizedMethodListHolder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class ListHolderTest
{

    // Method source to provide instances of both implementations
    private Stream<Arguments> listHolderProvider()
    {
        return Stream.of(
                Arguments.of(new SynchronizedMethodListHolder()),
                Arguments.of(new SynchronizedBlockListHolder()));
    }

    @ParameterizedTest
    @MethodSource ("listHolderProvider")
    void testAddCopyAndIterateConcurrently(ListHolder listHolder) throws InterruptedException
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
                IntStream.range(0, itemsPerThread)
                        .forEach(j -> listHolder.addCopyAndIterate("Item " + (i * itemsPerThread + j)));
            }
            finally
            {
                latch.countDown();
            }
        }));

        // Wait for all threads to finish
        latch.await(1, TimeUnit.MINUTES);
        executor.shutdown();

        // Verify the internal state by getting a copy of the list
        List<String> listCopy = listHolder.getListCopy();
        int expectedMinSize = threadCount * itemsPerThread;
        assertTrue(listCopy.size() >= expectedMinSize, "List size should be at least " + expectedMinSize);

        // Verify the presence of all expected items
        Set<String> expectedItems = new HashSet<>();
        IntStream.range(0, threadCount * itemsPerThread).forEach(i -> expectedItems.add("Item " + i));

        Set<String> actualItems = new HashSet<>(listCopy);
        assertTrue(actualItems.containsAll(expectedItems), "List should contain all expected items");
    }
}
