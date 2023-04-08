package arem.handlers;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import arem.Algoritmos.AFD.Arbol;
import arem.Algoritmos.AFD.nodo;
import arem.Algoritmos.GAL.LectorDeArchivos;

public class GALhandler extends handler {

    public GALhandler() {
        String fileNane = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/Archivos Yal/slr-1.yal";
        LectorDeArchivos archivo = new LectorDeArchivos(fileNane);
        if (archivo.isHasError()) {
            return;
        }
        Map<String, String> expandedActions = archivo.getExpandedActions();
        StringBuilder finalExpression = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = expandedActions.entrySet().iterator();
        for (Map.Entry<String, String> actionEntry : expandedActions.entrySet()) {
            String key = actionEntry.getKey();
            String value = actionEntry.getValue();

            if (value.startsWith("|")) {
                value = value.substring(1);
            }

            finalExpression.append(getExpresion(Optional.of(value), Optional.of(key)));
            if (iterator.hasNext()) {
                finalExpression.append("|");
            }
        }
        Arbol arbol = new Arbol(finalExpression.toString());
        nodo root = arbol.getRoot();
        arem.Grafo.arbol visualizacionArbol = new arem.Grafo.arbol();
        visualizacionArbol.graficar(root);
    }
}
