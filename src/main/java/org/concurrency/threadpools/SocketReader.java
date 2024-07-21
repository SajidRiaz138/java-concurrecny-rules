package org.concurrency.threadpools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SocketReader class reads data from a socket in a thread-safe manner.
 */
public final class SocketReader implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(SocketReader.class.getName());
    private final Socket socket;
    private final BufferedReader in;
    private final Object lock = new Object();

    public SocketReader(String host, int port) throws IOException
    {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run()
    {
        synchronized (lock)
        {
            try
            {
                readData();
            }
            catch (IOException e)
            {
                LOGGER.log(Level.SEVERE, "Error reading data", e);
            }
            finally
            {
                try
                {
                    shutdown();
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.SEVERE, "Error shutting down socket", e);
                }
            }
        }
    }

    private void readData() throws IOException
    {
        String string;
        while (!Thread.currentThread().isInterrupted() && (string = in.readLine()) != null)
        {
            System.out.println("Received: " + string);
        }
    }

    public void shutdown() throws IOException
    {
        socket.close();
    }
}
