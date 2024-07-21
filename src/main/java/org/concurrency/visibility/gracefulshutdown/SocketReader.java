package org.concurrency.visibility.gracefulshutdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The SocketReader class reads data from a socket.
 * The design of the class assumes that only one thread will be
 * calling the run method and thus accessing the readData method at any given time.
 * It ensures that the socket and input stream are properly closed when the thread is shut down.
 */
public final class SocketReader implements Runnable
{
    private final Socket socket;
    private final BufferedReader bufferedReader;

    public SocketReader(String host, int port) throws IOException
    {
        this.socket = new Socket(host, port);
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    /**
     * The run method reads data from the socket.
     * It will terminate when the socket is closed or an IOException occurs.
     */
    @Override
    public void run()
    {
        try
        {
            readData();
        }
        catch (IOException ie)
        {
            System.out.println(ie.getMessage());
        }
        finally
        {
            cleanup();
        }
    }

    /**
     * Reads data from the input stream until the socket is closed or the end of the stream is reached.
     *
     * @throws IOException if an I/O error occurs
     */
    public void readData() throws IOException
    {
        String string;
        try
        {
            while ((string = bufferedReader.readLine()) != null)
            {
                System.out.println(string);
            }
        }
        finally
        {
            cleanup();
        }
    }

    /**
     * Signals the reader to shut down by closing the socket.
     */
    public void shutdown() throws IOException
    {
        socket.close();
    }

    /**
     * Closes the input stream and the socket.
     */
    private void cleanup()
    {
        try
        {
            if (bufferedReader != null)
            {
                bufferedReader.close();
            }
            if (socket != null && !socket.isClosed())
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            // Handle exception
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        SocketReader reader = new SocketReader("localhost", 25);
        Thread thread = new Thread(reader);
        thread.start();
        Thread.sleep(1000);
        reader.shutdown(); // Shut down the thread
        thread.join(); // Wait for the thread to finish
    }
}
