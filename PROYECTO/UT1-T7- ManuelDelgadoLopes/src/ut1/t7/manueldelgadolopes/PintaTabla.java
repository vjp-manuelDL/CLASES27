package ut1.t7.manueldelgadolopes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PintaTabla extends Thread {
    private int numero;

    public PintaTabla(int numero) {
        this.numero = numero;
    }

    @Override
    public void run() {
        File directorio = new File("TABLAS");
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        File archivo = new File(directorio, numero + ".txt");

        try (FileWriter escritor = new FileWriter(archivo)) {
            // Formato exacto que se ve en la captura del fichero 4.txt
            escritor.write("La Tabla del " + numero + " es:\n");
            escritor.write("------------------------------\n\n");
            for (int i = 0; i <= 10; i++) {
                escritor.write(numero + " x " + i + " = " + (numero * i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
