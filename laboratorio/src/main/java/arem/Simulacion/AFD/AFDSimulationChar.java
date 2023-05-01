package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.IAFDs;

public class AFDSimulationChar implements IAFDs {
    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS;
    private StringBuilder expresion;

    public AFDSimulationChar(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        this.tablaS = tablaS;
        expresion = new StringBuilder();
    }

    @Override
    public String getExpresion() {
        return expresion.toString();
    }

    @Override 
    public boolean simulate(String input){
        EstadosAFN currentState = getInitialState(tablaS);
        this.expresion.append(input);

        for (int i = 0; i < input.length(); i++){
            char symbol = input.charAt(i);

            if (!tablaS.containsKey(currentState)) {
                return false;
            }

            Map<Character, Set<EstadosAFN>> transitions = tablaS.get(currentState);
            Set<EstadosAFN> nextStateSet = transitions.get(symbol);
            if (nextStateSet == null || nextStateSet.isEmpty()) {
                return false;
            }
            currentState = nextStateSet.iterator().next();
        }

        return currentState.getIdentificador() == TipoGrafo.FINAL;
    }

    private EstadosAFN getInitialState(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        for (EstadosAFN state : tablaS.keySet()) {
            if (state.getIdentificador() == TipoGrafo.INICIAL) {
                return state;
            }
        }
        return null;
    }
}
