package org.concurrency.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolHandleRuntimeException
{
    private static final Logger LOGGER = Logger.getLogger(ThreadPoolHandleRuntimeException.class.getName());
    private static final ThreadFactory factory = new ExceptionThreadFactory(new RuntimeExceptionHandler());
    private static final ExecutorService pool = Executors.newFixedThreadPool(10, factory);

    public void executeTask(Task task)
    {
        pool.execute(task);
    }

    public static void main(String[] args)
    {
        ThreadPoolHandleRuntimeException service = new ThreadPoolHandleRuntimeException();

        for (int i = 0; i < 10; i++)
        {
            service.executeTask(new Task());
        }

        service.shutdown();

        // Properly shut down the pool service
        Runtime.getRuntime().addShutdownHook(new Thread(service::shutdown));
    }

    /**
     * Shuts down the pool service gracefully.
     */
    public void shutdown()
    {
        pool.shutdown();
        try
        {
            pool.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Thread pool did not shutdown properly " + e.getMessage());
        }
    }

}

/**
 * RuntimeExceptionHandler handles uncaught exceptions thrown by threads.
 */
class RuntimeExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private static final Logger LOGGER = Logger.getLogger(RuntimeException.class.getName());

    @Override
    public void uncaughtException(Thread thread, Throwable t)
    {
        // Recovery or logging code
       LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), t);
    }
}

/**
 * ExceptionThreadFactory creates new threads and sets an uncaught exception handler.
 */
class ExceptionThreadFactory implements ThreadFactory
{
    private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
    private final Thread.UncaughtExceptionHandler handler;

    public ExceptionThreadFactory(Thread.UncaughtExceptionHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public Thread newThread(Runnable run)
    {
        Thread thread = defaultFactory.newThread(run);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }
}

/**
 * Task class represents a unit of work to be executed by the thread pool.
 */
class Task implements Runnable
{
    @Override
    public void run()
    {
        if (Math.random() > 0.5)
        {
            throw new RuntimeException("Simulated exception");
        }
        else
        {
            System.out.println("Task completed successfully");
        }
    }
}
