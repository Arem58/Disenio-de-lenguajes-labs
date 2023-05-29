package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.fusesource.jansi.Ansi;

import arem.Algoritmos.enums.TipoGrafo;

public class LR0 {
    private Map<EstadoGAS, Map<String, Set<EstadoGAS>>> automata;

    public Map<EstadoGAS, Map<String, Set<EstadoGAS>>> getAutomata() {
        return automata;
    }

    private Set<EstadoGAS> listaEstados;

    public Set<EstadoGAS> getListaEstados() {
        return listaEstados;
    }

    private List<Produccion> productions;
    public static int idGAS;

    public LR0(List<Produccion> productions, Set<String> tokensGAS) {
        this.productions = productions;
        automata = new HashMap<>();
        listaEstados = new LinkedHashSet<>();
        idGAS = 0;
        ContruccionLR0(estadoInicial(), tokensGAS);
    }

    private void ContruccionLR0(EstadoGAS estadoInicial, Set<String> tokensGAS) {
        Queue<EstadoGAS> cola = new LinkedList<>();
        cola.offer(estadoInicial);
        while (!cola.isEmpty()) {
            EstadoGAS actual = cola.poll();
            llenarTabla(actual, tokensGAS);
            nuevosEstados(actual, cola);
        }
    }

    private void llenarTabla(EstadoGAS actual, Set<String> tokensGAS) {
        List<Produccion> nuevoCuerpo = new ArrayList<>();

        for (Produccion produccion : actual.getCabezal()) {
            if (produccion.estadoAceptacion())
                continue;
            if (!actual.getNoTerminales().contains(produccion.getSimbolo())
                    && !tokensGAS.contains(produccion.getSimbolo())) {
                nuevoCuerpo.addAll(Closure(produccion.getSimbolo()));
                actual.addNoterminal(produccion.getSimbolo());
            }
        }

        while (!nuevoCuerpo.isEmpty()) {
            List<Produccion> temp = new ArrayList<>(nuevoCuerpo);
            nuevoCuerpo.clear();

            for (Produccion produccion : temp) {
                if (produccion.estadoAceptacion())
                    continue;
                if (!actual.getNoTerminales().contains(produccion.getSimbolo())
                        && !tokensGAS.contains(produccion.getSimbolo())) {
                    nuevoCuerpo.addAll(Closure(produccion.getSimbolo()));
                    actual.addNoterminal(produccion.getSimbolo());
                }
            }
            actual.addCuerpo(temp);
        }
    }

    private void nuevosEstados(EstadoGAS actual, Queue<EstadoGAS> cola) {
        Map<String, List<Produccion>> grupos = actual.getTabla().stream()
                .filter(p -> !p.estadoAceptacion()) // Se filtran las producciones que su punto ha llegado al final
                .collect(Collectors.groupingBy(Produccion::getSimbolo, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<Produccion>> entry : grupos.entrySet()) {
            String transicion = entry.getKey();
            List<Produccion> grupo = entry.getValue();

            List<Produccion> clones = new ArrayList<>();
            for (Produccion produccion : grupo) {
                Produccion clon = new Produccion(produccion);
                clon.movePosicionPunto();
                clones.add(clon);
            }
            EstadoGAS nuevoEstado = new EstadoGAS(clones);

            boolean existe = false;
            for (EstadoGAS estado : listaEstados) {
                if (estado.equals(nuevoEstado)) {
                    existe = true;
                    nuevoEstado = estado;
                    idGAS--;
                    break;
                }
            }

            if (!existe) {
                listaEstados.add(nuevoEstado);
                cola.offer(nuevoEstado);
                updateEstado(nuevoEstado);
            }
            updateAutomata(actual, nuevoEstado, transicion);
        }
    }

    private void updateEstado(EstadoGAS nuevoEstado) {
        for (Produccion produccion : nuevoEstado.getCabezal()) {
            if (produccion.estadoAceptacion()) {
                if (produccion.getNoTerminal().equals("S'")) {
                    nuevoEstado.setIdentificador(TipoGrafo.FINAL);
                } else {
                    nuevoEstado.setIdentificador(TipoGrafo.ACEPTACION);
                }
            }
        }
    }

    private void updateAutomata(EstadoGAS actual, EstadoGAS nuevoEstado, String transicion) {
        automata.putIfAbsent(actual, new LinkedHashMap<>());
        Map<String, Set<EstadoGAS>> transiciones = automata.get(actual);
        transiciones.computeIfAbsent(transicion, k -> new HashSet<>()).add(nuevoEstado);
    }

    private EstadoGAS estadoInicial() {
        // Introducimos la produccion inical
        Produccion inicial = productions.get(0);
        Produccion inicialGramatica = new Produccion("S'", Collections.singletonList(inicial.getNoTerminal()));
        EstadoGAS estadoInicial = new EstadoGAS(Collections.singletonList(inicialGramatica));
        listaEstados.add(estadoInicial);
        automata.put(estadoInicial, new LinkedHashMap<>());
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

    public void imprimirEstados() {
        for (EstadoGAS estado : this.getListaEstados()) {
            Ansi.Color color = getColor(estado);

            System.out.println(Ansi.ansi().fg(color).a("┌─────────────────────┐").reset());
            System.out.println(Ansi.ansi().fg(color).a("│ Estado: " + estado.getId()).reset());
            System.out.println(Ansi.ansi().fg(color).a("│ Producciones:").reset());
            for (Produccion produccion : estado.getTabla()) {
                String noTerminal = produccion.getNoTerminal();
                List<String> finalProduccion = produccion.getFinalProduccion();
                int posicionPunto = produccion.getPosicionPunto();

                // Concatenamos la lista de producción final con el punto en la posición
                // correcta
                String produccionConPunto = String.join(" ", finalProduccion.subList(0, posicionPunto)) +
                        " . " +
                        String.join(" ", finalProduccion.subList(posicionPunto, finalProduccion.size()));

                System.out.println(Ansi.ansi().fg(color).a("│    " + noTerminal + " -> " + produccionConPunto).reset());
            }
            System.out.println(Ansi.ansi().fg(color).a("└─────────────────────┘").reset());
        }
    }

    private Ansi.Color getColor(EstadoGAS estado) {
        Ansi.Color color;
        switch (estado.getIdentificador()) {
            case FINAL:
                color = Ansi.Color.GREEN;
                break;
            case ACEPTACION:
                color = Ansi.Color.MAGENTA;
                break;
            default:
                color = Ansi.Color.DEFAULT;
                break;
        }
        return color;
    }
}
