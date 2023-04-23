package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.interfaces.IAFDs;

public class AFDSimulationChar implements IAFDs {
    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS;

    public AFDSimulationChar(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        this.tablaS = tablaS;
    }

    @Override 
    public boolean simulate(String expresion){
        for (int i = 0; i < expresion.length(); i++){
            char symbol = expresion.charAt(i);
        }
        return true;
    }
}
