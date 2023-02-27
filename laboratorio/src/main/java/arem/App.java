package arem;

import arem.Funciones.RevisionEx;
import arem.Algoritmos.AFN;
import arem.Funciones.Expansion;
import arem.Funciones.Lenguaje;
import arem.Funciones.Postfix;

import java.util.Scanner;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
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
        AFN afn = new AFN(expresion);
        System.out.println("Transiciones: " + afn.getTransitions());
        System.out.println("Estados: " + afn.getEstadosT().toString());
        System.out.println("Estado final: " + afn.getEstadoFinal());
        System.out.println("Estado inicial: " + afn.getEstadoInicial());
    }
}
