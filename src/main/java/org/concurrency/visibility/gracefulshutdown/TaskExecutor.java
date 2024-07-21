package org.concurrency.visibility.gracefulshutdown;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The TaskExecutor class manages a list of task results and runs tasks in a separate thread.
 * It uses thread interruption to signal when to stop running tasks.
 */
public final class TaskExecutor implements Runnable
{
    private final List<TaskResult> taskResults = new ArrayList<>();
    private final Object lock = new Object();

    /**
     * Returns the list of task results.
     *
     * @return the list of task results
     */
    public List<TaskResult> getTaskResults()
    {
        return Collections.unmodifiableList(taskResults);
    }

    @Override
    public void run()
    {
        int taskCount = 1000;
        synchronized (lock)
        {
            while (!Thread.currentThread().isInterrupted() && taskCount > 0)
            {
                int randomNumber = ThreadLocalRandom.current().nextInt(123);
                taskResults.add(new TaskResult(randomNumber));
                taskCount--;

                // Simulate some work
                try
                {
                    Thread.sleep(10); // Sleep for a short time to simulate work
                }
                catch (InterruptedException e)
                {
                    // Restore the interrupted status
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        TaskExecutor taskExecutor = new TaskExecutor();
        Thread thread = new Thread(taskExecutor);
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();

        // Wait for the thread to finish
        thread.join();

        // Print the task results
        for (TaskResult result : taskExecutor.getTaskResults())
        {
            System.out.println("Task result: " + result.getResult());
        }
    }
}
