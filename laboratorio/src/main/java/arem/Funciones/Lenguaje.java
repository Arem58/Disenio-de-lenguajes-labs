package arem.Funciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lenguaje {
    public static List<Character> lenguajeInicial = new ArrayList<Character>();
    public static Map<Character, Character> nuevoSimb = new HashMap<>();
    public static String EndofLine;

    private static List<Character> expresionLen = new ArrayList<>();

    public static List<Character> operadoresIniciales = Arrays.asList('|', '?', '*', '+', '^');
    private static List<Character> parentesis = Arrays.asList('(', ')');

    public static boolean operadores;

    private static void setExpresionLen(String expresionLen) {
        for (int i = 0; i < expresionLen.length(); i++) {
            char c = expresionLen.charAt(i);
            Lenguaje.expresionLen.add(c);
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

    private static void replaceOperatorWithRandomCharacter(char c) {
        Random rand = new Random();
        int numAleatorio = rand.nextInt(4351, 5792);
        char[] chars = Character.toChars(numAleatorio);
        while (Lenguaje.expresionLen.contains(chars[0])) {
            numAleatorio = rand.nextInt(4351, 5792);
            chars = Character.toChars(numAleatorio);
        }
        Lenguaje.operadores = true;
        Lenguaje.nuevoSimb.put(c, chars[0]);
        Lenguaje.nuevoSimb.put(chars[0], c);
    }

    public static void setLenguajeInicial(String expresion) {
        Lenguaje.operadores = false;
        Lenguaje.setExpresionLen(expresion);
        Pattern operatorPattern = Pattern.compile("\\'(.*?)\\'");

        int startIndex = 0;
        Matcher operatorMatcher = operatorPattern.matcher(expresion);
        while (operatorMatcher.find(startIndex)) {
            String operator = operatorMatcher.group(1);
            for (char op : operator.toCharArray()) {
                if (op == '\t' || op == '\s' || op == '\n' || operadoresIniciales.contains(op)
                        || parentesis.contains(op)) {
                    replaceOperatorWithRandomCharacter(op);
                }
            }
            startIndex = operatorMatcher.end();
        }

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (!operadoresIniciales.contains(c) && !parentesis.contains(c) && !lenguajeInicial.contains(c)) {
                if (Postfix.precedence.containsKey(c)) {
                    replaceOperatorWithRandomCharacter(c);
                }
                Lenguaje.lenguajeInicial.add(c);
            }
        }
    }

    public static void setLenguajeFinal(String expresion) {
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (!Postfix.precedence.containsKey(c) && !lenguajeInicial.contains(c)) {
                int indice = Lenguaje.lenguajeInicial.indexOf(Lenguaje.nuevoSimb.get(c));
                if (indice != -1) {
                    Lenguaje.lenguajeInicial.set(indice, c);
                } else {
                    Lenguaje.lenguajeInicial.add(c);
                }
            }
        }
    }

    public static String ConvertirCar(String expresion) {

        String newEx = "";
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (Lenguaje.nuevoSimb.containsKey(c))
                c = Lenguaje.nuevoSimb.get(c);
            newEx += c;
        }

        return newEx;
    }
}
