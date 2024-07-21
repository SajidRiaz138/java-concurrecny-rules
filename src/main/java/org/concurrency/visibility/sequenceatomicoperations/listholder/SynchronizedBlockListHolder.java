package org.concurrency.visibility.sequenceatomicoperations.listholder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SynchronizedBlockListHolder implements ListHolder
{
    private final List<String> strings =
            Collections.synchronizedList(new ArrayList<String>());

    @Override
    public void addCopyAndIterate(String str)
    {
        synchronized (strings)
        {
            strings.add(str);
            String[] addressCopy = strings.toArray(new String[0]);

            // Iterate through the array addressCopy
            for (String s : addressCopy)
            {
                System.out.println(s);
            }
        }
    }

    @Override
    public List<String> getListCopy()
    {
        synchronized (strings)
        {
            return new ArrayList<>(strings);
        }
    }
}
