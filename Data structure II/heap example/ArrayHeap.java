/**
 * This file pedagogical material for the course
 * CS 241: Data Structures and Algorithms II
 * taught at California State Polytechnic University - Pomona, and
 * cannot be used without express written consent from the author.
 * 
 * Copyright (c) 2014 - Edwin Rodr&iacute;guez.
 */
package edu.csupomona.cs.cs241.structures;

import java.util.Arrays;

/**
 * @author Edwin Rodr&iacute;guez
 * 
 */
public class ArrayHeap<V extends Comparable<V>> implements Heap<V> {

	private int INITIAL_SIZE = 100;

	private MODE mode;

	private Object[] theHeap;

	private int elemCount = 0;

	private int lastIndex = 0;

	public ArrayHeap() {
		mode = Heap.MODE.MAX;
		theHeap = new Object[INITIAL_SIZE];
	}

	@Override
	public void add(V value) {
		ensureCapacity();

		if (elemCount++ > 0) {
			++lastIndex;
		}

		theHeap[lastIndex] = value;
		siftUp(lastIndex);
	}

	private void siftUp(int index) {
		assert (index >= 0 && index <= (lastIndex));

		int parentIndex;
		boolean swapped = true;

		while (swapped && (index > 0)) {
			swapped = false;
			parentIndex = (index - 1) / 2;

			if (compareValues(index, parentIndex)) {
				swapValues(index, parentIndex);
				index = parentIndex;
				swapped = true;
			}
		}
	}

	private void ensureCapacity() {
		if (lastIndex == (theHeap.length - 1)) {
			theHeap = Arrays.copyOf(theHeap, 2 * theHeap.length);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V[] toArray() {
		V[] result = null;

		if (elemCount > 0) {
			result = (V[]) java.lang.reflect.Array.newInstance(
					theHeap[0].getClass(), elemCount);

			System.arraycopy(theHeap, 0, result, 0, elemCount);

			return result;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove() {
		Object result = null;

		if (elemCount > 0) {
			result = theHeap[0];
			swapValues(0, lastIndex);
			theHeap[lastIndex] = null;
			--elemCount;

			if (elemCount > 0) {
				--lastIndex;
				siftDown(0);
			}
		}

		return (V) result;
	}

	@Override
	public void fromArray(V[] array) {
		int newCapacity = computeCapacity(array.length);
		theHeap = Arrays.copyOf(array, newCapacity);
		elemCount = array.length;
		lastIndex = elemCount > 1 ? elemCount - 1 : 0;
		heapify();
	}

	private void heapify() {
		if (elemCount > 1) {
			for (int i = (lastIndex - 1) / 2; i >= 0; --i) {
				siftDown(i);
			}
		}
	}

	private int computeCapacity(int neededCapacity) {
		int currentCapacity = theHeap.length;

		while (neededCapacity > currentCapacity) {
			currentCapacity *= 2;
		}

		return currentCapacity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V[] getSortedContents() {
		V[] result = null;
		int oldLast = lastIndex;

		if (elemCount > 0) {
			while (lastIndex > 0) {
				swapValues(0, lastIndex);
				--lastIndex;
				siftDown(0);
			}
			
			result = toArray();
			lastIndex = oldLast;
			heapify();
		}

		return result;
	}


	@Override
	public Heap.MODE getMode() {
		return mode;
	}

	@Override
	public void setMode(Heap.MODE mode) {
		this.mode = mode;
		heapify();
	}

	private void siftDown(int index) {
		assert (index >= 0 && index <= (lastIndex));

		if (index <= ((lastIndex - 1) / 2)) {
			int ch1Index;
			int ch2Index;
			int swapIndex = 0;
			boolean swapped;

			do {
				swapped = false;
				ch1Index = 2 * index + 1;

				if (ch1Index <= lastIndex) {
					ch2Index = ch1Index + 1;

					swapIndex = ch2Index <= lastIndex
							&& compareValues(ch2Index, ch1Index) ? ch2Index
							: ch1Index;
				}

				if (compareValues(swapIndex, index)) {
					swapValues(swapIndex, index);
					index = swapIndex;
					swapped = true;
				}
			} while (swapped);
		}
	}

	private void swapValues(int index1, int index2) {
		Object value = theHeap[index1];
		theHeap[index1] = theHeap[index2];
		theHeap[index2] = value;
	}

	private boolean compareValues(int ch2Index, int ch1Index) {
		if (mode == Heap.MODE.MAX) {
			return ((V) theHeap[ch2Index]).compareTo((V) theHeap[ch1Index]) > 0;
		} else {
			return ((V) theHeap[ch2Index]).compareTo((V) theHeap[ch1Index]) < 0;
		}
	}

}
