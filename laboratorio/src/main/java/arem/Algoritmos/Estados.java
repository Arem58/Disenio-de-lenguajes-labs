package arem.Algoritmos;

import java.util.LinkedList;
import java.util.List;

public class Estados {

    private int id;
    private List<Estados> siguienteES;
    private List<Estados> pasatoEs;

    public Estados(int id) {
        this.id = id;
        this.siguienteES = new LinkedList<Estados>();
        this.pasatoEs = new LinkedList<Estados>();
    }

    // public static int createState(){
    //     int estado = AFN.stateCount;
    //     AFN.stateCount ++;
    //     return estado;
    // }

    @Override
    public String toString(){
        return ""+id+"";
    }

  
}
