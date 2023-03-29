package arem.handlers;

import arem.Funciones.Lenguaje;

public class AFDhandler extends handler {


    public AFDhandler() {
        super();
    }

    private void AFD(){
        System.out.println(expresion);
    }

    @Override
    protected String getInput() {
        String input = super.getInput();
        
        Lenguaje.EndofLine = "#";
        input += "#";

        return input;
    }

    @Override
    protected String getExpresion() {
        String expresion = super.getExpresion();

        return expresion;
    }
}
