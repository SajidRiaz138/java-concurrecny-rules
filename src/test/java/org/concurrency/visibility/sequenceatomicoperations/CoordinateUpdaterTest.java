package org.concurrency.visibility.sequenceatomicoperations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoordinateUpdaterTest
{

    @Test
    public void testInitialDistanceFromOrigin()
    {
        CoordinateUpdater updater = new CoordinateUpdater(3.0, 4.0);
        double expectedDistance = 5.0; // 3^2 + 4^2 = 9 + 16 = 25; sqrt(25) = 5
        assertEquals(expectedDistance, updater.distanceFromOrigin(), 0.0001);
    }

    @Test
    public void testUpdateCoordinates()
    {
        CoordinateUpdater updater = new CoordinateUpdater(1.0, 1.0);
        updater.update(6.0, 8.0);
        double expectedDistance = 10.0; // 6^2 + 8^2 = 36 + 64 = 100; sqrt(100) = 10
        assertEquals(expectedDistance, updater.distanceFromOrigin(), 0.0001);
    }

    @Test
    public void testConcurrentUpdates() throws InterruptedException
    {
        CoordinateUpdater updater = new CoordinateUpdater(0.0, 0.0);

        Thread t1 = new Thread(() ->
        {
            for (int i = 0; i < 100; i++)
            {
                updater.update(3.0, 4.0);
            }
        });

        Thread t2 = new Thread(() ->
        {
            for (int i = 0; i < 100; i++)
            {
                updater.update(5.0, 12.0);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        double distance1 = updater.distanceFromOrigin(); // Either 5 or 13
        assertTrue(distance1 == 5.0 || distance1 == 13.0);
    }
}
