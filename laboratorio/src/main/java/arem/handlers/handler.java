package arem.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import arem.Algoritmos.GAL.Ejemplo3;
import arem.Funciones.Expansion;
import arem.Funciones.Lenguaje;
import arem.Funciones.Lenguaje2;
import arem.Funciones.Postfix;
import arem.Funciones.RevisionEx;

public class handler {
    private final Scanner container = new Scanner(System.in);
    public static String EndofLine = "";

    protected String getInput() {
        System.out.println("Ingrese una expresion regular: ");
        return container.nextLine();
    }

    public String getExpresion(Optional<String> expresionInicial, Optional<String> optionalKey, Optional<List<Character>> optionalList) {

        RevisionEx revision;
        Expansion expansion;
        Postfix postfix;

        String expresion = expresionInicial.orElseGet(this::getInput);
        String key = optionalKey.orElse("");

        revision = optionalKey.isPresent() ? new RevisionEx(expresion, key) : new RevisionEx(expresion);
        expresion = revision.getExpresion();

        if (key != "") {
            expresion = "(" + expresion + key + ")";
            expresion = Lenguaje2.replaceSpacesWithSlashS(expresion);
            System.out.println("Expresion: " + expresion);
            Lenguaje2.setLenguajeInicial(expresion, key);
            if (Lenguaje2.operadores)
                expresion = Lenguaje2.ConvertirCar(expresion);
            Ejemplo3 expander = new Ejemplo3(expresion, optionalList.get());
            expresion = expander.expand();
            System.out.println("Expanded regex: " + expresion);
        } else {
            while (!revision.isCorrecta()) {
                System.out.println("Corrige la expreion o ingresa una nueva: ");
                expresion = container.nextLine();
                revision.updateExpresion(expresion);
            }
            Lenguaje.setLenguajeInicial(expresion);
            if (Lenguaje.operadores)
                expresion = Lenguaje.ConvertirCar(expresion);
        }

        if (revision.isSus() || key != "") {
            expansion = new Expansion(expresion);
            expresion = expansion.getExpresion();
            System.out.println("Expresion extendida: " + expresion);
        }

        if (key == "") {
            postfix = new Postfix();
            expresion = postfix.infixToPostfix(expresion);
            Lenguaje.setLenguajeFinal(expresion);
            System.out.println("Postfix: " + expresion);
        }

        return expresion;
    }
}
