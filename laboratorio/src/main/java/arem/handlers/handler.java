package arem.handlers;

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

    protected String getInput() {
        System.out.println("Ingrese una expresion regular: ");
        return container.nextLine();
    }

    protected String getExpresion(Optional<String> expresionInicial, Optional<String> optionalKey) {

        RevisionEx revision;
        Expansion expansion;
        Postfix postfix;

        String expresion = expresionInicial.orElseGet(this::getInput);
        String key = optionalKey.orElse("");

        revision = new RevisionEx(expresion);
        expresion = revision.getExpresion();

        // while (!revision.isCorrecta()) {
        //     System.out.println("Corrige la expreion o ingresa una nueva: ");
        //     expresion = container.nextLine();
        //     revision.updateExpresion(expresion);
        // }

        if (key != "") {
            expresion = "(" + expresion + key + ")";
            expresion = Lenguaje2.replaceSpacesWithSlashS(expresion);
            Lenguaje2.setLenguajeInicial(expresion, key);
            if (Lenguaje2.operadores)
                expresion = Lenguaje2.ConvertirCar(expresion);
            Ejemplo3 expander = new Ejemplo3(expresion);
            expresion = expander.expand();
            System.out.println("Expanded regex: " + expresion);
        } else {
            Lenguaje.setLenguajeInicial(expresion);
            if (Lenguaje.operadores || Lenguaje2.operadores)
                expresion = Lenguaje.ConvertirCar(expresion);
        }

        if (revision.isSus()) {
            expansion = new Expansion(expresion);
            expresion = expansion.getExpresion();
            System.out.println("Expresion extendida: " + expresion);
        }

        postfix = new Postfix();
        expresion = postfix.infixToPostfix(expresion);
        if (key != "") {
            Lenguaje2.setLenguajeFinal(expresion);
        } else {
            Lenguaje.setLenguajeFinal(expresion);
        }
        System.out.println("Postfix: " + expresion);

        return expresion;
    }
}
