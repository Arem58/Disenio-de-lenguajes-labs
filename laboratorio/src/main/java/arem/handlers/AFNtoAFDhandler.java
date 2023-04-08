package arem.handlers;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import arem.Algoritmos.AFN2.AFN2;
import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.AFN2.GE;
import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.AFNtoAFD.Subconjuntos;
import arem.Algoritmos.AFNtoAFD.Tabla;
import arem.Grafo.grafo;

public class AFNtoAFDhandler extends handler {
    protected GE<EstadosAFN> geAFD;

    public AFNtoAFDhandler() {
        super();
        AFNtoAFD();
    }

    protected void AFNtoAFD() {
        AFN2 afn2 = new AFN2(getExpresion(Optional.empty(), Optional.empty()));
        GE<Estados2> ge = afn2.getGe();
        Tabla tabla = new Tabla(ge); 
        printTabla(tabla.getTabla());
        Subconjuntos subconjuntos = new Subconjuntos(ge.getEntrada(), ge.getSalida(), tabla.getTabla());
        geAFD = new GE<>(subconjuntos.getTablaS(), subconjuntos.getListaEstados());
        imprimir(geAFD);
        grafo<EstadosAFN> grafo = new grafo(geAFD);
    }

    private void printTabla(Map<Estados2, Map<Character, Set<Estados2>>> tabla) {
        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : tabla.entrySet()) {
            Estados2 nodo = entry.getKey();
            Map<Character, Set<Estados2>> transiciones = entry.getValue();
            System.out.print("Nodo " + nodo + ": ");
            for (Map.Entry<Character, Set<Estados2>> transicion : transiciones.entrySet()) {
                System.out.print(transicion.getKey() + ": ");
                System.out.print(transicion.getValue().toString() + " ");
            }
            System.out.println();
        }
    }

    protected void imprimir(GE<EstadosAFN> ge) {
        // OrdenarAFN ordenarAFN = new OrdenarAFN(ge);
        System.out.println(ge.getListaEstados().toString());
        Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> transicion = ge.getTransitions();

        // // Recorrer el mapa completo de transiciones
        for (Map.Entry<EstadosAFN, Map<Character, Set<EstadosAFN>>> entry : transicion.entrySet()) {
            EstadosAFN estado = entry.getKey();
            String sourceState = estado.getId(); // Estado de origen
            Map<Character, Set<EstadosAFN>> transitionMap = entry.getValue(); // Mapa interno de transiciones

            // Recorrer el mapa interno de transiciones
            for (Map.Entry<Character, Set<EstadosAFN>> transitionEntry : transitionMap.entrySet()) {
                char symbol = transitionEntry.getKey(); // Símbolo de entrada
                Set<EstadosAFN> destinationStates = transitionEntry.getValue(); // Conjunto de estados de destino

                // Recorrer el conjunto de estados de destino
                for (EstadosAFN destState : destinationStates) {
                    // Imprimir la información completa de la transición
                    System.out.println("Transición: " + sourceState + " --" + symbol + "--> " + destState.getId());
                }
            }
        }
    }
}
