package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class LR0 {
    private Map<EstadoGAS, Map<String, Set<EstadoGAS>>> automata;
    private List<EstadoGAS> listaEstados;
    private List<Produccion> productions;
    public static int idGAS;

    public LR0(List<Produccion> productions) {
        this.productions = productions;
        automata = new HashMap<>();
        listaEstados = new ArrayList<>();
        idGAS = 0;
        ContruccionLR0(estadoInicial());
    }

    private void ContruccionLR0(EstadoGAS estadoInicial) {
        Queue<EstadoGAS> cola = new LinkedList<>();
        cola.offer(estadoInicial);
        while (!cola.isEmpty()) {
            EstadoGAS actual = cola.poll();
            boolean esCabezal = true;
            List<Produccion> nuevoCuerpo = new ArrayList<>();
            while (true) {
                if (esCabezal) {
                    for (Produccion produccion : actual.getCabezal()) {
                        if (!actual.getNoTerminales().contains(produccion.getSimbolo())) {
                            nuevoCuerpo.addAll(Closure(produccion.getSimbolo()));
                        }
                    }
                    if (nuevoCuerpo.isEmpty())
                        break;
                    esCabezal = false;
                }
                List<Produccion> temp = nuevoCuerpo;
                if (!esCabezal) {
                    for (Produccion produccion : nuevoCuerpo) {
                        if (!actual.getNoTerminales().contains(produccion.getSimbolo())) {
                            nuevoCuerpo.addAll(Closure(produccion.getSimbolo()));
                        }
                    }
                    if (temp.equals(nuevoCuerpo))
                        break;

                    actual.addCuerpo(nuevoCuerpo);
                    nuevoCuerpo.clear();
                }
            }
        }
    }

    private EstadoGAS estadoInicial() {
        // Introducimos la produccion inical
        Produccion inicial = productions.get(0);
        Produccion inicialGramatica = new Produccion("S'", Collections.singletonList(inicial.getNoTerminal()));
        EstadoGAS estadoInicial = new EstadoGAS(Collections.singletonList(inicialGramatica));
        automata.put(estadoInicial, new HashMap<>());
        return estadoInicial;
    }

    private List<Produccion> Closure(String Simbolo) {
        List<Produccion> producciones = new ArrayList<>();
        for (Produccion produccion : productions) {
            String noTerminal = produccion.getNoTerminal();
            if (noTerminal.equals(Simbolo)) {
                producciones.add(produccion);
            }
        }
        return producciones;
    }
}
