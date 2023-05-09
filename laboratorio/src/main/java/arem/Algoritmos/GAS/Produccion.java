package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.List;

public class Produccion {
    private String noTerminal;

    public String getNoTerminal() {
        return noTerminal;
    }

    private List<String> finalProduccion;

    public List<String> getFinalProduccion() {
        return finalProduccion;
    }

    private int posicionPunto;

    public int getPosicionPunto() {
        return posicionPunto;
    }

    public Produccion(String noTerminal, List<String> finalProduccion) {
        this.noTerminal = noTerminal;
        this.finalProduccion = finalProduccion;
        posicionPunto = 0;
    }

    public Produccion(Produccion other) {
        this.noTerminal = other.noTerminal;
        this.finalProduccion = new ArrayList<>(other.finalProduccion);
        this.posicionPunto = other.posicionPunto;
    }
}
