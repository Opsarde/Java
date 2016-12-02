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
 * A Map ADT that has basic add, remove, 
 * get, size, and isEmpty methods.
 * 
 * @author Shun Lu
 */
public interface Map<K, V> {
    /**
     * Abstract method that adds value with a key associates
     * with it.
     * 
     * @param key key to add.
     * @param value value to add.
     */
    public void add(K key, V value);

    /**
     * Abstract method that remove given key and also its
     * value.
     * 
     * @param key given key to remove value and its key.
     * 
     * @return Removed value.
     */
    public V remove(K key);

    /**
     * Abstract method that returns value from a given key.
     * 
     * @param key given key to search value.
     * 
     * @return Found value.
     */
    public V get(K key);

    /**
     * Abstract method that returns Map size.
     * 
     * @return size.
     */
    public int size();

    /**
     * Abstract method that checks Map is empty.
     * 
     * @return True if empty.
     */
    public boolean isEmpty();
}