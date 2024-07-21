package org.concurrency.threadpools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Purpose of this sample is to highlight that be careful when dealing with ThreadLocal object
 * with Thread pool. Clear or remove thread local object after each task completion so that
 * reusable thread do not use stale value
 *
 * There is an other-way also to implement customer thread pool and add clean up logic in beforeExecute or after execute
 *
 */

public class DummyWebServer
{
    private static final Logger LOGGER = Logger.getLogger(DummyWebServer.class.getName());

    public static void main(String[] args)
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++)
        {
            threadPool.execute(() ->
            {
                try
                {
                    // Simulate processing an incoming request
                    UserSession session = SessionManager.getCurrentSession();

                    // Simulate user authentication
                    authenticateUser(session);

                    // Fetch user data
                    fetchUserData(session);

                    // Update user profile
                    updateUserProfile(session);

                }
                catch (Exception e)
                {
                    LOGGER.log(Level.SEVERE, "Error processing request", e);
                }
                finally
                {
                    // Ensure the ThreadLocal value is removed
                    SessionManager.removeCurrentSession();
                }
            });
        }

        threadPool.shutdown();
    }

    private static void authenticateUser(UserSession session)
    {
        // Simulate authentication
        session.setUserId("user" + Thread.currentThread().getId());
        session.setAuthenticated(true);
        LOGGER.log(Level.INFO, "User authenticated: " + session.getUserId());
    }

    private static void fetchUserData(UserSession session)
    {
        // Simulate fetching user data
        Map<String, Object> data = new HashMap<>();
        data.put("name", "User" + session.getUserId());
        data.put("email", session.getUserId() + "@example.com");
        session.setUserData(data);
        LOGGER.log(Level.INFO, "User data fetched for: " + session.getUserId());
    }

    private static void updateUserProfile(UserSession session)
    {
        // Simulate updating user profile
        if (session.isAuthenticated())
        {
            session.getUserData().put("profileUpdated", true);
            LOGGER.log(Level.INFO, "User profile updated for: " + session.getUserId());
        }
        else
        {
            LOGGER.log(Level.WARNING, "User not authenticated: " + session.getUserId());
        }
    }
}

class SessionManager
{
    private static final ThreadLocal<UserSession> sessionThreadLocal = ThreadLocal.withInitial(UserSession::new);

    public static UserSession getCurrentSession()
    {
        return sessionThreadLocal.get();
    }

    public static void removeCurrentSession()
    {
        UserSession session = sessionThreadLocal.get();
        session.clear(); // Clear session data before removing
        sessionThreadLocal.remove();
    }
}

class UserSession
{
    private String userId;
    private boolean authenticated;
    private Map<String, Object> userData;

    public UserSession()
    {
        this.userData = new HashMap<>();
        this.authenticated = false;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }

    public Map<String, Object> getUserData()
    {
        return userData;
    }

    public void setUserData(Map<String, Object> userData)
    {
        this.userData = userData;
    }

    public void clear()
    {
        this.userId = null;
        this.authenticated = false;
        this.userData.clear();
    }
}
