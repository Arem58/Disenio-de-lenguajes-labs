package arem.Algoritmos.AFN2;

import arem.Algoritmos.enums.TipoGrafo;
import arem.Algoritmos.interfaces.estados;

public class Estados2 implements estados {
    private int id;
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

    public Estados2() {
        this.id = AFN2.stateCount;
        identificador = TipoGrafo.NORMAL;
        AFN2.stateCount ++;
    }

    @Override
    public boolean equals(Object arg0) {
        if (this == arg0)
            return true;
        if (arg0 == null)
            return false;
        if (getClass() != arg0.getClass())
            return false;
        Estados2 other = (Estados2) arg0;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public String toString(){
        return ""+id+"";
    }
}
