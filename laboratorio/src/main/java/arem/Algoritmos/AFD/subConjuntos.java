package arem.Algoritmos.AFD;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.ILenguaje;

public class subConjuntos {
    private Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS;

    private Map<String, EstadosAFD> getAfdNodeData;
    private Set<String> acceptingStates;
    private Map<EstadosAFN, Set<Integer>> subConjuntos;
    private Set<EstadosAFN> listaEstados;

    private ILenguaje lenguaje;

    public Set<EstadosAFN> getListaEstados() {
        return listaEstados;
    }

    public Map<EstadosAFN, Map<String, Set<EstadosAFN>>> getTablaS() {
        return tablaS;
    }

    public subConjuntos(Map<String, EstadosAFD> getAfdNodeData, nodo root, Set<String> acceptingStates,
            ILenguaje lenguaje) {
        this.lenguaje = lenguaje;
        this.getAfdNodeData = getAfdNodeData;
        this.acceptingStates = acceptingStates;
        tablaS = new HashMap<>();
        subConjuntos = new HashMap<>();
        listaEstados = new HashSet<>();
        setTabla(root);
    }

    private void setTabla(nodo root) {
        Queue<Entry<EstadosAFN, Set<Integer>>> cola = new LinkedList<>();
        EstadosAFD inicial = getAfdNodeData.get(root.getIdentifier());
        EstadosAFN nuevoEstadoAFN = new EstadosAFN();
        subConjuntos.put(nuevoEstadoAFN, inicial.getFirstpos());

        cola.offer(new SimpleImmutableEntry<>(nuevoEstadoAFN, inicial.getFirstpos()));

        while (!cola.isEmpty()) {
            Entry<EstadosAFN, Set<Integer>> entry = cola.poll();
            EstadosAFN actual = entry.getKey();
            Set<Integer> Conjunto = entry.getValue();
            listaEstados.add(actual);
            for (Object elemento : lenguaje.getLenguajeInicial()) {
                String caracter = "";
                if (elemento instanceof Character) {
                    caracter = elemento.toString();
                } else if (elemento instanceof String) {
                    caracter = (String) elemento;
                }
                lenguajeHandler(caracter, Conjunto, actual, cola);
            }
        }
        setFinals();
    }

    private void lenguajeHandler(String c, Set<Integer> Conjunto, EstadosAFN actual,
            Queue<Entry<EstadosAFN, Set<Integer>>> cola) {
        Set<Integer> NuevoConjunto = new HashSet<>();
        for (Integer target : Conjunto) {
            EstadosAFD estadoActual = getAfdNodeData.get(target.toString());
            if (estadoActual.getSimbol().equals(c)) {
                NuevoConjunto.addAll(estadoActual.getFollowpos());
            }
        }

        if (!NuevoConjunto.isEmpty()) {
            EstadosAFN nuevoEstado = buscarSubconjunto(NuevoConjunto);
            if (nuevoEstado == null) {
                nuevoEstado = new EstadosAFN();
                subConjuntos.put(nuevoEstado, NuevoConjunto);
                cola.offer(new SimpleImmutableEntry<>(nuevoEstado, NuevoConjunto));
            }
            if (nuevoEstado != null) {
                Map<String, Set<EstadosAFN>> transiciones = tablaS.get(actual);
                if (transiciones == null) {
                    transiciones = new HashMap<>();
                    tablaS.put(actual, transiciones);
                }
                transiciones.put(c, Collections.singleton(nuevoEstado));
            }
        }
    }

    public void setFinals() {
        for (Map.Entry<EstadosAFN, Set<Integer>> entry : subConjuntos.entrySet()) {
            EstadosAFN estadosAFN = entry.getKey();
            Set<Integer> targes = entry.getValue();
            for (Integer target : targes) {
                EstadosAFD estadoActual = getAfdNodeData.get(target.toString());
                if (acceptingStates.contains(estadoActual.getSimbol())) {
                    estadosAFN.setIdentificador(TipoGrafo.FINAL);
                    estadosAFN.setValorAceptacion(estadoActual.getSimbol());
                }
            }
        }
    }

    private EstadosAFN buscarSubconjunto(Set<Integer> nuevoConjunto) {
        return subConjuntos.entrySet().stream()
                .filter(entry -> entry.getValue().equals(nuevoConjunto))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
