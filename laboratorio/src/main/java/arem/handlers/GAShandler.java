package arem.handlers;

import java.util.List;
import java.util.Set;

import arem.Algoritmos.GAS.LectorDeYalp;
import arem.Algoritmos.GAS.Produccion;
import arem.Funciones.AnalisisSintacticoHelper;

public class GAShandler {
    private Set<String> tokensGAS;

    public Set<String> getTokensGAS() {
        return tokensGAS;
    }

    private List<Produccion> productions;

    public GAShandler() {
        String fileNane = "laboratorio/src/main/java/arem/assets/Archivos Yalp/slr-3.yalp";
        LectorDeYalp archivo = new LectorDeYalp(fileNane);
        productions = archivo.getProductions();
        tokensGAS = archivo.getReturnedTokens();
    }
    
    public void firstAndfollow(){
        AnalisisSintacticoHelper ANH = new AnalisisSintacticoHelper(productions, tokensGAS);
    }
}
