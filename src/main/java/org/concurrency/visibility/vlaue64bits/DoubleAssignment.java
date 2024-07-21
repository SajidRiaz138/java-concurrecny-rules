package org.concurrency.visibility.vlaue64bits;

/**
 * <p>
 *     The Java Language Specification (JLS) allows 64-bit long and double values to be treated as two 32-bit values.
 *     For example, a 64-bit write operation could be performed as two separate 32-bit operations.
 *     Thread safe solution is declares instance variable  to volatile.
 *     Writes and reads of long and double volatile values are always atomic.
 * <p>
 *
 */

public class DoubleAssignment
{
    private volatile double salary = 0d;


    /**
     *
     * It is important to ensure that the argument to the update() method
     * is obtained from a volatile variable or obtained as the result of an atomic read.
     * Otherwise, a read of the variable argument can itself expose a vulnerability.
     *
     */
    public void update(double salary)
    {
        this.salary = salary;
    }

    public void display()
    {
        System.out.println(salary);
    }

}
