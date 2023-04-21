package arem.Algoritmos.AFD;

import java.util.LinkedList;
import java.util.Queue;

public class nodo {
    private static int idCounter = 0;
    
    private int id;
    private String identifier;
    private String value;
    
    private nodo left;
    private nodo right;
    
    public nodo(String value) {
        this.id = idCounter++;
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    public String getOperatorType() {
        char firstChar = identifier.charAt(0);
        if (firstChar == 'α') {
            return "|";
        } else if (firstChar == 'β') {
            return "*";
        } else if (firstChar == 'γ') {
            return ".";
        } else if (value.equals("ε")){
            return "ε";
        }else {
            return "";
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public int getId() {
        return id;
    }
    
    public void setLeft(nodo left) {
        this.left = left;
    }
    
    public void setRight(nodo right) {
        this.right = right;
    }
    
    public String getValue() {
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