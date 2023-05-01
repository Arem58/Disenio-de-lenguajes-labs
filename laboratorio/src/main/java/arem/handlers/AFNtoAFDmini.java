package arem.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.GE.GE;
import arem.Algoritmos.Minimizacion.AFDminimizado;
import arem.Grafo.GrafoGraphviz;
import arem.Grafo.grafo;

public class AFNtoAFDmini extends AFNtoAFDhandler {

    protected GE<EstadosAFN> geAFDmin;

    public GE<EstadosAFN> getGeAFDmin() {
        return geAFDmin;
    }

    public AFNtoAFDmini() {
        super();
    }

    @Override
    protected void AFNtoAFD() {
        super.AFNtoAFD();

        // AFDminimizado afdMini = new AFDminimizado(super.geAFD.getTransitions());
        AFDminimizado afdMinimizado = new AFDminimizado(super.geAFD.getTransitions());
        afdMinimizado.minimize();
        Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> minimizedAFD = afdMinimizado.getMinimizedAFD();
        // boolean isAccepted = afdMini.simulate(".;-/.");
        // System.out.println(
        // "La cadena 'ababb' es " + (isAccepted ? "aceptada" : "rechazada") + " por el
        // autómata minimizado.");
        geAFDmin = new GE<>(minimizedAFD);
        imprimir(geAFDmin);
        grafo<EstadosAFN> grafo = new grafo(geAFDmin);
        GrafoGraphviz<EstadosAFN> grafoGraphviz = new GrafoGraphviz<>(geAFDmin);
        String outputPath = "laboratorio/src/main/java/arem/assets/outputs/Minimizado/grafo.png";
        createGraph(grafoGraphviz, outputPath);
    }

    private void createGraph(GrafoGraphviz<EstadosAFN> grafoGraphviz, String outputPath) {
        try {
            grafoGraphviz.createGraphViz(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
