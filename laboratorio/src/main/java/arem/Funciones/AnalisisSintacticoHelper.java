package arem.Funciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.GAS.Produccion;

public class AnalisisSintacticoHelper {

    private List<Produccion> producciones;
    private Set<String> Tokens;

    private Map<String, Set<String>> first;

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    private Map<String, Set<String>> follow;

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    public AnalisisSintacticoHelper(List<Produccion> produccion, Set<String> Tokens) {
        this.producciones = produccion;
        this.Tokens = Tokens;
        first = new HashMap<>();
        follow = new HashMap<>();
        computandoFirst();
        computandoFollow();
        printMap(first, "FIRST");
        printMap(follow, "FOLLOW");
    }

    private void computandoFirst() {
        Set<Integer> index = new HashSet<>();
        for (Produccion item : producciones) {
            String noTerminal = item.getNoTerminal();
            calcularFirst(noTerminal, index);
        }
    }

    private Set<String> calcularFirst(String noTerminal, Set<Integer> index) {
        if (first.containsKey(noTerminal)) {
            return first.get(noTerminal);
        }

        Set<String> firstSet = new HashSet<>();
        for (Produccion produccion : producciones) {
            if (produccion.getNoTerminal().equals(noTerminal) && !index.contains(producciones.indexOf(produccion))) {
                List<String> finalProduccion = produccion.getFinalProduccion();
                String primerSimbolo = finalProduccion.get(0);

                if (Tokens.contains(primerSimbolo)) {
                    firstSet.add(primerSimbolo);
                } else {
                    index.add(producciones.indexOf(produccion));
                    firstSet.addAll(calcularFirst(primerSimbolo, index));
                }
            }
        }
        first.put(noTerminal, firstSet);
        return firstSet;
    }

    public void computandoFollow() {
        for (Produccion produccion : producciones) {
            follow.put(produccion.getNoTerminal(), new HashSet<>());
        }

        // Agrego el símbolo $
        follow.get(producciones.get(0).getNoTerminal()).add("$");

        boolean cambio;
        do {
            cambio = false;
            for (Produccion produccion : producciones) {
                String noTerminal = produccion.getNoTerminal();
                List<String> finalProduccion = new ArrayList<>(produccion.getFinalProduccion());

                for (int i = 0; i < finalProduccion.size(); i++) {
                    String symbol = finalProduccion.get(i);

                    if (!Tokens.contains(symbol)) {
                        Set<String> setFollow = follow.get(symbol);
                        Set<String> previousFollow = new HashSet<>(setFollow);

                        if (i + 1 < finalProduccion.size()) {
                            String nextSymbol = finalProduccion.get(i + 1);

                            if (Tokens.contains(nextSymbol)) {
                                setFollow.add(nextSymbol);
                            } else {
                                Set<String> firstSet = first.get(nextSymbol);
                                setFollow.addAll(firstSet);

                                if (firstSet.contains("ε")) {
                                    setFollow.remove("ε");
                                    setFollow.addAll(follow.get(nextSymbol));
                                }
                            }
                        } else {
                            setFollow.addAll(follow.get(noTerminal));
                        }

                        // Verifica si hubo cambios en este conjunto follow
                        if (!previousFollow.equals(setFollow)) {
                            cambio = true;
                        }
                    }
                }
            }
        } while (cambio); // Repetir hasta que los conjuntos FOLLOW no cambien
    }

    public void printMap(Map<String, Set<String>> map, String setName) {
        System.out.println(setName + ":");
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            System.out.print(setName + "(" + entry.getKey() + ") = { ");
            Set<String> set = entry.getValue();
            String setElements = String.join(", ", set);
            System.out.println(setElements + " }");
        }
        System.out.println();
    }
}
