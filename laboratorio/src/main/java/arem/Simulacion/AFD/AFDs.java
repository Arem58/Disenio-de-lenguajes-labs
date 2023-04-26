package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.interfaces.IAFDs;
import arem.Simulacion.InputHandler;
import arem.handlers.GALhandler;

public class AFDs {

    private IAFDs afd;
    private boolean correct;

    public boolean isCorrect() {
        return correct;
    }

    public IAFDs getAfd() {
        return afd;
    }

    private AFDs(IAFDs afd, String input) {
        this.afd = afd;
        AnsiConsole.systemInstall();
        correct = afd.simulate(input);
        if (correct) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("La expresion: " + afd.getExpresion().replace("\n", "") + " es aceptable").reset());
        }else{
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("La expresion: " + afd.getExpresion().replace("\n", "") + " no es aceptable").reset());
        }
        AnsiConsole.systemUninstall();
    }

    public interface KeyType {
    }

    public static AFDs createWithStringKey(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS, int option, Set<String> returnedTokens, Set<String> tokens) {
        return new AFDs(new AFDSimulationString(tablaS, returnedTokens, tokens), new InputHandler().handleInput(option));
    }

    public static AFDs createWithStringKey(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS, int option) {
        GALhandler galHandler = GALhandler.obtenerInstancia();
        return new AFDs(new AFDSimulationString(tablaS, galHandler), new InputHandler().handleInput(option));
    }

    public static AFDs createWithCharKey(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        return new AFDs(new AFDSimulationChar(tablaS), new InputHandler().handleInput(1));
    }

    private void Simulacion(String expresion) {

    }

}
