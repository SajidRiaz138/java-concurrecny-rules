package org.concurrency.visibility.immutability;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application
{
    public static void main(String[] args) throws InterruptedException
    {
        testConfigurationManager(new VolatileConfigurationManager());
        testConfigurationManager(new SynchronizedConfigurationManager());
        testConfigurationManager(new AtomicConfigurationManager());
    }

    private static void testConfigurationManager(ConfigurationManager configManager) throws InterruptedException
    {
        System.out.println("Testing " + configManager.getClass().getSimpleName());

        // Initial configuration
        Configuration initialConfig = new Configuration("jdbc:mysql://localhost:3306/mydb", 10);
        configManager.updateConfiguration(initialConfig);

        // Executor service to simulate concurrent access
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Runnable to simulate configuration update
        Runnable updateTask = () ->
        {
            for (int i = 0; i < 5; i++)
            {
                Configuration newConfig = new Configuration("jdbc:mysql://localhost:3306/mydb", 10 + i);
                configManager.updateConfiguration(newConfig);
                System.out.println("Updated configuration to: " + newConfig.getMaxConnections());
                try
                {
                    Thread.sleep(100); // Simulate time taken to update configuration
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Runnable to simulate configuration read
        Runnable readTask = () ->
        {
            for (int i = 0; i < 10; i++)
            {
                Configuration config = configManager.getConfiguration();
                if (config != null)
                {
                    System.out.println("Read configuration: " + config.getMaxConnections());
                }
                else
                {
                    System.out.println("Read configuration: null");
                }
                try
                {
                    Thread.sleep(50); // Simulate time taken to read configuration
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Submit tasks to executor service
        for (int i = 0; i < 3; i++)
        {
            executorService.submit(updateTask);
        }
        for (int i = 0; i < 2; i++)
        {
            executorService.submit(readTask);
        }

        // Shutdown the executor service
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
            Thread.sleep(100);
        }

        System.out.println("Finished testing " + configManager.getClass().getSimpleName());
    }
}
