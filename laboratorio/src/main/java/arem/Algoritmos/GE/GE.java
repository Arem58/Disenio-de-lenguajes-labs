package arem.Algoritmos.GE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.IGE;
import arem.Algoritmos.interfaces.estados;

public class GE<T extends estados> implements IGE<T> {
    private T entrada;

    private T salida;

    private Map<T, Map<Character, Set<T>>> transitions = new HashMap<>();

    private Set<T> listaEstados;

    public void setListaEstados(T listaEstados) {
        if (!this.listaEstados.contains(listaEstados))
            this.listaEstados.add(listaEstados);
    }

    public Set<T> getListaEstados() {
        return listaEstados;
    }

    public T getEntrada() {
        return entrada;
    }

    public T getSalida() {
        return salida;
    }

    public Map<T, Map<Character, Set<T>>> getTransitions() {
        return transitions;
    }

    public GE(T entrada, T salida, Map<T, Map<Character, Set<T>>> transitions, Set<T> estados) {
        this.entrada = entrada;
        this.salida = salida;
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new HashSet<>(estados);
    }

    public GE(Map<T, Map<Character, Set<T>>> transitions, Set<T> estados) {
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new HashSet<>(estados);
    }

    public GE(Map<T, Map<Character, Set<T>>> transitions) {
        this.transitions = new HashMap<>(transitions);
        this.listaEstados = new HashSet<>(transitions.keySet());
    }

    public void setEntradaSalida() {
        entrada.setIdentificador(TipoGrafo.INICIAL);
        salida.setIdentificador(TipoGrafo.FINAL);
    }
}
