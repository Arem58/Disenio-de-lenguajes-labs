package arem.Grafo;

import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.AFN2.GE;

public class grafo {

    private GE ge;

    public grafo(GE ge) {
        this.ge = ge;
        graficar();
    }

    public void graficar() {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Ejemplo de flechas");

        for(Estados2 estado: ge.getListaEstados()){
            if (ge.getEntrada().equals(estado)){
                Node nodo = graph.addNode(estado.toString());
                nodo.setAttribute("ui.label", estado.toString());
                nodo.setAttribute("ui.style", "fill-color: blue;");
            }else if (ge.getSalida().equals(estado)){
                Node nodo = graph.addNode(estado.toString());
                nodo.setAttribute("ui.label", estado.toString());
                nodo.setAttribute("ui.style", "fill-color: red;");
            }else{
                Node nodo = graph.addNode(estado.toString());
                nodo.setAttribute("ui.label", estado.toString());
            }
        }

        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : ge.getTransitions().entrySet()) {
            Estados2 estado = entry.getKey();
            Map<Character, Set<Estados2>> transitionMap = entry.getValue(); // Mapa interno de transiciones

            // Recorrer el mapa interno de transiciones
            for (Map.Entry<Character, Set<Estados2>> transitionEntry : transitionMap.entrySet()) {
                char symbol = transitionEntry.getKey(); // Símbolo de entrada
                Set<Estados2> destinationStates = transitionEntry.getValue(); // Conjunto de estados de destino

                // Recorrer el conjunto de estados de destino
                for (Estados2 destState : destinationStates) {
                    Edge edge = graph.addEdge(estado.toString() + destState.toString(), estado.toString(), destState.toString(), true);
                    Edge recursor = graph.getEdge(destState.toString() + estado.toString() );
                    if (recursor != null){
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