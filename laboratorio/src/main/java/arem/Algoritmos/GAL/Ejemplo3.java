package arem.Algoritmos.GAL;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejemplo3 {
    private String regex;
    private List<Character> validExpressions;
    private String[] rangePatterns = new String[] {
            "(''|\"\"|\\\\[tsn])", // delim
            "([\'\"])(\\w)(?:(\\1-\\1)(\\w))?(\\1)", // letter
            "\"(.*?)\"", // digit
            "\'(.*?)\'" // single character
    };

    public Ejemplo3(String regex, List<Character> validExpressions) {
        this.regex = regex;
        this.validExpressions = validExpressions;
    }

    public String expand() {
        StringBuilder expandedRegex = new StringBuilder();
        int lastIndex = 0;

        while (lastIndex < regex.length()) {
            boolean matched = false;
            for (String rangePattern : rangePatterns) {
                Pattern pattern = Pattern.compile(rangePattern);
                Matcher matcher = pattern.matcher(regex);
                if (matcher.find(lastIndex) && matcher.start() == lastIndex) {
                    matched = true;
                    expandedRegex.append(regex, lastIndex, matcher.start());

                    switch (rangePattern) {
                        case "(''|\"\"|\\\\[tsn])":
                            String specialChar = "";
                            if (matcher.group(1).length() == 1) {
                                specialChar = matcher.group(1);
                            } else {
                                switch (matcher.group(1)) {
                                    case "\\t":
                                        specialChar = "\t";
                                        break;
                                    case "\\s":
                                        specialChar = " ";
                                        break;
                                    case "\\n":
                                        specialChar = "\n";
                                        break;
                                }
                            }
                            expandedRegex.append(specialChar);
                            break;
                        case "([\'\"])(\\w)(?:(\\1-\\1)(\\w))?(\\1)":
                            char startChar = matcher.group(2).charAt(0);
                            if (matcher.group(3) != null) {
                                // Caso de rango
                                char endChar = matcher.group(4).charAt(0);
                                for (char c = startChar; c <= endChar; c++) {
                                    expandedRegex.append(c);
                                    if (c != endChar) {
                                        expandedRegex.append('|');
                                    }
                                }
                            } else {
                                // Caso de secuencia continua de caracteres
                                expandedRegex.append(startChar);
                                for (int i = 1; i < matcher.group(5).length() - 1; i++) {
                                    expandedRegex.append('|');
                                    expandedRegex.append(matcher.group(5).charAt(i));
                                }
                            }
                            break;
                        case "\"(.*?)\"":
                            for (int i = 0; i < matcher.group(1).length(); i++) {
                                char currentChar = matcher.group(1).charAt(i);
                                expandedRegex.append(currentChar);
                                if (currentChar == '\\' && i + 1 < matcher.group(1).length()) {
                                    // If the current character is a backslash, append the next character as well
                                    i++; // Increment i to skip the next character
                                    expandedRegex.append(matcher.group(1).charAt(i));
                                }
                                if (i != matcher.group(1).length() - 1
                                        && (!validExpressions.contains(currentChar))) {
                                    expandedRegex.append('|');
                                }
                            }
                            break;
                        case "\'(.*?)\'":
                            String matchedGroup = matcher.group(1);
                            for (int i = 0; i < matchedGroup.length(); i++) {
                                char currentChar = matchedGroup.charAt(i);
                                if (currentChar == '\\' && i + 1 < matchedGroup.length()) {
                                    i++; // Increment i to skip the next character
                                    currentChar = matchedGroup.charAt(i);
                                    switch (currentChar) {
                                        case 't':
                                            expandedRegex.append("\\t");
                                            break;
                                        case 'n':
                                            expandedRegex.append("\\n");
                                            break;
                                    }
                                } else {
                                    if (currentChar == ' ') {
                                        expandedRegex.append("\\s");
                                    } else {
                                        expandedRegex.append(currentChar);
                                    }
                                }
                                if (i != matchedGroup.length() - 1) {
                                    expandedRegex.append('|');
                                }
                            }
                            break;
                    }
                    lastIndex = matcher.end();
                    if (lastIndex < regex.length() && regex.charAt(lastIndex) == '\'') {
                        expandedRegex.append('|');
                    }
                    break;
                }
            }

            if (!matched) {
                expandedRegex.append(regex.charAt(lastIndex));
                lastIndex++;
            }
        }

        return expandedRegex.toString();
    }
}
