package org.concurrency.locks.consistentlockingpolicy;

/**
 * The Employee class represents an employee with a base salary.
 * This class provides methods to update the salary with a bonus percentage
 * and to retrieve the current salary. All methods that modify or access the salary
 * are synchronized on the Employee.class lock to ensure thread safety and consistent lock policy.
 */
public class Employee
{
    private double salary;

    public Employee(final double salary)
    {
        this.salary = salary;
    }

    public void updateBaseSalaryWithBonus(final double bonusPercentage)
    {
        synchronized (Employee.class)
        {
            new Helper().calculateBonus(bonusPercentage);
        }
    }

    public double getSalary()
    {
        synchronized (Employee.class)
        {
            return salary;
        }
    }

    /**
     * The Helper class is a private inner class that provides methods to calculate
     * bonuses and other benefits. All methods in the Helper class also synchronize
     * on the Employee.class lock to ensure consistent locking.
     */
    private class Helper
    {
        public void calculateBonus(final double bonusPercentage)
        {
            synchronized (Employee.class)
            {
                salary += salary * (bonusPercentage / 100);
            }
        }
    }
}
