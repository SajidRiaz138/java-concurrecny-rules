package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a bank account with a balance.
 * The balance is managed using BigDecimal for precision.
 * The class provides methods to deposit, withdraw, and get the balance.
 */
final public class BankAccount implements Comparable<BankAccount>
{
    private BigDecimal balanceAmount;
    private static final AtomicLong IdGenerator = new AtomicLong(0);
    private final long accountId;
    private final Lock lock = new ReentrantLock();

    public BankAccount(final BigDecimal balance)
    {
        this.balanceAmount = balance;
        this.accountId = BankAccount.IdGenerator.getAndIncrement();
    }

    public BigDecimal getBalance()
    {
        return balanceAmount;
    }

    void deposit(final BigDecimal amount)
    {
        balanceAmount = balanceAmount.add(amount);
    }

    void withdraw(final BigDecimal amount)
    {

        balanceAmount = balanceAmount.subtract(amount);
    }

    public long getAccountId()
    {
        return accountId;
    }

    Lock getLock()
    {
        return lock;
    }

    @Override
    public int compareTo(final BankAccount other)
    {
        return Long.compare(this.accountId, other.accountId);
    }
}
