package arem.Algoritmos.AFN2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Transiciones2 {
    private Map<Estados2, Map<Character, Set<Estados2>>> transicion;

    public Map<Estados2, Map<Character, Set<Estados2>>> getTransicion() {
        return transicion;
    }

    public Transiciones2(Character c, Estados2 entrada, Estados2 salida) {
        transicion = new HashMap<>();
        transicion.put(entrada, Collections.singletonMap(c, Collections.singleton(salida)));
    }

    public Transiciones2(Character c, Estados2 entrada, Set<Estados2> salida){
        transicion = new HashMap<>();
        transicion.put(entrada, Collections.singletonMap(c, salida));
    }
    
}
