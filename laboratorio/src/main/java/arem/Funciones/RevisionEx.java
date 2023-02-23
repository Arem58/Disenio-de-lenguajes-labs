package arem.Funciones;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RevisionEx {
    private String expresion;
    
    private boolean correcta;
    private boolean sus = false;
    
    private static final Map<Integer, String> errorMes;
    
    static {
        Map<Integer, String> tempMap = new HashMap<>();
        tempMap.put(0, "La expresion regular es correcta ");
        tempMap.put(1, "La expresion no esta balanceada");
        tempMap.put(2, "La expresion tiene parentesis vacíos ()");    
        tempMap.put(3, "La expresion comienza con una operacion");   
        errorMes = Collections.unmodifiableMap(tempMap);
    }
    
    public boolean isSus() {
        return sus;
    }

    public String getExpresion() {
        return expresion;
    }
    
    public boolean isCorrectaa() {
        return correcta;
    }

    public RevisionEx(String expresion) {
        this.expresion = expresion;
        revision();
    }

    public void updateExpresion(String newExpersion){
        this.expresion = newExpersion;
        revision();
    }

    private void mensajes(int errorM){
        System.out.println(errorMes.get(errorM) + expresion);
    }

    //Revisa la expresion regular si es correcta la expresion 
    private void revision(){

        int errorM = 0;
        
        Stack<Character> stack = new Stack<Character>();
        List<Character> operadores = Arrays.asList('|', '?', '*', '+', '^');
        String newE = "";
        char c;
        if (!operadores.contains(expresion.charAt(0))){
            for (int i = 0; i < expresion.length(); i++){
                c = expresion.charAt(i);
                //Elimina los espacion vacios
                if (c == ' '){
                    continue;
                }

                //Revisa que no haya parentesis en blanco
                if (c == '('){
                    stack.push(c);
                    if (expresion.charAt(i + 1) == ')'){
                        errorM = 2;
                        correcta = false;
                    }
                }else if (c == ')'){
                    stack.pop();
                }
                
                //Revisa si hay mas de un operador seguido
                if (operadores.contains(c)){
                    if (c == '?' || c == '+'){
                        sus = true;
                    }
                    
                    //Revisa si es el ultimo caracter
                    if (i != expresion.length() - 1){
                        char c2 = expresion.charAt(i + 1);
                        while (c == c2) {
                            i += 1;
                            c2 = expresion.charAt(i + 1);
                        }
                    }
                }
    
                newE += c;
            }
            this.expresion = newE;
    
            if (stack.isEmpty()){
                correcta = true;
            }else{
                correcta = false;
                errorM = 1;
            }
        }else{
            errorM = 3;
            correcta = false;
        }

        mensajes(errorM);
    }

}


