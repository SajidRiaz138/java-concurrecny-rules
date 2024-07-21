package org.concurrency.locks.prohibitedlocks;

/**
 * This class shows lock that leads to deadlock.
 * Never use reusable objects as lock
 * 
 */

public final class NonCompliantLocks
{

    // Never used reusable lock objects as lock

    private Boolean reusableLock = Boolean.FALSE; // Boolean literals are reusable so never used

    // Never lock on Boxed primitives

    private double balance = 0;
    private Double boxedDoubleLock = balance; // Boxed types may use the same instance for a range of integer values

    // Valid but not recommended. use always private final lock object.
    private final Integer counter = new Integer(0); // valid but not recommended
    private final Object lockObject = new Object(); // recommended lock on object


    // Be careful with String locks. intern() push string to string pool so may be reused by others

    private final String badlock = new String("BADLOCK").intern(); // never used lock with intern function

    //  Same true for string literla

    private final String dirtyLock = "dirtyLock"; // same string pool issue

    // Valid lock on String. Always use new String object.

    private final String validLock = new String("ValidLock");


    // Always think about when getting lock by getClass() method. It determines the Class type at runtime
    // and it can lead to inconsistent locking in inheritance hierarchy Base and derive class.


}
