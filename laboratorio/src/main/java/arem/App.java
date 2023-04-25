package arem;

import java.io.IOException;
import java.util.Scanner;

import arem.Simulacion.AFD.AFDs;
import arem.Simulacion.AFN.AFNs;
import arem.handlers.AFDhandler;
import arem.handlers.AFNhandler;
import arem.handlers.AFNtoAFDhandler;
import arem.handlers.AFNtoAFDmini;
import arem.handlers.GALhandler;
import arem.handlers.GeneradorLexerhandler;

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
                        6. Generar Lexer
                        7. Salir
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
    public static void main(String[] args) {
        menuPrincipal();
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                AFNhandler afn = new AFNhandler();
                new AFNs(afn.getGe(), 1);
                break;

            case 2:
                new AFNtoAFDhandler();
                break;

            case 3:
                new AFNtoAFDmini();
                break;

            case 4:
                AFDhandler afd = new AFDhandler();
                AFDs.createWithStringKey(afd.getGeAFD(), 1);
                break;

            case 5:
                new GALhandler();
                break;

            case 6:
                new GeneradorLexerhandler();
                break;

            case 7:
                System.out.println("Has salido del programa");
                break;
            default:
                System.out.println("Opcion incorrecta");
                break;
        }
    }
}
