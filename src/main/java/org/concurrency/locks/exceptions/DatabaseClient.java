package org.concurrency.locks.exceptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The DatabaseClient class manages database connections and ensures
 * thread safety using ReentrantLock. It ensures that locks are released
 * properly even when exceptions occur during database operations.
 *  Encapsulates operations that could throw an exception in a try block
 *  immediately after acquiring the lock (which cannot throw).
 *  The lock is acquired just before the try block,
 *  which guarantees that it is held when the finally block executes.
 */
public final class DatabaseClient
{

    private final Lock lock = new ReentrantLock();

    /**
     * Establishes a connection to the database, performs some operations,
     * and ensures the connection is closed properly even if exceptions occur.
     *
     * @param dbUrl the database URL
     * @param user the database user
     * @param password the database password
     */
    public void doDatabaseOperation(String dbUrl, String user, String password)
    {
        lock.lock();
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password))
        {
            // Perform database operations
            Statement statement = connection.createStatement();
        }
        catch (SQLException e)
        {
            // Forward to handler
        }

        finally
        {
            lock.unlock(); // unlock in finally

            // Forward to handler
        }
    }
}
