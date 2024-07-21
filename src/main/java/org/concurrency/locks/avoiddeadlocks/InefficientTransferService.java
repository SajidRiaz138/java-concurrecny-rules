package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;

/**
 * An inefficient implementation of the TransferService interface.
 * This implementation uses a private static lock to ensure thread safety,
 * which imposes a performance penalty because it restricts the system to performing transfers sequentially.
 * Two transfers involving four distinct accounts (with distinct target accounts) cannot be performed concurrently.
 * This penalty increases considerably as the number of BankAccount objects increases.
 * Consequently, this solution fails to scale well.
 */

public final class InefficientTransferService implements TransferService
{
    private static final Object lock = new Object();

    @Override
    public void transfer(final BankAccount from, BankAccount to, BigDecimal amount)
    {
        synchronized (lock)
        {
            TransferValidationUtil.validateTransfer(from, to, amount);
            from.withdraw(amount);
            to.deposit(amount);
        }
    }
}
