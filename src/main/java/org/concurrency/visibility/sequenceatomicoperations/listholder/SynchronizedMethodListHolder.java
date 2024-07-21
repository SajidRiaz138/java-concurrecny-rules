package org.concurrency.visibility.sequenceatomicoperations.listholder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A thread-safe implementation of ListHolder that uses synchronized methods.
 * This class provides methods to add a string to the list, copy the list, and iterate over the list.
 */
public final class SynchronizedMethodListHolder implements ListHolder
{
    private final List<String> strings = Collections.synchronizedList(new ArrayList<String>());

    @Override
    public synchronized void addCopyAndIterate(String str)
    {
        strings.add(str);
        String[] addressCopy = strings.toArray(new String[0]);
        for (String s : addressCopy)
        {
            System.out.println(s);
        }
    }

    @Override
    public synchronized List<String> getListCopy()
    {
        return new ArrayList<>(strings);
    }
}
