package arem.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import arem.Algoritmos.GAS.EstadoGAS;
import arem.Algoritmos.GAS.LR0;
import arem.Algoritmos.GAS.LectorDeYalp;
import arem.Algoritmos.GAS.Produccion;
import arem.Algoritmos.GE.GEString;
import arem.Funciones.AnalisisSintacticoHelper;
import arem.Grafo.GrafoGraphviz;

public class GAShandler {
    private Set<String> tokensGAS;
    private GEString<EstadoGAS> geLR0;

    public Set<String> getTokensGAS() {
        return tokensGAS;
    }

    private List<Produccion> productions;

    public GAShandler() {
        String fileNane = "laboratorio/src/main/java/arem/assets/Archivos Yalp/slr-1.yalp";
        LectorDeYalp archivo = new LectorDeYalp(fileNane);
        productions = archivo.getProductions();
        tokensGAS = archivo.getReturnedTokens();
    }
    
    public void firstAndfollow(){
        AnalisisSintacticoHelper ANH = new AnalisisSintacticoHelper(productions, tokensGAS);
    }

    public void LR0(){
        LR0 lr0 = new LR0(productions, tokensGAS);
        lr0.imprimirEstados();
        geLR0 = new GEString<>(lr0.getAutomata(), lr0.getListaEstados());
        GrafoGraphviz<EstadoGAS> grafoGraphviz = new GrafoGraphviz<>(geLR0);
        String outputPath = "laboratorio/src/main/java/arem/assets/outputs/grafoLR0.png";
        createGraph(grafoGraphviz, outputPath);
    }

    private void createGraph(GrafoGraphviz<EstadoGAS> grafoGraphviz, String outputPath) {
        try {
            grafoGraphviz.createGraphViz(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
