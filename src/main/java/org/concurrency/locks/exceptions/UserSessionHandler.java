package org.concurrency.locks.exceptions;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The UserSessionHandler class manages user sessions in a thread-safe manner using a ReentrantLock.
 * The manageSession method ensures that the lock is properly acquired and released,
 * and avoids throwing a NullPointerException by checking for null references.
 */
public final class UserSessionHandler
{

    private final Date sessionStartDate = new Date();
    private final Lock lock = new ReentrantLock();
    private static final Logger LOGGER = Logger.getLogger(UserSessionHandler.class.getName());

    /**
     * Manages the user session, ensuring thread safety and avoiding NullPointerException.
     *
     * @param sessionToken the session token, which may be null
     */
    public void manageSession(String sessionToken)
    {
        lock.lock();
        try
        {
            String sessionDateString = sessionStartDate.toString();
            if (sessionToken != null && sessionToken.equals(sessionDateString))
            {
                // Perform session-related operations
                if (validateSession(sessionToken))
                {
                    extendSession();
                    LOGGER.log(Level.INFO, "Session validated and extended for token: {0}", sessionToken);
                }
            }
            else
            {
                LOGGER.log(Level.WARNING, "Invalid or null session token: {0}", sessionToken);
            }
            // Additional session management logic

        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Validates the session token.
     *
     * @param sessionToken the session token to validate
     * @return true if the session token is valid, false otherwise
     */
    private boolean validateSession(String sessionToken)
    {
        // Validate the session token
        return sessionToken != null && !sessionToken.isEmpty();
    }

    /**
     * Extends the session.
     */
    private void extendSession()
    {
        // Extend the session
        sessionStartDate.setTime(System.currentTimeMillis());
    }
}
