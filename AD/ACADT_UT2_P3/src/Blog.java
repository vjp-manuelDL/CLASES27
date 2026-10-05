import java.util.ArrayList;
import java.util.List;

public class Blog {
    private Autor autor;
    // La lista se llamará "entradas" en Java, pero en el XML las etiquetas son "entrada"
    private List<Entrada> entradas = new ArrayList<Entrada>();

    public Blog() {}

    public void add(Entrada entrada) {
        entradas.add(entrada);
    }

    public Autor getAutor() { return autor; }
    public List<Entrada> getEntradas() { return entradas; }
}