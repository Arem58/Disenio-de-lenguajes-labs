package arem.Grafo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class grafo {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Ejemplo de flechas");

        // Agregamos los nodos
        Node nodeA = graph.addNode("A");
        nodeA.setAttribute("ui.label", "Nodo A");

        Node nodeB = graph.addNode("B");
        nodeB.setAttribute("ui.label", "Nodo B");

        Node nodeC = graph.addNode("C");
        nodeC.setAttribute("ui.label", "Nodo C");

        // Agregamos las aristas
        Edge edgeAB = graph.addEdge("AB", "A", "B", true);
        Edge edgeBC = graph.addEdge("BC", "B", "C", true);
        Edge edgeCA = graph.addEdge("CA", "C", "A", true);

        // Agregamos estilo con una hoja de estilos
        // String css = "node { size: 20px; fill-color: #555; text-color: white; text-size: 20; text-alignment: center; }" +
        //              "edge { arrow-size: 20px, 5px; arrow-shape: arrow; fill-color: black; }";
        // graph.setAttribute("ui.stylesheet", css);
        graph.setAttribute("ui.stylesheet", "url('arem/Grafo/styles.css')");

        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
}}