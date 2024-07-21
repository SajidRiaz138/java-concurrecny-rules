package org.concurrency.visibility.gracefulshutdown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The DatabaseQueryExecutor class is responsible for executing a SQL query in a separate thread.
 * It uses a ThreadLocal to manage database connections, ensuring each thread has its own connection.
 * This class also provides a method to cancel the currently executing statement.
 */
public final class DatabaseQueryExecutor implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(DatabaseQueryExecutor.class.getName());
    private final String query;
    private volatile Statement stmt;

    /**
     * Constructs a DatabaseQueryExecutor with the specified query.
     *
     * @param query the SQL query to execute
     */
    public DatabaseQueryExecutor(String query)
    {
        this.query = query;
    }

    /**
     * ThreadLocal to manage database connections, ensuring each thread has its own connection.
     */
    private static final ThreadLocal<Connection> connectionHolder = ThreadLocal.withInitial(() ->
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(
                    "jdbc:driver:name",
                    "username",
                    "password");
        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to create database connection", e);
        }
        return connection;
    });

    /**
     * Returns the database connection for the current thread.
     *
     * @return the Connection object for the current thread
     */
    public Connection getConnection()
    {
        return connectionHolder.get();
    }

    /**
     * Cancels the currently executing statement.
     *
     * @return true if the statement was successfully canceled, false otherwise
     */
    public boolean cancelStatement()
    {
        Statement tmpStmt = stmt;
        if (tmpStmt != null)
        {
            try
            {
                tmpStmt.cancel();
                return true;
            }
            catch (SQLException e)
            {
                LOGGER.log(Level.SEVERE, "Failed to cancel statement", e);
            }
        }
        return false;
    }

    /**
     * Executes the query in a separate thread.
     */
    @Override
    public void run()
    {
        try (Connection connection = getConnection())
        {
            if (connection != null)
            {
                stmt = connection.createStatement();
            }
            if (stmt == null || (stmt.getConnection() != connection))
            {
                throw new IllegalStateException("Statement or connection is not properly initialized");
            }
            ResultSet rs = stmt.executeQuery(query);
            // Process the ResultSet
            while (rs.next())
            {
                // Example processing: print the first column value
                System.out.println(rs.getString(1));
            }
        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "SQL execution error", e);
        }
        finally
        {
            cleanup();
        }
    }

    /**
     * Cleans up resources after execution.
     */
    private void cleanup()
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                LOGGER.log(Level.SEVERE, "Failed to close statement", e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        DatabaseQueryExecutor executor = new DatabaseQueryExecutor("SELECT * FROM example_table");
        Thread thread = new Thread(executor);
        thread.start();
        Thread.sleep(5000);
        executor.cancelStatement();
        thread.join();
    }
}
