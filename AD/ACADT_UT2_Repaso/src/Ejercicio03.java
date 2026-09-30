import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio03 {
    public static void main(String[] args) {
        try {
            // 1. Cargamos las palabras tabu desde tabu.txt
            List<String> palabrasTabu = new ArrayList<>();
            BufferedReader lectorTabu = new BufferedReader(new FileReader("tabu.txt"));
            String palabra;
            while ((palabra = lectorTabu.readLine()) != null) {
                if (!palabra.trim().isEmpty()) {
                    palabrasTabu.add(palabra.trim().toLowerCase());
                }
            }
            lectorTabu.close();

            // 2. Accedemos a la carpeta "emails"
            File carpetaEmails = new File("emails");
            if (!carpetaEmails.exists()) {
                System.out.println("La carpeta 'emails' no existe.");
                return;
            }

            File[] ficherosEmails = carpetaEmails.listFiles();
            if (ficherosEmails == null) return;

            // 3. Analizamos cada email
            for (int i = 0; i < ficherosEmails.length; i++) {
                File archivoEmail = ficherosEmails[i];
                if (archivoEmail.isFile()) {
                    BufferedReader lectorEmail = new BufferedReader(new FileReader(archivoEmail));
                    String linea;
                    String remitente = "Desconocido";
                    int contadorAlarmantes = 0;

                    while ((linea = lectorEmail.readLine()) != null) {
                        if (linea.toLowerCase().startsWith("remitente:")) {
                            remitente = linea.substring(10).trim();
                        }

                        // Limpiamos la linea de puntos, comas y simbolos, extrayendo solo las palabras
                        String[] palabras = linea.toLowerCase().split("[^a-záéíóúñ0-9]+");
                        for (int j = 0; j < palabras.length; j++) {
                            if (palabrasTabu.contains(palabras[j])) {
                                contadorAlarmantes++;
                            }
                        }
                    }
                    lectorEmail.close();

                    System.out.println("-> Archivo '" + archivoEmail.getName() + "' analizado. Palabras tabu encontradas: " + contadorAlarmantes);

                    // Si supera las 3 palabras alarmantes, avisamos
                    if (contadorAlarmantes > 3) {
                        System.out.println("El trabajador " + remitente + " esta infringiendo las normativas de seguridad");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}