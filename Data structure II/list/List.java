/**
 * This file pedagogical material for the course
 * CS 240: Data Structures and Algorithms
 * taught at California State Polytechnic University - Pomona, and
 * cannot be used without express written consent from the author.
 * 
 * Copyright (c) 2013 - Edwin Rodr&iacute;guez.
 */
package edu.cpp.cs.cs241.prog_assgmnt_1;

/**
 * @author Edwin Rodr&iacute;guez
 *
 */
public interface List<K extends Comparable<K>,V> {
	
	public abstract boolean add(K key,V value);
	
	public abstract V remove(K key);
	
	public abstract V remove(int n);
	
	public abstract V remove();
	
	public abstract V lookup(K key);
	
	public abstract int size();
	
	public abstract V get(int n);
}


