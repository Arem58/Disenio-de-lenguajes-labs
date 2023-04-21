package arem.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import arem.Algoritmos.AFD.Arbol;
import arem.Algoritmos.AFD.Tabla;
import arem.Algoritmos.AFD.nodo;
import arem.Algoritmos.AFD.subConjuntos;
import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.GE.GEString;
import arem.Algoritmos.interfaces.ILenguaje;
import arem.Funciones.Lenguaje;
import arem.Funciones.Lenguaje2;
import arem.Grafo.GrafoGraphviz;
import arem.Grafo.grafo;

public class AFDhandler extends handler {

    protected GEString<EstadosAFN> geAFD;
    private Set<String> acceptingStates = new HashSet<>();
    private ILenguaje lenguaje;
    private String expression;

    public AFDhandler() {
        super();
        this.lenguaje = new Lenguaje();
        acceptingStates = new HashSet<>(Collections.singleton("#"));
        this.expression = getExpresion(Optional.empty(), Optional.empty());
        AFD();
    }

    public AFDhandler(Set<String> acceptingStates, String finalExpression){
        super();
        this.lenguaje = new Lenguaje2();
        this.acceptingStates.addAll(acceptingStates);
        this.expression = finalExpression;
        AFD();
    }

    private void AFD(){
        Arbol arbolg = new Arbol(expression);
        nodo root = arbolg.getRoot();
        nodo.bfs(root);
        Tabla tabla = new Tabla(root);
        tabla.printTable();
        subConjuntos subconjuntos = new subConjuntos(tabla.getAfdNodeData(), root, acceptingStates, lenguaje);
        geAFD = new GEString<>(subconjuntos.getTablaS(), subconjuntos.getListaEstados());
        // grafo<EstadosAFN> grafo = new grafo(geAFD);
        GrafoGraphviz<EstadosAFN> grafoGraphviz = new GrafoGraphviz<>(geAFD);
        String outputPath = "laboratorio/src/main/java/arem/assets/outputs/output.png";
        try {
            grafoGraphviz.createGraphViz(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInput() {
        String input = super.getInput();
        
        EndofLine = "#";
        input = "(" + input + ")#";

        return input;
    }

    // @Override
    // protected String getExpresion(Optional<String> expresionInicial, Optional<String> key) {
    //     String expresion = super.getExpresion(expresionInicial, Optional.empty());

    //     return expresion;
    // }
}
