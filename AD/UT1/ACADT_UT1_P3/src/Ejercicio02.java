import java.io.*;
import java.util.Scanner;

public class Ejercicio02 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        File f = new File("tiempo.dat");

        // aqui se guardan los datos introducidos por teclado
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
            System.out.println("Introduce los datos de la semana (7 dias):");

            for (int i = 0; i < 7; i++) {
                System.out.println("Dia " + (i + 1));
                System.out.print("Fecha (ej. Lunes): ");
                String fecha = entrada.nextLine();

                System.out.print("Temperatura minima: ");
                double min = entrada.nextDouble();

                System.out.print("Temperatura maxima: ");
                double max = entrada.nextDouble();
                entrada.nextLine(); // limpiar

                // aqui se escriben los datos primitivos en el fichero binario
                dos.writeUTF(fecha);
                dos.writeDouble(min);
                dos.writeDouble(max);
            }
            dos.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo crear el archivo tiempo.dat");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida");
        } catch (Exception e) {
            System.out.println("Datos invalidos introducidos");
        }

        // aqui se lee el fichero para buscar el dia con mas calor
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            String diaMasCaluroso = "";
            double tempMaxAbsoluta = -999.0;

            try {
                while (true) {
                    String fecha = dis.readUTF();
                    double min = dis.readDouble();
                    double max = dis.readDouble();

                    if (max > tempMaxAbsoluta) {
                        tempMaxAbsoluta = max;
                        diaMasCaluroso = fecha;
                    }
                }
            } catch (EOFException e) {
                // fin del archivo capturado correctamente
                dis.close();
            }

            System.out.println("El dia mas caluroso fue: " + diaMasCaluroso + " con " + tempMaxAbsoluta + " grados.");

        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el fichero para leer");
        } catch (IOException e) {
            System.out.println("Error de lectura del fichero");
        }

        entrada.close();
    }
}