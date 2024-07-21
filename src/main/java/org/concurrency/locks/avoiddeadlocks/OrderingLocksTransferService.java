package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;

/**
 * A thread-safe implementation of the TransferService interface that uses ordered locking
 * to prevent deadlocks. Bank accounts are always locked in a consistent order based on
 * their natural ordering, as defined by the compareTo method.
 */
public class OrderingLocksTransferService implements TransferService
{

    @Override
    public void transfer(BankAccount from, BankAccount to, BigDecimal amount)
    {
        BankAccount firstLock, secondLock;
        if (from.compareTo(to) < 0)
        {
            firstLock = from;
            secondLock = to;
        }
        else
        {
            firstLock = to;
            secondLock = from;
        }
        synchronized (firstLock)
        {
            synchronized (secondLock)
            {
                TransferValidationUtil.validateTransfer(from, to, amount);
                to.deposit(amount);
                from.withdraw(amount);
            }
        }
    }
}
