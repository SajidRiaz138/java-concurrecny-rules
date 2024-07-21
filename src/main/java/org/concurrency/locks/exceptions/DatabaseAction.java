package org.concurrency.locks.exceptions;

import java.sql.Connection;

/**
 * The DatabaseAction interface represents an action to be performed on a database connection.
 */
public interface DatabaseAction
{
    void performAction(Connection connection) throws Exception;
}
