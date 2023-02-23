package arem.Funciones;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Postfix {    
    private static final Map<Character, Integer> precedence;
    
    static {
        Map<Character, Integer> REs = new HashMap<>();
        REs.put('(', 1);
        REs.put('|', 2);    
        REs.put('.', 3);   
        REs.put('?', 4);
        REs.put('*', 4);
        REs.put('+', 4);
        REs.put('^', 5);
        precedence = Collections.unmodifiableMap(REs);
    }

    //Agrega el formato que se necesita para pasar la expresion a postfix
    private String formatRegEx(String regex){
        String res = "";
        List<Character> operadores = Arrays.asList('|', '?', '*', '+', '^');
        List<Character> binaryOperadores = Arrays.asList('^', '|');
        
        for (int i = 0; i < regex.length(); i++ ){
            char c1 = regex.charAt(i);

            if (i + 1 < regex.length()){
                char c2 = regex.charAt(i + 1);
                res += c1;

                if (c1 != '(' && c2 != ')' && !operadores.contains(c2) && !binaryOperadores.contains(c1)){
                    res += ".";
                } 
            }
        }
        res += regex.charAt(regex.length() - 1);
        // System.out.println(res);
        return res;
    }

    private Integer getPrecedence(Character operador){
        Integer precedencia = precedence.get(operador);
        return precedencia == null ? 6 : precedencia;
    };

    //Metodo de conversion a postfix
    public String infixToPostfix(String regex){
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<Character>(); 
        String formattedREgEx = formatRegEx(regex);
      
        formattedREgEx.chars().forEach(c -> {
            char character = (char) c;
            switch (character){
                case '(':
                    stack.push(character);
                break;
                case ')':
                    while (stack.peek() != '('){
                        postfix.append(stack.pop());
                    }
                    stack.pop();
                break;
                default:
                    while (stack.size() > 0){
                        char peekedChar = stack.peek();
                        int peekedCharPrecedence = getPrecedence(peekedChar);
                        int currentCharPrecedende = getPrecedence(character);
                        
                        if(peekedCharPrecedence >= currentCharPrecedende){
                            postfix.append(stack.pop());
                        }else{
                            break;
                        }
                    }
                    stack.push(character);
                    break;
            }
        });

        while (stack.size() > 0) {
            postfix.append(stack.pop());            
        }
        return postfix.toString();
    }
}
