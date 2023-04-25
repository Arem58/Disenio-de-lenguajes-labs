package arem.Simulacion.AFN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import arem.Algoritmos.AFN2.Estados2;
import arem.Algoritmos.GE.GE;
import arem.Algoritmos.enums.TipoGrafo;
import arem.Simulacion.InputHandler;

public class AFNs {

    private GE<Estados2> ge;
    private String expresion;

    public AFNs(GE<Estados2> ge, int opcion) {
        this.ge = ge;
        AnsiConsole.systemInstall();
        expresion = new InputHandler().handleInput(opcion);
        boolean correct = simulate(expresion);
        if (correct) {
            System.out.println(
                    Ansi.ansi().fg(Ansi.Color.GREEN).a("La expresion: " + expresion + " es aceptable").reset());
        } else {
            System.out.println(
                    Ansi.ansi().fg(Ansi.Color.RED).a("La expresion: " + expresion + " no es aceptable").reset());
        }
        AnsiConsole.systemUninstall();
    }

    public boolean simulate(String input) {
        if (ge == null || ge.getTransitions().isEmpty()) {
            return false;
        }

        Estados2 initialState = ge.getEntrada();
        return dfs(initialState, 0, input, new ArrayList<>(), false, null);
    }

    private boolean dfs(Estados2 state, int position, String input, List<Estados2> visited, boolean reachedFinal,
            Estados2 prevState) {
        char symbol = position < input.length() ? input.charAt(position) : '\0';
        reachedFinal = reachedFinal || state.getIdentificador() == TipoGrafo.FINAL || ge.getSalida().equals(state);

        if (visited.contains(state)) {
            int index = visited.lastIndexOf(state);
            visited = visited.subList(0, index + 1);
    
            if (prevState != null && ge.getTransitions().get(state).get('ε') != null) {
                Estados2 nextState = epsilonPathExists(state, prevState, new HashSet<>(), symbol);
                if (nextState != null) {
                    state = nextState;
                    position++;
                } else {
                    return false;
                }
            }
        } else {
            visited.add(state);
        }

        Map<Character, Set<Estados2>> transitions = ge.getTransitions().get(state);

        if (transitions == null) {
            return reachedFinal && position == input.length();
        }

        Set<Estados2> nextStates = transitions.get(symbol);

        // Verificar si hay una transición con el caracter actual antes de explorar las
        // transiciones epsilon
        if (nextStates != null) {
            for (Estados2 nextState : nextStates) {
                System.out.println(
                        "Transición de " + state.getId() + " a " + nextState.getId() + " con símbolo " + symbol);
                if (dfs(nextState, position + 1, input, new ArrayList<>(visited), reachedFinal, state)) {
                    return true;
                }
            }
        }

        // Verificar transiciones epsilon
        Set<Estados2> epsilonStates = transitions.get('ε');
        if (epsilonStates != null) {
            for (Estados2 epsilonState : epsilonStates) {
                System.out.println("Transición epsilon de " + state.getId() + " a " + epsilonState.getId());
                if (dfs(epsilonState, position, input, new ArrayList<>(visited), reachedFinal, state)) {
                    return true;
                }
            }
        }

        if (position == input.length()) {
            System.out.println("Estado actual: " + state.getId() + ", posición: " + position);
            return reachedFinal;
        }

        return false;
    }

    private Estados2 epsilonPathExists(Estados2 a, Estados2 b, Set<Estados2> visited, char symbol) {
        if (a.equals(b)) {
            return null;
        }
    
        visited.add(a);
    
        Map<Character, Set<Estados2>> transitions = ge.getTransitions().get(a);
        if (transitions == null) {
            return null;
        }
    
        Set<Estados2> symbolStates = transitions.get(symbol);
        if (symbolStates != null) {
            for (Estados2 symbolState : symbolStates) {
                if (!visited.contains(symbolState)) {
                    System.out.println("Transición de " + a.getId() + " a " + symbolState.getId() + " con símbolo " + symbol);
                    return symbolState;
                }
            }
        }
    
        Set<Estados2> epsilonStates = transitions.get('ε');
        if (epsilonStates != null) {
            for (Estados2 epsilonState : epsilonStates) {
                if (!visited.contains(epsilonState)) {
                    Estados2 result = epsilonPathExists(epsilonState, b, new HashSet<>(visited), symbol);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
    
        return null;
    }    
}
