package arem.Algoritmos.interfaces;

import java.util.Collections;
import java.util.Map;

public interface IAFDs {
    boolean simulate(String expresion);

    default Map<Integer, Map<String, String>> getTokensReturned() {
        return Collections.emptyMap();
    }

    String getExpresion();
}
