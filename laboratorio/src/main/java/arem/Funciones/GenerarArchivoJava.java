package arem.Funciones;

import java.io.*;

public class GenerarArchivoJava {

    public static void main(String[] args) {

        // Definir la ruta y el nombre del archivo que se va a generar
        String rutaArchivo = "/home/arem/Documents/Universidad/Lenguaje de Programacion/Laboratorios/Disenio-de-lenguajes-labs/laboratorio/src/main/java/arem/assets/archivosJava/MiArchivo.java";

        try {
            // Crear una instancia de FileWriter con el nombre del archivo
            FileWriter archivo = new FileWriter(rutaArchivo);

            // Escribir el contenido del archivo, que incluye la clase con el método main 
            archivo.write("package arem.assets.archivosJava;\n");
            archivo.write("public class MiArchivo {\n\n");
            archivo.write("    public static void main(String[] args) {\n");
            archivo.write("        System.out.println(\"Hola, mundo!\");\n");
            archivo.write("    }\n\n");
            archivo.write("}\n");

            // Cerrar el archivo
            archivo.close();

            // Mostrar un mensaje de éxito
            System.out.println("El archivo " + rutaArchivo + " se ha generado exitosamente.");

        } catch (IOException e) {
            // Mostrar un mensaje de error si no se pudo generar el archivo
            System.out.println("Ocurrió un error al generar el archivo " + rutaArchivo + ": " + e.getMessage());
        }
    }
}
