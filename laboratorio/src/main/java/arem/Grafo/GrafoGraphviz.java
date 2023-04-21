package arem.Grafo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.GE.GE;
import arem.Algoritmos.GE.GEString;
import arem.Algoritmos.interfaces.IGE;
import arem.Algoritmos.interfaces.estados;
import static guru.nidi.graphviz.model.Factory.*;

public class GrafoGraphviz<T extends estados> {

    private Object ge;

    public GrafoGraphviz(GE<T> ge) {
        this.ge = ge;
    }

    public GrafoGraphviz(GEString<T> geString) {
        this.ge = geString;
    }

    public void createGraphViz(String outputPath) throws IOException {
        MutableGraph graph = mutGraph("Ejemplo de flechas").setDirected(true);
        Map<T, MutableNode> nodeMap = new HashMap<>();

        Set<T> listaEstados;
        if (ge instanceof IGE) {
            listaEstados = ((IGE<T>) ge).getListaEstados();
        } else {
            listaEstados = ((GEString<T>) ge).getListaEstados();
        }

        for (T estado : listaEstados) {
            MutableNode node = mutNode(estado.toString()).add(Label.of(estado.toString()));
            nodeMap.put(estado, node);
            graph.add(node);

            switch (estado.getIdentificador()) {
                case FINAL:
                    node.add(Color.RED, Shape.DOUBLE_CIRCLE);
                    break;
                case INICIAL:
                    node.add(Color.BLUE);
                    break;
            }
        }

        Map<T, Map<String, Set<T>>> transitions = new HashMap<>();

        if (ge instanceof IGE) {
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
        
            Map<T, Set<String>> combinedTransitions = new HashMap<>();
        
            for (Map.Entry<String, Set<T>> transitionEntry : transitionMap.entrySet()) {
                String symbol = transitionEntry.getKey();
                Set<T> destinationStates = transitionEntry.getValue();
        
                for (T destState : destinationStates) {
                    if (!combinedTransitions.containsKey(destState)) {
                        combinedTransitions.put(destState, new HashSet<>());
                    }
                    combinedTransitions.get(destState).add(symbol);
                }
            }
        
            MutableNode sourceNode = nodeMap.get(estado);
        
            for (Map.Entry<T, Set<String>> combinedEntry : combinedTransitions.entrySet()) {
                T destState = combinedEntry.getKey();
                Set<String> symbols = combinedEntry.getValue();
                MutableNode destNode = nodeMap.get(destState);
        
                String combinedLabel = String.join(",", symbols);
                sourceNode.addLink(to(destNode).with(Label.of(combinedLabel), Style.BOLD));
            }
        }

        Graphviz.useEngine(new GraphvizCmdLineEngine()); // Use GraphvizV8Engine
        Graphviz.fromGraph(graph).width(1200).render(Format.PNG).toFile(new File(outputPath));
        System.out.println("Archivo '" + outputPath + "' creado.");
    }
}
