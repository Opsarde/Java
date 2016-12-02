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
 * ListSet class that implements Set interface
 * and implemented with list data structure.
 * Has property of no duplicates.
 * 
 * @author Shun Lu
 */
public class ListSet<E> implements Set<E>
{
    /**
     * This field represents {@link ListSet}'s list
     * that stores values and has no duplicates.
     */
    private ArrayList<E> list;
    
    /**
     * This field represents {@link ListSet}'s size.
     */
    private int size;
    
    /**
     * Default constructor that initializes {@link list} 
     * and {@link size} to 0.
     */
    public ListSet(){
        list = new ArrayList<>();
        size = 0;
    }
    
    /**
     * This method adds value to this {@link ListSet}.
     * 
     * @param data
     *     value going to be stored in this {@link ListSet}.
     */
    public void add(E data){
        if (contains(data)){
            return;
        }
        list.add(data);
        ++size;
    }
    
    /**
     * This method removes value in this {@link ListSet}.
     * 
     * @param data
     *     value to be removed from {@link ListSet}.
     */
    public void remove(E data){
        if (contains(data)){
            list.remove(data);
            --size;
            return;
        }
    }
    
    /**
     * This method checks existence of a value in this {@link ListSet}.
     * 
     * @param data
     *     value to check in {@link ListSet}.
     * @return True if exist, false otherwise.
     */
    public boolean contains(E data){
        return list.contains(data);
    }
    
    /**
     * This method returns size of this {@link ListSet}.
     */
    public int size(){
        return size;
    }
    
    /**
     * A method for checking empty size.
     * 
     * @return True if empty.
     */
    public boolean isEmpty(){
        return size == 0;
    }
}
