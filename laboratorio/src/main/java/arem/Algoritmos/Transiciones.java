package arem.Algoritmos;

public class Transiciones {
    private char simbolo;
    private Estados esActual;
    private Estados esFinal;

    public Transiciones(char simbolo, Estados esActual, Estados esFinal) {
        this.simbolo = simbolo;
        this.esActual = esActual;
        this.esFinal = esFinal;
    }

    public String toString() {
        return String.format(esActual.toString() + " =="+simbolo+"==> " + esFinal.toString());
    }
}
