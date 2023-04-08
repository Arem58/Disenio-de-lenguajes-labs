package arem.Grafo;

import arem.Algoritmos.AFD.nodo;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class arbol {
    private Graph graph;

    public arbol() {
        graph = new SingleGraph("Árbol");
        graph.setAttribute("ui.stylesheet", "node { size: 30px; text-size: 16px; fill-color: #FFF; stroke-color: black; stroke-width: 2px; text-color: black; } edge { size: 2px; }");
    }

    public void graficar(nodo root) {
        System.setProperty("org.graphstream.ui", "swing");
        Node rootNode = addOrGetNode(root);
        rootNode.setAttribute("xy", 0, 0);
        addNode(root, 1, 0, 100, 5);
        Viewer viewer = graph.display(true);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
    }

    private void addNode(nodo node, int depth, int x, int xSpacing, int maxDepth) {
        if (depth >= maxDepth) {
            return;
        }
    
        int newXSpacing = xSpacing * 2 / (depth + 1);
    
        if (node.getLeft() != null) {
            Node leftNode = addOrGetNode(node.getLeft());
            leftNode.setAttribute("xy", x - newXSpacing, -depth * 50);
            addOrGetEdge(node, node.getLeft());
            addNode(node.getLeft(), depth + 1, x - newXSpacing, newXSpacing, maxDepth);
        }
    
        if (node.getRight() != null) {
            Node rightNode = addOrGetNode(node.getRight());
            rightNode.setAttribute("xy", x + newXSpacing, -depth * 50);
            addOrGetEdge(node, node.getRight());
            addNode(node.getRight(), depth + 1, x + newXSpacing, newXSpacing, maxDepth);
        }
    }

    private Node addOrGetNode(nodo treeNode) {
        String id = "node_" + treeNode.hashCode();
        Node node = graph.getNode(id);
        if (node == null) {
            node = graph.addNode(id);
        }
        node.setAttribute("ui.label", Character.toString(treeNode.getValue()));
        return node;
    }

    private void addOrGetEdge(nodo source, nodo target) {
        String edgeId = "edge_" + source.hashCode() + "_" + target.hashCode();
        if (graph.getEdge(edgeId) == null) {
            Node sourceNode = graph.getNode("node_" + source.hashCode());
            Node targetNode = graph.getNode("node_" + target.hashCode());
            if (sourceNode != null && targetNode != null) {
                graph.addEdge(edgeId, sourceNode, targetNode);
            }
        }
    }
}


