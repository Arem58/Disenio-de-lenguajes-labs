package arem.Grafo;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import arem.Algoritmos.AFD.nodo;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class arbol {
    public static void visualizeTree(nodo root, String outputPath) throws IOException {
        MutableGraph graph = mutGraph("tree").setDirected(true);
        Map<Integer, MutableNode> nodeMap = new HashMap<>();

        preOrderTraversal(root, nodeMap, graph);
        Graphviz.fromGraph(graph).width(1200).render(Format.PNG).toFile(new File(outputPath));

        System.out.println("Archivo '" + outputPath + "' creado.");
    }

    private static void preOrderTraversal(nodo treeNode, Map<Integer, MutableNode> nodeMap, MutableGraph graph) {
        if (treeNode == null) {
            return;
        }
    
        // Utiliza el atributo Label para establecer el valor del nodo
        int nodeId = treeNode.getId();
        MutableNode currentNode = mutNode(Integer.toString(nodeId)).add(Label.of(treeNode.getValue()), Color.BLACK);
        nodeMap.put(nodeId, currentNode);
        graph.add(currentNode);
    
        nodo leftChild = treeNode.getLeft();
        nodo rightChild = treeNode.getRight();
    
        if (leftChild != null) {
            preOrderTraversal(leftChild, nodeMap, graph);
            graph.add(currentNode.addLink(mutNode(Integer.toString(leftChild.getId()))));
        }
    
        if (rightChild != null) {
            preOrderTraversal(rightChild, nodeMap, graph);
            graph.add(currentNode.addLink(mutNode(Integer.toString(rightChild.getId()))));
        }
    }    
}
