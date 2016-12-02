
//
// Name:       Lu, Shun
// Project:    #1
// Due:        Tuesday May 5, 2015
// Course:     cs-240-02-sp15
//
// Description:
// This program reads Integers from file and return as
// array and print 20 numbers per line followed by
// count, average, and standard deviation.
//


import java.util.Scanner;

public class Stat
{
   final static double ZERO = 0;
   
   public static void main(String[] args)
   {
      Scanner k = new Scanner(System.in);
      Integer[] array = readData(k);

      
      System.out.println("List: ");
      for (int i = 0; i < array.length; i++)
      {
         System.out.print(array[i] + " ");
         if ((i + 1) % 20 == 0)
            System.out.print("\n");
      }
      System.out.print("\n");
      System.out.println("Count = " + array.length);
      System.out.printf("Average = %.2f\n", average(array));
      System.out.printf("Standard Deviation = %.2f\n", stdDev(array));
   }

   public static Integer[] readData(Scanner k)
   {
      SinglyLinkedList list = new SinglyLinkedList();
      while (k.hasNext())
         list.addLast(k.nextInt());
      return list.toArray();
   }

   public static Double average(Integer[] array)
   {
      if (array.length == 0)
         return ZERO;
      Double sum = (double) 0;
      for (Integer e : array)
         sum += e;
      return sum / array.length;
   }

   public static Double stdDev(Integer[] array)
   {
      if (array.length == 0)
         return ZERO;
      Double sum = (double) 0;
      for (Integer e : array)
         sum += (e - average(array)) * (e - average(array));
      return Math.sqrt(sum / array.length);
   }
}
   
class SinglyLinkedList
{
   protected Node head;
   protected int length;

   public SinglyLinkedList()
   {
      head = null;
      length = 0;
   }

   public void addFirst(Integer data)
   {

      head = new Node(data, head);
      length++;
   }

   public void addLast(Integer data)
   {
      Node node = new Node(data, null);

      if (head == null)
      {
         head = node;
      } 
      else
      {
         Node trav = head;
         while (trav.getNext() != null)
         {
            trav = trav.getNext();
         }
         trav.setNext(node);
      }
      length++;
   }

   public boolean search(Integer element)
   {
      Node trav = head;

      while (trav != null)
      {
         if (trav.getData() == element)
         {
            return true;
         }
         trav = trav.getNext();
      }
      return false;
   }

   public int countFrequency(Integer element)
   {
      Node trav = head;
      int count = 0;

      while (trav != null)
      {
         if (trav.getData() == element)
         {
            ++count;
         }
         trav = trav.getNext();
      }
      return count;
   }

   public Integer[] toArray()
   {
      Integer[] array = new Integer[length];
      Node trav = head;
      
      for (int i = 0; i < array.length; i++)
      {
         array[i] = trav.getData();
         trav = trav.getNext();
      }
      return array;
   }
   
   public void print()
   {
      Node trav = head;

      while (trav != null)
      {
         System.out.println(trav.getData());
         trav = trav.getNext();
      }
   }
}

class Node
{
   private Integer data;
   private Node next;

   public Node(Integer data, Node next)
   {
      this.data = data;
      this.next = next;
   }

   public void setData(Integer data)
   {
      this.data = data;
   }

   public void setNext(Node next)
   {
      this.next = next;
   }

   public int getData()
   {
      return data;
   }

   public Node getNext()
   {
      return next;
   }
}
