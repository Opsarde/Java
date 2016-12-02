/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodriguez
 *
 * Programming Assignment #4
 *
 * <An assignment that implements a basic BRTree data structure
 *  including toPrettyString.>
 *
 * Shun Lu
 * 
 */

package edu.cpp.cs.cs241.prog_assgmnt_4;

/**
 * Tree interface.
 * 
 * @author Shun
 */
public interface Tree<K extends Comparable<K>, V> {
    /**
     * 
     * @param key key.
     * @param value value.
     */
    public void add(K key, V value);
    /**
     * 
     * @param key key.
     * @return value.
     */
    public V remove(K key);
    /**
     * 
     * @param key key
     * @return value.
     */
    public V lookup(K key);
    /**
     * 
     * @return string.
     */
    public String toPrettyString();
}
