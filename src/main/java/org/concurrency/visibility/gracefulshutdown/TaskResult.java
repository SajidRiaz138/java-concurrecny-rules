package org.concurrency.visibility.gracefulshutdown;

/**
 * TaskResult represents the result of a task.
 */
public final class TaskResult
{
    private final int result;

    public TaskResult(int result)
    {
        this.result = result;
    }

    public int getResult()
    {
        return result;
    }
}
