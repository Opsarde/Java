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

/**
 * A NodeHeap class that implements Heap interface.
 * Internal structure is node.
 * Has variable root and size.
 * 
 * @author Shun Lu
 */
public class NodeHeap<V extends Comparable<V>> implements Heap<V> {

    /**
     * This field represents {@link NodeHeap}'s first node.
     */
    private Node<V> root;

    /**
     * This field represents {@link NodeHeap}'s size.
     */
    private int size;
    
    /**
     * Default constructor that sets {@link root} to null
     * and {@link size} to 0.
     */
    public NodeHeap(){
        root = null;
        size = 0;
    }
    
    /**
     * This method adds a node to {@link NodeHeap}.
     * 
     * @param value
     *             data that needs to be added to {@code this} {@link NodeHeap}.
     */
    public void add(V value) {
    // add a node to last position + 1
        if (size == 0){
            root = new Node<>(value, null, null, null);
            size++;
        }
        else if (size >= 1) {
            int newSize = size + 1;
            // int left = 0;
            // int right = 1;
            // get to position to be added
            String binary = Integer.toBinaryString(newSize);
            Node<V> trav = root;
            for (int i = 1; i < binary.length(); i++) {
                switch (binary.charAt(i)) {
                    case '0':
                        if (trav.getLeft() != null)
                            trav = trav.getLeft();
                        else {
                            trav.setLeft(new Node<V>(value, trav, null, null));
                            shiftup(trav.getLeft());
                        }
                        break;
                    case '1':
                        if (trav.getRight() != null)
                            trav = trav.getRight();
                        else {
                            trav.setRight(new Node<V>(value, trav, null, null));
                            shiftup(trav.getRight());
                        }
                        break;
                }
            }
            size = newSize;
        }
    }

    /**
     * This method transforms {@code this} {@link NodeHeap}
     * to array representation of Heap.
     * 
     * @return The array.
     */
    @SuppressWarnings("unchecked")
    public V[] toArray() {
        V[] result = null;
        if (size > 0) {
            result = (V[]) java.lang.reflect.Array.newInstance(
                    root.getData().getClass(), size);
            Node<V> trav = root;
            String binary;
            // traverse all sub heaps, add value to index during traversing
            for (int i = 0; i < size; i++) {
                binary = Integer.toBinaryString(i + 1);
                for (int j = 1; j < binary.length(); j++) {
                    switch (binary.charAt(j)) {
                        case '0':
                            trav = trav.getLeft();
                            break;
                        case '1':
                            trav = trav.getRight();
                            break;
                    }
                }
                result[i] = trav.getData();
                trav = root;
            }
        }
        return result;
    }

    /**
     * This method removes first node in {@link NodeHeap}.
     * 
     * @return The removed value.
     */
    public V remove() {
        V result = null;
        //only root
        if (size == 1) {
            result = root.getData();
            root = null;
            size--;
        }
        //has leaf
        else if (size > 1) {
            int newSize = size - 1;
            result = root.getData();
            // int left = 0;
            // int right = 1;
            // traverse to last node
            String binary = Integer.toBinaryString(size);
            Node<V> trav = root;
            for (int i = 1; i < binary.length(); i++) {
                switch (binary.charAt(i)) {
                    case '0':
                        trav = trav.getLeft();
                        break;
                    case '1':
                        trav = trav.getRight();
                        break;
                }
            }
            swap(root, trav);
            if (trav.getParent().getLeft() == trav) {
                trav.getParent().setLeft(null);
                trav.setParent(null);
            }
            else {
                trav.getParent().setRight(null);
                trav.setParent(null);
            }
            shiftdown(root);
            size = newSize;
        }
        return result;
    }

    /**
     * This method transforms array to heap.
     * 
     * @param array
     *             Array that needs to be transformed.
     */
    public void fromArray(V[] array) {
        // heapified automatically
        for (V value : array){
            add(value);
        }
    }

    /**
     * This method makes this {@link NodeHeap} into an array and HeapSort
     * it, then return this array.
     * {@code this} {@link NodeHeap}'s content should not be modified.
     * 
     * @return The sorted array.
     */
    @SuppressWarnings("unchecked")
    public V[] getSortedContents() {
        V[] result = null;
        // popping root, working like queue
        if (size > 0) {
            result = (V[]) java.lang.reflect.Array.newInstance(
                         root.getData().getClass(), size);
            V[] unsorted = toArray();
            int i = size - 1;
            while (size != 0) {
                result[i--] = remove();
            }
            fromArray(unsorted);
        }
        return result;
    }
    
