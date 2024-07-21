package org.concurrency.visibility.gracefulshutdown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A final class that implements the GracefulStop interface to provide
 * a mechanism for gracefully stopping a thread. The thread will continue
 * running until the shutdown method is called, which sets a flag to
 * terminate the loop.
 */
public final class GracefulTaskTerminator implements GracefulStop
{
    private static final Logger LOG = LoggerFactory.getLogger(GracefulTaskTerminator.class.getName());
    private volatile boolean done = false;

    /**
     * The main execution method of the thread. This method will continue
     * running until the shutdown method is called or thread interrupted. The thread performs its
     * task within this method.
     */
    @Override
    public void run()
    {
        while (!done)
        {
            try
            {
                // Perform task
                LOG.info("Task is running {} with ",Thread.currentThread().getName());
                Thread.sleep(1000); // Simulating work with sleep
            }
            catch (InterruptedException exception)
            {
                LOG.info("Reset interrupted Status {}", exception.getMessage());
                Thread.currentThread().interrupt(); // Reset interrupted status
                LOG.info("exit the task with interrupt {}", exception.getMessage());
                done = true;
            }
        }
        LOG.info("Task shutdown gracefully {} for ", Thread.currentThread().getName());
    }

    /**
     * Initiates an orderly shutdown of the thread. This method sets a flag
     * that causes the run method to exit its loop, allowing the thread to
     * terminate gracefully.
     */
    @Override
    public void shutdown()
    {
        done = true;
    }
}
