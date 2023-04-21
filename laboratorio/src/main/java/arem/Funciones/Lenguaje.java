package arem.Funciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import arem.Algoritmos.interfaces.ILenguaje;
import arem.handlers.handler;

public class Lenguaje implements ILenguaje {
    public static List<Character> lenguajeInicial = new ArrayList<Character>();
    public static Map<Character, Character> nuevoSimb = new HashMap<>();

    private static List<Character> expresionLen = new ArrayList<>();

    public static List<Character> operadoresIniciales = Arrays.asList('|', '?', '*', '+', '^');
    private static List<Character> parentesis = Arrays.asList('(', ')');
    private static List<Character> caracteresIgnorados = Arrays.asList();

    public static boolean operadores;

    @Override
    public List<Character> getLenguajeInicial() {
        return lenguajeInicial;
    }

    private static void setExpresionLen(String expresionLen) {
        for (int i = 0; i < expresionLen.length(); i++) {
            char c = expresionLen.charAt(i);
            Lenguaje.expresionLen.add(c);
        }
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

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (!operadoresIniciales.contains(c) && !parentesis.contains(c) && !lenguajeInicial.contains(c)) {
                if (handler.EndofLine.isEmpty() || c != handler.EndofLine.charAt(0)) {
                    if (Postfix.precedence.containsKey(c) || caracteresIgnorados.contains(c)) {
                        replaceOperatorWithRandomCharacter(c);
                    }
                    Lenguaje.lenguajeInicial.add(c);
                }
            }
        }
    }

    public static void setLenguajeFinal(String expresion) {
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (!Postfix.precedence.containsKey(c) && !lenguajeInicial.contains(c)) {
                if (handler.EndofLine.isEmpty() || c != handler.EndofLine.charAt(0)){
                    int indice = Lenguaje.lenguajeInicial.indexOf(Lenguaje.nuevoSimb.get(c));
                    if (indice != -1) {
                        Lenguaje.lenguajeInicial.set(indice, c);
                    } else {
                        Lenguaje.lenguajeInicial.add(c);
                    }
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
