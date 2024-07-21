package org.concurrency.locks.avoiddeadlocks;

import java.math.BigDecimal;

/**
 * Utility class for validating bank account transfer operations.
 */
public class TransferValidationUtil
{

    /**
     * Validates the parameters for a transfer operation.
     *
     * @param from   the bank account from which the amount is to be debited
     * @param to     the bank account to which the amount is to be credited
     * @param amount the amount to transfer
     * @throws IllegalArgumentException if the transfer cannot be completed due to insufficient funds or invalid amount
     * @throws NullPointerException     if either bank account is null
     */
    public static void validateTransfer(BankAccount from, BankAccount to, BigDecimal amount)
    {
        if (from == null || to == null)
        {
            throw new NullPointerException("Bank accounts cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (from.getAccountId() == to.getAccountId())
        {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }
        if (from.getBalance().compareTo(amount) < 0)
        {
            throw new IllegalArgumentException("Transfer cannot be completed due to insufficient funds");
        }
    }
}
