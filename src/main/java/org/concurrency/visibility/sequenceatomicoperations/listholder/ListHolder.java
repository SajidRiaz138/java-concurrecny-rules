package org.concurrency.visibility.sequenceatomicoperations.listholder;

import java.util.List;

/**
 * Interface for processing a list of strings in a thread-safe manner.
 * Implementations of this interface should ensure atomicity of adding,
 * copying, and iterating operations.
 */
public interface ListHolder
{
    /**
     * Adds a string to the list, retrieves a copy of the list, and iterates over the list.
     * The implementation should ensure that these operations are atomic, preventing
     * other threads from seeing an intermediate state.
     *
     * @param str the string to add to the list
     */
    void addCopyAndIterate(String str);

    /**
     * Returns a copy of the current list of strings.
     *
     * @return a copy of the list of strings
     */
    List<String> getListCopy();
}
