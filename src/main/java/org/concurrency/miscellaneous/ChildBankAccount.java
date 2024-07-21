package org.concurrency.miscellaneous;

/**
 * ChildBankAccount overrides the thread-safe methods of BankAccount and ensures they remain thread-safe.
 */
public class ChildBankAccount extends BankAccount
{
    private final Object subclassLock = new Object();

    public ChildBankAccount(int initialBalance)
    {
        super(initialBalance);
    }

    /**
     * Deposits the specified amount into the account in a thread-safe manner.
     * This method overrides the base class method and ensures thread safety.
     * @param amount the amount to deposit
     */
    @Override
    public void deposit(int amount)
    {
        synchronized (subclassLock)
        {
            super.deposit(amount);
        }
    }

    /**
     * Withdraws the specified amount from the account in a thread-safe manner.
     * This method overrides the base class method and ensures thread safety.
     * @param amount the amount to withdraw
     */
    @Override
    public void withdraw(int amount)
    {
        synchronized (subclassLock)
        {
            super.withdraw(amount);
        }
    }
}
