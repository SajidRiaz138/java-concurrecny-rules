package org.concurrency.visibility.compoundoperation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FeatureToggleTest
{
    static Stream<FeatureToggle> provideFeatureToggles()
    {
        return Stream.of(
                new SynchronizedFeatureToggle(),
                new VolatileFeatureToggle(),
                new ReadWriteLockFeatureToggle(),
                new AtomicBooleanFeatureToggle());
    }

    @ParameterizedTest
    @MethodSource ("provideFeatureToggles")
    public void testConcurrentToggleAndCheck(FeatureToggle featureToggle) throws InterruptedException
    {
        final int numThreads = 3;
        final int togglesPerThread = 5;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        Runnable toggleTask = () ->
        {
            for (int i = 0; i < togglesPerThread; i++)
            {
                featureToggle.toggle();
                System.out.println("Toggled to: " + featureToggle.isFeatureEnabled());
                try
                {
                    Thread.sleep(100); // Simulate some work
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            latch.countDown();
        };

        for (int i = 0; i < numThreads; i++)
        {
            executorService.submit(toggleTask);
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // Verify the final state
        boolean finalState = featureToggle.isFeatureEnabled();
        System.out.println("Final state: " + finalState);
        // Since we toggle 15 times (3 threads * 5 toggles each), final state should be false because initial state is true and we toggle an odd number of times
        assertFalse(finalState);
    }
}
