package org.concurrency.threadpools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class CustomThreadPoolExecutor extends ThreadPoolExecutor
{
    private static final Logger LOGGER = Logger.getLogger(CustomThreadPoolExecutor.class.getName());

    public CustomThreadPoolExecutor(
                                    int corePoolSize,
                                    int maximumPoolSize,
                                    long keepAliveTime,
                                    TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r, t);
        if (t != null)
        {
            LOGGER.log(Level.SEVERE, "Task terminated with an exception", t);
            handleException(t);
        }
    }

    @Override
    public void beforeExecute(Thread t, Runnable r)
    {
        if (t == null || r == null)
        {
            throw new NullPointerException();
        }
        super.beforeExecute(t, r);
        // you can set here Thread local or some other things that you want before task execution
    }

    private void handleException(Throwable t)
    {
        // Implement your exception handling logic here
        LOGGER.log(Level.SEVERE, "Handling exception", t);
    }

    @Override
    public void terminated()
    {
        super.terminated();
        LOGGER.info("ThreadPoolExecutor has terminated.");
    }
}
