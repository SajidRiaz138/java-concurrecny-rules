package org.concurrency.visibility.gracefulshutdown;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GracefulStopTest
{

    static Stream<GracefulStop> gracefulStopStream()
    {
        return Stream.of(new GracefulStopTask(), new GracefulTaskTerminator());
    }

    @ParameterizedTest
    @MethodSource ("gracefulStopStream")
    public void testShutdown(GracefulStop task) throws InterruptedException
    {
        Thread taskThread = new Thread(task);
        taskThread.start();

        // Let the thread run for a bit
        TimeUnit.SECONDS.sleep(1);

        // Shutdown the task
        task.shutdown();

        // Wait for the thread to finish
        taskThread.join();

        // Verify that the thread has stopped
        assertFalse(taskThread.isAlive(), "Thread should have stopped");
    }

    @ParameterizedTest
    @MethodSource ("gracefulStopStream")
    public void testShutdownIsThreadSafe(GracefulStop task) throws InterruptedException
    {
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        TimeUnit.MILLISECONDS.sleep(1000);

        task.shutdown();

        t1.join();
        t2.join();

        // Verify that the task thread has stopped
        assertFalse(t1.isAlive(), "Task thread one should have stopped");
        assertFalse(t2.isAlive(), "Task thread one should have stopped");
    }

    @ParameterizedTest
    @MethodSource ("gracefulStopStream")
    public void testInterruptClearsStatus(GracefulStop task) throws InterruptedException
    {
        Thread taskThread = new Thread(task);
        taskThread.start();
        // Interrupt the thread
        taskThread.interrupt();

        // Give some time for the thread to process the interrupt
        TimeUnit.MILLISECONDS.sleep(100);

        // Check that the interrupt status is cleared in the task thread
        assertTrue(taskThread.isInterrupted(), "Thread interrupt status should be cleared");
        // Verify that the thread has stopped
        assertFalse(taskThread.isAlive(), "Thread should have stopped");
    }
}
