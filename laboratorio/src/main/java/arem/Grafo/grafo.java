package arem.Grafo;

import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import arem.Algoritmos.AFN2.GE;
import arem.Algoritmos.interfaces.estados;

public class grafo<T extends estados> {

    private GE<T> ge;

    public grafo(GE<T> ge) {
        this.ge = ge;
        graficar();
    }

    public void graficar() {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Ejemplo de flechas");

        for (T estado : ge.getListaEstados()) {
            Node nodo = graph.addNode(estado.toString());
            switch (estado.getIdentificador()) {
                case FINAL:
                    nodo.setAttribute("ui.label", estado.toString());
                    nodo.setAttribute("ui.style", "fill-color: red;");
                    break;
                case INICIAL:
                    nodo.setAttribute("ui.label", estado.toString());
                    nodo.setAttribute("ui.style", "fill-color: blue;");
                    break;
                case NORMAL:
                    nodo.setAttribute("ui.label", estado.toString());
                    break;
            }
            // if (ge.getEntrada().equals(estado)) {
            //     Node nodo = graph.addNode(estado.toString());
            //     nodo.setAttribute("ui.label", estado.toString());
            //     nodo.setAttribute("ui.style", "fill-color: blue;");
            // } else if (ge.getSalida().equals(estado)) {
            //     Node nodo = graph.addNode(estado.toString());
            //     nodo.setAttribute("ui.label", estado.toString());
            //     nodo.setAttribute("ui.style", "fill-color: red;");
            // } else {
            //     Node nodo = graph.addNode(estado.toString());
            //     nodo.setAttribute("ui.label", estado.toString());
            // }
        }

        for (Map.Entry<T, Map<Character, Set<T>>> entry : ge.getTransitions().entrySet()) {
            T estado = entry.getKey();
            Map<Character, Set<T>> transitionMap = entry.getValue(); // Mapa interno de transiciones

            // Recorrer el mapa interno de transiciones
            for (Map.Entry<Character, Set<T>> transitionEntry : transitionMap.entrySet()) {
                char symbol = transitionEntry.getKey(); // Símbolo de entrada
                Set<T> destinationStates = transitionEntry.getValue(); // Conjunto de estados de destino

                // Recorrer el conjunto de estados de destino
                for (T destState : destinationStates) {
                    Edge edge = graph.addEdge(estado.toString() + destState.toString(), estado.toString(),
                            destState.toString(), true);
                    Edge recursor = graph.getEdge(destState.toString() + estado.toString());
                    if (recursor != null) {
                        edge.setAttribute("ui.style", "text-offset: -30px, 0px;");
                        recursor.setAttribute("ui.style", "text-offset: 30px, 0px;");
                    }
                    edge.setAttribute("ui.label", symbol);
                }
            }
        }
        graph.setAttribute("ui.stylesheet", "url('arem/Grafo/styles.css')");

        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
    }
}