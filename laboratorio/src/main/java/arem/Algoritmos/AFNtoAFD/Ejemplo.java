package arem.Algoritmos.AFNtoAFD;

public class Ejemplo {
    public static void dfs(Map<Integer, Map<Character, Set<Integer>>> transitions, int node, int newId, Set<Integer> visited) {
        visited.add(node);
        Map<Character, Set<Integer>> nodeTransitions = transitions.get(node);
        Map<Character, Set<Integer>> newTransitions = new HashMap<>();
        for (Map.Entry<Character, Set<Integer>> entry : nodeTransitions.entrySet()) {
            Character symbol = entry.getKey();
            Set<Integer> targets = entry.getValue();
            Set<Integer> newTargets = new HashSet<>();
            for (int target : targets) {
                if (!visited.contains(target)) {
                    dfs(transitions, target, newId + 1, visited);
                }
                Set<Integer> epsilonTargets = new HashSet<>();
                addEpsilonTargets(transitions, target, visited, epsilonTargets);
                newTargets.addAll(epsilonTargets);
            }
            newTransitions.put(symbol, newTargets);
        }
        transitions.put(newId, newTransitions);
        transitions.remove(node);
    }
    
    public static void addEpsilonTargets(Map<Integer, Map<Character, Set<Integer>>> transitions, int node, Set<Integer> visited, Set<Integer> epsilonTargets) {
        visited.add(node);
        Map<Character, Set<Integer>> nodeTransitions = transitions.get(node);
        Set<Integer> targets = nodeTransitions.getOrDefault('ε', Collections.emptySet());
        for (int target : targets) {
            if (!visited.contains(target)) {
                dfs(transitions, target, node, visited);
            }
            epsilonTargets.add(target);
            addEpsilonTargets(transitions, target, visited, epsilonTargets);
        }
    }
    void ejemplo(){
        for (Estados2 nodo : nodos) {
            Map<Character, Set<Estados2>> transiciones = new HashMap<>();
            for (Character simbolo : alfabeto) {
                transiciones.put(simbolo, new HashSet<>());
            }
            tablaTransiciones.put(nodo, transiciones);
        }
        
        for (Map.Entry<Estados2, Map<Character, Set<Estados2>>> entry : tablaTransiciones.entrySet()) {
            Estados2 nodo = entry.getKey();
            Map<Character, Set<Estados2>> transiciones = entry.getValue();
        
            for (Character simbolo : transiciones.keySet()) {
                Set<Estados2> estadosDestino = transiciones.get(simbolo);
                if (nodo.getTransiciones().containsKey(simbolo)) {
                    estadosDestino.addAll(nodo.getTransiciones().get(simbolo));
                }
                if (estadosDestino.isEmpty() && simbolo != 'ε') { // Manejar el caso del último nodo
                    estadosDestino.add(nodo); // Agregar una transición a sí mismo
                }
                transiciones.put(simbolo, estadosDestino);
            }
        
            Set<Estados2> visitados = new HashSet<>();
            Set<Estados2> epsilonTargets = new HashSet<>();
            addEpsilon(nodo, visitados, epsilonTargets);
            for (Estados2 estado : epsilonTargets) {
                if (!estado.equals(nodo)) {
                    for (Character simbolo : alfabeto) {
                        Set<Estados2> estadosDestino = transiciones.get(simbolo);
                        if (estado.getTransiciones().containsKey(simbolo)) {
                            estadosDestino.addAll(estado.getTransiciones().get(simbolo));
                        }
                        transiciones.put(simbolo, estadosDestino);
                    }
                }
            }
        }
    }
}