    /**
     * This method swaps contents in two nodes.
     * 
     * @param node1
     *             Current node that needs to be swapped.
     * @param node2
     *             Node that needs to be swapped by current node.
     */
    private void swap(Node<V> node1, Node<V> node2){
        V temp = node1.getData();
        node1.setData(node2.getData());
        node2.setData(temp);;
    }
    
    /**
     * This method performs shift up of one node.
     * 
     * @param node
     *            Node that needs to be shifted up.
     */
    private void shiftup(Node<V> node){
        // precondition: size is greater than 1 and something need to be swapped
        boolean swapped = true;
        Node<V> trav = node;
        // takes only log n
        // shift up current node till parent is greater than current
        while (trav != root && swapped == true){
            swapped = false;
            if(compareValues(trav, trav.getParent())){
                swap(trav, trav.getParent());
                swapped = true;
            }
            trav = trav.getParent();
        }
    }
    
    /**
     * This method performs shift down of one node.
     * 
     * @param node
     *            Node that needs to be shifted down.
     */
    private void shiftdown(Node<V> node){
        boolean swapped = true;
        Node<V> trav = node;
        //loop till leaf, stop immediately if no swap
        while (!trav.isLeaf() && swapped) {
            swapped = false;
            if (trav.getLeft() != null && trav.getRight() != null) {
                // two children exist
                // decide which one to swap
                Node<V> chosenChild = compareValues(trav.getLeft(),
                        trav.getRight()) ? trav.getLeft() : trav.getRight();
                // should it swap with node
                if (!compareValues(trav, chosenChild)) {
                    swap(trav, chosenChild);
                    swapped = true;
                    trav = chosenChild;
                }
            }
            // directly swap with left child
            else if (!compareValues(trav, trav.getLeft())) {
                swap(trav, trav.getLeft());
                swapped = true;
                trav = trav.getLeft();
            }
        } 
    }
    
    /**
     * This method compares two nodes' contents.
     * 
     * @param node2
     *             Comparison based on this node.
     * @param node1
     *             Node that needs to be compared with.
     * @return true if greater.  False otherwise.
     */
    private boolean compareValues(Node<V> node2, Node<V> node1) {
        return node2.getData().compareTo(node1.getData()) > 0;   //ex: return true if node > parent
    }
    
    /**
     * This method returns size of this {@link NodeHeap}.
     * 
     * @Return size.
     * 
     */
    public int getSize(){
        return size;
    }
}

/**
 * A generic Node class that stores data, parent
 * left child, and right child in node.
 * 
 * @author Shun Lu
 */
class Node<T>
{
   /**
    * This field represents {@link Node}'s data.
    */
   private T data;
   
   /**
    * These fields represent {@link Node}'s pointers to
    * parent, left child, right child.
    */
   private Node<T> parent, left, right;

   /**
    * Constructor of {@link Node} that sets all fields from parameter.
    * 
    * @param data data of {@link Node}.
    * @param parent parent of {@link Node}.
    * @param left left child of {@link Node}.
    * @param right right child of {@link Node}.
    */
   public Node(T data, Node<T> parent, Node<T> left, Node<T> right)
   {
      this.data = data;
      this.parent = parent;
      this.left = left;
      this.right = right;
   }

   /**
    * Method that sets data.
    * @param data data.
    */
   public void setData(T data)
   {
      this.data = data;
   }
   
   /**
    * Method that sets parent.
    * @param parent parent.
    */
   public void setParent(Node<T> parent)
   {
      this.parent = parent;
   }

   /**
    * Method that sets left child.
    * @param left left child.
    */
   public void setLeft(Node<T> left)
   {
      this.left = left;
   }
   
   /**
    * Method that sets right child.
    * @param right right child.
    */
   public void setRight(Node<T> right)
   {
      this.right = right;
   }

   /**
    * Method that returns data.
    */
   public T getData()
   {
      return data;
   }

   /**
    * Method that returns parent.
    */
   public Node<T> getParent()
   {
      return parent;
   }
   
   /**
    * Method that returns left.
    */
   public Node<T> getLeft()
   {
      return left;
   }
   
   /**
    * Method that returns right.
    */
   public Node<T> getRight()
   {
      return right;
   }
   
   /**
    * Method that checks if it is leaf.
    */
   public boolean isLeaf(){
       return (left == null) && (right == null);
   }
   
   /**
    * Method that checks if it is root.
    */
   public boolean isRoot(){
       return parent == null;
   }
}