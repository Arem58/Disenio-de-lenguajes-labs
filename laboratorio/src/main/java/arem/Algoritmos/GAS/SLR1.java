package arem.Algoritmos.GAS;

import java.util.Map;
import java.util.Set;

import org.fusesource.jansi.Ansi;

import arem.Algoritmos.enums.TipoGrafo;
import arem.Funciones.AnalisisSintacticoHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SLR1 {
    private Map<EstadoGAS, Map<String, Set<EstadoGAS>>> LR0;
    private Map<String, Map<String, String>> tablaSLR1;
    private Map<String, Set<String>> follow;
    private Map<Produccion, String> tablaReduce;

    private Set<EstadoGAS> listaEstados;
    private Set<String> tokensGAS;
    private List<Produccion> productions;

    public SLR1(Map<EstadoGAS, Map<String, Set<EstadoGAS>>> LR0, List<Produccion> productions,
            Set<EstadoGAS> listaEstados, Set<String> tokensGAS) {
        this.LR0 = LR0;
        this.productions = productions;
        this.listaEstados = listaEstados;
        this.tokensGAS = tokensGAS;
        tablaSLR1 = new LinkedHashMap<>();
        tablaReduce = new HashMap<>();
        AnalisisSintacticoHelper ANH = new AnalisisSintacticoHelper(productions, tokensGAS);
        follow = ANH.getFollow();
        ConstruccionSLR1();
    }

    private void ConstruccionSLR1() {
        ContruccionDeLaTabla();
        Reduce();
        imprimirTablaSLR1();
    }

    private void ContruccionDeLaTabla() {
        for (EstadoGAS estadoGAS : listaEstados) {
            Map<String, Set<EstadoGAS>> transiciones = LR0.get(estadoGAS);
            Map<String, String> contenido = new LinkedHashMap<>();
            if (transiciones != null) {
                for (Map.Entry<String, Set<EstadoGAS>> transicion : transiciones.entrySet()) {
                    String simbolo = transicion.getKey();
                    Set<EstadoGAS> estado = transicion.getValue();
                    EstadoGAS transicionEstado = estado.iterator().next();
                    String IDtransicion = transicionEstado.getId().substring(1);
                    if (tokensGAS.contains(simbolo)) {
                        contenido.put(simbolo, "S" + IDtransicion);
                    } else {
                        contenido.put(simbolo, IDtransicion);
                    }
                }
            }

            if (estadoGAS.getIdentificador().equals(TipoGrafo.FINAL)) {
                contenido.put("$", "ACCEPT");
            }

            String ID = estadoGAS.getId().substring(1);
            tablaSLR1.put(ID, contenido);
        }
    }

    private void ContruccionTablaReduce() {
        int id = 1;
        for (Produccion produccion : productions) {
            String reduce = "R" + id;
            tablaReduce.put(produccion, reduce);
            id++;
        }
    }

    private void Reduce() {
        ContruccionTablaReduce();
        for (EstadoGAS estadoGAS : listaEstados) {
            if (!estadoGAS.getIdentificador().equals(TipoGrafo.ACEPTACION))
                continue;
            Map<String, String> contenido = new LinkedHashMap<>();
            for (Produccion produccion : estadoGAS.getCabezal()) {
                for (Map.Entry<Produccion, String> produccionRedux : tablaReduce.entrySet()) {
                    String redux = produccionRedux.getValue();
                    Produccion actualProduccion = produccionRedux.getKey();
                    if (produccion.equalsProducciones(actualProduccion) && produccion.estadoAceptacion()) {
                        for (Map.Entry<String, Set<String>> followActual : follow.entrySet()) {
                            String noTerminal = followActual.getKey();
                            if (noTerminal.equals(produccion.getNoTerminal())) {
                                Set<String> listaSimbolos = followActual.getValue();
                                for (String simbolo : listaSimbolos) {
                                    contenido.put(simbolo, redux);
                                }
                            }
                        }
                    }
                }
            }
            String ID = estadoGAS.getId().substring(1);
            Map<String, String> mapaExistente = tablaSLR1.get(ID);
            if (mapaExistente == null) {
                tablaSLR1.put(ID, contenido);
            } else {
                for (Map.Entry<String, String> entry : contenido.entrySet()) {
                    String key = entry.getKey();
                    if (mapaExistente.containsKey(key)) {
                        System.err.println("Ya existe una entrada para la llave: " + key
                                + " en el estado para ID: " + estadoGAS.getId());
                        System.exit(1);
                    } else {
                        mapaExistente.put(key, entry.getValue());
                    }
                }
            }
        }
    }

    public void imprimirTablaSLR1() {
        for (Map.Entry<String, Map<String, String>> entry : tablaSLR1.entrySet()) {
            String key1 = entry.getKey();
            Map<String, String> innerMap = entry.getValue();

            System.out
                    .println(Ansi.ansi().fg(Ansi.Color.RED).a("┌───────────────────────────────────────────┐").reset());

            for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
                String key2 = innerEntry.getKey();
                String value = innerEntry.getValue();

                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("│").reset() + " " + key1 + "[" + key2 + "] -> "
                        + value + Ansi.ansi().fg(Ansi.Color.RED).a(" │").reset());
            }

            System.out
                    .println(Ansi.ansi().fg(Ansi.Color.RED).a("└───────────────────────────────────────────┘").reset());
        }
    }
}
