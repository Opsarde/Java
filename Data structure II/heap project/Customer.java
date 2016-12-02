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
 * A Customer class that implements Comparable interface
 * and compare based on priority.
 * It has fields of customer's name and customer's priority.
 * 
 * @author Shun Lu
 */
public class Customer implements Comparable<Customer>{
    
    /**
     * This field represents {@link Customer}'s name.
     */
    private String name;
    
    /**
     * This field represents {@link Customer}'s priority.
     */
    private Integer priority;
    
    
    /**
     * Default constructor that sets name and priority to null.
     */
    public Customer() {
        name = null;
        priority = null;
    }
    
    /**
     * Constructor that sets this name and this priority by given parameters.
     * 
     * @param name
     *            Name to be set.
     * @param priority
     *            Priority to be set.
     */
    public Customer(String name, Integer priority) {
        this.name = name;
        this.priority = priority;
    }
    
    /**
     * This method returns Customer's name.
     * 
     * @return name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * This method returns Customer's priority.
     * 
     * @return priority.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * This method overrides compareTo in Comparable
     * interface.  Comparing based on priority. 
     * 
     * @return 1 if greater.  -1 if less.  0 if equal.
     */
    @Override
    public int compareTo(Customer c) {
        if (this.priority > c.getPriority()) {
            return 1;
        }
        else if (this.priority < c.getPriority()) {
            return -1;
        }
        else
            return 0;
    }
}

