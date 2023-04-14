package arem.handlers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import arem.Algoritmos.AFD.Arbol;
import arem.Algoritmos.AFD.nodo;
import arem.Algoritmos.GAL.LectorDeArchivos;
import arem.Funciones.Lenguaje2;
import arem.Funciones.Postfix;

import arem.Grafo.arbol;

public class GALhandler extends handler {

    public GALhandler() throws IOException {
        String fileNane = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/Archivos Yal/Test.yal";
        LectorDeArchivos archivo = new LectorDeArchivos(fileNane);
        if (archivo.isHasError()) {
            return;
        }
        Map<String, String> expandedActions = archivo.getExpandedActions();
        StringBuilder finalExpression = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = expandedActions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> actionEntry = iterator.next();
            String key = actionEntry.getKey();
            String value = actionEntry.getValue();

            if (value.startsWith("|")) {
                value = value.substring(1);
            }

            finalExpression.append(getExpresion(Optional.of(value), Optional.of(key)));

            if (iterator.hasNext()) {
                finalExpression.append("|");
            } else {
                finalExpression.append("#");
            }
        }
        System.out.println("Expresion final: " + finalExpression.toString());
        finalExpression = new StringBuilder(postfix(finalExpression.toString()));
        Arbol arbolg = new Arbol(finalExpression.toString());
        nodo root = arbolg.getRoot();
        arbol.visualizeTree(root, "example.png");
        // nodo.bfs(root);
        // arem.Grafo.arbol visualizacionArbol = new arem.Grafo.arbol();
        // visualizacionArbol.graficar(root);
    }

    private String postfix(String finalExpression) {
        Postfix postfix = new Postfix();
        finalExpression = postfix.infixToPostfix(finalExpression);
        Lenguaje2.setLenguajeFinal(finalExpression);
        System.out.println("Postfix: " + finalExpression);
        return finalExpression;
    }
}
