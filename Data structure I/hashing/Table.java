
//
//  Name:       Lu, Shun
//  Homework:   #2
//  Due:        Tuesday May 19, 2015
//  Course:     cs-240-02-sp15
//
//  Description:
//  This program reads words from declare.txt and 
//  put them in table with capacities 1000, 2000, 4000, 8000
//  and print statistics for each table
//


import java.util.Scanner;

public class Table<K, E>
{
   private int manyItems;
   private Object[] keys;
   private Object[] data;
   private boolean[] hasBeenUsed;
   private int collision;             // new counting element

   public Table(int capacity)
   {
      if (capacity <= 0)
         throw new IllegalArgumentException("Capacity is negative.");
      keys = new Object[capacity];
      data = new Object[capacity];
      hasBeenUsed = new boolean[capacity];
   }

   @SuppressWarnings("unchecked")
   public E put(K key, E element)
   {
      int index = findIndex(key);
      E answer;

      if (index != -1)
      {
         answer = (E) data[index];
         data[index] = element;
         return answer;
      } 
      else if (manyItems < data.length)
      {
         index = hash(key);
         while (keys[index] != null)
         {
            index = nextIndex(index);
            collision++;             // tracking collision
         }
         keys[index] = key;
         data[index] = element;
         hasBeenUsed[index] = true;
         manyItems++;
         return null;
      } 
      else
      {
         throw new IllegalStateException("Table is full.");
      }
   }

   @SuppressWarnings("unchecked")
   public E remove(K key)
   {
      int index = findIndex(key);
      E answer = null;

      if (index != -1)
      {
         answer = (E) data[index];
         keys[index] = null;
         data[index] = null;
         manyItems--;
      }

      return answer;
   }

   private int findIndex(K key)
   {
      int count = 0;
      int i = hash(key);

      while ((count < data.length) && (hasBeenUsed[i]))
      {
         if (key.equals(keys[i]))
            return i;
         count++;
         i = nextIndex(i);
      }

      return -1;
   }

   private int hash(K key)
   {
      return Math.abs(key.hashCode()) % data.length;
   }

   private int nextIndex(int i)
   {
      if (i + 1 == data.length)
         return 0;
      else
         return i + 1;
   }
   
   public boolean containsKey(K key)
   {
      return (findIndex(key) != -1);
   }
   
   @SuppressWarnings("unchecked")
   public E get(K key)
   {
      int index = findIndex(key);
      
      if (index == -1)
         return null;
      else
         return (E) data[index];
   }
   
   public String toString()
   {
      return (data.length + " " + manyItems + " " + collision + " " + 
              ((double) collision / manyItems * 100) + "%");
   }
   
   public static void main(String[] args)
   {
      Scanner k = new Scanner(System.in);
      Table<String, String> t1 = new Table<>(1000);
      Table<String, String> t2 = new Table<>(2000);
      Table<String, String> t3 = new Table<>(4000);
      Table<String, String> t4 = new Table<>(8000);
      String word;
      int wordCount = 0;
      
      while (k.hasNext())
      {
         word = k.next();
         t1.put(word, word);
         t2.put(word, word);
         t3.put(word, word);
         t4.put(word, word);
         wordCount++;
      }
      System.out.println("Word count: " + wordCount);
      System.out.println(t1 + "\n" + t2 + "\n" + t3 + "\n" + t4);
   }
}
