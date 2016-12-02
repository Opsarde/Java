/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodriguez
 *
 * Programming Assignment #4
 *
 * <An assignment that implements a basic BRTree data structure
 *  including toPrettyString.>
 *
 * Shun Lu
 * 
 */

package edu.cpp.cs.cs241.prog_assgmnt_4;

import java.util.LinkedList;
import java.util.Queue;


/**
 * enum structure for Color.
 */
enum Color {
    RED, BLACK
};

/**
 * RedBlackTree implements Tree<K, V>
 * has add, remove, lookup, toPrettyString
 * Self-balancing with five properties of an RBTree.
 * 
 * @author Shun Lu
 */
public class RBTree<K extends Comparable<K>, V> implements Tree<K, V> {
    /**
     * This field represents {@link RBTree}'s root.
     */
    private Node<K, V> root;
    /**
     * This field represents {@link RBTree}'s size.
     */
    private int size;
    /**
     * Constructor to set everything to null or 0.
     */
    public RBTree() {
        root = null;
        size = 0;
    }

    /**
     * Add a node to RBTree.
     * 
     * @param key key to be stored.
     * @param value value with this key.
     */
    public void add(K key, V value) {
        //PREPARING NEW NIL-RED-NIL
        Node<K, V> leftNil = new Node<>(null, null, null, null, null, Color.BLACK);       //Black
        Node<K, V> rightNil = new Node<>(null, null, null, null, null, Color.BLACK);      //Guards
        Node<K, V> newNode = new Node<>(key, value, null, leftNil, rightNil, Color.RED);
        leftNil.parent = newNode;
        rightNil.parent = newNode;
        Node<K, V> cur = root;
        
        if (root == null) {                               //case 1.0
            root = newNode;
            root.color = Color.BLACK;  //re-color
            ++size;
            return;
        }
        else if (size > 0) {                              //case 2.0
            //GET TO LAST NODE IN THE TREE
            while (!cur.isNil()) {         
                if (cur.key.compareTo(key) < 0) {
                    cur = cur.right;
                }
                else {
                    cur = cur.left;
                }
            }                              
            //ADD NEW NODE, REPLACE THAT NIL
            Node<K, V> parent = cur.parent;              //case 2.1
            if (cur == parent.left) {
                cur = newNode;
                parent.left = cur;
                cur.parent = parent;
            }
            else {
                cur = newNode;
                parent.right = cur;
                cur.parent = parent;
            }
            ++size;
            //CHECK RED OR NOT HERE
            if (cur.parent.color == Color.RED) {       //case 2.2
                //FIX
                addFix(cur);
            }
        }
    }

    /**
     * Remove a node from RBTree.
     * Will automatically balance this tree.
     * 
     * @param key key to remove.
     * @return founded key's value.
     */
    public V remove(K key) {
        //Method will use predecessor for removing
        V result = null;
        if (size == 0) {                                          //case 1.0
            return null;
        }
        else if (size == 1) {                                     //case 1.1
            if (key.equals(root.key)) {
                result = root.value;
                root = null;
                --size;
                return result;
            }
        }
        else {                                                    //case 2.0
            Node<K, V> cur = root;
            Node<K, V> parentCur;          //link to bypass
            Node<K, V> lastCurPosition;    //track last removed node
            Node<K, V> nilReplacer = new Node<>(null, null, null, null, null, Color.BLACK);
            //need this to determine fix
            boolean requireFix = false;
            boolean found = false;
            
            //search if found
            while (!found && !cur.isNil()) {
                if (cur.key.compareTo(key) < 0) {
                    cur = cur.right;
                }
                else if (cur.key.compareTo(key) > 0) {
                    cur = cur.left;
                }
                else
                    found = true;
            }
            //this will return null when it gets to nil
            if (!cur.isNil()) {
                result = cur.value;
                //inside start induced case
                parentCur = cur.parent;
                if (cur.isLeaf()) {                              //case 2.1
                    if (cur.color == Color.BLACK)
                        requireFix = true;
                    //replace that node with Nil
                    if (parentCur.left == cur) { //if it is left leaf
                        parentCur.left = nilReplacer;
                        nilReplacer.parent = parentCur;
                    }
                    else {
                        parentCur.right = nilReplacer;
                        nilReplacer.parent = parentCur;
                    }
                    //set cur to replaced node
                    //possible case: it has no left and right pointer
                    cur = nilReplacer;
                }
                else if (cur.hasOneChild()) {                    //case 2.2
                    if (cur.color == Color.BLACK &&
                        ((!cur.left.isNil() && cur.left.color == Color.BLACK) ||
                         (!cur.right.isNil() && cur.right.color == Color.BLACK)))
                        requireFix = true;
                    //checking is cur == left or right
                    if (cur.isLeft() && !cur.isRoot()) {
                        //checking which cur.child to link
                        if (!cur.left.isNil()) {
                            parentCur.left = cur.left;
                            cur.left.parent = parentCur;
                            cur = cur.left;
                        }
                        else {
                            parentCur.left = cur.right;
                            cur.right.parent = parentCur;
                            cur = cur.right;
                        }
                    }
                    else if (cur.isRight() && !cur.isRoot()) {
                        if (!cur.right.isNil()) {
                            parentCur.right = cur.right;
                            cur.right.parent = parentCur;
                            cur = cur.right;
                        }
                        else {
                            parentCur.right = cur.left;
                            cur.left.parent = parentCur;
                            cur = cur.left;
                        }
                    }
                    else {
                        root = !cur.left.isNil() ? cur.left : cur.right;
                        root.parent = null;
                        root.color = Color.BLACK;
                        cur = root;
                    }
                    //color replaced node to black
                    cur.color = Color.BLACK;
                }
                else {                                         //case 2.3
                    //find its predecessor
                    lastCurPosition = getRightMost(cur.left);
                    if (lastCurPosition.color == Color.BLACK &&
                        lastCurPosition.left.color == Color.BLACK)
                        requireFix = true;
                    //NOTE: lastCurPosition always have: right == nil
                    //      left will be either nil or something.
                    //      don't need to do anything to right.
                    parentCur = lastCurPosition.parent;
                    cur.key = lastCurPosition.key;
                    cur.value = lastCurPosition.value;
                    if (lastCurPosition.isLeft()) {
                        parentCur.left = lastCurPosition.left;
                        cur = parentCur.left;
                    }
                    else {
                        parentCur.right = lastCurPosition.left;
                        cur = parentCur.right;
                    }
                    cur.parent = parentCur;
                    cur.color = Color.BLACK;
                }
                --size;
            }
            if (requireFix)
                removeFix(cur);
        }
        return result;
    }

