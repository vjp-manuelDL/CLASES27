package logica;

/**
 * Clase que representa la lógica de un libro en la biblioteca.
 */
public class Libro {
    // Atributos privados requeridos
    private int numPaginas;
    private String titulo;
    private boolean almacenado;

    // Constructor con todos los campos
    public Libro(int numPaginas, String titulo, boolean almacenado) {
        this.numPaginas = numPaginas;
        this.titulo = titulo;
        this.almacenado = almacenado;
    }

    // Getters y Setters
    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAlmacenado() {
        return almacenado;
    }

    public void setAlmacenado(boolean almacenado) {
        this.almacenado = almacenado;
    }

    /**
     * Retorna la frase descriptiva formateada del libro.
     */
    public String obtenerTextoResultado() {
        String estado = almacenado ? "está en la biblioteca" : "no está en la biblioteca";
        return titulo + ", de " + numPaginas + " páginas, " + estado + ".";
    }
}