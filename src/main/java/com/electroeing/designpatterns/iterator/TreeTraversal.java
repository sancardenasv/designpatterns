package com.electroeing.designpatterns.iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

class Node<T> {
    public T value;
    public Node<T> left, right, parent;

    public Node(T value) {
        this.value = value;
    }

    public Node(T value, Node<T> left, Node<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;

        left.parent = right.parent = this;
    }
}

class InOrderIterator<T> implements Iterator<T> {
    private Node<T> current, root;
    private boolean yieldedStart;

    public InOrderIterator(Node<T> root) {
        this.root = current = root;

        while (current.left != null) {
            current = current.left;
        }
    }

    private boolean hasRightmostParent(Node<T> node) {
        if (node.parent == null) return false;
        return (node.equals(node.parent.left) || hasRightmostParent(node.parent));
    }

    @Override
    public boolean hasNext() {
        return current.left != null || current.right != null || hasRightmostParent(current);
    }

    @Override
    public T next() {
        if (!yieldedStart) {
            yieldedStart = true;
            return current.value;
        }

        if (current.right != null) {
            current = current.right;
            while (current.left != null)
                current = current.left;
            return current.value;
        } else {
            Node<T> p = current.parent;
            while (p != null && current == p.right) {
                current = p;
                p = p.parent;
            }
            current = p;
            if (current == null) {
                throw new NoSuchElementException();
            }
            return current.value;
        }
    }
}

class BinaryTree<T> implements Iterable<T> {
    private Node<T> root;

    public BinaryTree(Node<T> root) {
        this.root = root;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator<>(root);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for(T item : this) {
            action.accept(item);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }
}

public class TreeTraversal {
    private final static Logger logger = LoggerFactory.getLogger(TreeTraversal.class);

    public static void main(String[] args) {
        //   1
        //  / \
        // 2   3
        Node<Integer> root = new Node<>(1,
                new Node<>(2),
                new Node<>(3));

        InOrderIterator<Integer> it = new InOrderIterator<>(root);

        final StringBuilder sb = new StringBuilder();
        while (it.hasNext())
        {
            sb.append(it.next()).append(", ");
        }
        logger.info("Result - {}", sb);

        BinaryTree<Integer> tree = new BinaryTree<>(root);
        final StringBuilder sb2 = new StringBuilder();
        for (int n : tree) {
            sb2.append(n).append(", ");
        }
        logger.info("Result2 - {}", sb2);

    }
}