    /**
     * Search a given key.
     * 
     * @param key this key to search.
     * @return key's value if found.  Return null if not found.
     */
    public V lookup(K key) {
        Node <K, V> cur = root;
        while (!cur.isNil()) {
            if (cur.key.compareTo(key) < 0) {
                cur = cur.right;
            }
            else if (cur.key.compareTo(key) > 0) {
                cur = cur.left;
            }
            else
                return cur.value;
        }
        return null;
    }

    /**
     * Print RBTree out in a pyramid fashion.
     * Using BFS to print.
     * 
     * @return string form of this RBTree.
     */
    public String toPrettyString() {
        if (root == null)
            return null;
        String str = "";
        int spaceCounter = 32;
        Queue<Node<K, V>> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int currentLevel = q.size();
            for (int i = spaceCounter; i > 0; --i) {
                str += " ";
            }
            while (currentLevel > 0) {
                Node<K, V> current = q.remove();

                str += current.value + "," + current.color;
                if (!current.left.isNil() && current.left != null) {
                    q.add(current.left);
                }
                if (!current.right.isNil() && current.right != null) {
                    q.add(current.right);
                }
                --currentLevel;
                for (int i = spaceCounter * 2 - 8; i > 0; --i) {
                    str += " ";
                }
            }
            str += "\n";
            spaceCounter /= 2;
        }
        return str;
    }
    
    /**
     * Get right most node.
     * 
     * @param cur Current node to calculate.
     * @return this rightmost node.
     */
    private Node<K, V> getRightMost(Node<K, V> cur) {
        while (!cur.right.isNil()) {
            cur = cur.right;
        }
        return cur;
    }
    
    /**
     * Get sibling node.
     * 
     * @param cur current node.
     * @return sibling node.
     */
    private Node<K, V> getSibling(Node<K, V> cur) {
        if (cur.parent != null)
            return cur == cur.parent.left ? cur.parent.right : cur.parent.left;
        return null;
    }
    
    /**
     * Perform a left rotation.
     * 
     * @param cur current pivot node.
     */
    private void leftRotate(Node<K, V> cur) {
        Node<K, V> a = cur;
        Node<K, V> b = cur.right;
        a.right = b.left;
        b.left.parent = a;
        b.left = a;
        b.parent = a.parent;
        a.parent = b;
        if (b.parent != null) {
            if (b.parent.left == a)
                b.parent.left = b;
            else
                b.parent.right = b;
        }
    }

    /**
     * Perform a right rotation.
     * 
     * @param cur current pivot node.
     */
    private void rightRotate(Node<K, V> cur) {
        Node<K, V> b = cur;
        Node<K, V> a = cur.left;
        b.left = a.right;
        a.right.parent = b;
        a.right = b;
        a.parent = b.parent;
        b.parent = a;
        if (a.parent != null) {
            if (a.parent.left == b)
                a.parent.left = a;
            else if (a.parent.right == b)
                a.parent.right = a;
        }
    }

    /**
     * Perform fixation for this RBTree that satisfies five properties.
     * Case1: parent and uncle are red.
     * Case2: parent and uncle have different color and cur is internal.
     * Case3: parent and uncle have differenc color and cur is external.
     * 
     * @param cur current node pointer.
     */
    private void addFix(Node<K, V> cur) {
        //At this point, must have grandparent;
        if (cur == root) {
            cur.color = Color.BLACK;
            return;
        }
        Node<K, V> grandparent = cur.parent.parent;   //pointer of cur.parent.parent
        Node<K, V> parent = cur.parent;               //pointer of cur.parent
        if (grandparent.left.color == Color.RED &&    //case 2.2.1
            grandparent.right.color == Color.RED) {
            cur = grandparent;
            cur.left.color = Color.BLACK;
            cur.right.color = Color.BLACK;
            cur.color = Color.RED;
            if (cur.parent == root)
                cur = root;
        }
        else if ((grandparent.left.color == Color.BLACK && grandparent.right.color == Color.RED) ||    //case 2.2.2
                 (grandparent.right.color == Color.BLACK && grandparent.left.color == Color.RED)) { 
            if (grandparent.left.right == cur || grandparent.right.left == cur) {
                Node<K, V> refNode = cur.parent;
                if (cur.parent.left == cur) {
                    rightRotate(cur.parent);
                }
                else {
                    leftRotate(cur.parent);
                }
                    cur = refNode;
                    grandparent = cur.parent.parent;
                    parent = cur.parent;
            }
            if (grandparent.left.left == cur) {                      //case 2.2.3
                rightRotate(grandparent);
            }
            else if (grandparent.right.right == cur) {
                leftRotate(grandparent);
            }
            parent.color = Color.BLACK;
            // grandparent is a pointer
            grandparent.color = Color.RED;
            if (grandparent.parent.isRoot())
                root = grandparent.parent;
            cur = root;
        }   
        addFix(cur);
    }
    
    
    /**
     * Perform remove fixation for this RBTree that satisfies five properties.
     * Case1: sibling is red.
     * Case2: sibling is black, sibling's two children are black.
     * Case3: sibling is black, sibling's internal child is red.
     * Case4: sibling is black, sibling's external child is red.
     * 
     * @param cur current node pointer.
     */
    private void removeFix(Node<K, V> cur) {

        if (cur == root) {                           //case 3.0
            cur.color = Color.BLACK;
            return;
        }
        else {
            Node<K, V> sibling = getSibling(cur);
            if (sibling.color == Color.RED) {       //case 3.1
                cur.parent.color = Color.RED;
                sibling.color = Color.BLACK;
                if (cur.parent == root) {
                    root = sibling;
                }
                if(cur.parent.left == cur){
                    leftRotate(cur.parent);
                } else {
                    rightRotate(cur.parent);
                }
            }
            else if (sibling.color == Color.BLACK) {        //case 3.2
                if (sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK) { //case 3.2.1
                    sibling.color = Color.RED;
                    if (cur.parent.color == Color.BLACK) {
                        cur = cur.parent;
                    }
                    else {
                        cur.parent.color = Color.BLACK;
                        cur = root;
                    }
                }
                else if ((cur.parent.left == cur && sibling.left.color == Color.RED) ||       //case 3.2.2
                         (cur.parent.right == cur && sibling.right.color == Color.RED)) {
                    sibling.color = Color.RED;
                    if (sibling.parent.right == sibling) {
                        sibling.left.color = Color.BLACK;
                        rightRotate(sibling);
                    }
                    else {
                        sibling.right.color = Color.BLACK;
                        leftRotate(sibling);
                    }
                }
                else {                                                     //case 3.2.3
                    sibling.color = sibling.parent.color;
                    sibling.parent.color = Color.BLACK;
                    if (cur.parent == root) {
                        root = sibling;
                    }
                    if (sibling.parent.right == sibling) {
                        sibling.right.color = Color.BLACK;
                        leftRotate(cur.parent);
                    }
                    else {
                        sibling.left.color = Color.BLACK;
                        rightRotate(cur.parent);
                    }
                    cur = root;
                }
            }
        }
        removeFix(cur);
    }
}


class Node<K, V> {

    protected K key;
    protected V value;
    protected Node<K, V> parent;
    protected Node<K, V> left;
    protected Node<K, V> right;
    protected Color color;

    // maybe not needed, just in case
    public Node(K key, V value, Node<K, V> parent, Node<K, V> left,
                Node<K, V> right, Color color) {
        this.key = key;
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.color = color;
    }

    public boolean isRoot() {
        return parent == null && color == Color.BLACK;
    }

    public boolean isLeaf() {
        return left.isNil() && right.isNil();
    }
    
    public boolean isNil() {
        return key == null && value == null && color == Color.BLACK;
    }
    
    public boolean hasOneChild() {
        return (left.isNil() && !right.isNil()) ||
               (!right.isNil() && left.isNil());
    }
    
    public boolean isLeft() {
        //can also use compareTo to check
        return !isRoot() && parent.left.key == key;
    }
    
    public boolean isRight() {
        return !isRoot() && parent.right.key == key;
    }
}
