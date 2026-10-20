import java.io.Serializable;  // ← AÑADE ESTA LÍNEA
import java.util.ArrayList;
import java.util.List;

public class ListaLibros implements Serializable {
    private List<Libro> lista = new ArrayList<Libro>();

    public ListaLibros() { }

    public void add(Libro lib) {
        lista.add(lib);
    }

    public List<Libro> getListaLibros() {
        return lista;
    }
}