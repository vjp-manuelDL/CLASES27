import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejercicio03 {
    public static void main(String[] args) {
        try {
            if (!Files.exists(Paths.get("tabu.txt"))) {
                System.out.println("Error: No se encuentra el archivo 'tabu.txt' en la raíz.");
                return;
            }

            // Leemos las palabras tabú con la codificación de Windows
            List<String> palabrasTabu = Files.readAllLines(Paths.get("tabu.txt"), StandardCharsets.ISO_8859_1);

            // Ruta del correo a comprobar (puedes cambiarla para probar)
            String rutaEmail = "emails/email1.txt";

            File email = new File(rutaEmail);
            if (!email.exists() || !email.isFile()) {
                System.out.println("Error: No se encuentra el archivo de correo: " + rutaEmail);
                return;
            }

            // Lectura del contenido del email
            String contenido = new String(Files.readAllBytes(email.toPath()), StandardCharsets.ISO_8859_1).toLowerCase();
            int contadorAlarmantes = 0;

            // Recorremos cada palabra tabú y contamos cuántas veces aparece repetida
            for (String tabu : palabrasTabu) {
                String palabra = tabu.trim().toLowerCase();
                if (!palabra.isEmpty()) {
                    Pattern pattern = Pattern.compile(Pattern.quote(palabra));
                    Matcher matcher = pattern.matcher(contenido);
                    while (matcher.find()) {
                        contadorAlarmantes++; // Suma 1 por cada repetición encontrada
                    }
                }
            }

            // Comprobación si supera las 3 palabras alarmantes en total
            if (contadorAlarmantes > 3) {
                String trabajador = email.getName()
                        .replace("From-", "")
                        .replace(".txt", "");

                System.out.println("El trabajador " + trabajador + " está infringiendo las normativas de seguridad (Total: " + contadorAlarmantes + ")");
            } else {
                System.out.println("El trabajador del archivo " + email.getName() + " no infringe la normativa (palabras alarmantes: " + contadorAlarmantes + ").");
            }

        } catch (Exception e) {
            System.out.println("Error procesando el email: " + e.getMessage());
        }
    }
}