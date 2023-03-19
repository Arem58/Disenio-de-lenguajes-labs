package arem.handlers;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFN2.AFN2;
import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.AFN2.GE;
import arem.Algoritmos.AFNtoAFD.Tabla;

public class AFNtoAFDhandler extends handler {

    public AFNtoAFDhandler() {
        super();
        AFNtoAFD();
    }

    private void AFNtoAFD() {
        AFN2 afn2 = new AFN2(expresion);
        GE ge = afn2.getGe();
        Tabla tabla = new Tabla(ge); 
        printTabla(tabla.getTabla());
    }

    private void printTabla(Map<Estados2, Map<Character, Set<Estados2>>> tabla) {
        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : tabla.entrySet()) {
            Estados2 nodo = entry.getKey();
            Map<Character, Set<Estados2>> transiciones = entry.getValue();
            System.out.print("Nodo " + nodo + ": ");
            for (Map.Entry<Character, Set<Estados2>> transicion : transiciones.entrySet()) {
                System.out.print(transicion.getKey() + ": ");
                System.out.print(transicion.getValue().toString() + " ");
            }
            System.out.println();
        }
    }
}
