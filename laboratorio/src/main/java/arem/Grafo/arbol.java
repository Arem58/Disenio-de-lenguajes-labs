package arem.Grafo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Node;
import guru.nidi.graphviz.attribute.Shape;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFD.nodo;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class arbol {
    public static void visualizeTree(nodo root, String outputPath, Set<String> acceptingStates) throws IOException {
        MutableGraph graph = mutGraph("tree").setDirected(true);
        Map<Integer, MutableNode> nodeMap = new HashMap<>();

        preOrderTraversal(root, nodeMap, graph, acceptingStates);
        Graphviz.fromGraph(graph).width(1200).render(Format.PNG).toFile(new File(outputPath));

        System.out.println("Archivo '" + outputPath + "' creado.");
    }

    private static void preOrderTraversal(nodo treeNode, Map<Integer, MutableNode> nodeMap, MutableGraph graph, Set<String> acceptingStates) {
        if (treeNode == null) {
            return;
        }
    
        // Utiliza el atributo Label para establecer el valor del nodo
        int nodeId = treeNode.getId();
        String label = getLabel(treeNode.getValue());
        MutableNode currentNode = mutNode(Integer.toString(nodeId)).add(Label.of(label));
        if (acceptingStates.contains(treeNode.getValue())) {
            currentNode.add(Shape.DOUBLE_CIRCLE);
        }
        nodeMap.put(nodeId, currentNode);
        graph.add(currentNode);
    
        nodo leftChild = treeNode.getLeft();
        nodo rightChild = treeNode.getRight();
    
        if (leftChild != null) {
            preOrderTraversal(leftChild, nodeMap, graph, acceptingStates);
            graph.add(currentNode.addLink(mutNode(Integer.toString(leftChild.getId()))));
        }
    
        if (rightChild != null) {
            preOrderTraversal(rightChild, nodeMap, graph, acceptingStates);
            graph.add(currentNode.addLink(mutNode(Integer.toString(rightChild.getId()))));
        }
    }

    private static String getLabel(String value) {
        StringBuilder labelBuilder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char symbol = value.charAt(i);
            if (symbol == '\\') {
                i++;
                if (i < value.length()) {
                    char nextSymbol = value.charAt(i);
                    switch (nextSymbol) {
                        case 'n':
                            labelBuilder.append("\\\\n");
                            break;
                        case 't':
                            labelBuilder.append("\\\\t");
                            break;
                        case 's':
                            labelBuilder.append("\\\\s");
                            break;
                        default:
                            labelBuilder.append(nextSymbol);
                    }
                }
            } else {
                labelBuilder.append(symbol);
            }
        }
        return labelBuilder.toString();
    }
}
