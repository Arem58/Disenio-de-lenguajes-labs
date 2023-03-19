package arem.handlers;

import java.util.Scanner;

import arem.Funciones.Expansion;
import arem.Funciones.Lenguaje;
import arem.Funciones.Postfix;
import arem.Funciones.RevisionEx;

public class handler {
    protected String expresion;

    public handler() {
        this.expresion = getExpresion();
    }

    private String getExpresion(){
        System.out.println("Ingrese una expresion regular: ");

        RevisionEx revision;
        Expansion expansion;
        Postfix postfix;

        Scanner container = new Scanner(System.in);

        String expresion = container.nextLine();

        revision = new RevisionEx(expresion);
        expresion = revision.getExpresion();
        Lenguaje.setLenguajeInicial(expresion);
        if (Lenguaje.operadores)
            expresion = Lenguaje.ConvertirCar(expresion);

        while (!revision.isCorrecta()){
            System.out.println("Corrige la expreion o ingresa una nueva: ");
            expresion = container.nextLine();
            revision.updateExpresion(expresion);
        }
        container.close();

        if (revision.isSus()){
            expansion = new Expansion(expresion);
            expresion = expansion.getExpresion();
            System.out.println("Expresion extendida: " + expresion);
        }

        postfix = new Postfix();
        expresion = postfix.infixToPostfix(expresion);
        Lenguaje.setLenguajeFinal(expresion);
        System.out.println("Postfix: " + expresion);

        return expresion;
    }
}
