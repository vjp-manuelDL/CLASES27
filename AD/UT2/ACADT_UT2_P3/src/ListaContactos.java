import java.util.ArrayList;
import java.util.List;

public class ListaContactos {
    private List<Contacto> lista = new ArrayList<Contacto>();

    public ListaContactos() { }

    public void add(Contacto con) {
        lista.add(con);
    }

    public List<Contacto> getListaContactos() {
        return lista;
    }
}