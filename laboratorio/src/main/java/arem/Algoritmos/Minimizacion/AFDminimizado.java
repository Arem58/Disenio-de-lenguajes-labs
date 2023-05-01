package arem.Algoritmos.Minimizacion;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;

public class AFDminimizado {

    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afd;
    private Set<Set<EstadosAFN>> totalSet = new HashSet<>();

    public Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> getMinimizedAFD() {
        Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> minimizedAFD = new HashMap<>();
    
        for (Set<EstadosAFN> stateSet : totalSet) {
            // Encuentra el estado con el número más bajo en el conjunto
            EstadosAFN lowestState = stateSet.stream()
                    .min(Comparator.comparing(EstadosAFN::getId))
                    .orElseThrow(() -> new IllegalStateException("Conjunto vacío en totalSet"));
    
            Map<Character, Set<EstadosAFN>> transitions = afd.get(lowestState);
            Map<Character, Set<EstadosAFN>> newTransitions = new HashMap<>();
    
            for (Character symbol : transitions.keySet()) {
                Set<EstadosAFN> targetStates = transitions.get(symbol);
                for (Set<EstadosAFN> targetSet : totalSet) {
                    if (belongsToSameSet(targetStates, targetSet)) {
                        // Encuentra el estado con el número más bajo en el conjunto de estados destino
                        EstadosAFN lowestTargetState = targetSet.stream()
                                .min(Comparator.comparing(EstadosAFN::getId))
                                .orElseThrow(() -> new IllegalStateException("Conjunto vacío en totalSet"));
    
                        newTransitions.put(symbol, new HashSet<>(Collections.singleton(lowestTargetState)));
                        break;
                    }
                }
            }
    
            minimizedAFD.put(lowestState, newTransitions);
        }
    
        return minimizedAFD;
    }           

    public AFDminimizado(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afd) {
        this.afd = afd;
    }

    public void minimize() {
        init(totalSet);
        int count = 0;
        while (true) {
            System.out.println("Iteración: " + count); // Agregar mensaje de registro
            if (count == totalSet.size())
                break;
            else
                count = 0;
            Set<Set<EstadosAFN>> copyOfTotalSet = new HashSet<>(totalSet);
            for (Set<EstadosAFN> set : copyOfTotalSet) {
                if (isIndivisible(set)) {
                    count++;
                    continue;
                } else {
                    System.out.println("Minimizando: " + set); // Agregar mensaje de registro
                    minimize(set);
                }
            }
        }
    }    

    private void init(Set<Set<EstadosAFN>> totalSet) {
        Set<EstadosAFN> terminal = new HashSet<>();
        Set<EstadosAFN> nonTerminal = new HashSet<>();

        for (EstadosAFN state : afd.keySet()) {
            if (isAceptacion(state)) {
                terminal.add(state);
            } else {
                nonTerminal.add(state);
            }
        }
        if (!nonTerminal.isEmpty()) {
            totalSet.add(nonTerminal);
        }
        totalSet.add(terminal);
    }

    private boolean isEndState(EstadosAFN state) {
        return isAceptacion(state);
    }

    private boolean isIndivisible(Set<EstadosAFN> set) {
        if (set.size() == 1)
            return true;
        else {
            EstadosAFN state = set.iterator().next();
            Map<Character, Set<EstadosAFN>> stateTransitions = afd.get(state);
            if (stateTransitions == null) {
                return true; // Si no hay transiciones para el estado, lo consideramos indivisible
            }
            for (Character input : stateTransitions.keySet()) {
                Set<EstadosAFN> temp = new HashSet<>();
                for (EstadosAFN s : set) {
                    Set<EstadosAFN> destination = afd.get(s).get(input);
                    if (destination != null && !destination.isEmpty()) {
                        temp.addAll(destination);
                    }
                }
                if (inTotalSet(temp))
                    continue;
                else {
                    return false;
                }
            }
        }
        return true;
    }    

