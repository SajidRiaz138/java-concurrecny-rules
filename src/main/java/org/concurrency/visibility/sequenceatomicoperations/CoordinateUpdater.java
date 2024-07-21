package org.concurrency.visibility.sequenceatomicoperations;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe class that maintains atomic references for coordinates.
 * This class provides methods to update these coordinates and calculate
 * the distance from the origin.
 * The class is synchronized to ensure atomicity of operations involving
 * multiple calls.
 */
public final class CoordinateUpdater
{
    private final AtomicReference<Double> x;
    private final AtomicReference<Double> y;

    public CoordinateUpdater(Double initialX, Double initialY)
    {
        x = new AtomicReference<>(initialX);
        y = new AtomicReference<>(initialY);
    }

    /**
     * Updates the values of both coordinates.
     * This method is synchronized to ensure that the update of both values
     * is atomic, preventing other threads from seeing an intermediate state.
     *
     * @param newX the new x-coordinate
     * @param newY the new y-coordinate
     */
    public synchronized void update(Double newX, Double newY)
    {
        x.set(newX);
        y.set(newY);
    }

    /**
     * Calculates the distance from the origin to the current coordinates.
     * This method is synchronized to ensure that the values are read atomically,
     * preventing other threads from modifying the values during the read.
     *
     * @return the distance from the origin to the current coordinates
     */
    public synchronized double distanceFromOrigin()
    {
        double xValue = x.get();
        double yValue = y.get();
        return Math.sqrt(xValue * xValue + yValue * yValue);
    }
}
