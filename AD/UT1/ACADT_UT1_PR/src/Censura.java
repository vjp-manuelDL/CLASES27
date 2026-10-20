import java.io.*;
import java.util.HashMap;
import java.util.Map;

class Censura {

    public void aplicarCensura(String rutaEntrada, String rutaCensura, String rutaSalida) {
        File fEntrada = new File(rutaEntrada);
        File fCensura = new File(rutaCensura);
        File fSalida = new File(rutaSalida);

        // usamos una estructura de diccionario para guardar las palabras censuradas
        HashMap<String, String> diccionarioCensura = new HashMap<>();

        // aqui leemos el fichero de censura y llenamos el diccionario
        try {
            BufferedReader brCensura = new BufferedReader(new FileReader(fCensura));
            String lineaCensura = "";

            while ((lineaCensura = brCensura.readLine()) != null) {
                String[] partes = lineaCensura.split(":");
                // si la linea tiene formato correcto palabra:sustituto la guardamos
                if (partes.length == 2) {
                    diccionarioCensura.put(partes[0], partes[1]);
                }
            }
            brCensura.close();

        } catch (FileNotFoundException e) {
            System.out.println("Fichero de censura no encontrado.");
            return; // salimos del metodo si no hay censura
        } catch (IOException e) {
            System.out.println("Error al leer el fichero de censura.");
            return;
        }

        // aqui leemos el fichero de entrada, aplicamos censura y escribimos en el fichero de salida
        try {
            BufferedReader brEntrada = new BufferedReader(new FileReader(fEntrada));
            BufferedWriter bwSalida = new BufferedWriter(new FileWriter(fSalida));

            String linea = "";

            while ((linea = brEntrada.readLine()) != null) {
                // aqui sustituimos cada palabra censurada por su valor correspondiente
                for (Map.Entry<String, String> entradaDiccionario : diccionarioCensura.entrySet()) {
                    linea = linea.replace(entradaDiccionario.getKey(), entradaDiccionario.getValue());
                }

                // aqui escribimos la linea ya modificada y añadimos salto de linea
                bwSalida.write(linea);
                bwSalida.newLine();
            }

            // aqui cerramos los flujos al terminar
            brEntrada.close();
            bwSalida.close();

            System.out.println("Proceso finalizado. Se ha creado el fichero de salida con la censura aplicada.");

        } catch (FileNotFoundException e) {
            System.out.println("Fichero de entrada no encontrado.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al aplicar la censura y generar el fichero.");
        }
    }
}