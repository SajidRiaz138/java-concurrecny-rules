package org.concurrency.visibility.sequenceatomicoperations.mapoperations;

public class KeyedAccumulatorUtils
{

    static long getValidSum(Integer value, Integer currentSum)
    {
        long newSum = (currentSum == null) ? (long) value : (long) currentSum + value;
        if (newSum > Integer.MAX_VALUE)
        {
            throw new ArithmeticException("Sum overflow");
        }
        return newSum;
    }
}
