package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.interfaces.IAFDs;
import arem.Simulacion.InputHandler;
import arem.handlers.GALhandler;

public class AFDs {

    private IAFDs afd;

    private AFDs(IAFDs afd, String input) {
        this.afd = afd;
        boolean aceptado = afd.simulate(input);
        if (aceptado) {
            System.out.println("La expresion: " + afd.getExpresion() + " es aceptable");
        }else{
            System.out.println("La expresion: " + afd.getExpresion() + " no es aceptable");
        }
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
