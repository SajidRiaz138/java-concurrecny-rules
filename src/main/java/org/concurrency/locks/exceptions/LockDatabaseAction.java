package org.concurrency.locks.exceptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The LockDatabaseAction class provides a method to perform actions on a database connection
 * with thread safety using a ReentrantLock.
 *
 * <p>The execute-around idiom provides a generic mechanism to perform resource allocation and cleanup operations
 * so that the client can focus on specifying only the required functionality. This idiom reduces clutter in client code
 * and provides a secure mechanism for resource management.</p>
 */
public final class LockDatabaseAction
{

    private static final Lock lock = new ReentrantLock();

    /**
     * Executes the specified action on a database connection.
     * Ensures the connection is properly managed and closed, and the lock is always released.
     *
     * @param dbUrl the database URL
     * @param user the database user
     * @param password the database password
     * @param action the action to be performed on the database connection
     */
    public static void execute(String dbUrl, String user, String password, DatabaseAction action)
    {
        lock.lock();
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password))
        {
            action.performAction(connection);
        }
        catch (Exception e)
        {
            // Forward to handler
        }
        finally
        {
            lock.unlock();
            // Forward to handler
        }
    }
}
