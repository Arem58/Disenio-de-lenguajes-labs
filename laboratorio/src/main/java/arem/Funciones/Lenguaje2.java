package arem.Funciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lenguaje2 {
    public static List<String> lenguajeInicial = new ArrayList<String>();
    public static Map<String, String> nuevoSimb = new HashMap<>(); // Para tokens a reemplazar fuera de las comillas
    public static Map<String, String> simbolosDentroComillas = new HashMap<>(); // Para tokens a reemplazar dentro de
                                                                                // las comillas
    public static Map<String, String> viejoSimb = new HashMap<>(); // Para revertir los tokens reemplazados
    public static String EndofLine;

    private static List<Character> expresionLen = new ArrayList<>();

    public static List<Character> operadoresIniciales = Arrays.asList('|', '?', '*', '+', '^', '-');
    private static List<Character> parentesis = Arrays.asList('(', ')');

    public static boolean operadores;

    private static void setExpresionLen(String expresionLen) {
        for (int i = 0; i < expresionLen.length(); i++) {
            char c = expresionLen.charAt(i);
            Lenguaje2.expresionLen.add(c);
        }
    }

    public static String replaceSpacesWithSlashS(String expresion) {
        StringBuilder modifiedExpresion = new StringBuilder();
        Pattern spacePattern = Pattern.compile("\\'(.*?)\\'");

        int startIndex = 0;
        Matcher spaceMatcher = spacePattern.matcher(expresion);
        while (spaceMatcher.find(startIndex)) {
            modifiedExpresion.append(expresion, startIndex, spaceMatcher.start());
            String content = spaceMatcher.group(1);

            // Solo reemplazar si el contenido es un espacio vacío
            if (content.equals(" ")) {
                content = content.replace(" ", "\\s");
            }

            modifiedExpresion.append("'").append(content).append("'");
            startIndex = spaceMatcher.end();
        }
        modifiedExpresion.append(expresion.substring(startIndex));

        return modifiedExpresion.toString();
    }

    private static void replaceOperatorWithRandomCharacter(String s, Map<String, String> nuevoSimb,
            Map<String, String> viejoSimb) {
        Random rand = new Random();
        int numAleatorio = rand.nextInt(4351, 5792);
        char[] chars = Character.toChars(numAleatorio);
        while (Lenguaje2.expresionLen.contains(chars[0])) {
            numAleatorio = rand.nextInt(4351, 5792);
            chars = Character.toChars(numAleatorio);
        }
        Lenguaje2.operadores = true;
        nuevoSimb.put(s, String.valueOf(chars[0]));
        viejoSimb.put(String.valueOf(chars[0]), s);
    }

    public static void setLenguajeInicial(String expresion, String token) {
        Lenguaje2.operadores = false;
        Lenguaje2.setExpresionLen(expresion);
        Pattern quotePattern = Pattern.compile("(\\'(.*?)\\')|(\\\"(.*?)\\\")");
        boolean insideQuotes = false;

        int startIndex = 0;
        Matcher quoteMatcher = quotePattern.matcher(expresion);
        while (quoteMatcher.find(startIndex)) {
            String operator = quoteMatcher.group(2) != null ? quoteMatcher.group(2) : quoteMatcher.group(4);

            for (int i = 0; i < operator.length(); i++) {
                String currentChar = String.valueOf(operator.charAt(i));

                if (currentChar.equals("\\")) {
                    if (i + 1 < operator.length()) {
                        i++;
                        currentChar = currentChar + operator.charAt(i);
                    }
                }

                if (currentChar.equals("\\s") || currentChar.equals("\\t") || currentChar.equals("\\n")
                        || operadoresIniciales.contains(currentChar.charAt(0))
                        || parentesis.contains(currentChar.charAt(0)) && (!lenguajeInicial.contains(currentChar))) {
                    replaceOperatorWithRandomCharacter(currentChar, simbolosDentroComillas, viejoSimb);
                    lenguajeInicial.add(currentChar);
                }
            }

            startIndex = quoteMatcher.end();
        }

        if (!lenguajeInicial.contains(token)) {
            replaceOperatorWithRandomCharacter(token, nuevoSimb, viejoSimb);
            lenguajeInicial.add(token);
        }

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            String s = String.valueOf(c);

            if (c == '\'' || c == '\"') {
                insideQuotes = !insideQuotes;
                continue;
            }

            if (!insideQuotes && !operadoresIniciales.contains(c) && !parentesis.contains(c)
                    && !lenguajeInicial.contains(s)) {

                StringBuilder sequence = new StringBuilder(s);
                for (int j = i + 1; j < expresion.length(); j++) {
                    char nextChar = expresion.charAt(j);
                    if (operadoresIniciales.contains(nextChar) || parentesis.contains(nextChar)) {
                        break;
                    }
                    sequence.append(nextChar);
                    i = j;
                }

                if (sequence.toString().equals(token)) {
                    continue;
                }

                if (Postfix.precedence.containsKey(c)) {
                    replaceOperatorWithRandomCharacter(s, nuevoSimb, viejoSimb);
                }
                lenguajeInicial.add(s);
            }
        }
    }

    public static void setLenguajeFinal(String expresion) {
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            String s = String.valueOf(c);
            if (!Postfix.precedence.containsKey(c) && !lenguajeInicial.contains(s)) {
                int indice = Lenguaje2.lenguajeInicial.indexOf(Lenguaje2.nuevoSimb.get(s));
                if (indice != -1) {
                    Lenguaje2.lenguajeInicial.set(indice, s);
                } else {
                    Lenguaje2.lenguajeInicial.add(s);
                }
            }
        }
    }

    public static String ConvertirCar(String expresion) {
        String replacedTokens = replaceTokens(expresion, simbolosDentroComillas);
        return replaceAllTokens(replacedTokens, nuevoSimb);
    }

    private static String replaceTokens(String expresion, Map<String, String> tokenMap) {
        StringBuilder newEx = new StringBuilder();
        int index = 0;
        int quoteCount = 0;

        while (index < expresion.length()) {
            char currentChar = expresion.charAt(index);

            if (currentChar == '\'' || currentChar == '\"') {
                quoteCount++;
            }

            boolean insideQuotes = quoteCount % 2 != 0;
            boolean tokenReplaced = false;

            if (insideQuotes) {
                for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (expresion.startsWith(key, index)) {
                        newEx.append(value);
                        index += key.length();
                        tokenReplaced = true;
                        break;
                    }
                }
            }

            if (!tokenReplaced) {
                newEx.append(currentChar);
                index++;
            }
        }

        return newEx.toString();
    }

    private static String replaceAllTokens(String expresion, Map<String, String> tokenMap) {
        StringBuilder newEx = new StringBuilder();
        int index = 0;

        while (index < expresion.length()) {
            char currentChar = expresion.charAt(index);

            boolean tokenReplaced = false;
            for (Map.Entry<String, String> entry : Lenguaje2.nuevoSimb.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (expresion.startsWith(key, index)) {
                    newEx.append(value);
                    index += key.length();
                    tokenReplaced = true;
                    break;
                }
            }

            if (!tokenReplaced) {
                newEx.append(currentChar);
                index++;
            }
        }

        return newEx.toString();
    }

    public static String RevertirCar(String expresion) {
        return replaceAllTokens(expresion, viejoSimb);
    }
}
