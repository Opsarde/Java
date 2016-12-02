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

import java.util.ArrayList;

/**
 * ListMap class that implements Map interface
 * and implemented with list data structure.
 * Has property of requiring both key and value.
 * 
 * @author Shun Lu
 */
public class ListMap<K, V> implements Map<K, V>{

    /**
     * This field represents size of {@link ListMap}'.
     */
    private int size;
    
    /**
     * This field represents listing storage of keys 
     * for {@link ListMap}.
     */
    private ArrayList<K> keys;
    
    /**
     * This field represents listing storage of values 
     * for {@link ListMap}.
     */
    private ArrayList<V> values;
    
    /**
     * Default constructor for initialize two lists
     * and size to 0.
     */
    public ListMap(){
        keys = new ArrayList<>();
        values = new ArrayList<>();
        size = 0;
    }

    /**
     * This method adds a key and a value
     * to this {@link ListMap}, for each key and value
     * are stored in two separated lists. 
     * 
     * @param key
     *     Given value-associated key.
     * @param value
     *     Given key-associated value.
     */
    public void add(K key, V value) {
        // unsorted
        if (!keys.contains(key)) {
            keys.add(key);
            values.add(value);
            ++size;
        }        
    }

    /**
     * This method removes a key with its value 
     * from this {@link ListMap}.  
     * 
     * @param key
     *      Given key to remove.
     *      
     *@return Value of this key, and null if not exist.
     */
    public V remove(K key) {
        int index = getIndex(key);
        if (index != -1){
            keys.remove(key);
            --size;
            return values.remove(index);
        }
        return null;
    }

    /**
     * This method searches for given key in this
     * {@link ListMap}.
     * 
     * @param key
     *      Given key to search.
     *      
     *@return Value mapped to given key if found, else null.
     */
    public V get(K key) {
        int index = getIndex(key);
        if (index != -1) {
            return values.get(index);
        }
        return null;
    }

    /**
     * This method returns this {@link ListMap}'s
     * size.
     */
    public int size() {
        return size;
    }

    /**
     * This method returns True if empty, and false
     * if not empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * This private method returns index of key located
     * in this {@link ListMap}.  Return -1 if key is not
     * in this {@link ListMap}.
     */
    private int getIndex(K key) {
        return keys.indexOf(key);
    }
}