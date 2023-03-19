package arem;

import java.util.Scanner;

import arem.handlers.AFNhandler;
import arem.handlers.AFNtoAFDhandler;

/**
 * Hello world!
 */
public final class App {

    private static void menuPrincipal(){
        System.out.println("""
            --------Menu----------
                1. AFN
                2. AFN --> AFD
                3. Opciones
                4. Salir
            ----------------------
        """); 
    }

    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) { 
        menuPrincipal();
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        switch(opcion){
            case 1:
                new AFNhandler();
                break;
            
            case 2:
                new AFNtoAFDhandler();
                break;

            case 3:
                break;
            
            default:
                System.out.println("Opcion incorrecta");
                break;
        }
    }
}
