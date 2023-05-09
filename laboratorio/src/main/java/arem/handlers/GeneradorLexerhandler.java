package arem.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import arem.Algoritmos.GAL.LectorDeArchivos;
import arem.Funciones.GenerarArchivoJava;
import arem.Funciones.MapIO;
import arem.assets.archivosJava.Expresion.ExpresionData;

public class GeneradorLexerhandler {

    public GeneradorLexerhandler() {
        String fileNane = "laboratorio/src/main/java/arem/assets/Archivos Yal/slr-1.yal";
        LectorDeArchivos archivo = new LectorDeArchivos(fileNane);
        if (archivo.isHasError()) {
            return;
        }
        ExpresionData data = new ExpresionData(archivo.getExpandedActions(), archivo.getReturnedTokens(),
                archivo.getExpandedActions().keySet(), archivo.getUnsustitutedExpressions());

        String mapFileName = "laboratorio/src/main/java/arem/assets/archivosJava/Expresion/expanded_actions.dat";

        writeExpresion(data, mapFileName);

        GenerarArchivoJava.generarArchivoJava(mapFileName);
    }

    private void writeExpresion(ExpresionData data, String mapFileName) {
        try {
            MapIO.saveDataToFile(mapFileName, data);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos en el archivo: " + e.getMessage());
        }
    }
}
