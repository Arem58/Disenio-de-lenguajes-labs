package arem.Simulacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputHandler {

    public String handleInput(int option) {
        Scanner scanner = new Scanner(System.in);
        String data;

        if (option == 1) {
            // Leer entrada del usuario desde la consola
            data = readUserInput(scanner);
        } else {
            // Leer entrada del usuario desde un archivo
            data = handlerReadFileInput("laboratorio/src/main/java/arem/assets/Simulacion/slr-1.1.yal.run");
        }

        return data;
    }

    private String readUserInput(Scanner scanner) {
        System.out.print("Ingrese los datos: ");
        String data = scanner.nextLine();
        return data;
    }

    private String handlerReadFileInput(String filePath) {
        List<String> lines = readFileInput(filePath).stream()
            .filter(line -> !line.trim().isEmpty())
            .collect(Collectors.toList());

        int opcion;
        String expresion;

        do {
            System.out.println("\nSeleccione una expresión:");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ". " + lines.get(i));
            }
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            opcion = scanner.nextInt() - 1;

            if (opcion >= 0 && opcion < lines.size()) {
                expresion = lines.get(opcion);
                System.out.println("\nExpresión seleccionada: " + expresion + "\n");
                break;
            } else {
                System.out.println("Opción inválida. Por favor, seleccione una expresión válida.");
            }
        } while (true);

        return expresion;
    }

    private List<String> readFileInput(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            lines = Arrays.stream(content.split("(?<=\\n)"))
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return lines;
    }
}
