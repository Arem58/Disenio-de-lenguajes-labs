package arem.Algoritmos.GAS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LectorDeYalp {
    private final String TOKEN_PATTERN = "%token(?:\\s+(\\w+))+";
    private final String TOKEN_IGNORE = "IGNORE\\s+(\\w+)";
    private final String DIVISOR = "%%";
    private final String COMMENTS = "/\\*.*?\\*/";

    public static Map<String, String> simbNoTerminal = new HashMap<>();
    private Set<String> returnedTokens;

    private List<Produccion> productions;

    public List<Produccion> getProductions() {
        return productions;
    }

    public Set<String> getReturnedTokens() {
        return returnedTokens;
    }

    private String fileName;

    public LectorDeYalp(String fileName) {
        this.fileName = fileName;
        returnedTokens = new HashSet<>();
        productions = new LinkedList<>();
        readingFile();
    }

    public void readingFile() {
        boolean tokensSectionFound = false;
        boolean productionsSectionFound = false;

        String currentSection = "tokens";

        // Producciones
        String key = "";
        StringBuilder produccion = new StringBuilder("");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

                Matcher commentsMatcher = Pattern.compile(COMMENTS).matcher(line);
                Matcher tokenMatcher = Pattern.compile(TOKEN_PATTERN).matcher(line);
                Matcher ignoreToken = Pattern.compile(TOKEN_IGNORE).matcher(line);
                Matcher divisorMatcher = Pattern.compile(DIVISOR).matcher(line);

                if (commentsMatcher.find())
                    continue;

                if (tokenMatcher.find()) {
                    tokensSectionFound = true;
                    String[] tokens = line.replace("%token", "").trim().split("\\s+");
                    for (String token : tokens) {
                        returnedTokens.add(token);
                    }
                } else if (ignoreToken.find() && tokensSectionFound) {
                    returnedTokens.remove(ignoreToken.group(1));
                } else if (divisorMatcher.find()) {
                    productionsSectionFound = true;
                } else {
                    if (productionsSectionFound && !line.equals("")) {
                        if (line.contains(":")) {
                            key = "";
                            produccion.setLength(0);
                            String[] noTerminal = line.split(":");
                            key = getNoTerminal(noTerminal[0].trim());
                            currentSection = "produccion";
                            continue;
                        } else if (line.contains(";")) {
                            currentSection = "finalProduccion";
                        }

                        if ("produccion".equals(currentSection)) {
                            produccion.append(line);
                        } else if ("finalProduccion".equals(currentSection)) {
                            String[] finalS = produccion.toString().split("\\s+");
                            List<String> simbolos = new LinkedList<>();
                            Produccion newProduccion;

                            for (String simbol : finalS) {
                                if (simbol.equals("|")) {
                                    newProduccion = new Produccion(key, simbolos);
                                    productions.add(newProduccion);
                                    simbolos = new LinkedList<>();
                                    continue;
                                }

                                if (!simbol.isEmpty()) {
                                    if (getNoTerminal(simbol) != null) {
                                        simbolos.add(getNoTerminal(simbol));
                                    } else {
                                        simbolos.add(simbol);
                                    }
                                }
                            }
                            newProduccion = new Produccion(key, simbolos);
                            productions.add(newProduccion);
                            currentSection = "key";
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNoTerminal(String noTerminal) {
        if (returnedTokens.contains(noTerminal)) {
            return null;
        }

        char c = noTerminal.charAt(0);
        c = Character.toUpperCase(c);
        simbNoTerminal.put(noTerminal, String.valueOf(c));
        simbNoTerminal.put(String.valueOf(c), noTerminal);

        return String.valueOf(c);
    }
}
