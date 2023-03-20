package arem.Algoritmos.AFNtoAFD;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFN2.Estados2;

public class TransicionesAFD {
    private Map<Character, Set<EstadosAFN>> Transicion;

    public Map<Character, Set<EstadosAFN>> getTransicion() {
        return Transicion;
    }

    public TransicionesAFD( Character simbolo, Map<EstadosAFN, Set<Estados2>> subConjuntos){
        Transicion = new HashMap<>();
        Set<EstadosAFN> nodos = new HashSet<>();
        for (Map.Entry<EstadosAFN, Set<Estados2>> entry: subConjuntos.entrySet()){
            nodos.add(entry.getKey());
        }
        Transicion.put(simbolo, nodos);
    }
}
