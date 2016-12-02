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

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *  This Table class reads input from user, stores data 
 *  as Customer in a Heap,
 *  and output Customer's name based on priority.
 */
public class Table {
    public static void main(String[] args) {
        // This is Scanner object that works
        // as input
        Scanner k = new Scanner(System.in);
        // Declares a NodeHeap that stores Customer type data
        NodeHeap<Customer> queue = new NodeHeap<>();
        // name of customer
        String name;
        // priority number of customer
        int priority;
        // user input
        String input;
        // a variable to check true/false
        boolean check = true;
        while (check) {
            System.out.println("This is an application for seating host.");
            System.out.println("Enter 1 to add a customer.");
            System.out.println("Enter 2 to give a customer seat.");
            System.out.println("Enter -1 to terminate the application.");
            System.out.println("----------------------------------------");
            input = k.nextLine();
            if (input.equals("-1") || input.equals("1") || input.equals("2")) {
                if (input.equals("1")) {
                    System.out.println("Enter customer's name and priority # to continue.");
                    System.out.print("Name: ");
                    name = k.nextLine();
                    System.out.println("Priority: \n" + "\t7. VIP\n"
                            + "\t6. Advanced call\n" + "\t5. Senior\n"
                            + "\t4. Veteran\n"
                            + "\t3. Group(more than 4 people\n"
                            + "\t2. Family with children\n"
                            + "\t1. Everyone else\n");
                    // To prevent crash of application
                    // if worker has no idea what to input
                    try {
                    priority = k.nextInt();
                    } catch (InputMismatchException e) {
                        priority = -1;
                    }
                    if (!(priority <= 7 && priority >= 1)) {
                        // loop to ask user to input priority
                        while (check) {
                            System.out.println("Please enter a value between 1 and 7: ");
                            k.nextLine();
                            try {
                            priority = k.nextInt();
                            } catch (InputMismatchException e) {
                                priority = -1;
                            }
                            check = (!(priority <= 7 && priority >= 1)) ? true : false;
                        }
                    }
                    queue.add(new Customer(name, priority));
                    System.out.println("Adding complete.\n");
                    k.nextLine();
                    check = true;
                }
                else if (input.equals("2")) {
                    if (queue.getSize() == 0)
                        System.out.println("There is no customer in queue.\n");
                    else
                        System.out.println("The next available free seat will be given to: "
                                        + queue.remove().getName() + "\n");
                }
                else {
                    System.out.println("Good bye.");
                    check = false;
                    k.close();
                }
            }
            else {
                System.out.println("\nPlease only enter: 1, 2, or -1.\n");
            }
        }
    }
}
