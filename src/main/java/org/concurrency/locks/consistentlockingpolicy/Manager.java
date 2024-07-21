package org.concurrency.locks.consistentlockingpolicy;

/**
 * The Manager class represents a manager who inherits from Employee.
 * Managers have additional benefits such as performance bonuses.
 * This class provides methods to calculate and retrieve the total compensation
 * including the base salary and performance bonus. All methods are synchronized
 * on the Base class Employee.class lock to ensure consistent locking with the base class.
 */
public class Manager extends Employee
{
    private double performanceBonus;

    public Manager(final double salary, final double performanceBonus)
    {
        super(salary);
        this.performanceBonus = performanceBonus;
    }

    public double calculateTotalCompensation()
    {
        synchronized (Employee.class)
        {
            return getSalary() + performanceBonus;
        }
    }

    public void updatePerformanceBonus(final double newBonus)
    {
        synchronized (Employee.class)
        {
            this.performanceBonus = newBonus;
        }
    }

    public double getPerformanceBonus()
    {
        synchronized (Employee.class)
        {
            return performanceBonus;
        }
    }
}
