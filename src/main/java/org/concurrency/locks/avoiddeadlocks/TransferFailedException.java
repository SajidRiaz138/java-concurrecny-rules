package org.concurrency.locks.avoiddeadlocks;

public class TransferFailedException extends RuntimeException
{
    public TransferFailedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TransferFailedException(String message)
    {
        super(message);
    }
}
