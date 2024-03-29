package arem.Algoritmos.GAL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LectorDeArchivosAntiguo {
    private static final String LET_PATTERN = "let\\s+(\\w+)\\s*=\\s*(.+)";
    private static final String RULE_PATTERN = "rule\\s+(tokens)\\s*=\\s*(.+)";

    public static void main(String[] args) {
        String fileName = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/Archivos Yal/slr-1.yal";

        Map<String, String> definitions = new HashMap<>();
        List<String> header = new ArrayList<>();
        List<String> trailer = new ArrayList<>();
        List<String> rules = new ArrayList<>();
        List<List<String>> actions = new ArrayList<>();
        Set<String> tokens = new HashSet<>();

        boolean headerSectionFound = false;
        boolean definitionsSectionFound = false;
        boolean rulesSectionFound = false;
        boolean trailerSectionFound = false;

        String currentSection = "header";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

                Matcher letMatcher = Pattern.compile(LET_PATTERN).matcher(line);
                boolean isDefinition = letMatcher.find();
                Matcher ruleMatcher = Pattern.compile(RULE_PATTERN).matcher(line);

                // Verifica las comillas en la linea
                if (checkLineForErrors(line, tokens, isDefinition)) {
                    return;
                }

                if (isDefinition) {
                    if (!validateOrder(headerSectionFound, definitionsSectionFound, rulesSectionFound,
                            trailerSectionFound, line)) {
                        return;
                    }
                    definitionsSectionFound = true;
                    definitions.put(letMatcher.group(1), letMatcher.group(2));
                } else if (ruleMatcher.find()) {
                    if (!validateOrder(headerSectionFound, definitionsSectionFound, rulesSectionFound,
                            trailerSectionFound, line)) {
                        return;
                    }
                    rulesSectionFound = true;
                    rules.add(ruleMatcher.group(2));
                    actions.add(new ArrayList<>());
                    currentSection = "rules";
                } else if (line.startsWith("{") && "header".equals(currentSection)) {
                    if (!rulesSectionFound) {
                        headerSectionFound = true;
                        currentSection = "trailer";
                    } else {
                        trailerSectionFound = true;
                    }
                } else {
                    if ("header".equals(currentSection)) {
                        header.add(line);
                    } else if ("trailer".equals(currentSection)) {
                        trailer.add(line);
                    } else if ("rules".equals(currentSection)) {
                        if (line.equals("") || line.startsWith("(")) {
                            continue;
                        }
                        actions.get(actions.size() - 1).add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (actions.isEmpty()) {
            System.err.println("Error en la sintaxis: No se ha reconocido ninguna regla de tokens.");
            return;
        }

        for (String token : tokens) {
            if (!definitions.containsKey(token)) {
                System.err.println("Error en la sintaxis: Se reconocio un token que no tiene definición: " + token);
                return;
            }
        }

        definitions.forEach((key, value) -> System.out.println(key + " = " + value));

        System.out.println("\nActions:");
        actions.forEach(actionList -> {
            actionList.forEach(System.out::println);
            System.out.println("---");
        });

        Map<String, String> expandedActions = new LinkedHashMap<>();
        String actionTokens = "";

        for (List<String> actionList : actions) {
            for (String action : actionList) {
                String originalAction = action;

                String expandedAction = expandExpression(action, definitions);

                // Verificar si la accion original no fue sustituida
                if (originalAction.equals(expandedAction)) {
                    // vamos a limiar originalAction
                    originalAction = originalAction.replaceAll("\\|\\s", "");
                    originalAction = originalAction.replaceAll("\\{.*?}", "");
                    originalAction = originalAction.replaceAll("\\(\\*.*?\\*\\)", "");
                    if (!originalAction.matches("\\s*(?:'[^']*'|\"[^\"]*\")\\s*")) {
                        System.err.println("Error en la sintaxis: La acción '" + originalAction.trim()
                                + "' no fue sustituida correctamente.");
                        return;
                    }
                }

                // Eliminar el espacio que sigue a cada barra vertical (|)
                expandedAction = expandedAction.replaceAll("\\|\\s", "|");

                // Capturar el contenido entre llaves
                Pattern returnPattern = Pattern.compile("\\{(.*?)\\}");
                Matcher returnMatcher = returnPattern.matcher(expandedAction);
                if (returnMatcher.find()) {
                    actionTokens = returnMatcher.group(1);
                    actionTokens = actionTokens.replaceAll("return", "").trim();
                } else {
                    actionTokens = action;
                    actionTokens = actionTokens.replaceAll("\\|", "").trim();
                }

                expandedAction = expandedAction.replaceAll("\\{.*?}", ""); // Eliminar contenido entre llaves
                expandedAction = expandedAction.replaceAll("\\(\\*.*?\\*\\)", ""); // Eliminar comentarios
                expandedAction = expandedAction.replaceAll("\\[", "(");
                expandedAction = expandedAction.replaceAll("\\]", ")");
                expandedActions.put(actionTokens, expandedAction.trim());
            }
        }

        // Construir la expresión regular final
        StringBuilder finalRegex = new StringBuilder();
        for (Map.Entry<String, String> actionEntry : expandedActions.entrySet()) {
            finalRegex.append(actionEntry.getValue());
        }

        System.out.println("Expresión regular generada: " + finalRegex.toString());
    }

    public static String expandExpression(String expression, Map<String, String> definitions) {
        boolean replaced = false;
        String expandedExpression = expression;

        if (!expression.matches("'[^']*'")) {
            for (Map.Entry<String, String> definitionEntry : definitions.entrySet()) {
                String key = definitionEntry.getKey();
                String value = definitionEntry.getValue();
                String newExpression = expandedExpression.replaceAll("\\b" + key + "\\b", value);

                if (!newExpression.equals(expandedExpression)) {
                    replaced = true;
                    expandedExpression = newExpression;
                    break;
                }
            }
        }

        if (replaced) {
            return expandExpression(expandedExpression, definitions);
        } else {
            return expandedExpression;
        }
    }

    private static boolean checkLineForErrors(String line, Set<String> nonQuotedExpressions, boolean isDefinition) {
        int singleQuotes = 0;
        int doubleQuotes = 0;
        int openParentheses = 0;
        int closeParentheses = 0;
        boolean comment = false;
        boolean insideQuotes = false;
        boolean afterEqualSign = false;
        boolean failed = false;
        char currentQuote = '\0';
        StringBuilder currentNonQuotedExpression = new StringBuilder();

        List<Character> ignoredChars = Arrays.asList('[', ']', '(', ')', '+', '*', '?', '|', '-', '.', '_');

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (comment) {
                if (currentChar == '*' && i + 1 < line.length() && line.charAt(i + 1) == ')') {
                    comment = false;
                    i++;
                }
                continue;
            }

            if (currentChar == '=') {
                afterEqualSign = true;
                continue;
            }

            if (!insideQuotes && (currentChar == '\'' || currentChar == '\"')) {
                insideQuotes = true;
                currentQuote = currentChar;
                if (currentNonQuotedExpression.length() > 0 && !comment && afterEqualSign) {
                    nonQuotedExpressions.add(currentNonQuotedExpression.toString());
                    currentNonQuotedExpression.setLength(0);
                }
            } else if (insideQuotes && currentChar == currentQuote) {
                insideQuotes = false;
                currentQuote = '\0';
            } else if (!insideQuotes && !comment && isDefinition && afterEqualSign) { // Modifica esta línea
                if (Character.isWhitespace(currentChar) || ignoredChars.contains(currentChar)) {
                    if (currentNonQuotedExpression.length() > 0) {
                        nonQuotedExpressions.add(currentNonQuotedExpression.toString());
                        currentNonQuotedExpression.setLength(0);
                    }
                } else {
                    currentNonQuotedExpression.append(currentChar);
                }
            }

            if (currentChar == '(' && !insideQuotes) {
                if (i + 1 < line.length() && line.charAt(i + 1) == '*') {
                    comment = true;
                    i++;
                } else if (!comment) {
                    openParentheses++;
                }
            } else if (currentChar == ')' && !insideQuotes) {
                if (comment) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '*') {
                        comment = false;
                        i++;
                    }
                } else {
                    closeParentheses++;
                }
            }

            if (currentChar == '\'') {
                singleQuotes++;
            } else if (currentChar == '\"') {
                doubleQuotes++;
            }
        }

        if (currentNonQuotedExpression.length() > 0 && !comment && afterEqualSign) {
            nonQuotedExpressions.add(currentNonQuotedExpression.toString());
        }

        if (singleQuotes % 2 != 0) {
            System.err.println("Error: Hay un número impar de comillas simples en la línea: " + line);
            failed = true;
        }
        if (doubleQuotes % 2 != 0) {
            System.err.println("Error: Hay un número impar de comillas dobles en la línea: " + line);
            failed = true;
        }
        if (openParentheses != closeParentheses) {
            System.err.println("Error: El número de paréntesis abiertos y cerrados no coincide en la línea: " + line);
            failed = true;
        }
        if (comment) {
            System.err.println("Error: Hay un comentario sin cerrar en la línea: " + line);
            failed = true;
        }
        return failed;
    }

    private static boolean validateOrder(boolean headerSectionFound, boolean definitionsSectionFound,
            boolean rulesSectionFound, boolean trailerSectionFound, String line) {
        if (headerSectionFound && (definitionsSectionFound || rulesSectionFound || trailerSectionFound)) {
            System.err.println(
                    "Error: La sección de header debe estar antes de las definiciones regulares en la línea: " + line);
            return false;
        }
        if (!definitionsSectionFound && (rulesSectionFound || trailerSectionFound)) {
            System.err.println(
                    "Error: La sección de definiciones debe estar antes de la sección de reglas en la línea: " + line);
            return false;
        }
        if (!rulesSectionFound && trailerSectionFound) {
            System.err.println(
                    "Error: La sección de reglas debe estar antes de la sección de trailer en la línea: " + line);
            return false;
        }
        return true;
    }
}
