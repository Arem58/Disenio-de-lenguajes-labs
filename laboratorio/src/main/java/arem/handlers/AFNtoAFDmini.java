package arem.handlers;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.GE.GE;
import arem.Algoritmos.Minimizacion.AFDminimizado;
import arem.Grafo.grafo;

public class AFNtoAFDmini extends AFNtoAFDhandler {

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
        System.out.println("hola");
        // boolean isAccepted = afdMini.simulate(".;-/.");
        // System.out.println(
        // "La cadena 'ababb' es " + (isAccepted ? "aceptada" : "rechazada") + " por el
        // autómata minimizado.");
        GE<EstadosAFN> geAFDmin = new GE<>(afdMinimizado.getMinimizedAFD());
        imprimir(geAFDmin);
        grafo<EstadosAFN> grafo = new grafo(geAFDmin);
    }
}
