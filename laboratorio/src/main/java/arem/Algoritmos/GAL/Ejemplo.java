package arem.Algoritmos.GAL;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejemplo {
    public static void main(String[] args) {
        String regex = "('0'-'9')+(.('0'-'9')+)?('E'('+''-')?('0'-'9')+)?";
        String expandedRegex = expandRangesAndChars(regex);
        System.out.println("Expanded regex: " + expandedRegex);
    }

    private static String expandRangesAndChars(String regex) {
        String rangePattern = "\\(([^\\(\\)]+)\\)|(?<!\\\\)'(.+)'-'(.+)'|\"(.+?)\"|(\\\\[ts])";
        StringBuilder expandedRegex = new StringBuilder();
        Pattern pattern = Pattern.compile(rangePattern);
        Matcher matcher = pattern.matcher(regex);
        int lastIndex = 0;

        while (matcher.find()) {
            expandedRegex.append(regex, lastIndex, matcher.start());

            if (matcher.group(1) != null) { // Inside parentheses
                expandedRegex.append("(").append(expandRangesAndChars(matcher.group(1))).append(")");
            } else if (matcher.group(2) != null && matcher.group(3) != null) { // Range
                char startChar = matcher.group(2).charAt(0);
                char endChar = matcher.group(3).charAt(0);
                for (char c = startChar; c <= endChar; c++) {
                    expandedRegex.append(c);
                    if (c != endChar) {
                        expandedRegex.append('|');
                    }
                }
            } else if (matcher.group(4) != null) { // Individual characters
                String chars = matcher.group(4);
                for (int i = 0; i < chars.length(); i++) {
                    expandedRegex.append(chars.charAt(i));
                    if (i < chars.length() - 1) {
                        expandedRegex.append('|');
                    }
                }
            } else if (matcher.group(5) != null) { // Special characters
                String specialChar = "";
                switch (matcher.group(5)) {
                    case "\\t":
                        specialChar = "\t";
                        break;
                    case "\\s":
                        specialChar = " ";
                        break;
                }
                expandedRegex.append(specialChar);
            }

            lastIndex = matcher.end();
        }

        expandedRegex.append(regex.substring(lastIndex));
        return expandedRegex.toString();
    }
}
