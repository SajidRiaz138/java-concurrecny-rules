package org.concurrency.threadpools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SocketRequestHandler class handles incoming socket requests using a thread pool.
 * It manages a fixed-size thread pool to process socket requests efficiently.
 */
public final class SocketRequestHandler
{
    private static final Logger LOGGER = Logger.getLogger(SocketRequestHandler.class.getName());
    private final Helper helper = new Helper();
    private final ServerSocket server;
    private final ExecutorService exec;

    private SocketRequestHandler(int port, int poolSize) throws IOException
    {
        this.server = new ServerSocket(port);
        this.exec = Executors.newFixedThreadPool(poolSize);
    }

    public static SocketRequestHandler newInstance(int poolSize) throws IOException
    {
        return new SocketRequestHandler(0, poolSize); // Selects next available port
    }

    /**
     * Starts handling incoming requests. This method blocks until the server socket is closed.
     */
    public void handleRequests()
    {
        try
        {
            final Socket socket = server.accept();
            exec.submit(() -> helper.handle(socket));
        }
        catch (IOException e)
        {
            if (!server.isClosed())
            {
                LOGGER.log(Level.SEVERE, "Error accepting connection", e);
            }
        }
    }

    /**
     * Shuts down the request handler gracefully, closing the server socket and thread pool.
     */
    public void shutdown()
    {
        try
        {
            server.close();
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to close server socket", e);
        }
        exec.shutdown();
    }

    public static void main(String[] args)
    {
        try
        {
            SocketRequestHandler handler = SocketRequestHandler.newInstance(10);
            //The shutdown hook is added to ensure that the server can clean up resources gracefully when the JVM shuts down.
            Runtime.getRuntime().addShutdownHook(new Thread(handler::shutdown));
            handler.handleRequests();
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to start SocketRequestHandler", e);
        }
    }
}

/**
 * Helper class to handle socket requests.
 */
class Helper
{
    private static final Logger LOGGER = Logger.getLogger(Helper.class.getName());

    /**
     * Handles the incoming socket request.
     *
     * @param socket the socket to handle
     */
    public void handle(Socket socket)
    {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream())
        {

            // Example operation: echoing received data back to the client
            byte[] buffer = new byte[1024];
            int bytesRead = input.read(buffer);
            if (bytesRead != -1)
            {
                String received = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                LOGGER.log(Level.INFO, "Received: {0}", received);

                // Echo back the received data
                output.write(("Echo: " + received).getBytes(StandardCharsets.UTF_8));
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error handling socket", e);
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                LOGGER.log(Level.SEVERE, "Failed to close socket", e);
            }
        }
    }
}
