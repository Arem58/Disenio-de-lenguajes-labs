package arem.Algoritmos.AFN2;

public class Estados2 {
    private int id;

    public int getId() {
        return id;
    }

    public Estados2(int id) {
        this.id = id;
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
