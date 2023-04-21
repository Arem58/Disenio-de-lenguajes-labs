package arem.Algoritmos.AFD;

import java.util.Set;

public class EstadosAFD {
    private boolean nullable;
    private Set<Integer> firstpos;
    private Set<Integer> lastpos;
    private Set<Integer> followpos;
    private String simbol;

    private nodo nodo;

    public EstadosAFD(nodo nodo) {
        this.nodo = nodo;
        simbol = nodo.getValue();
    }

    public boolean isNullable() {
        return nullable;
    }
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    public Set<Integer> getFirstpos() {
        return firstpos;
    }
    public void setFirstpos(Set<Integer> firstpos) {
        this.firstpos = firstpos;
    }
    
    public Set<Integer> getLastpos() {
        return lastpos;
    }
    public void setLastpos(Set<Integer> lastpos) {
        this.lastpos = lastpos;
    }
    
    public Set<Integer> getFollowpos() {
        return followpos;
    }
    public void setFollowpos(Set<Integer> followpos) {
        this.followpos = followpos;
    }

    public String getSimbol() {
        return simbol;
    }
    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }
}
