package aoc.aoc.util;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {

    private Node<T> root;

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
        }
    }

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private Node<T> insertRec(Node<T> root, T value) {
        if (root == null) {
            root = new Node<>(value);
            return root;
        }

        if (value.compareTo(root.value) < 0) {
            root.left = insertRec(root.left, value);
        } else if (value.compareTo(root.value) >= 0) {
            root.right = insertRec(root.right, value);
        }

        return root;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        inOrderTraversal(root, list);

        return list;
    }

    private void inOrderTraversal(Node<T> root, List<T> list) {
        if (root != null) {
            inOrderTraversal(root.left, list);
            list.add(root.value);
            inOrderTraversal(root.right, list);
        }
    }
}
