package org.concurrency.threadpools;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The ManagedExecutorService class manages a pool of threads to execute tasks with proper exception handling.
 */
public final class ManagedExecutorService
{
    private static final Logger LOGGER = Logger.getLogger(ManagedExecutorService.class.getName());
    private final ExecutorService pool;

    public ManagedExecutorService(int poolSize)
    {
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void submitTasks(List<TaskWithExceptionHandling> tasks)
    {
        List<Future<String>> futures = new ArrayList<>();
        for (TaskWithExceptionHandling task : tasks)
        {
            futures.add(pool.submit(task));
        }

        for (Future<String> future : futures)
        {
            try
            {
               String result = future.get();
               LOGGER.log(Level.INFO, "Task result: {0}", result);
            }
            catch (ExecutionException | InterruptedException e)
            {
                LOGGER.log(Level.SEVERE, "Exception occurred while executing task", e.getCause());
            }
        }
    }

    public void shutdown()
    {
        pool.shutdown();
        try
        {
            while (!pool.awaitTermination(60, TimeUnit.SECONDS))
            {
                pool.shutdownNow();
                LOGGER.severe("Pool did not terminate");
            }
        }
        catch (InterruptedException ie)
        {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args)
    {
        ManagedExecutorService service = new ManagedExecutorService(5);
        List<TaskWithExceptionHandling> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            tasks.add(new TaskWithExceptionHandling("Task" + i));
        }
        service.submitTasks(tasks);
        service.shutdown();
    }
}

/**
 * TaskWithExceptionHandling simulates a task that can throw exceptions.
 */
class TaskWithExceptionHandling implements Callable<String>
{
    private static final Logger LOGGER = Logger.getLogger(TaskWithExceptionHandling.class.getName());
    private final String taskName;

    public TaskWithExceptionHandling(String taskName)
    {
        this.taskName = taskName;
    }

    @Override
    public String call() throws Exception
    {
        try
        {
            // Simulate some work
            if (Math.random() > 0.5)
            {
                throw new RuntimeException("Simulated exception in task: " + taskName);
            }
            return "Task " + taskName + " completed successfully";
        }
        catch (Exception e)
        {
           // LOGGER.log(Level.SEVERE, "Exception in task: " + taskName, e);
            throw e; // Rethrow exception to be handled by Future
        }
    }
}
