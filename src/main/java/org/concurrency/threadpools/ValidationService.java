package org.concurrency.threadpools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ValidationService class manages a pool of threads to validate and sanitize input fields.
 */
public class ValidationService
{
    private static final Logger LOGGER = Logger.getLogger(ValidationService.class.getName());
    private final ExecutorService pool;

    public ValidationService(int poolSize)
    {
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public static void main(String[] args)
    {
        ValidationService service = new ValidationService(3);
        try
        {
            System.out.println(service.fieldAggregator("Hello..,,,,", "How´+´+´", "You88666$€"));
        }
        catch (InterruptedException | ExecutionException e)
        {
            LOGGER.log(Level.SEVERE, "Error during field aggregation", e);
        }
        finally
        {
            service.shutdown();
        }
    }

    public void shutdown()
    {
        pool.shutdown();
        try
        {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS))
            {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    LOGGER.severe("Pool did not terminate");
            }
        }
        catch (InterruptedException ie)
        {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public StringBuilder fieldAggregator(String... inputs) throws InterruptedException, ExecutionException
    {
        StringBuilder sb = new StringBuilder();
        List<Future<String>> results = new ArrayList<>();

        // Submits the tasks to thread pool
        for (String input : inputs)
        {
            results.add(pool.submit(new ValidateAndSanitizeTask(input)));
        }

        for (Future<String> result : results)
        {
            sb.append(result.get());
        }

        return sb;
    }
}

/**
 * The ValidateAndSanitizeTask class validates and sanitizes a given input.
 */
class ValidateAndSanitizeTask implements Callable<String>
{
    private final String input;

    ValidateAndSanitizeTask(String input)
    {
        this.input = input;
    }

    @Override
    public String call() throws Exception
    {
        // Validate input (throw exception if validation fails)
        if (input == null || input.isEmpty())
        {
            throw new IllegalArgumentException("Invalid input: " + input);
        }

        // Sanitize input and return
        return sanitizeInput(input);
    }

    private String sanitizeInput(String input)
    {
        return input.trim().replaceAll("[^a-zA-Z0-9]", "");
    }
}
