package arem.Algoritmos.interfaces;

import java.util.Map;
import java.util.Set;

public interface IGE<T extends estados> {
    Set<T> getListaEstados();
    Map<T, Map<Character, Set<T>>> getTransitions();
}