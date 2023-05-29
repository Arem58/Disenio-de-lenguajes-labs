package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.estados;

public class EstadoGAS implements estados {
    private String id;
    private TipoGrafo identificador;

    @Override
    public void setIdentificador(TipoGrafo identificador) {
        this.identificador = identificador;
    }

    @Override
    public TipoGrafo getIdentificador() {
        return this.identificador;
    }

    @Override
    public String getId() {
        return id;
    }

    private List<Produccion> cabezal;
    private List<Produccion> cuerpo;
    private Set<String> noTerminales;

    public void addNoterminal(String noTerminal) {
        noTerminales.add(noTerminal);
    }

    public Set<String> getNoTerminales() {
        return noTerminales;
    }

    public List<Produccion> getCuerpo() {
        return cuerpo;
    }

    public List<Produccion> getCabezal() {
        return cabezal;
    }

    public EstadoGAS(List<Produccion> cabezal) {
        this.cabezal = cabezal;
        cuerpo = new ArrayList<>();
        noTerminales = new HashSet<>();
        identificador = TipoGrafo.NORMAL;
        id = "I" + LR0.idGAS;
        LR0.idGAS++;
    }

    public void addCuerpo(List<Produccion> cuerpo) {
        this.cuerpo.addAll(cuerpo);
    }

    public List<Produccion> getTabla() {
        List<Produccion> tabla = new ArrayList<>();
        tabla.addAll(cabezal);
        tabla.addAll(cuerpo);
        return tabla;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        EstadoGAS estado = (EstadoGAS) obj;
        return Objects.equals(cabezal, estado.cabezal);
    }

    @Override
    public int hashCode(){
        return Objects.hash(cabezal);
    }

    @Override
    public String toString(){
        return ""+id+"";
    }
}
