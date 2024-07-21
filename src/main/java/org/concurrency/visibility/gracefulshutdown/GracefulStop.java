package org.concurrency.visibility.gracefulshutdown;

/**
 * An interface for threads or services that can be gracefully stopped.
 * Implementations of this interface should ensure that any ongoing tasks
 * are completed or properly handled before the thread is terminated.
 * This is typically used to stop threads in a controlled manner to avoid
 * data corruption or inconsistent states.
 */
public interface GracefulStop extends Runnable
{

    /**
     * Initiates an orderly shutdown of the implementing thread or service.
     * This method should attempt to stop all processing in a controlled manner,
     * ensuring that any ongoing operations are either completed or properly terminated.
     * Implementations should handle any necessary cleanup and ensure that resources
     * are released properly.
     */
    void shutdown();
}
