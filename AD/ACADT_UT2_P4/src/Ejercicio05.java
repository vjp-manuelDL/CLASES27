import com.google.gson.Gson;

public class Ejercicio05 {
    public static void main(String[] args) {
        // Simulamos un texto JSON que contiene una lista (empieza por corchete [ )
        String textoJsonArray = "[" +
                "{\"titulo\":\"Libro Uno\", \"autor\":\"Autor Uno\"}," +
                "{\"titulo\":\"Libro Dos\", \"autor\":\"Autor Dos\"}" +
                "]";

        Gson gson = new Gson();

        // Le decimos a Gson que nos convierta el texto en una formacion (Array) de Libros
        Libro[] listaLibros = gson.fromJson(textoJsonArray, Libro[].class);

        System.out.println("Lista de objetos recuperados:");

        // Recorremos la lista para imprimir cada libro
        for (int i = 0; i < listaLibros.length; i++) {
            Libro l = listaLibros[i];
            System.out.println("- " + l.getTitulo() + " (por " + l.getAutor() + ")");
        }
    }
}