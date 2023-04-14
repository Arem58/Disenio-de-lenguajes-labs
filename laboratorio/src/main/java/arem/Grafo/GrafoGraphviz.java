package arem.Grafo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFN2.GE;
import arem.Algoritmos.interfaces.estados;
import static guru.nidi.graphviz.model.Factory.*;

public class GrafoGraphviz<T extends estados> {

    private GE<T> ge;

    public GrafoGraphviz(GE<T> ge) {
        this.ge = ge;
    }

    public void createGraphViz(String outputPath) throws IOException {
        MutableGraph graph = mutGraph("Ejemplo de flechas").setDirected(true);
        Map<T, MutableNode> nodeMap = new HashMap<>();

        for (T estado : ge.getListaEstados()) {
            MutableNode node = mutNode(estado.toString()).add(Label.of(estado.toString()));
            nodeMap.put(estado, node);
            graph.add(node);

            switch (estado.getIdentificador()) {
                case FINAL:
                    node.add(Color.RED);
                    break;
                case INICIAL:
                    node.add(Color.BLUE);
                    break;
            }
        }

        for (Map.Entry<T, Map<Character, Set<T>>> entry : ge.getTransitions().entrySet()) {
            T estado = entry.getKey();
            Map<Character, Set<T>> transitionMap = entry.getValue();

            for (Map.Entry<Character, Set<T>> transitionEntry : transitionMap.entrySet()) {
                char symbol = transitionEntry.getKey();
                Set<T> destinationStates = transitionEntry.getValue();

                for (T destState : destinationStates) {
                    MutableNode sourceNode = nodeMap.get(estado);
                    MutableNode destNode = nodeMap.get(destState);

                    sourceNode.addLink(to(destNode).with(Label.of(Character.toString(symbol)), Style.BOLD));
                }
            }
        }

        Graphviz.fromGraph(graph).width(1200).render(Format.PNG).toFile(new File(outputPath));
        System.out.println("Archivo '" + outputPath + "' creado.");
    }
}
