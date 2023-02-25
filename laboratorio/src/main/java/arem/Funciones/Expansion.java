package arem.Funciones;

public class Expansion {
    private String expresion;

    public String getExpresion() {
        return expresion;
    }

    public Expansion(String expresion) {
        this.expresion = exExpre(expresion);
    }

    public String exExpre(String expresion){
        String newE = "";
        for (int i = 0; i < expresion.length(); i++){
            char c = expresion.charAt(i);
            
            if ( c == '('){
                String cache = "";
                newE += c;
                i += 1; 
                c = expresion.charAt(i);
                while (c != ')'){
                    cache += c;
                    i += 1;
                    c = expresion.charAt(i);
                }
                //Recursion
                cache = exExpre(cache);
                i += 1;
                c = expresion.charAt(i);
                if (c == '?'){
                    cache = "(" + cache + ")" + "|ε)";
                }else if (c == '+'){
                    cache = cache + ")" + "(" + cache + ")*";
                }
                newE += cache;
                continue;
            }
            if (i != expresion.length() - 1){
                if (c != '?' && c != '+' && expresion.charAt(i+1) != '?' && expresion.charAt(i + 1) != '+'){
                    newE += c;
                    continue;
                }
            }else {
                if (c != '?' && c != '+'){
                    newE += c;
                    continue;
                }
            }

            if (c == '?'){
                c = expresion.charAt(i-1);
                newE += "(" + String.valueOf(c) + "|ε)";
                continue;
            }

            if (c == '+'){
                c = expresion.charAt(i-1);
                newE += String.valueOf(c) + String.valueOf(c) + "*";
                continue;
            }
        }
        return newE;
    }
}

