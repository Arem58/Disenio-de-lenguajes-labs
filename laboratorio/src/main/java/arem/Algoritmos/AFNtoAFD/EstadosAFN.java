package arem.Algoritmos.AFNtoAFD;

import java.util.Objects;

import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.estados;

public class EstadosAFN implements estados {
    private Character id;
    private TipoGrafo identificador;

    @Override
    public void setIdentificador(TipoGrafo identificador) {
        this.identificador = identificador;
    }

    @Override
    public TipoGrafo getIdentificador(){
        return this.identificador;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    public EstadosAFN() {
        this.id = Subconjuntos.id;
        identificador = TipoGrafo.NORMAL;
        Subconjuntos.id++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadosAFN estados2 = (EstadosAFN) o;
        return Objects.equals(id, estados2.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString(){
        return ""+id+"";
    }
}
