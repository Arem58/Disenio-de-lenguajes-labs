package arem.Algoritmos.AFN2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GE {
    private Estados2 entrada; 
    
    private Estados2 salida;
    
    private Map<Estados2, Map<Character, Set<Estados2>>> transitions = new HashMap<>();
    
    private Set<Estados2> listaEstados;

    public void setListaEstados(Estados2 listaEstados) {
        if (!this.listaEstados.contains(listaEstados))
            this.listaEstados.add(listaEstados);
    }

    public Set<Estados2> getListaEstados() {
        return listaEstados;
    }

    public Estados2 getEntrada() {
        return entrada;
    }
    
    public Estados2 getSalida() {
        return salida;
    }
    
    public Map<Estados2, Map<Character, Set<Estados2>>> getTransitions() {
        return transitions;
    }
    
    public GE(Estados2 entrada, Estados2 salida, Map<Estados2, Map<Character, Set<Estados2>>> transitions, Set<Estados2> estados) {
        this.entrada = entrada;
        this.salida = salida;
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new HashSet<>(estados);
    }
}
