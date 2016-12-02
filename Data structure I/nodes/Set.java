
//
//Name:       Lu, Shun
//Project:    #2
//Due:        Thursday May 21, 2015
//Course:     cs-240-02-sp15
//
//Description:
//This program adds objects to sets and using definition of sets
//to compare each set.  It prints object's state of set and generates
//union, intersection, and complement sets of two sets.
//

public class Set<T>
{
   protected Node<T> head;
   protected int length;

   public Set()
   {
      head = new Node<>(null, null);  // dummy head, head should always point to dummy head
      length = 0;
   }
   
   // list of all required methods, specify return type for each method
   
   public boolean addElement(T data)  // no duplicate, add last (easy to add to another set)
   {
      if (contain(data))
         return false;    
      Node<T> trav = head;
      while (trav.getNext() != null)
         trav = trav.getNext();
      Node<T> node = new Node<>(data, null);
      trav.setNext(node);
      length++;
      return true;
   }
   
   public boolean remove(T data)
   {
      if (contain(data))
      {
         Node<T> trav = head;

         while (trav.getNext() != null)
         {
            if (trav.getNext().getData().equals(data))
            {
               Node<T> target = trav.getNext();
               trav.setNext(target.getNext());
               target.setNext(null);
               length--;
               return true;
            }
            trav = trav.getNext();
         }
      }
      return false;
   }
   
   public boolean contain(T data)
   {
      Node<T> trav = head.getNext();

      while (trav != null)
      {
         if (trav.getData().equals(data))
         {
            return true;
         }
         trav = trav.getNext();
      }
      return false;
   }
   
   public Integer size()
   {
      return length;
   }
   
   public boolean subsetOf(Set<T> target)
   {
      
      for (Node<T> trav = head.getNext(); trav != null; trav = trav.getNext())
      {
         if (!target.contain(trav.getData()))
            return false;
      }
      return true;
   }
   
   public boolean isEqual(Set<T> target)
   {
      return subsetOf(target) && target.size().equals(size());
   }
   
   public Set<T> union(Set<T> target)
   {
      Set<T> newSet = new Set<>();
      Node<T> trav = head.getNext();
      Node<T> trav2 = target.head.getNext();   // need to traverse both sets
      while (trav != null || trav2 != null)
      {
         if (trav != null)                     // solve null pointer exception
         {
            newSet.addElement(trav.getData());
            trav = trav.getNext();
         }
         if (trav2 != null)
         {
            newSet.addElement(trav2.getData());
            trav2 = trav2.getNext();
         }
      }
      return newSet;
   }
   
   public Set<T> intersection(Set<T> target)
   {
      Set<T> newSet = new Set<>();
      Node<T> trav = head.getNext();
      while (trav != null)
      {
         if (target.contain(trav.getData()))
            newSet.addElement(trav.getData());
         trav = trav.getNext();
      }
      return newSet;
   }
   
   public Set<T> complement(Set<T> target)
   {
      Set<T> newSet = new Set<>();
      Node<T> trav =head.getNext();
      while (trav != null)
      {
         if (!target.contain(trav.getData()))
            newSet.addElement(trav.getData());
         trav = trav.getNext();
      }
      return newSet;
   }
   
   public String toString()
   {
      String str = "";
      
      for (Node<T> trav = head.getNext(); trav != null; trav = trav.getNext())
         str += trav.getData() + ",";
      if (str.length() != 0)             // solve out of index exception
         return "{" + str.substring(0, str.length() - 1) + "}";
      return "{}";
   }
   
   public static void main(String[] args)
   {
      // testing generic
      Set<String> stringtest = new Set<>();
      Set<String> stringtest2 = new Set<>();
      Set<String> stringtest3 = new Set<>();
      stringtest.addElement("cat");
      stringtest.addElement("dog");
      stringtest2.addElement("dog");
      stringtest3.addElement("dog");
      System.out.println(stringtest + " " + stringtest.size());
      stringtest.remove("dog");
      System.out.println(stringtest + " " + stringtest.size());
      System.out.println(stringtest.contain("cat") + " " + stringtest.contain("dog"));
      System.out.println(stringtest.isEqual(stringtest2) + " " + 
                         stringtest2.isEqual(stringtest3));
      System.out.println("-----------------");
      // testing subsetOf, union, intersection, complement
      Set<Integer> acase1 = new Set<>();
      Set<Integer> bcase1 = new Set<>();
      Set<Integer> acase2 = new Set<>();
      Set<Integer> bcase2 = new Set<>();
      Set<Integer> acase3 = new Set<>();
      Set<Integer> bcase3 = new Set<>();
      Set<Integer> acase4 = new Set<>();
      Set<Integer> bcase4 = new Set<>();
      Set<Integer> acase5 = new Set<>();
      Set<Integer> bcase5 = new Set<>();
      acase1.addElement(1);
      acase1.addElement(2);
      acase1.addElement(3);
      bcase1.addElement(2);
      bcase1.addElement(1);
      bcase1.addElement(3);
      acase2.addElement(1);
      bcase2.addElement(1);
      bcase2.addElement(2);
      acase3.addElement(1);
      acase3.addElement(2);
      acase3.addElement(3);
      bcase3.addElement(2);
      bcase3.addElement(3);
      bcase3.addElement(4);
      bcase3.addElement(5);
      acase4.addElement(1);
      bcase4.addElement(2);
      bcase4.addElement(3);
      bcase5.addElement(1);
      System.out.println(acase1.subsetOf(bcase1) + " " + 
                         acase1.union(bcase1) + " " + 
                         acase1.intersection(bcase1) + " " +
                         acase1.complement(bcase1));
      System.out.println(acase2.subsetOf(bcase2) + " " + 
                         acase2.union(bcase2) + " " + 
                         acase2.intersection(bcase2) + " " +
                         acase2.complement(bcase2));
      System.out.println(acase3.subsetOf(bcase3) + " " + 
                         acase3.union(bcase3) + " " + 
                         acase3.intersection(bcase3) + " " +
                         acase3.complement(bcase3));
      System.out.println(acase4.subsetOf(bcase4) + " " + 
                         acase4.union(bcase4) + " " + 
                         acase4.intersection(bcase4) + " " +
                         acase4.complement(bcase4));
      System.out.println(acase5.subsetOf(bcase5) + " " + 
                         acase5.union(bcase5) + " " + 
                         acase5.intersection(bcase5) + " " +
                         acase5.complement(bcase5));
   }
}

class Node<E>
{
   private E data;
   private Node<E> next;

   public Node(E data, Node<E> next)
   {
      this.data = data;
      this.next = next;
   }

   public void setData(E data)
   {
      this.data = data;
   }

   public void setNext(Node<E> next)
   {
      this.next = next;
   }

   public E getData()
   {
      return data;
   }

   public Node<E> getNext()
   {
      return next;
   }
}
