package arem.Algoritmos.GE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.interfaces.estados;

public class GEString<T extends estados>{
    private Map<T, Map<String, Set<T>>> transitions = new HashMap<>();
    private Set<T> listaEstados;

    public Map<T, Map<String, Set<T>>> getTransitions() {
        return transitions;
    }

    public Set<T> getListaEstados() {
        return listaEstados;
    }

    public GEString(T entrada, T salida, Map<T, Map<String, Set<T>>> transitions, Set<T> estados) {
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new HashSet<>(estados);
    }

    public GEString(Map<T, Map<String, Set<T>>> transitions, Set<T> estados) {
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new LinkedHashSet<>(estados);
    }
}