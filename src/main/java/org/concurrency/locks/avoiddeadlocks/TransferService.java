package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;

/**
 * Interface for different strategies of transferring amounts between bank accounts.
 */
public interface TransferService
{

    /**
     * Transfers the specified amount from one bank account to another.
     * Ensures that the transfer is thread-safe.
     *
     * @param from   the bank account from which the amount is to be debited
     * @param to     the bank account to which the amount is to be credited
     * @param amount the amount to transfer
     * @throws IllegalArgumentException if the transfer cannot be completed due to insufficient funds
     * @throws NullPointerException     if either bank account is null
     */
    void transfer(BankAccount from, BankAccount to, BigDecimal amount);
}
