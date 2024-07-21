package org.concurrency.miscellaneous;

/**
 * BankAccount provides thread-safe methods for depositing and withdrawing money.
 */
public class BankAccount
{
    private int balance;
    private final Object lock = new Object();

    public BankAccount(int initialBalance)
    {
        this.balance = initialBalance;
    }

    /**
     * Deposits the specified amount into the account in a thread-safe manner.
     * @param amount the amount to deposit
     */
    public void deposit(int amount)
    {
        synchronized (lock)
        {
            if (amount > 0)
            {
                balance += amount;
                System.out.println("Deposited " + amount + ", new balance: " + balance);
            }
        }
    }

    /**
     * Withdraws the specified amount from the account in a thread-safe manner.
     * @param amount the amount to withdraw
     */
    public void withdraw(int amount)
    {
        synchronized (lock)
        {
            if (amount > 0 && amount <= balance)
            {
                balance -= amount;
                System.out.println("Withdrew " + amount + ", new balance: " + balance);
            }
            else
            {
                System.out.println("Withdrawal of " + amount + " failed, current balance: " + balance);
            }
        }
    }

    /**
     * Returns the current balance of the account in a thread-safe manner.
     * @return the current balance
     */
    public int getBalance()
    {
        synchronized (lock)
        {
            return balance;
        }
    }

    public static void main(String[] args)
    {
        ChildBankAccount account = new ChildBankAccount(1000);

        Runnable depositTask = () ->
        {
            for (int i = 0; i < 10; i++)
            {
                account.deposit(100);
            }
        };

        Runnable withdrawTask = () ->
        {
            for (int i = 0; i < 10; i++)
            {
                account.withdraw(100);
            }
        };

        Thread thread1 = new Thread(depositTask);
        Thread thread2 = new Thread(withdrawTask);

        thread1.start();
        thread2.start();

        try
        {
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final balance: " + account.getBalance());
    }
}
