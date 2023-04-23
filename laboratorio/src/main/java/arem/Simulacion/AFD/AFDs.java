package arem.Simulacion.AFD;

import java.util.Map;
import java.util.Set;

import arem.Algoritmos.AFNtoAFD.EstadosAFN;
import arem.Algoritmos.interfaces.IAFDs;
import arem.Simulacion.InputHandler;

public class AFDs {

    private IAFDs afd;

    private AFDs(IAFDs afd, String input){
        this.afd = afd;
        // String expresion = input;
        System.out.println(afd.simulate(input));
        // Simulacion(expresion);
    }

    public interface KeyType {}
    
    public static AFDs createWithStringKey(Map<EstadosAFN, Map<String, Set<EstadosAFN>>> tablaS, int option) {
        return new AFDs(new AFDSimulationString(tablaS), new InputHandler(). handleInput(option));
    }

    public static AFDs createWithCharKey(Map<EstadosAFN, Map<Character, Set<EstadosAFN>>> tablaS) {
        return new AFDs(new AFDSimulationChar(tablaS), new InputHandler().handleInput(1));
    }

    private void Simulacion(String expresion){
        
    }

}
