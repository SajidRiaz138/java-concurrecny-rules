package org.concurrency.locks.exceptions;

import java.sql.Connection;
import java.sql.Statement;

/**
 * The Client class demonstrates the use of the LockDatabaseAction class to perform
 * database operations in a thread-safe manner.
 */
public final class Client
{
    public void performDatabaseOperation(String dbUrl, String user, String password)
    {
        LockDatabaseAction.execute(dbUrl, user, password, (connection) ->
        {
            {
                // Perform operations on the database connection
                Statement statement = connection.createStatement();
            }
        });
    }
}
