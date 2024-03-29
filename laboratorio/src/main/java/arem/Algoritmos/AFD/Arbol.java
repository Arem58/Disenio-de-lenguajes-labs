package arem.Algoritmos.AFD;

import java.util.Stack;

import arem.Funciones.Lenguaje;
import arem.Funciones.Lenguaje2;

public class Arbol {
    private String expresion;
    private Stack<nodo> stack;
    private nodo root;
    private int concatId = 1;
    private int orId = 1;
    private int kleeneId = 1;
    private int characterId = 1;

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
            nodo newNodo;
            switch (c) {
                case '.':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    newNodo = new nodo(simbolo);
                    nodo nodoFinal = stack.pop();
                    nodo nodoInicial = stack.pop();

                    newNodo.setLeft(nodoInicial);
                    newNodo.setRight(nodoFinal);

                    newNodo.setIdentifier("γ" + concatId);
                    concatId++;
                    break;

                case '|':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    newNodo = new nodo(simbolo);
                    nodo nodoDerecha = stack.pop();
                    nodo nodoIzquierda = stack.pop();

                    newNodo.setLeft(nodoIzquierda);
                    newNodo.setRight(nodoDerecha);

                    newNodo.setIdentifier("α" + orId);
                    orId++;
                    break;

                case '*':
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    newNodo = new nodo(simbolo);
                    nodo nodoExtraido = stack.pop();

                    newNodo.setLeft(nodoExtraido);

                    newNodo.setIdentifier("β" + kleeneId);
                    kleeneId++;
                    break;

                default:
                    if (Lenguaje2.viejoSimb.containsKey(simbolo))
                        simbolo = Lenguaje2.viejoSimb.get(simbolo);
                    else if (Lenguaje.nuevoSimb.containsKey(simbolo.charAt(0)))
                        simbolo = Lenguaje.nuevoSimb.get(simbolo.charAt(0)).toString();
                    newNodo = new nodo(simbolo);

                    newNodo.setIdentifier(Integer.toString(characterId));
                    characterId++;
                    break;
            }
            stack.push(newNodo);
        }
        root = stack.pop();
    }
}
