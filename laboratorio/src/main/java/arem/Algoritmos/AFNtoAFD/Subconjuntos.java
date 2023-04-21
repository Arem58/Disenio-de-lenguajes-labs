package arem.Algoritmos.AFNtoAFD;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Funciones.Lenguaje;

public class Subconjuntos {
    private Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS;
    private Map<EstadosAFN, Set<Estados2>> subConjuntos;
    private Set<EstadosAFN> listaEstados;

    private Map<Estados2, Map<Character, Set<Estados2>>> tablaT;
    private Estados2 inicial;
    private Estados2 finalN;

    public static int id;

    public Set<EstadosAFN> getListaEstados() {
        return listaEstados;
    }

    public Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> getTablaS() {
        return tablaS;
    }

    public Subconjuntos(Estados2 inicial, Estados2 finalN, Map<Estados2, Map<Character, Set<Estados2>>> tablaT) {
        tablaS = new HashMap<>();
        subConjuntos = new HashMap<>();
        listaEstados = new HashSet<>();
        this.inicial = inicial;
        this.finalN = finalN;
        this.tablaT = tablaT;
        id = 0;
        setTabla();
    }

    private void setTabla() {
        Deque<Set<Estados2>> pila = new LinkedList<>();
        Deque<EstadosAFN> pilaEstados = new LinkedList<>();
        Set<Estados2> conjuntoInicial = cerraduraEpsilon(Collections.singleton(inicial));

        EstadosAFN nuevoEstadoAFN = new EstadosAFN();
        subConjuntos.put(nuevoEstadoAFN, conjuntoInicial);
        pila.offer(conjuntoInicial);
        pilaEstados.offer(nuevoEstadoAFN);

        while (!pila.isEmpty() || !pilaEstados.isEmpty()) {
            Set<Estados2> Conjunto = pila.poll();
            EstadosAFN actual = pilaEstados.poll();
            for (Character caracter : Lenguaje.lenguajeInicial) {
                if (caracter == 'ε') {
                    continue;
                }
                Set<Estados2> NuevoConjunto = new HashSet<>();
                for (Estados2 target : Conjunto) {
                    Map<Character, Set<Estados2>> conjunto = tablaT.get(target);
                    if (conjunto.containsKey(caracter)) {
                        Set<Estados2> nodo = conjunto.get(caracter);
                        Estados2 nodoActual = nodo.iterator().next();
                        NuevoConjunto.addAll(tablaT.get(nodoActual).get('ε'));
                    }
                }
                EstadosAFN nuevoEstado = buscarSubconjunto(NuevoConjunto);
                if (nuevoEstado == null && !todosSonEstadosInvalidos(NuevoConjunto)) {
                    nuevoEstado = new EstadosAFN();
                    subConjuntos.put(nuevoEstado, NuevoConjunto);
                    pilaEstados.offer(nuevoEstado);
                    pila.offer(NuevoConjunto);
                }
                if (nuevoEstado != null) {
                    tablaS.computeIfAbsent(actual, k -> new HashMap<>()).merge(caracter,
                            Collections.singleton(nuevoEstado),
                            (s1, s2) -> {
                                s1.addAll(s2);
                                return s1;
                            });
                }

            }
        }
        setFinals();
        setLIsta();
    }

    public void setLIsta() {
        listaEstados.addAll(subConjuntos.keySet());
        listaEstados.addAll(tablaS.keySet().stream()
                .filter(e -> !esInaccesible(e) && !todosSonEstadosInvalidos(subConjuntos.get(e))
                        && !subConjuntos.get(e).isEmpty())
                .collect(Collectors.toList()));

    }

    private boolean todosSonEstadosInvalidos(Set<Estados2> estados) {
        for (Estados2 estado : estados) {
            if (estado.getIdentificador() != TipoGrafo.INVALID) {
                return false;
            }
        }
        return true;
    }

    public void setFinals() {
        for (Map.Entry<EstadosAFN, Set<Estados2>> entry : subConjuntos.entrySet()) {
            EstadosAFN estadosAFN = entry.getKey();
            Set<Estados2> targes = entry.getValue();
            for (Estados2 target : targes) {
                if (target.equals(finalN)) {
                    estadosAFN.setIdentificador(TipoGrafo.FINAL);
                    estadosAFN.setValorAceptacion(target.getId());
                }
            }
        }
    }

    private EstadosAFN buscarSubconjunto(Set<Estados2> nuevoConjunto) {
        return subConjuntos.entrySet().stream()
                .filter(entry -> entry.getValue().equals(nuevoConjunto))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean esInaccesible(EstadosAFN estado) {
        for (Map<Character, Set<EstadosAFN>> transiciones : tablaS.values()) {
            for (Set<EstadosAFN> destinos : transiciones.values()) {
                if (destinos.contains(estado)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<Estados2> cerraduraEpsilon(Set<Estados2> estados) {
        Set<Estados2> cerradura = new HashSet<>(estados);
        Deque<Estados2> pila = new LinkedList<>(estados);
    
        while (!pila.isEmpty()) {
            Estados2 estado = pila.poll();
            Set<Estados2> transicionesEpsilon = tablaT.get(estado).getOrDefault('ε', Collections.emptySet());
            for (Estados2 transicionEpsilon : transicionesEpsilon) {
                if (cerradura.add(transicionEpsilon)) {
                    pila.push(transicionEpsilon);
                }
            }
        }
    
        return cerradura;
    }    
}
