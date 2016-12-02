/**
 * This file pedagogical material for the course
 * CS 241: Data Structures and Algorithms II
 * taught at California State Polytechnic University - Pomona, and
 * cannot be used without express written consent from the author.
 * 
 * Copyright (c) 2014 - Edwin Rodr&iacute;guez.
 */
package edu.csupomona.cs.cs241.structures;

/**
 * @author Edwin Rodr&iacute;guez
 * 
 */
public interface Heap<V extends Comparable<V>> {
	
	public static enum MODE {MAX, MIN};

	/**
	 * @param value
	 */
	public void add(V value);

	/**
	 * @return
	 */
	public V[] toArray();

	/**
	 * @return
	 */
	public V remove();
	
	/**
	 * @param array
	 */
	public void fromArray(V[] array);

	/**
	 * @return
	 */
	public V[] getSortedContents();
	
	/**
	 * @return
	 */
	public MODE getMode();
	
	/**
	 * @param mode
	 */
	public void setMode(MODE mode);

}
