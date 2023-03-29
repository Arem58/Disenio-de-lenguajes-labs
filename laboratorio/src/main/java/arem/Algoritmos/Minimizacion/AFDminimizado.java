package arem.Algoritmos.Minimizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Funciones.Lenguaje;

public class AFDminimizado {

    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afd;
    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afdMini;

    public Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> getAfdMini() {
        return afdMini;
    }

    public AFDminimizado(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> afd) {
        this.afd = afd;
        minimizeAFD();
    }

    public void minimizeAFD() {
        List<Set<EstadosAFN>> partitions = initializePartitions();
        Queue<Set<EstadosAFN>> queue = new LinkedList<>(partitions);
        Set<Character> alphabet = new HashSet<>(Lenguaje.lenguajeInicial);

        while (!queue.isEmpty()) {
            Set<EstadosAFN> partition = queue.poll();

            for (Character symbol : alphabet) {
                Map<Set<EstadosAFN>, Set<EstadosAFN>> affectedPartitions = new HashMap<>();

                for (EstadosAFN state : partition) {
                    Map<Character, Set<EstadosAFN>> transitions = afd.get(state);
                    Set<EstadosAFN> targetStates = transitions.get(symbol);

                    if (targetStates != null) {
                        for (EstadosAFN targetState : targetStates) {
                            Set<EstadosAFN> affectedPartition = findPartition(targetState, partitions);
                            affectedPartitions.computeIfAbsent(affectedPartition, k -> new HashSet<>()).add(targetState);
                        }
                    }
                }

                for (Set<EstadosAFN> affectedPartition : affectedPartitions.keySet()) {
                    if (affectedPartition.size() > 1) {
                        Set<EstadosAFN> intersection = affectedPartitions.get(affectedPartition);
                        Set<EstadosAFN> difference = new HashSet<>(affectedPartition);
                        difference.removeAll(intersection);

                        partitions.remove(affectedPartition);
                        partitions.add(intersection);
                        partitions.add(difference);

                        if (queue.contains(affectedPartition)) {
                            queue.remove(affectedPartition);
                            queue.add(intersection);
                            queue.add(difference);
                        } else {
                            if (intersection.size() <= difference.size()) {
                                queue.add(intersection);
                            } else {
                                queue.add(difference);
                            }
                        }
                    }
                }
            }
        }

        buildMinimizedAFD(partitions);
    }

    private Set<EstadosAFN> findPartition(EstadosAFN state, List<Set<EstadosAFN>> partitions) {
        for (Set<EstadosAFN> partition : partitions) {
            if (partition.contains(state)) {
                return partition;
            }
        }
        return null;
    }

    private List<Set<EstadosAFN>> initializePartitions() {
        Set<EstadosAFN> finalStates = new HashSet<>();
        Set<EstadosAFN> nonFinalStates = new HashSet<>();

        for (EstadosAFN state : afd.keySet()) {
            if (state.getIdentificador() == TipoGrafo.FINAL) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }

        List<Set<EstadosAFN>> partitions = new ArrayList<>();
        partitions.add(finalStates);
        partitions.add(nonFinalStates);
        return partitions;
    }

    private void split(Set<EstadosAFN> partition, Set<Character> alphabet, List<Set<EstadosAFN>> newPartitions,
            List<Set<EstadosAFN>> partitions) {
        Map<String, Set<EstadosAFN>> groups = new HashMap<>();

        for (EstadosAFN state : partition) {
            Map<Character, Set<EstadosAFN>> transitions = afd.get(state);

            StringBuilder groupKeyBuilder = new StringBuilder();
            for (Character symbol : alphabet) {
                Set<EstadosAFN> targetStates = transitions.get(symbol);
                if (targetStates != null) {
                    for (EstadosAFN targetState : targetStates) {
                        groupKeyBuilder.append(
                                findPartitionIndex(targetState, newPartitions.isEmpty() ? partitions : newPartitions));
                        groupKeyBuilder.append(",");
                    }
                } else {
                    groupKeyBuilder.append("_,");
                }
            }

            String groupKey = groupKeyBuilder.toString();
            groups.computeIfAbsent(groupKey, k -> new HashSet<>()).add(state);
        }

        newPartitions.addAll(groups.values());
    }

    private int findPartitionIndex(EstadosAFN state, List<Set<EstadosAFN>> partitions) {
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).contains(state)) {
                return i;
            }
        }
        return -1;
    }

    private void buildMinimizedAFD(List<Set<EstadosAFN>> partitions) {
        Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> minimizedAFD = new HashMap<>();
        Map<EstadosAFN, EstadosAFN> stateMapping = new HashMap<>();

        for (Set<EstadosAFN> partition : partitions) {
            EstadosAFN representativeState = partition.iterator().next();
            for (EstadosAFN state : partition) {
                stateMapping.put(state, representativeState);
            }
        }

        for (Map.Entry<EstadosAFN, Map<Character, Set<EstadosAFN>>> entry : afd.entrySet()) {
            EstadosAFN originalState = entry.getKey();
            EstadosAFN representativeState = stateMapping.get(originalState);
            if (!minimizedAFD.containsKey(representativeState)) {
                Map<Character, Set<EstadosAFN>> newTransitions = new HashMap<>();
                for (Map.Entry<Character, Set<EstadosAFN>> transition : entry.getValue().entrySet()) {
                    Set<EstadosAFN> newTargetStates = new HashSet<>();
                    for (EstadosAFN targetState : transition.getValue()) {
                        newTargetStates.add(stateMapping.get(targetState));
                    }
                    newTransitions.put(transition.getKey(), newTargetStates);
                }
                minimizedAFD.put(representativeState, newTransitions);
            }
        }

        afdMini = minimizedAFD;
    }

    public boolean simulate(String input) {
        if (afdMini == null || afdMini.isEmpty()) {
            return false;
        }
    
        EstadosAFN currentState = getInitialState(afdMini);
    
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);
            
            if (!afdMini.containsKey(currentState)) {
                return false;
            }
            
            Map<Character, Set<EstadosAFN>> transitions = afdMini.get(currentState);
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

}
