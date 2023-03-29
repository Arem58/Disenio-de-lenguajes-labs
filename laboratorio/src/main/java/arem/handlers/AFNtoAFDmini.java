package arem.handlers;

import arem.Algoritmos.AFN2.GE;
import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.Minimizacion.AFDminimizado;
import arem.Grafo.grafo;

public class AFNtoAFDmini extends AFNtoAFDhandler {

    public AFNtoAFDmini() {
        super();
    }

    @Override
    protected void AFNtoAFD() {
        super.AFNtoAFD();

        AFDminimizado afdMini = new AFDminimizado(super.geAFD.getTransitions());
        boolean isAccepted = afdMini.simulate(".;-/.");
        System.out.println(
                "La cadena 'ababb' es " + (isAccepted ? "aceptada" : "rechazada") + " por el autómata minimizado.");
        GE<EstadosAFN> geAFDmin = new GE<>(afdMini.getAfdMini());
        imprimir(geAFDmin);
        grafo<EstadosAFN> grafo = new grafo(geAFDmin);
    }
}
