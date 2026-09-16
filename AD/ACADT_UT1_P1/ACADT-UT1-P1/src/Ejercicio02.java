import java.io.File;
import java.io.IOException;

public class Ejercicio02 {

    public static void main(String[] args) {

        // 1. Definimos las rutas de las carpetas
        File carpetaAD = new File("2DAM/AD");
        File carpetaPSP = new File("2DAM/PSP");

        // 2. Creamos las carpetas
        if (carpetaAD.mkdirs()) {
            System.out.println("Carpetas 2DAM/AD creadas.");
        }
        if (carpetaPSP.mkdirs()) {
            System.out.println("Carpeta 2DAM/PSP creada.");
        }

        // 3. Definimos los archivos a crear
        File archivoP1 = new File("2DAM/AD/P1.txt");
        File archivoUD1_AD = new File("2DAM/AD/UD1.txt");
        File archivoUD2_AD = new File("2DAM/AD/UD2.txt");

        File archivoUD1_PSP = new File("2DAM/PSP/UD1.txt");
        File archivoUD2_PSP = new File("2DAM/PSP/UD2.txt");

        File archivoMisNotas = new File("2DAM/misNotas.txt");

        // 4. Creamos los archivos físicos
        try {
            archivoP1.createNewFile();
            archivoUD1_AD.createNewFile();
            archivoUD2_AD.createNewFile();

            archivoUD1_PSP.createNewFile();
            archivoUD2_PSP.createNewFile();

            archivoMisNotas.createNewFile();

            System.out.println("Todos los archivos .txt han sido creados correctamente.");

        } catch (IOException e) {
            System.out.println("Ha ocurrido un error al crear los archivos: " + e.getMessage());
        }
    }
}