package arem;

import arem.Funciones.RevisionEx;
import arem.Algoritmos.AFN;
import arem.Algoritmos.AFN2.AFN2;
import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.AFN2.GE;
import arem.Funciones.Expansion;
import arem.Funciones.Lenguaje;
import arem.Funciones.Postfix;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
        // AFN afn = new AFN(expresion);
        // System.out.println("Transiciones: " + afn.getTransitions());
        // System.out.println("Estados: " + afn.getEstadosT().toString());
        // System.out.println("Estado final: " + afn.getEstadoFinal());
        // System.out.println("Estado inicial: " + afn.getEstadoInicial());
        AFN2 afn2 = new AFN2(expresion);
        GE ge = afn2.getGe();
        System.out.println(ge.getListaEstados().toString());
        System.out.println("Estado inicial:" + ge.getEntrada().toString());
        System.out.println("Estado final:" + ge.getSalida().toString());
        Map<Estados2, Map<Character, Set<Estados2>>> transicion = ge.getTransitions();

        // Recorrer el mapa completo de transiciones
        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : transicion.entrySet()) {
            Estados2 estado = entry.getKey();
            int sourceState = estado.getId(); // Estado de origen
            Map<Character, Set<Estados2>> transitionMap = entry.getValue(); // Mapa interno de transiciones

            // Recorrer el mapa interno de transiciones
            for (Map.Entry<Character, Set<Estados2>> transitionEntry : transitionMap.entrySet()) {
                char symbol = transitionEntry.getKey(); // Símbolo de entrada
                Set<Estados2> destinationStates = transitionEntry.getValue(); // Conjunto de estados de destino

                // Recorrer el conjunto de estados de destino
                for (Estados2 destState : destinationStates) {
                    // Imprimir la información completa de la transición
                    System.out.println("Transición: " + sourceState + " --" + symbol + "--> " + destState.getId());
                }
            }
        }
    }
}
