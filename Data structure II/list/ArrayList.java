/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodríguez
 *
 * Programming Assignment #1
 *
 * <An exercise for refreshing java programming by creating an ArrayList>
 *
 * Shun Lu
 * 
 */

package edu.cpp.cs.cs241.prog_assgmnt_1;

/**
 * This class implements from given List interface.  
 * It represents an array list that grows and shrinks automatically, has
 * keys, and values. It stores both key and value and tracks it's size. Just
 * like java's Arraylist class.
 * 
 * @author Shun Lu
 */
public class ArrayList<K extends Comparable<K>,V> implements List<K,V>{
    /**
     * This field represents {@link ArrayList}'s key in array.
     */
    private K[] keys;
    /**
     * This field represents {@link ArrayList}'s value in array.
     */
    private V[] values;
    // private T[] array;
    /**
     * This field represents {@link ArrayList}'s size.
     */
    private int size;

    /**
     * The default constructor for the class {@link ArrayList}. Initially a
     * {@link ArrayList} has no {@link #keys} and {@link #values} with size of
     * {@code 0}.
     */
    @SuppressWarnings("unchecked")
    public ArrayList() {
        // array = (T[]) new Object[0];
        keys =  (K[]) new Comparable[0];
        values = (V[]) new Object[0];
        size = 0;
    }

    /**
     * This method adds a key and value to {@code this} {@link ArrayList}.
     * Returns {@code true} if a key with value is added. Returns {@code false}
     * if there is already a key.
     * 
     * @param key
     *            The key to be given to {@code this} {@link ArrayList}.
     * @param value
     *            The value to be given to {@code this} {@link ArrayList}.
     * 
     */
    @SuppressWarnings("unchecked")
    public boolean add(K key, V value) {
        for (K k : keys) {
            if (k.equals(key))
                return false;
        }
        K[] newK = (K[]) new Comparable[++size];
        V[] newV = (V[]) new Object[size];
        for (int i = 0; i < keys.length; i++) {
            newK[i] = keys[i];
            newV[i] = values[i];
        }
        newK[size - 1] = key;
        newV[size - 1] = value;
        keys = newK;
        values = newV;
        return true;
    }

    /**
     * This method removes this {@code #keys[n]} of the {@link ArrayList}. 
     * Returns this removed {@code values[n]}.
     * 
     * @param n
     *            The index to be given to {@code this} {@link ArrayList}.
     *            
     * @throws IndexOutOfBoundsException
     *            Given index is out of bound.
     */
    @SuppressWarnings("unchecked")
    public V remove(int n) {
        if (n < 0 || n >= size)
            throw new IndexOutOfBoundsException("Index is out of bound.");
        int breakp = n;
        V value = values[n];
        V[] newV = (V[]) new Object[--size];
        K[] newK = (K[]) new Comparable[size];
        if (size == 0)
            // Nothing to remove
            ;
        else if (n == size) {
            for (int i = 0; i < size; i++) {
                newV[i] = values[i];
                newK[i] = keys[i];
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                newV[i] = (i < n) ? values[i] : values[++breakp];
                newK[i] = (i < n) ? keys[i] : keys[breakp];
            }
        }
        values = newV;
        keys = newK;
        return value;
    }

    /**
     * This method removes the first element of this {@link ArrayList}.
     * Returns this removed {@code values[n]}.
     */
    public V remove() {
        return remove(0);
    }

    /**
     * This method removes value that associates {@code key} of this
     * {@link ArrayList}. Returns this removed {@code values[n]}.
     * 
     * @param key
     *            Given key to {@code this} {@link ArrayList}.
     */
    public V remove(K key) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(key)) {
                return remove(i);
            }
        }
        return null;
    }

    /**
     * This method returns value that in given index of this {@link ArrayList}.
     * 
     * @param n
     *            Given index to {@code this} {@link ArrayList}.
     *            
     * @throws IndexOutOfBoundsException
     *            Given index is out of bound.
     */
    public V get(int n) {
        if (n < 0 || n >= size)
            throw new IndexOutOfBoundsException("Index is out of bound.");
        return values[n];
    }

    /**
     * This method returns value by given {@code key} of this
     * {@link ArrayList}.
     * 
     * @param key
     *            Given key to {@code this} {@link ArrayList}.
     */
    public V lookup(K key) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(key))
                return values[i];
        }
        return null;
    }

    /**
     * This method returns this {@code size} of {@link ArrayList}.
     */
    public int size() {
        return size;
    }
}
