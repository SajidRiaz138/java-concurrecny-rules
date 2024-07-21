package org.concurrency.visibility.gracefulshutdown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The TaskManager class manages a list of task results and runs tasks in a separate thread.
 * It uses a volatile boolean flag to signal when to stop running tasks.
 */
public final class TaskManager implements Runnable
{
    private final List<TaskResult> taskResults = new ArrayList<>();
    private volatile boolean done = false;

    /**
     * Returns the list of task results.
     *
     * @return the list of task results
     */
    public List<TaskResult> getTaskResults()
    {
        return Collections.unmodifiableList(taskResults);
    }

    /**
     * Signals the TaskManager to stop running tasks.
     */
    public void shutdown()
    {
        done = true;
    }

    @Override
    public synchronized void run()
    {
        Random random = new Random(123L);
        int taskCount = 1000; // Example task count

        while (!done && taskCount > 0)
        {
            taskResults.add(new TaskResult(random.nextInt(100)));
            taskCount--;
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        TaskManager taskManager = new TaskManager();
        Thread thread = new Thread(taskManager);
        thread.start();
        Thread.sleep(5000);
        taskManager.shutdown();

        // Print the task results
        for (TaskResult result : taskManager.getTaskResults())
        {
            System.out.println("Task result: " + result.getResult());
        }
    }
}
