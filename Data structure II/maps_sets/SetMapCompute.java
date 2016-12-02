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

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

/**
 * A program that computes time taken for
 * filling and lookup set and map.
 */
public class SetMapCompute {
    public static void main(String[] args) {
        Random rand = new Random();
        HashSet<Integer> hashset = new HashSet<>();
        HashMap<Integer, String> hashmap = new HashMap<>();
        ListSet<Integer> listset = new ListSet<>();
        ListMap<Integer, String> listmap = new ListMap<>();
        long initTime, finalTime;
        double total = 100000;
        
        //Filling Sets
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            hashset.add(i);
        }
        finalTime = System.currentTimeMillis();
        System.out.println("Filling hashset: " + (finalTime - initTime));
        
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            listset.add(i);
        }
        finalTime = System.currentTimeMillis();
        System.out.println("Filling listset: " + (finalTime - initTime));
        
        //Filling Maps
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            hashmap.put(i, null);
        }
        finalTime = System.currentTimeMillis();
        System.out.println("Filling hashmap: " + (finalTime - initTime));
        
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            listmap.add(i, null);
        }
        finalTime = System.currentTimeMillis();
        System.out.println("Filling listmap: " + (finalTime - initTime));
        
        //lookup
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            hashset.contains(rand.nextInt(1000000));
        }
        System.out.println("hashset lookup average: " + 
                           (System.currentTimeMillis() - initTime) / total);
        
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            listset.contains(rand.nextInt(1000000));
        }
        System.out.println("listset lookup average: " + 
                           (System.currentTimeMillis() - initTime) / total);
        
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            hashmap.containsKey(rand.nextInt(1000000));
        }
        System.out.println("hashmap lookup average: " + 
                           (System.currentTimeMillis() - initTime) / total);
        
        initTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            listmap.get(rand.nextInt(1000000));
        }
        System.out.println("listmap lookup average: " + 
                           (System.currentTimeMillis() - initTime) / total);
        
        // fill 1 million
        // lookup randomly
        //
        
        
    }
}