    private boolean inTotalSet(Set<EstadosAFN> temp) {
        if (temp.isEmpty())
            return true;
        Set<Integer> indexs = new HashSet<>();
        for (EstadosAFN state : temp) {
            indexs.add(getSetNumber(state));
        }
        return indexs.size() == 1;
    }

    private int getSetNumber(EstadosAFN state) {
        int i = 0;
        for (Set<EstadosAFN> a : totalSet) {
            for (EstadosAFN b : a) {
                if (b.equals(state))
                    return i;
            }
            i++;
        }
        return -1;
    }

    private void minimize(Set<EstadosAFN> set) {
        System.out.println("Minimizando subconjunto: " + set); // Agregar mensaje de registro
        Map<String, Set<EstadosAFN>> partitions = new HashMap<>();
    
        for (Character input : afd.get(set.iterator().next()).keySet()) {
            for (EstadosAFN state : set) {
                Set<EstadosAFN> destination = afd.get(state).get(input);
    
                if (destination != null) {
                    String partitionKey = "";
                    for (Set<EstadosAFN> subset : totalSet) {
                        if (belongsToSameSet(destination, subset)) {
                            partitionKey = subset.toString();
                            break;
                        }
                    }
    
                    if (!partitions.containsKey(partitionKey)) {
                        partitions.put(partitionKey, new HashSet<>());
                    }
                    partitions.get(partitionKey).add(state);
                }
            }
        }
    
        // Verifica si hay estados duplicados en las particiones
        Map<EstadosAFN, Integer> stateCounts = new HashMap<>();
        for (Set<EstadosAFN> partition : partitions.values()) {
            for (EstadosAFN state : partition) {
                stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
            }
        }
    
        for (Map.Entry<EstadosAFN, Integer> entry : stateCounts.entrySet()) {
            if (entry.getValue() > 1) {
                // Si hay un estado duplicado, elimínalo de la partición que sea idéntica a 'set'
                partitions.get(set.toString()).remove(entry.getKey());
            }
        }
    
        if (partitions.size() > 1) {
            System.out.println("Particiones encontradas: " + partitions); // Agregar mensaje de registro
            totalSet.remove(set);
            Set<EstadosAFN> remainingStates = new HashSet<>(set);
            for (Set<EstadosAFN> newSet : partitions.values()) {
                totalSet.add(newSet);
                remainingStates.removeAll(newSet);
            }
            if (!remainingStates.isEmpty()) {
                totalSet.add(remainingStates);
            }
        }
    }                

    private boolean belongsToSameSet(Set<EstadosAFN> destination, Set<EstadosAFN> subset) {
        for (EstadosAFN state : destination) {
            if (!subset.contains(state)) {
                return false;
            }
        }
        return true;
    }

    public boolean simulate(String input) {
        if (afd == null || afd.isEmpty()) {
            return false;
        }

        EstadosAFN currentState = getInitialState(afd);

        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);

            if (!afd.containsKey(currentState)) {
                return false;
            }

            Map<Character, Set<EstadosAFN>> transitions = afd.get(currentState);
            Set<EstadosAFN> nextStateSet = transitions.get(symbol);
            if (nextStateSet == null || nextStateSet.isEmpty()) {
                return false;
            }
            currentState = nextStateSet.iterator().next();
        }

        return currentState.getIdentificador() == TipoGrafo.FINAL;
    }

    private EstadosAFN getInitialState(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afd) {
        EstadosAFN finalInitialState = null;

        for (EstadosAFN state : afd.keySet()) {
            if (state.getIdentificador() == TipoGrafo.INICIAL) {
                return state;
            } else if (state.getIdentificador() == TipoGrafo.FINAL) {
                Map<Character, Set<EstadosAFN>> transitions = afd.get(state);
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

    private boolean isAceptacion(EstadosAFN state) {
        return state.getIdentificador() == TipoGrafo.FINAL;
    }
}
