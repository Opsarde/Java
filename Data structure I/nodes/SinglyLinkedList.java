//
//   Name:       Lu, Shun
//   Homework:   #1
//   Due:        Thursday April 23, 2015
//   Course:     cs-240-02-sp15
//
//   Description:
//      This program is designed to add integers in
//      a singly linked list that allows no duplicated integers.
//

public class SinglyLinkedList
{
   protected Node head;
   protected int length;

   public SinglyLinkedList()
   {
      head = null;
      length = 0;
   }

   public void addFirstNoDuplicate(int data)
   {
      Node trav = head;

      while (trav != null)
      {
         if (trav.getData() == data)
         {
            return;
         }
         trav = trav.getNext();
      }
      head = new Node(data, head);
      length++;
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

   public static void main(String[] args)
   {
      SinglyLinkedList list = new SinglyLinkedList();

      System.out.println("number of items without add: " + list.length);
      list.addFirstNoDuplicate(-99999);
      list.addFirstNoDuplicate(0);
      System.out.println("number of items added: " + list.length);
      list.addFirstNoDuplicate(0);
      System.out.println("number of items duplicated added: " + list.length);
      list.addFirstNoDuplicate(99999);
      System.out.println("number of items added: " + list.length);
      list.print();
   }
}

class Node
{
   private int data;
   private Node next;

   public Node(int data, Node next)
   {
      this.data = data;
      this.next = next;
   }

   public void setData(int data)
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