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
    public static Map<String, String> nuevoSimb = new HashMap<>();
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

    private static void replaceOperatorWithRandomCharacter(String s) {
        Random rand = new Random();
        int numAleatorio = rand.nextInt(4351, 5792);
        char[] chars = Character.toChars(numAleatorio);
        while (Lenguaje2.expresionLen.contains(chars[0])) {
            numAleatorio = rand.nextInt(4351, 5792);
            chars = Character.toChars(numAleatorio);
        }
        Lenguaje2.operadores = true;
        Lenguaje2.nuevoSimb.put(s, String.valueOf(chars[0]));
        Lenguaje2.nuevoSimb.put(String.valueOf(chars[0]), s);
    }

    public static void setLenguajeInicial(String expresion, String token) {
        Lenguaje2.operadores = false;
        Lenguaje2.setExpresionLen(expresion);
        Pattern operatorPattern = Pattern.compile("\\'(.*?)\\'");

        int startIndex = 0;
        Matcher operatorMatcher = operatorPattern.matcher(expresion);
        while (operatorMatcher.find(startIndex)) {
            String operator = operatorMatcher.group(1);

            if (operator.equals("\\s") || operator.equals("\\t") || operator.equals("\\n")
                    || operadoresIniciales.contains(operator.charAt(0))
                    || parentesis.contains(operator.charAt(0))) {
                replaceOperatorWithRandomCharacter(operator);
                if (!lenguajeInicial.contains(operator)) {
                    lenguajeInicial.add(operator);
                }
            }

            startIndex = operatorMatcher.end();
        }

        replaceOperatorWithRandomCharacter(token);
        if (!lenguajeInicial.contains(token)) {
            lenguajeInicial.add(token);
        }

        boolean insideQuotes = false;
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            String s = String.valueOf(c);

            if (c == '\'') {
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
                    replaceOperatorWithRandomCharacter(s);
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
        return replaceTokens(expresion);
    }
    
    private static String replaceTokens(String expresion) {
        StringBuilder newEx = new StringBuilder();
        int index = 0;
        boolean insideQuotes = false;
    
        while (index < expresion.length()) {
            char currentChar = expresion.charAt(index);
    
            if (currentChar == '\'') {
                insideQuotes = !insideQuotes;
                newEx.append(currentChar);
                index++;
                continue;
            }
    
            String currentToken = "";
            for (Map.Entry<String, String> entry : Lenguaje2.nuevoSimb.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
    
                if (expresion.startsWith(key, index)) {
                    currentToken = key;
                    newEx.append(value);
                    index += key.length();
                    break;
                }
            }
    
            if (currentToken.equals("")) {
                newEx.append(currentChar);
                index++;
            } else {
                if (!lenguajeInicial.contains(currentToken)) {
                    lenguajeInicial.add(currentToken);
                }
            }
        }
    
        return newEx.toString();
    }

    public static String RevertirCar(String expresion) {
        String originalEx = expresion;
        for (Map.Entry<String, String> entry : Lenguaje2.nuevoSimb.entrySet()) {
            originalEx = originalEx.replace(entry.getValue(), entry.getKey());
        }
        return originalEx;
    }
}
