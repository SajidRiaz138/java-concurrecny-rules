package org.concurrency.locks.consistentlockingpolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ThreadSafeListViewTest
{
    private ThreadSafeListView threadSafeListView;

    @BeforeEach
    public void setUp()
    {
        threadSafeListView = new ThreadSafeListView();
        threadSafeListView.addElement("Element 1");
        threadSafeListView.addElement("Element 2");
        threadSafeListView.addElement("Element 3");
        threadSafeListView.setSubListView(0, 2);
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException
    {
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++)
        {
            executorService.submit(() ->
            {
                try
                {
                    int totalLength = threadSafeListView.processSubList();
                    assertEquals(17, totalLength, "Total length of sublist items should be consistent");
                }
                finally
                {
                    latch.countDown();
                }
            });
        }

        latch.await(1, TimeUnit.MINUTES);
        executorService.shutdown();

        // Verify the list size remains unchanged
        assertEquals(3, threadSafeListView.getList().size());
    }

    @Test
    public void testConcurrentModification() throws InterruptedException
    {
        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++)
        {
            executorService.submit(() ->
            {
                try
                {
                    threadSafeListView.addElement("Element " + (Math.random() * 100));
                }
                finally
                {
                    latch.countDown();
                }
            });
        }

        latch.await(1, TimeUnit.MINUTES);
        executorService.shutdown();

        // Verify the list size has increased by the number of threads
        assertEquals(3 + numThreads, threadSafeListView.getList().size());
    }
}
