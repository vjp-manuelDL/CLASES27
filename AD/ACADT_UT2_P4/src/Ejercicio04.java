import com.google.gson.Gson;

public class Ejercicio04 {
    public static void main(String[] args) {
        // Simulamos un texto JSON que hemos leido de un archivo
        String textoJson = "{\"titulo\":\"Dracula\", \"autor\":\"Bram Stoker\"}";

        Gson gson = new Gson();

        // Convertimos el texto JSON en un objeto Libro de verdad
        Libro libroRecuperado = gson.fromJson(textoJson, Libro.class);

        System.out.println("Objeto recuperado:");
        System.out.println("Titulo: " + libroRecuperado.getTitulo());
        System.out.println("Autor: " + libroRecuperado.getAutor());
    }
}