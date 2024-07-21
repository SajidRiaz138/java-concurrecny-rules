package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;

/**
 * A thread-safe implementation of the TransferService interface that uses ReentrantLocks
 * with tryLock to prevent deadlocks. This implementation continuously attempts to acquire
 * locks on both bank accounts and performs the transfer once both locks are successfully acquired,
 * using exponential backoff to avoid livelocks.
 *
 * <p>Pros and Cons of Exponential Backoff:</p>
 * <p><strong>Pros:</strong></p>
 * <ul>
 *     <li>Reduced Contention: Systematically increases wait time, reducing the frequency of contention.</li>
 *     <li>Avoids Livelocks: Helps avoid scenarios where multiple threads keep retrying without making progress.</li>
 * </ul>
 * <p><strong>Cons:</strong></p>
 * <ul>
 *     <li>Performance Impact: Can introduce delays, impacting performance, especially if contention is low.</li>
 *     <li>Complexity: Adds complexity to the code.</li>
 * </ul>
 */
public class ReentrantLocksTransferService implements TransferService
{

    private static final int MAX_RETRY = 5;

    @Override
    public void transfer(BankAccount from, BankAccount to, BigDecimal amount)
    {
        int retryCount = 0;
        long backoff = 50; // initial backoff time in milliseconds

        while (true)
        {
            if (from.getLock().tryLock())
            {
                try
                {
                    if (to.getLock().tryLock())
                    {
                        try
                        {
                            TransferValidationUtil.validateTransfer(from, to, amount);
                            to.deposit(amount);
                            from.withdraw(amount);
                            break; // Break the loop once the transfer is successful
                        }
                        finally
                        {
                            to.getLock().unlock();
                        }
                    }
                }
                finally
                {
                    from.getLock().unlock();
                }
            }
            backoffTime(retryCount, backoff);
        }
    }

    private void backoffTime(int retryCount, long backoff)
    {
        try
        {
            Thread.sleep(backoff);
            backoff = Math.min(backoff * 2, 1000); // exponential backoff with a max cap
            retryCount++;
            if (retryCount > MAX_RETRY)
            {
                throw new TransferFailedException("Transfer failed after maximum retries");
            }
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            throw new TransferFailedException("Transfer interrupted", ex);
        }
    }
}
