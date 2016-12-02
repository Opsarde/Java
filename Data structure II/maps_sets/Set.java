/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr¨ªguez
 *
 * Programming Assignment #3
 *
 * <An assignment that helps to understand data structure
 *  of set and map and to see the improved efficiency by hashing.>
 *
 * Shun Lu
 * 
 */
package edu.cpp.cs.cs241.prog_assgmnt_3;

/**
 * A Set ADT that has basic add, remove, 
 * contains, size, and isEmpty methods.
 * 
 * 
 * @author Shun Lu
 */
public interface Set<V> {
    /**
     * Abstract method that adds value to Set.
     * 
     * @param value value to add.
     */
    public void add(V value);

    /**
     * Abstract method that removes value from Set.
     * 
     * @param value value to remove.
     */
    public void remove(V value);

    /**
     * Abstract method that returns true or false
     * if value exists in Set.
     * 
     * @param value Value to search.
     * 
     * @return True if in Set.
     */
    public boolean contains(V value);

    /**
     * Abstract method that returns size of Set.
     * 
     * @return Size.
     */
    public int size();

    /**
     * Abstract method that checks Set is empty.
     * 
     * @return True if empty.
     */
    public boolean isEmpty();
}