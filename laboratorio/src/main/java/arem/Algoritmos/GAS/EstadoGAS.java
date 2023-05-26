package arem.Algoritmos.GAS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EstadoGAS {
    private String id;

    public String getId() {
        return id;
    }

    private List<Produccion> cabezal;
    private List<Produccion> cuerpo;
    private Set<String> noTerminales;

    public void addNoterminal(String noTerminal){
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
}
