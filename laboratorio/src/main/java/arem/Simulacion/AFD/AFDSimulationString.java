package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.IAFDs;

public class AFDSimulationString implements IAFDs {
    private Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS;

    public AFDSimulationString(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS) {
        this.tablaS = tablaS;
    }

    @Override
    public boolean simulate(String expresion) {

        EstadosAFN currentState = getInitialState(tablaS);

        for(int i = 0; i < expresion.length(); i++){
            String current;
            if (i < expresion.length() - 1){
                current = expresion.substring(i, i + 1);
                if (current.equals("\\")){
                    current = current + expresion.substring(i + 1, i + 2);
                    i++; // Incrementar i para saltar el siguiente carácter
                }
            } else {
                current = expresion.substring(i, i + 1);
            }
            Map<String, Set<EstadosAFN>> transitions = tablaS.get(currentState);
            Set<EstadosAFN> nextStatesSet = transitions.get(current);
            if (nextStatesSet == null || nextStatesSet.isEmpty()){
                return false;
            }
            currentState = nextStatesSet.iterator().next();
        }
        return currentState.getIdentificador() == TipoGrafo.FINAL;
    }

    private EstadosAFN getInitialState(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> afd) {
        EstadosAFN finalInitialState = null;

        for (EstadosAFN state : afd.keySet()) {
            if (state.getIdentificador() == TipoGrafo.INICIAL) {
                return state;
            } else if (state.getIdentificador() == TipoGrafo.FINAL) {
                Map<String, Set<EstadosAFN>> transitions = afd.get(state);
                for (Set<EstadosAFN> targetStates : transitions.values()) {
                    if (targetStates.contains(state)) {
                        finalInitialState = state;
                        break;
                    }
                }
            }
        }

        return finalInitialState;
    }
}
