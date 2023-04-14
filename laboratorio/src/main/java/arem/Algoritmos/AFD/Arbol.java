package arem.Algoritmos.AFD;

import java.io.IOException;
import java.util.Stack;

import arem.Funciones.Lenguaje;
import arem.Funciones.Lenguaje2;
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
            String simbolo = Character.toString(c);
            switch (c) {
                case '.':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    nodo nodoConcat = new nodo(simbolo);
                    nodo nodoFinal = stack.pop();
                    nodo nodoInicial = stack.pop();

                    // vamos a declarar las hojas de nodo
                    nodoConcat.setLeft(nodoInicial);
                    nodoConcat.setRight(nodoFinal);

                    stack.push(nodoConcat);
                    break;

                case '|':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    nodo nodoOr = new nodo(simbolo);
                    nodo nodoDerecha = stack.pop();
                    nodo nodoIzquierda = stack.pop();

                    // vamos a declarar las hojas de nodo
                    nodoOr.setLeft(nodoIzquierda);
                    nodoOr.setRight(nodoDerecha);

                    stack.push(nodoOr);
                    break;

                case '*':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    nodo nodoKleene = new nodo(simbolo);
                    nodo nodoExtraido = stack.pop();

                    nodoKleene.setLeft(nodoExtraido);

                    stack.push(nodoKleene);
                    break;

                default:
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    nodo nodo = new nodo(simbolo);
                    stack.push(nodo);
                    break;
            }
        }
        root = stack.pop();
    }

    // public static void main(String[] args) throws IOException {
    //     String postfix = "ab|*a.b.b.";
    //     Arbol arbolC = new Arbol(postfix);
    //     nodo root = arbolC.getRoot();
    //     nodo.bfs(root);

    //     arbol.visualizeTree(root, "example.png");
    // }
}
