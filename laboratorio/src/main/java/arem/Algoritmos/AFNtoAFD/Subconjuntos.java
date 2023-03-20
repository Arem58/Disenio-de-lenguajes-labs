package arem.Algoritmos.AFNtoAFD;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
    
    public static char id;
    
    public Set<EstadosAFN> getListaEstados() {
        return listaEstados;
    }

    public Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> getTablaS() {
        return tablaS;
    }
    
    public Subconjuntos(Estados2 inicial, Estados2 finalN,Map<Estados2, Map<Character, Set<Estados2>>> tablaT) {
        tablaS = new HashMap<>();
        subConjuntos = new HashMap<>();
        listaEstados = new HashSet<>();
        this.inicial = inicial;
        this.finalN = finalN;
        this.tablaT = tablaT;
        id = 'A';
        setTabla();
    }

    private void setTabla() {
        Map<Character, Set<Estados2>> TransicionesNodo = tablaT.get(inicial);
        
        Deque<Set<Estados2>> pila = new LinkedList<>();
        Deque<EstadosAFN> pilaEstados = new LinkedList<>();
        boolean nuevosConjuntos = true;
        EstadosAFN nodoTemp = new EstadosAFN();
        subConjuntos.put(nodoTemp, TransicionesNodo.get('ε'));
        pilaEstados.offer(nodoTemp);
        pila.offer(TransicionesNodo.get('ε'));

        Map<Character, Boolean> conjuntoEncontrado = new HashMap<>();
        for (Character caracter : Lenguaje.lenguajeInicial) {
            conjuntoEncontrado.put(caracter, false);
        }

        while (nuevosConjuntos || !pila.isEmpty() || !pilaEstados.isEmpty()) {
            Set<Estados2> Conjunto = pila.poll();
            EstadosAFN nodoInicial = pilaEstados.poll();
            for (Character caracter : Lenguaje.lenguajeInicial) {
                Map<EstadosAFN, Set<Estados2>> conjuntos = new HashMap<>();
                Set<Estados2> NuevoConjunto = new HashSet<>();

                nuevosConjuntos = true;
                for (Estados2 target : Conjunto) {
                    Map<Character, Set<Estados2>> conjunto = tablaT.get(target);
                    if (conjunto.containsKey(caracter)) {
                        Set<Estados2> nodo = conjunto.get(caracter);
                        Estados2 nodoActual = nodo.iterator().next();
                        NuevoConjunto.addAll(tablaT.get(nodoActual).get('ε'));
                    }
                }
                EstadosAFN newEstado = null;
                if (!subConjuntos.isEmpty()) {
                    for (Map.Entry<EstadosAFN, Set<Estados2>> entry : subConjuntos.entrySet()) {
                        Set<Estados2> viejosConjuntos = entry.getValue();
                        if (NuevoConjunto.equals(viejosConjuntos)) {
                            nuevosConjuntos = false;
                            newEstado = entry.getKey();
                            break;
                        }
                    }
                }
                if (nuevosConjuntos) {
                    newEstado = new EstadosAFN();
                    pila.offer(NuevoConjunto);
                    pilaEstados.offer(newEstado);
                    subConjuntos.put(newEstado, NuevoConjunto);
                    nuevosConjuntos = true;
                }
                conjuntos.put(newEstado, NuevoConjunto);
                TransicionesAFD transicionesAFD = new TransicionesAFD(caracter, conjuntos);
                tablaS.computeIfAbsent(nodoInicial, k -> new HashMap<>()).putAll(transicionesAFD.getTransicion());
                conjuntoEncontrado.put(caracter, nuevosConjuntos);
            }
            nuevosConjuntos = false;
            for (Boolean encontrado : conjuntoEncontrado.values()) {
                if (encontrado) {
                    nuevosConjuntos = true;
                    break;
                }
            }
            // Conjunto = pila.poll();
            // nodoInicial = pilaEstados.poll();
        }
        setFinals();
        setLIsta();
    }

    public void setLIsta(){
        listaEstados.addAll(tablaS.keySet());
    }

    public void setFinals(){
        for(Map.Entry<EstadosAFN, Set<Estados2>> entry: subConjuntos.entrySet()){
            EstadosAFN estadosAFN = entry.getKey();
            Set<Estados2> targes = entry.getValue();
            for (Estados2 target: targes){
                if(target.equals(finalN)){
                    estadosAFN.setIdentificador(TipoGrafo.FINAL);
                }
            }
        }
    }
}
