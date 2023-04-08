package arem.Algoritmos.AFD;

import java.util.Stack;

import arem.Grafo.arbol;

public class Arbol {
    private String expresion;
    private Stack<nodo> stack;
    private nodo root; 

    public nodo getRoot() {
        return root;
    }

    public Arbol(String expresion) {
        this.expresion = expresion;
        stack = new Stack<>();
        makeTree();
    }

    private void makeTree() {
        for (char c : expresion.toCharArray()) {
            switch (c) {
                case '.':
                    nodo nodoConcat = new nodo(c);
                    nodo nodoFinal = stack.pop();
                    nodo nodoInicial = stack.pop();

                    // vamos a declarar las hojas de nodo
                    nodoConcat.setLeft(nodoInicial);
                    nodoConcat.setRight(nodoFinal);

                    stack.push(nodoConcat);
                    break;

                case '|':
                    nodo nodoOr = new nodo(c);
                    nodo nodoDerecha = stack.pop();
                    nodo nodoIzquierda = stack.pop();

                    // vamos a declarar las hojas de nodo
                    nodoOr.setLeft(nodoIzquierda);
                    nodoOr.setRight(nodoDerecha);

                    stack.push(nodoOr);
                    break;

                case '*':
                    nodo nodoKleene = new nodo(c);
                    nodo nodoExtraido = stack.pop();

                    nodoKleene.setLeft(nodoExtraido);

                    stack.push(nodoKleene);
                    break;

                default:
                    nodo nodo = new nodo(c);
                    stack.push(nodo);
                    break;
            }
        }
        root = stack.pop();
    }

    // public static void main(String[] args) {
    //     String postfix = "ab|*a.b.b.";
    //     Arbol arbol = new Arbol(postfix);
    //     nodo root = arbol.makeTree();
    //     nodo.bfs(root);

    //     arem.Grafo.arbol visualizacionArbol = new arem.Grafo.arbol();
    //     visualizacionArbol.graficar(root);
    // }
}
