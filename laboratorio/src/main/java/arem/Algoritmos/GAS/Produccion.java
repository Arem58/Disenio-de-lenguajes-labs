package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void movePosicionPunto(){
        posicionPunto++;
    }

    public String getSimbolo() {
        return finalProduccion.get(posicionPunto);
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

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Produccion produccion = (Produccion) obj;
        return posicionPunto == produccion.posicionPunto &&
               Objects.equals(noTerminal, produccion.noTerminal) &&
               Objects.equals(finalProduccion, produccion.finalProduccion);
    }

    @Override
    public int hashCode(){
        return Objects.hash(noTerminal, finalProduccion, posicionPunto);
    }
}
