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
                    if (i >= expresion.length())
                        break;
                    c = expresion.charAt(i);
                }
                //Recursion
                cache = exExpre(cache);
                i += 1;
                if (i >= expresion.length())
                    return cache;
                c = expresion.charAt(i);
                newE += concatPra(c, cache);
                continue;
            }else if (c == ')'){
                String cache = "";
                int parentesis = 0;
                int j = newE.length() - 1;
                c = newE.charAt(j);
                while (true){
                    if (c == ')')
                        parentesis -= 1;
                    
                    if (c == '(')
                        parentesis += 1;
                    
                    if (parentesis == 0)
                        break;
                        
                    j -= 1;
                    c = newE.charAt(j);
                }
                for (int k = j; k<newE.length(); k ++){
                    cache += newE.charAt(k);
                }
                newE = newE.substring(0, j) + newE.charAt(j);
                i += 1;
                if (i >= expresion.length())
                    continue;
                c = expresion.charAt(i);
                
                newE += concatPra(c, cache);
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

    private String concatPra(Character c, String cache){
        if (c == '?'){
            cache = "(" + cache + ")" + "|ε)";
        }else if (c == '+'){
            cache = cache + ")" + "(" + cache + ")*";
        }else {
            cache += ")";
        }
        return cache;
    }
}

