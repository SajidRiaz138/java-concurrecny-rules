package org.concurrency.locks.consistentlockingpolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ThreadSafeListView class demonstrates a thread-safe way to manage a list and its sublist view.
 * It follows the policy of not synchronizing on a collection view if the backing collection is accessible.
 * All compound operations on the list and its sublist are synchronized on the backing collection to ensure thread safety.
 */
public class ThreadSafeListView
{
    // Backing collection and wrapper on list object
    private final List<String> listView = Collections.synchronizedList(new ArrayList<>());
    // View on the backing collection
    private List<String> subListView = listView.subList(0, 0);

    public List<String> getList()
    {
        return listView;
    }

    /**
     * Processes the sublist by iterating over its elements and performing an operation.
     * This method is synchronized on the backing collection to ensure thread safety.
     *
     * @return the total length of all strings in the sublist
     */
    public int processSubList()
    {
        int totalLength = 0;
        synchronized (listView)
        { // Synchronize on the backing list
            for (String item : subListView)
            {
                totalLength += item.length();
            }
        }
        return totalLength;
    }

    /**
     * Adds an element to the backing list.
     * This operation is thread-safe as the list is a synchronized list.
     *
     * @param element the element to add to the list
     */
    public void addElement(String element)
    {
        listView.add(element);
    }

    /**
     * Sets the sublist view to a specified range within the backing list.
     * This method is synchronized on the backing collection to ensure thread safety.
     *
     * @param fromIndex the starting index of the sublist
     * @param toIndex   the ending index of the sublist
     */
    public void setSubListView(int fromIndex, int toIndex)
    {
        synchronized (listView)
        {
            // Adjust subListView to a new range within the listView
            subListView = listView.subList(fromIndex, toIndex);
        }
    }
}
