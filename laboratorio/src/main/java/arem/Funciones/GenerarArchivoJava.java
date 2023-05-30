package arem.Funciones;

import java.io.*;

public class GenerarArchivoJava {

    public static void generarArchivoJava(String mapFileName) {

        String rutaArchivo = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/archivosJava/Lexer.java";

        try {
            FileWriter archivo = new FileWriter(rutaArchivo);

            archivo.write("package arem.assets.archivosJava;\n\n");
            archivo.write("import java.io.IOException;\n");
            archivo.write("import java.util.Collections;\n");
            archivo.write("import java.util.HashSet;\n");
            archivo.write("import java.util.Iterator;\n");
            archivo.write("import java.util.List;\n");
            archivo.write("import java.util.Map;\n");
            archivo.write("import java.util.Optional;\n");
            archivo.write("import java.util.Set;\n\n");
            archivo.write("import org.fusesource.jansi.Ansi;\n\n");
            archivo.write("import arem.Algoritmos.interfaces.IAFDs;\n");
            archivo.write("import arem.Funciones.Lenguaje2;\n");
            archivo.write("import arem.Funciones.MapIO;\n");
            archivo.write("import arem.Funciones.Postfix;\n");
            archivo.write("import arem.Simulacion.AFD.AFDs;\n");
            archivo.write("import arem.assets.archivosJava.Expresion.ExpresionData;\n");
            archivo.write("import arem.handlers.AFDhandler;\n");
            archivo.write("import arem.handlers.GAShandler;\n");
            archivo.write("import arem.handlers.handler;\n\n");
            archivo.write("public class Lexer {\n\n");
            archivo.write("    private static List<Character> unsustitutedExpressions;\n");
            archivo.write("    private static Map<String, String> expandedActions;\n");
            archivo.write("    private static Map<String, String> dictionaryTokens;\n");
            archivo.write("    private static Set<String> returnedTokens;\n");
            archivo.write("    private static Set<String> Tokens;\n\n");
            archivo.write("    public static void main(String[] args) {\n");
            archivo.write("        String mapFileName = \"" + mapFileName + "\";\n");
            archivo.write("        readExpresion(mapFileName);\n\n");
            archivo.write("        if (expandedActions != null && returnedTokens != null && Tokens != null) {\n");
            archivo.write("            StringBuilder finalExpression = new StringBuilder();\n\n");
            archivo.write("            finalExpression.append(\"(\");\n");
            archivo.write("            Iterator<Map.Entry<String, String>> iterator = expandedActions.entrySet().iterator();\n");
            archivo.write("            handler handler = new handler();\n");
            archivo.write("            while (iterator.hasNext()) {\n");
            archivo.write("                Map.Entry<String, String> actionEntry = iterator.next();\n");
            archivo.write("                String key = actionEntry.getKey();\n");
            archivo.write("                String value = actionEntry.getValue();\n\n");
            archivo.write("                if (value.startsWith(\"|\")) {\n");
            archivo.write("                    value = value.substring(1);\n");
            archivo.write("                }\n\n");
            archivo.write("                finalExpression.append(handler.getExpresion(Optional.of(value), Optional.of(key), Optional.of(unsustitutedExpressions)));\n\n");
            archivo.write("                if (iterator.hasNext()) {\n");
            archivo.write("                    finalExpression.append(\"|\");\n");
            archivo.write("                } else {\n");
            archivo.write("                    finalExpression.append(\")#\");\n");
            archivo.write("                    handler.EndofLine = \"#\";\n");
            archivo.write("                }\n");
            archivo.write("            }\n\n");
            archivo.write("            System.out.println(\"Expresion final: \" + finalExpression.toString());\n");
            archivo.write("            finalExpression = new StringBuilder(postfix(finalExpression.toString()));\n");
            archivo.write("            Set<String> acceptingStates = new HashSet<>(Collections.singleton(handler.EndofLine));\n\n");
            archivo.write("            AFDhandler afd = new AFDhandler(acceptingStates, finalExpression.toString());\n");
            archivo.write("            AFDs instance = AFDs.createWithStringKey(afd.getGeAFD(), 0, returnedTokens, Tokens);\n");
            archivo.write("            IAFDs afdInstance = instance.getAfd();\n\n");
            archivo.write("                printTokensInfo(afdInstance.getTokensReturned());\n\n");
            archivo.write("        } else {\n");
            archivo.write("            System.out.println(\"El mapa no se pudo cargar correctamente.\");\n");
            archivo.write("        }\n");
            archivo.write("        GAShandler gas = new GAShandler(dictionaryTokens); \n");
            archivo.write("        Set<String> tokensAL = gas.getTokensGAS(); \n");
            archivo.write("        boolean error = false; \n");
            archivo.write("        for (String token : tokensAL) { \n");
            archivo.write("            if (!returnedTokens.contains(token)){ \n");
            archivo.write("                error = true; \n");
            archivo.write("                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a(\"Error el token \" + token + \" no se encuentra en el generado en el yalex\").reset()); \n");
            archivo.write("                break;");
            archivo.write("            } \n");
            archivo.write("        } \n");
            archivo.write("        if (!error){ \n");
            archivo.write("            gas.LR0(); \n");
            archivo.write("        } \n");
            archivo.write("    }\n\n");
            archivo.write("    private static void readExpresion(String mapFileName) {\n");
            archivo.write("        ExpresionData loadedData = null;\n\n");
            archivo.write("        try {\n");
            archivo.write("            loadedData = MapIO.loadDataFromFile(mapFileName);\n");
            archivo.write("        } catch (IOException | ClassNotFoundException e) {\n");
            archivo.write("            System.err.println(\"Error al cargar los datos desde el archivo: \" + e.getMessage());\n");
            archivo.write("        }\n\n");
            archivo.write("        if (loadedData != null) {\n");
            archivo.write("            expandedActions = loadedData.getExpandedActions();\n");
            archivo.write("            dictionaryTokens = loadedData.getDictionaryTokens();\n");
            archivo.write("            returnedTokens = loadedData.getReturnedTokens();\n");
            archivo.write("            Tokens = loadedData.getTokens();\n");
            archivo.write("            unsustitutedExpressions = loadedData.getValidExpressions();\n");
            archivo.write("        }\n");
            archivo.write("    }\n\n");
            archivo.write("    private static String postfix(String finalExpression) {\n");
            archivo.write("        Postfix postfix = new Postfix();\n");
            archivo.write("        finalExpression = postfix.infixToPostfix(finalExpression);\n");
            archivo.write("        Lenguaje2.setLenguajeFinal(finalExpression);\n");
            archivo.write("        System.out.println(\"Postfix: \" + finalExpression);\n");
            archivo.write("        return finalExpression;\n");
            archivo.write("    }\n\n");
            archivo.write("    public static void printTokensInfo(Map<Integer, Map<String, String>> tokensReturned) {\n");
            archivo.write("        StringBuilder tokensInfo = new StringBuilder();\n\n");
            archivo.write("        for (Map.Entry<Integer, Map<String, String>> entry : tokensReturned.entrySet()) {\n");
            archivo.write("            Map<String, String> innerMap = entry.getValue();\n");
            archivo.write("            for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {\n");
            archivo.write("                String token = innerEntry.getKey();\n");
            archivo.write("                String value = innerEntry.getValue();\n");
            archivo.write("                if (\"Error\".equals(token)) {\n");
            archivo.write("                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format(\"----- ----- ----- ----- ----- ----- ----- -----%n\")).reset());\n");
            archivo.write("                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format(\"Token: %s%nValue: %s%n\", token, value)).reset());\n");
            archivo.write("                     tokensInfo.append(Ansi.ansi().fg(Ansi.Color.RED).a(String.format(\"----- ----- ----- ----- ----- ----- ----- -----%n%n\")).reset());\n");
            archivo.write("                } else { \n");
            archivo.write("                     tokensInfo.append(String.format(\"----- ----- ----- ----- ----- ----- ----- -----%n\"));\n");
            archivo.write("                     tokensInfo.append(String.format(\"Token: %s%nValue: %s%n\", token, value));\n");
            archivo.write("                     tokensInfo.append(String.format(\"----- ----- ----- ----- ----- ----- ----- -----%n%n\"));\n");
            archivo.write("                }\n");
            archivo.write("            }\n");
            archivo.write("        }\n\n");
            archivo.write("        System.out.println(tokensInfo.toString());\n");
            archivo.write("    }\n");
            archivo.write("}\n");

            archivo.close();

            System.out.println("El archivo " + rutaArchivo + " se ha generado exitosamente.");

        } catch (IOException e) {
            System.out.println("Ocurrió un error al generar el archivo " + rutaArchivo + ": " + e.getMessage());
        }
    }
}
