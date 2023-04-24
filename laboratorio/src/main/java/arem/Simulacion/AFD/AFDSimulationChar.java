package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.interfaces.IAFDs;

public class AFDSimulationChar implements IAFDs {
    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS;
    private StringBuilder expresion;

    public AFDSimulationChar(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        this.tablaS = tablaS;
    }

    @Override
    public String getExpresion() {
        return expresion.toString();
    }

    @Override 
    public boolean simulate(String expresion){
        for (int i = 0; i < expresion.length(); i++){
            char symbol = expresion.charAt(i);
        }
        return true;
    }
}
