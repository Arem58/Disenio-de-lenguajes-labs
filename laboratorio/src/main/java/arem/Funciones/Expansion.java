package arem.Funciones;

public class Expansion {
    private String expresion;

    public String getExpresion() {
        return expresion;
    }

    public Expansion(String expresion) {
        this.expresion = exExpre(expresion);
    }

    public static String exExpre(String expresion) {
        StringBuilder newE = new StringBuilder();
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            if (c == '(') {
                String cache = "" + c;
                i += 1;
                c = expresion.charAt(i);
                boolean continua = true;
                while (c != ')') {
                    if (c == '(') {
                        newE.append(cache);
                        continua = false;
                        i -= 1;
                        break;
                    }
                    cache += c;
                    i += 1;
                    if (i >= expresion.length())
                        break;
                    c = expresion.charAt(i);
                }
                if (!continua)
                    continue;
                // Recursion
                // cache = exExpre(cache);
                cache += c;
                // if (i >= expresion.length())
                // return cache;
                c = (i + 1 < expresion.length()) ? expresion.charAt(i + 1) : '\0';
                if (c == '+' || c == '?')
                    i += 1;
                newE.append(concatPra(c, cache));
                continue;
            } else if (c == ')') {
                if ((i + 1 < expresion.length() && (expresion.charAt(i + 1) != '+' && expresion.charAt(i + 1) != '?')) || i == expresion.length() - 1) {
                    newE.append(c);
                    continue;
                }
                String cache = "";
                newE.append(")");
                int parentesis = 0;
                int j = newE.length() - 1;
                int originalJ = j;
                // if (i + 1 < expresion.length()
                //         && (newE.charAt(j) != ')' && expresion.charAt(i + 1) == '+' || expresion.charAt(i + 1) == '?'))
                //     parentesis -= 1;
                while (true) {
                    if (c == ')')
                        parentesis -= 1;

                    if (c == '(')
                        parentesis += 1;

                    if (parentesis == 0) {
                        break;
                    }

                    j -= 1;
                    c = newE.charAt(j);
                }
                if (j == originalJ) {
                    newE.append(')');
                    continue;
                }
                for (int k = j; k < newE.length(); k++) {
                    cache += newE.charAt(k);
                }
                newE = new StringBuilder(newE.substring(0, j));
                i += 1;
                if (i >= expresion.length())
                    continue;
                c = expresion.charAt(i);
                newE.append(concatPra(c, cache));
                continue;
            }

            if (i != expresion.length() - 1) {
                if (c != '?' && c != '+' && expresion.charAt(i + 1) != '?' && expresion.charAt(i + 1) != '+') {
                    newE.append(c);
                    continue;
                }
            } else {
                if (c != '?' && c != '+') {
                    newE.append(c);
                    continue;
                }
            }

            if (c == '?') {
                c = expresion.charAt(i - 1);
                newE.append("(").append(c).append("|ε)");
                continue;
            }

            if (c == '+') {
                c = expresion.charAt(i - 1);
                newE.append(concatPra(c, String.valueOf(c)));
                continue;
            }
        }
        return newE.toString();
    }

    private static String concatPra(char operator, String cache) {
        if (operator == '?') {
            cache = "(" + cache + "|ε)";
        } else if (operator == '+') {
            cache = "(" + cache + cache + "*)";
        }
        return cache;
    }
}
