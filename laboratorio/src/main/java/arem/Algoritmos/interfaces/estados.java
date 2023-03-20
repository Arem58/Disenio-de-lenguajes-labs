package arem.Algoritmos.interfaces;

import arem.Algoritmos.enums.TipoGrafo;

public interface estados {
    public String getId();

    public TipoGrafo getIdentificador();

    public void setIdentificador(TipoGrafo identificador);
}
