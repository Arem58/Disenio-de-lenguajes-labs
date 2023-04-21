package arem.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import arem.Algoritmos.AFN2.AFN2;
import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.GE.GE;
import arem.Grafo.GrafoGraphviz;
import arem.Grafo.grafo;

public class AFNhandler extends handler {
    public AFNhandler() {
        super();
        AFN();
    }

    private void AFN() {
        AFN2 afn2 = new AFN2(getExpresion(Optional.empty(), Optional.empty()));
        // // boolean isAccepted = afn2.simulate(".;-/.");
        // System.out.println(
        // "La cadena 'ababb' es " + (isAccepted ? "aceptada" : "rechazada") + " por el
        // autómata minimizado.");
        GE<Estados2> ge = afn2.getGe();
        ge.setEntradaSalida();
        grafo<Estados2> grafo = new grafo(ge);
        imprimir(ge);
        GrafoGraphviz<Estados2> grafoGraphviz = new GrafoGraphviz<>(ge);
        String outputPath = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/outputs/output.png";
        try {
            grafoGraphviz.createGraphViz(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimir(GE<Estados2> ge) {
        // OrdenarAFN ordenarAFN = new OrdenarAFN(ge);
        System.out.println(ge.getListaEstados().toString());
        System.out.println("Estado inicial:" + ge.getEntrada().toString());
        System.out.println("Estado final:" + ge.getSalida().toString());
        Map<Estados2, Map<Character, Set<Estados2>>> transicion = ge.getTransitions();

        // // Recorrer el mapa completo de transiciones
        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : transicion.entrySet()) {
            Estados2 estado = entry.getKey();
            int sourceState = Integer.parseInt(estado.getId()); // Estado de origen
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
