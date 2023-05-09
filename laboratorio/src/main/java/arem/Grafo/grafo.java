package arem.Grafo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import arem.Algoritmos.GE.GE;
import arem.Algoritmos.GE.GEString;
import arem.Algoritmos.interfaces.IGE;
import arem.Algoritmos.interfaces.estados;

public class grafo<T extends estados> {

    private Object ge;

    public grafo(GE<T> ge) {
        this.ge = ge;
        graficar();
    }

    public grafo(GEString<T> geString) {
        this.ge = geString;
        graficar();
    }

    public void graficar() {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Ejemplo de flechas");

        Set<T> listaEstados;
        if (ge instanceof IGE) {
            listaEstados = ((IGE<T>) ge).getListaEstados();
        } else {
            listaEstados = ((GEString<T>) ge).getListaEstados();
        }

        for (T estado : listaEstados) {
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
        }

        Map<T, Map<String, Set<T>>> transitions;
        if (ge instanceof IGE) {
            transitions = new HashMap<>();
            for (Map.Entry<T, Map<Character, Set<T>>> entry : ((IGE<T>) ge).getTransitions().entrySet()) {
                Map<String, Set<T>> stringTransitions = new HashMap<>();
                for (Map.Entry<Character, Set<T>> innerEntry : entry.getValue().entrySet()) {
                    stringTransitions.put(Character.toString(innerEntry.getKey()), innerEntry.getValue());
                }
                transitions.put(entry.getKey(), stringTransitions);
            }
        } else {
            transitions = ((GEString<T>) ge).getTransitions();
        }

        for (Map.Entry<T, Map<String, Set<T>>> entry : transitions.entrySet()) {
            T estado = entry.getKey();
            Map<String, Set<T>> transitionMap = entry.getValue();

            for (Map.Entry<String, Set<T>> transitionEntry : transitionMap.entrySet()) {
                String symbol = transitionEntry.getKey(); 
                Set<T> destinationStates = transitionEntry.getValue(); 

                for (T destState : destinationStates) {
                    String edgeId = estado.toString() + destState.toString();
                    Edge edge = graph.getEdge(edgeId);
                
                    if (edge == null) { 
                        edge = graph.addEdge(edgeId, estado.toString(), destState.toString(), true);
                        edge.setAttribute("ui.label", symbol);
                    } else { 
                        String existingLabel = (String) edge.getAttribute("ui.label");
                        edge.setAttribute("ui.label", existingLabel + ", " + symbol);
                    }
                
                    Edge recursor = graph.getEdge(destState.toString() + estado.toString());
                    if (recursor != null) {
                        edge.setAttribute("ui.style", "text-offset: -30px, 0px;");
                        recursor.setAttribute("ui.style", "text-offset: 30px, 0px;");
                    }
                }
            }
        }
        graph.setAttribute("ui.stylesheet", "url('arem/Grafo/styles.css')");

        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
    }
}