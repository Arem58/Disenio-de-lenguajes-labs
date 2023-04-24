package arem.Funciones;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import arem.assets.archivosJava.Expresion.ExpresionData;

public class MapIO {
    public static void saveDataToFile(String fileName, ExpresionData data) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(data);
        }
    }

    public static ExpresionData loadDataFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (ExpresionData) objectInputStream.readObject();
        }
    }
}
