package org.concurrency.visibility.immutability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationManagerTest
{
    static Stream<ConfigurationManager> provideConfiguration()
    {
        return Stream.of(
                new AtomicConfigurationManager(),
                new SynchronizedConfigurationManager(),
                new VolatileConfigurationManager());
    }

    @ParameterizedTest
    @MethodSource ("provideConfiguration")
    public void testConcurrentUpdateAndRead(ConfigurationManager configManager) throws InterruptedException
    {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Runnable updateTask = () ->
        {
            for (int i = 0; i < 5; i++)
            {
                Configuration newConfig = new Configuration("jdbc:mysql://localhost:3306/mydb", 10 + i);
                configManager.updateConfiguration(newConfig);
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable readTask = () ->
        {
            for (int i = 0; i < 10; i++)
            {
                Configuration config = configManager.getConfiguration();
                if (config != null)
                {
                    System.out.println("Read configuration: " + config.getMaxConnections());
                }
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };

        for (int i = 0; i < 3; i++)
        {
            executorService.submit(updateTask);
        }
        for (int i = 0; i < 2; i++)
        {
            executorService.submit(readTask);
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        Configuration finalConfig = configManager.getConfiguration();
        assertEquals(14, finalConfig.getMaxConnections());
    }
}
