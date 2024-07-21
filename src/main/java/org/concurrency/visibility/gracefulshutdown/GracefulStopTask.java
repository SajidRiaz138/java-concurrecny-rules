package org.concurrency.visibility.gracefulshutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class GracefulStopTask implements GracefulStop
{
    private final AtomicBoolean done = new AtomicBoolean(false);
    private static final Logger LOG = LoggerFactory.getLogger(GracefulStopTask.class.getName());

    /**
     * The main execution method of the thread. This method will continue
     * running until the shutdown method is called or thread interrupted. The thread performs its
     * task within this method.
     */
    @Override
    public void run()
    {
        while (!done.get())
        {
            try
            {
                // Perform task
                LOG.info("Task is running {} with ", Thread.currentThread().getName());
                Thread.sleep(1000); // Simulating work with sleep
            }
            catch (InterruptedException exception)
            {
                LOG.info("Reset interrupted Status {}", exception.getMessage());
                Thread.currentThread().interrupt(); // Reset interrupted status
                LOG.info("exit the task with interrupt {}", exception.getMessage());
                done.set(true);
            }
        }
        LOG.info("Task shutdown gracefully {} for ", Thread.currentThread().getName());
    }

    @Override
    public void shutdown()
    {
        done.set(true);
    }
}
