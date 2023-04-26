package arem.Simulacion.AFD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.IAFDs;
import arem.handlers.GALhandler;

public class AFDSimulationString implements IAFDs {
    private Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS;
    private GALhandler galHandler;
    private StringBuilder expresion;
    private Map<Integer, Map<String, String>> tokensReturned = new HashMap<>();
    private int tokenCounter = 0;
    private Set<String> returnedTokens = null;
    private Set<String> tokens = null;

    public AFDSimulationString(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS, Set<String> returnedTokens,
            Set<String> tokens) {
        this.tablaS = tablaS;
        this.returnedTokens = returnedTokens;
        this.tokens = tokens;
        expresion = new StringBuilder();
        tokensReturned = new HashMap<>();
    }

    public AFDSimulationString(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS, GALhandler galHandler) {
        this.tablaS = tablaS;
        this.galHandler = galHandler;
        expresion = new StringBuilder();
        tokensReturned = new HashMap<>();
    }

    public Map<Integer, Map<String, String>> getTokensReturned() {
        return tokensReturned;
    }

    @Override
    public String getExpresion() {
        return expresion.toString();
    }

    @Override
    public boolean simulate(String expresion) {

        EstadosAFN currentState = getInitialState(tablaS);
        StringBuilder cache = new StringBuilder();
        this.expresion.append(expresion);

        if (galHandler != null) {
            returnedTokens = galHandler.getTokensDevueltos();
            tokens = galHandler.getTokens();
        }

        for (int i = 0; i < expresion.length(); i++) {
            char rawChar = expresion.charAt(i);
            String current;

            if (rawChar == ' ') {
                current = "\\s";
            } else if (rawChar == '\t') {
                current = "\\t";
            } else if (rawChar == '\n') {
                current = "\\n";
            } else {
                current = String.valueOf(rawChar);
            }

            Map<String, Set<EstadosAFN>> transitions = tablaS.get(currentState);
            Set<EstadosAFN> nextStatesSet = transitions.get(current);

            boolean updateCurrentState = true;
            boolean isNextStatesSetEmpty = nextStatesSet == null || nextStatesSet.isEmpty();
            boolean isLastChar = i == expresion.length() - 1 && tokens != null;
            if (isNextStatesSetEmpty || isLastChar) {

                if (tokens == null && returnedTokens == null) {
                    return false;
                }

                if (currentState == getInitialState(tablaS) && isNextStatesSetEmpty){
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("Error", "Error");
                    tokensReturned.put(tokenCounter, tokenMap);
                    return false;
                }

                if (isLastChar && currentState == getInitialState(tablaS)){
                    currentState = nextStatesSet.iterator().next();
                    transitions = tablaS.get(currentState);
                    updateCurrentState = false;
                }

                // if (isLastChar && !isNextStatesSetEmpty) {
                //     cache.append(current);
                // }

                String newToken = getToken(transitions, tokens);
                if (newToken == null) {
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("Error", "Error");
                    tokensReturned.put(tokenCounter, tokenMap);
                    return false;
                }
                if (isLastChar && !isNextStatesSetEmpty) {
                    cache.append(current);
                    nextStatesSet = transitions.get(newToken);
                    currentState = nextStatesSet.iterator().next();
                }

                if (returnedTokens.contains(newToken)) {
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put(newToken, cache.toString());
                    // System.out.println("Token: " + newToken + " valor: " + cache);

                    tokensReturned.put(tokenCounter, tokenMap);
                    tokenCounter++;
                }
                if (i < expresion.length() && currentState.getIdentificador() != TipoGrafo.FINAL) {
                    currentState = getInitialState(tablaS);
                    updateCurrentState = false;
                    i -= 1;
                }
                cache.setLength(0);
            }
            if (updateCurrentState) {
                cache.append(current);
                currentState = nextStatesSet.iterator().next();
            }
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

    private EstadosAFN getFinalState(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> afd) {
        for (EstadosAFN state : afd.keySet()) {
            if (state.getIdentificador() == TipoGrafo.FINAL) {
                return state;
            }
        }
        return null;
    }    

    private String getToken(Map<String, Set<EstadosAFN>> transitions, Set<String> tokens) {
        if (tokens == null) {
            return null;
        }
        Set<String> aristas = transitions.keySet();
        for (String actual : aristas) {
            if (tokens.contains(actual)) {
                return actual;
            }
        }
        return null;
    }
}
