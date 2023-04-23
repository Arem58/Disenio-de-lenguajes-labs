package arem.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import arem.Algoritmos.GAL.LectorDeArchivos;
import arem.Funciones.Lenguaje2;
import arem.Funciones.Postfix;
import arem.Simulacion.AFD.AFDs;

public class GALhandler extends handler {

    private static GALhandler instancia;
    private static final String Collection = null;
    private Set<String> returnedTokens;

    public GALhandler() {
        String fileNane = "laboratorio/src/main/java/arem/assets/Archivos Yal/slr-1.yal";
        LectorDeArchivos archivo = new LectorDeArchivos(fileNane);
        if (archivo.isHasError()) {
            return;
        }
        Map<String, String> expandedActions = archivo.getExpandedActions();
        this.returnedTokens = archivo.getReturnedTokens();
        StringBuilder finalExpression = new StringBuilder();
        finalExpression.append("(");
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
                finalExpression.append(")#");
                EndofLine = "#";
            }
        }
        System.out.println("Expresion final: " + finalExpression.toString());
        finalExpression = new StringBuilder(postfix(finalExpression.toString()));
        Set<String> acceptingStates = new HashSet<>(Collections.singleton(EndofLine));
        System.out.println(Lenguaje2.lenguajeInicial);
        AFDhandler afd = new AFDhandler(acceptingStates, finalExpression.toString());
        AFDs.createWithStringKey(afd.getGeAFD(), 0);
        // nodo.bfs(root);
    }

    private String postfix(String finalExpression) {
        Postfix postfix = new Postfix();
        finalExpression = postfix.infixToPostfix(finalExpression);
        Lenguaje2.setLenguajeFinal(finalExpression);
        System.out.println("Postfix: " + finalExpression);
        return finalExpression;
    }

    public static GALhandler obtenerInstancia() {
        if (instancia == null) {
            instancia = new GALhandler();
        }
        return instancia;
    }

    public Set<String> getTokensDevueltos() {
        return returnedTokens;
    }
}
