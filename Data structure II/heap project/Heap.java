/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr¨ªguez
 *
 * Programming Assignment #1
 *
 * <An assignment that helps to understand structure
 *  of NodeHeap and acts as a restaurant table-assistant>
 *
 * Shun Lu
 * 
 */
package edu.cpp.cs.cs241.prog_assgmnt_1;

/**
 * An interface of generic Heap ADT that
 * has basic add and remove methods
 * in addition of getSortedContents,
 * fromArray, and toArray.
 * 
 * An heap should follow its property while adding or removing.
 * 
 * @author Shun Lu
 */
public interface Heap<V extends Comparable<V>> {
    
    /**
     * This method adds a content to Heap.
     */
    public void add(V value);

    /**
     * This method turns Heap into array.
     */
    public V[] toArray();

    /**
     * This method removes content from root in Heap and returns the content.
     */
    public V remove();

    /**
     * This method turns array to Heap.
     */
    public void fromArray(V[] array);

    /**
     * This method returns array that is sorted by HeapSort.
     */
    public V[] getSortedContents();
}