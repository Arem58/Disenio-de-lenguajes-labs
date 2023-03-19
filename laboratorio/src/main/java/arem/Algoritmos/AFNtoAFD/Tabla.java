package arem.Algoritmos.AFNtoAFD;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.AFN2.GE;

public class Tabla {
    private Map<Estados2, Map<Character, Set<Estados2>>> tabla;
    private GE ge;

    public Map<Estados2, Map<Character, Set<Estados2>>> getTabla() {
        return tabla;
    }

    public Tabla(GE ge) {
        this.ge = ge;
        tabla = new HashMap<>();
        Set<Estados2> visitados = new HashSet<>();
        getTablaT(ge.getTransitions(), ge.getEntrada(), visitados);
    }

    private void getTablaT(Map<Estados2, Map<Character, Set<Estados2>>> transiciones, Estados2 nodo,
            Set<Estados2> visitados) {
        if (visitados.contains(nodo)) {
            return; // Evitar ciclos
        }
        visitados.add(nodo);
        Map<Character, Set<Estados2>> TransicionesNodo = transiciones.get(nodo);
        Map<Character, Set<Estados2>> nuevasTransiciones = new HashMap<>();
        // Agregar nodo actual a transiciones epsilon
        Set<Estados2> epsilonTargets = new HashSet<>();
        epsilonTargets.add(nodo);
        if (TransicionesNodo != null) {
            for (Map.Entry<Character, Set<Estados2>> entry : TransicionesNodo.entrySet()) {
                Character simbolo = entry.getKey();
                Set<Estados2> targets = entry.getValue();
                Set<Estados2> newTargets = new HashSet<>();
                for (Estados2 target : targets) {
                    if (!visitados.contains(target)) {
                        getTablaT(transiciones, target, visitados);
                    }
                    newTargets.add(target);
                }
                nuevasTransiciones.put(simbolo, newTargets);
            }
        }
        addEpsilon(transiciones, nodo, new HashSet<>(), epsilonTargets);
        nuevasTransiciones.computeIfAbsent('ε', k -> new HashSet<>()).addAll(epsilonTargets);
        tabla.put(nodo, nuevasTransiciones);
    }

    private void addEpsilon(Map<Estados2, Map<Character, Set<Estados2>>> transiciones, Estados2 nodo,
            Set<Estados2> visitados, Set<Estados2> epsilonTargets) {
        if (visitados.contains(nodo)) {
            return; // Evitar ciclos
        }
        visitados.add(nodo);
        Map<Character, Set<Estados2>> TransicionesNodo = transiciones.get(nodo);
        if (TransicionesNodo != null) {
            Set<Estados2> targets = TransicionesNodo.getOrDefault('ε', Collections.emptySet());
            for (Estados2 target : targets) {
                if (!visitados.contains(target)) {
                    epsilonTargets.add(target);
                    addEpsilon(transiciones, target, visitados, epsilonTargets);
                }
            }
        }
    }
}
