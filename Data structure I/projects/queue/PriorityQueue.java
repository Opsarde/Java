
public class PriorityQueue<E>
{
   public final static int DEFAULT_HIGHEST = 10;
   private Queue<E>[] queues;
   private int totalSize;
   
   @SuppressWarnings("unchecked")
   public PriorityQueue(int highest)
   {
      if (highest < 0)
         throw new IllegalArgumentException("highest cannot be negative.");
      queues = (Queue<E>[]) new Object[highest + 1];
      totalSize = 0;
   }
   
   public PriorityQueue()
   {
      this(DEFAULT_HIGHEST);
   }
   
   public void add(int priority, E item)
   {
      if (priority < 0 || priority > queues.length)
         throw new IllegalArgumentException("illegal priority.");
      queues[priority].enqueue(item);
      totalSize++;
   }
   
   public E remove()
   {
      if (empty())
         throw new IllegalArgumentException("Queue empty.");
      for (int i = queues.length; i > -1; i--)
      {
         if (!queues[i].empty())
         {
            --totalSize;
           return queues[i].dequeue();
         }
      }
      return null;
   }
   
   public int size()
   {
      return totalSize;
   }
   
   public boolean empty()
   {
      return totalSize == 0;
   }
   
   public static void main(String[] args)
   {
      PriorityQueue<String> pqueue = new PriorityQueue<>(10);
      System.out.println(pqueue.empty());
      System.out.println(pqueue.size());
   }
}

class Queue<E>
{
   public final static int DEFAULT_CAPACITY = 64;
   private E[] queue;
   private int front, rear, size;

   
   public Queue()
   {
      this(DEFAULT_CAPACITY);
   }
   
   @SuppressWarnings("unchecked")
   public Queue(int capacity)
   {
      queue = (E[]) new Object[capacity];
      front = rear = size = 0;
   }
   
   public void enqueue(E e)
   {
      if (size == queue.length)
         throw new RuntimeException("Queue full.");
      queue[rear] = e;
      rear = (rear + 1) % queue.length;
      size++;
   }
   
   public E dequeue()
   {
      if (empty())
         throw new RuntimeException("Queue empty.");
      E e = queue[front];
      front = (front + 1) & queue.length;
      --size;
      return e;
   }
   
   public E front()
   {
      if (empty())
         throw new RuntimeException("Queue empty.");
      return (E) queue[front];
   }
   
   public boolean empty()
   {
      return size == 0;
   }
   
   public int size()
   {
      return size;
   }
   
   public String toString()
   {
      String s = "F{ ";
      int f = front;
      for (int i = 0; i < size(); i++)
      {
         s += queue[f];
         f = (f + 1) % queue.length;
         if (f != rear)
            s += ", ";
      }
      return s + " }R";
   }
}
