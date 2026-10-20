public class Libro {
    private String isbn, titulo, autor, editorial;

    public Libro(String isbn, String titulo, String autor, String editorial) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
    }
    // Getters
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
}