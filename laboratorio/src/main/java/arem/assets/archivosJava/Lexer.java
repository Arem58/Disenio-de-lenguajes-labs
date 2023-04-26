package arem.assets.archivosJava;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.fusesource.jansi.Ansi;

import arem.Algoritmos.interfaces.IAFDs;
import arem.Funciones.Lenguaje2;
import arem.Funciones.MapIO;
import arem.Funciones.Postfix;
import arem.Simulacion.AFD.AFDs;
import arem.assets.archivosJava.Expresion.ExpresionData;
import arem.handlers.AFDhandler;
import arem.handlers.handler;

public class Lexer {

    private static List<Character> unsustitutedExpressions;
    private static Map<String, String> expandedActions;
    private static Set<String> returnedTokens;
    private static Set<String> Tokens;

    public static void main(String[] args) {
        String mapFileName = "laboratorio/src/main/java/arem/assets/archivosJava/Expresion/expanded_actions.dat";
        readExpresion(mapFileName);

        if (expandedActions != null && returnedTokens != null && Tokens != null) {
            StringBuilder finalExpression = new StringBuilder();

            finalExpression.append("(");
            Iterator<Map.Entry<String, String>> iterator = expandedActions.entrySet().iterator();
            handler handler = new handler();
            while (iterator.hasNext()) {
                Map.Entry<String, String> actionEntry = iterator.next();
                String key = actionEntry.getKey();
                String value = actionEntry.getValue();

                if (value.startsWith("|")) {
                    value = value.substring(1);
                }

                finalExpression.append(handler.getExpresion(Optional.of(value), Optional.of(key), Optional.of(unsustitutedExpressions)));

                if (iterator.hasNext()) {
                    finalExpression.append("|");
                } else {
                    finalExpression.append(")#");
                    handler.EndofLine = "#";
                }
            }

            System.out.println("Expresion final: " + finalExpression.toString());
            finalExpression = new StringBuilder(postfix(finalExpression.toString()));
            Set<String> acceptingStates = new HashSet<>(Collections.singleton(handler.EndofLine));

            AFDhandler afd = new AFDhandler(acceptingStates, finalExpression.toString());
            AFDs instance = AFDs.createWithStringKey(afd.getGeAFD(), 0, returnedTokens, Tokens);
            IAFDs afdInstance = instance.getAfd();

                printTokensInfo(afdInstance.getTokensReturned());

        } else {
            System.out.println("El mapa no se pudo cargar correctamente.");
        }
    }

    private static void readExpresion(String mapFileName) {
        ExpresionData loadedData = null;

        try {
            loadedData = MapIO.loadDataFromFile(mapFileName);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos desde el archivo: " + e.getMessage());
        }

        if (loadedData != null) {
            expandedActions = loadedData.getExpandedActions();
            returnedTokens = loadedData.getReturnedTokens();
            Tokens = loadedData.getTokens();
            unsustitutedExpressions = loadedData.getValidExpressions();
        }
    }

    private static String postfix(String finalExpression) {
        Postfix postfix = new Postfix();
        finalExpression = postfix.infixToPostfix(finalExpression);
        Lenguaje2.setLenguajeFinal(finalExpression);
        System.out.println("Postfix: " + finalExpression);
        return finalExpression;
    }

    public static void printTokensInfo(Map<Integer, Map<String, String>> tokensReturned) {
        StringBuilder tokensInfo = new StringBuilder();

        for (Map.Entry<Integer, Map<String, String>> entry : tokensReturned.entrySet()) {
            Map<String, String> innerMap = entry.getValue();
            for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
                String token = innerEntry.getKey();
                String value = innerEntry.getValue();
                if ("Error".equals(token)) {
                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format("----- ----- ----- ----- ----- ----- ----- -----%n")).reset());
                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format("Token: %s%nValue: %s%n", token, value)).reset());
                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format("----- ----- ----- ----- ----- ----- ----- -----%n%n")).reset());
                } else { 
                     tokensInfo.append(String.format("----- ----- ----- ----- ----- ----- ----- -----%n"));
                     tokensInfo.append(String.format("Token: %s%nValue: %s%n", token, value));
                     tokensInfo.append(String.format("----- ----- ----- ----- ----- ----- ----- -----%n%n"));
                }
            }
        }

        System.out.println(tokensInfo.toString());
    }
}
