package arem.Funciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
        follow = new LinkedHashMap<>();
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

        // Agrego el simbolo $
        follow.get(producciones.get(0).getNoTerminal()).add("$");

        boolean cambio;
        cambio = false;
        int cambios = 0;
        for (int proceso = 0; proceso < 2; proceso++) {
            for (Produccion produccion : producciones) {
                String noTerminal = produccion.getNoTerminal();
                List<String> finalProduccion = new ArrayList<>(produccion.getFinalProduccion());

                if (finalProduccion.size() == 1) {
                    finalProduccion.add(0, "ε");
                }

                Set<String> setFollow = new HashSet<>();
                Set<String> previousFollow = new HashSet<>();

                if (proceso == 0 && finalProduccion.size() > 2) {
                    if (Tokens.contains(finalProduccion.get(1))) {
                        finalProduccion.add(0, "ε");
                        if (Tokens.contains(finalProduccion.get(1))) {
                            continue;
                        }
                    }
                    boolean casoEspecial = false;
                    if (noTerminal.equals(finalProduccion.get(1))) {
                        // nextProduction(finalProduccion, noTerminal, setFollow, 1);
                        casoEspecial = true;
                    }
                    setFollow = follow.get(finalProduccion.get(1));
                    previousFollow.addAll(setFollow);
                    for (int i = 2; i < finalProduccion.size(); i++) {
                        if (Tokens.contains(finalProduccion.get(i))) {
                            setFollow.add(finalProduccion.get(i));
                        } else {
                            // if (finalProduccion.get(i).equals(noTerminal)) {
                            //     nextProduction(finalProduccion, noTerminal, setFollow, i);
                            // } else 
                            if (casoEspecial) {
                                setFollow.addAll(follow.get(finalProduccion.get(i)));
                            } else {
                                Set<String> firstSet = first.get(finalProduccion.get(i));
                                setFollow.addAll(firstSet);
                            }
                        }
                    }
                }
                if (proceso == 1 && finalProduccion.size() <= 2) {
                    if (Tokens.contains(finalProduccion.get(1))) {
                        continue;
                    }
                    setFollow = follow.get(finalProduccion.get(1));
                    previousFollow.addAll(setFollow);
                    setFollow.addAll(follow.get(noTerminal));
                }
            }
        }
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

    private void nextProduction(List<String> finalProduccion, String noTerminal, Set<String> setFollow, int i) {
        for (Produccion replaceProduccion : producciones) {
            boolean sonIguales;
            boolean remplazoA = false;
            boolean simboloCambiado = false;
            if (finalProduccion.get(0).equals("ε")) {
                sonIguales = finalProduccion.subList(1, finalProduccion.size())
                        .equals(replaceProduccion.getFinalProduccion());
                remplazoA = true;
            } else {
                sonIguales = finalProduccion.equals(replaceProduccion.getFinalProduccion());
                remplazoA = true;
            }

            if (!sonIguales && noTerminal.equals(replaceProduccion.getNoTerminal())) {
                finalProduccion.set(i, replaceProduccion.getFinalProduccion().get(0));
                simboloCambiado = true;
            }
            if (simboloCambiado) {
                if (finalProduccion.get(i).equals("ε")) {
                    Set<String> noTerminalFollow = follow.get(noTerminal);
                    setFollow.addAll(noTerminalFollow);
                    break;
                } else {
                    if (!remplazoA) {
                        Set<String> firstSet = first.get(finalProduccion.get(i));
                        setFollow.addAll(firstSet);
                    }
                    break;
                }
            }
        }
    }
}
