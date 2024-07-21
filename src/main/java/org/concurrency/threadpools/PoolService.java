package org.concurrency.threadpools;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The PoolService class manages a pool of threads to handle socket reading tasks.
 */
public final class PoolService
{
    private static final Logger LOGGER = Logger.getLogger(PoolService.class.getName());
    private final ExecutorService pool;

    public PoolService(int poolSize)
    {
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void submitTask() throws IOException
    {
        pool.submit(new SocketReader("localhost", 8080));
    }

    public void shutdown()
    {
        pool.shutdown();
        try
        {
            while (!pool.awaitTermination(60, TimeUnit.SECONDS))
            {
                pool.shutdownNow();
            }
        }
        catch (InterruptedException ie)
        {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws IOException
    {
        PoolService service = new PoolService(5);
        service.submitTask();

        // Properly shut down the pool service
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            try
            {
                service.shutdown();
            }
            catch (Exception e)
            {
                LOGGER.log(Level.SEVERE, "Error during shutdown", e);
            }
        }));
    }
}
