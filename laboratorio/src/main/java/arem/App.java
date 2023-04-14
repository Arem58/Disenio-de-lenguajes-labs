package arem;

import java.io.IOException;
import java.util.Scanner;

import arem.handlers.AFDhandler;
import arem.handlers.AFNhandler;
import arem.handlers.AFNtoAFDhandler;
import arem.handlers.AFNtoAFDmini;
import arem.handlers.GALhandler;

/**
 * Hello world!
 */
public final class App {

    private static void menuPrincipal() {
        System.out.println("""
                    --------Menu----------
                        1. AFN
                        2. AFN --> AFD
                        3. AFN --> AFD mini
                        4. AFD
                        5. GLA
                        6. Salir
                    ----------------------
                """);
    }

    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException {
        menuPrincipal();
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                new AFNhandler();
                break;

            case 2:
                new AFNtoAFDhandler();
                break;

            case 3:
                new AFNtoAFDmini();
                break;

            case 4:
                new AFDhandler();
                break;

            case 5:
                new GALhandler();
                break;

            default:
                System.out.println("Opcion incorrecta");
                break;
        }
    }
}
