package arem;

import arem.Funciones.RevisionEx;
import arem.Funciones.Expansion;
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

        while (!revision.isCorrectaa()){
            System.out.println("Corrige la expreion o ingresa una nueva: ");
            expresion = container.nextLine();
            revision.updateExpresion(expresion);
        }

        if (revision.isSus()){
            expansion = new Expansion(expresion);
            expresion = expansion.getExpresion();
            System.out.println(expresion);
        }

        postfix = new Postfix();
        expresion = postfix.infixToPostfix(expresion);
        System.out.println(expresion);
    }
}
