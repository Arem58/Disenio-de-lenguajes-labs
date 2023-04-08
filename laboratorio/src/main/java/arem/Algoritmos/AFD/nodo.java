package arem.Algoritmos.AFD;

import java.util.LinkedList;
import java.util.Queue;

public class nodo {
    private char value;
    private nodo left;
    private nodo right;
    
    public nodo(char value) {
        this.value = value;
    }
    
    public void setLeft(nodo left) {
        this.left = left;
    }
    
    public void setRight(nodo right) {
        this.right = right;
    }
    
    public char getValue() {
        return value;
    }
    
    public nodo getLeft() {
        return left;
    }
    
    public nodo getRight() {
        return right;
    }
    
    public static void bfs(nodo root) {
        if (root == null) {
            return;
        }

        Queue<nodo> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                nodo node = queue.poll();
                System.out.print(node.getValue() + " ");

                if (node.getLeft() != null) {
                    queue.offer(node.getLeft());
                }

                if (node.getRight() != null) {
                    queue.offer(node.getRight());
                }
            }

            System.out.println();
        }
    }
}