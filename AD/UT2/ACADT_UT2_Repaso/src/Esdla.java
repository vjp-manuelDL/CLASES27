import java.util.ArrayList;
import java.util.List;

public class Esdla {
    private String autor;
    private List<Ciudad> ciudades = new ArrayList<>();
    private Personaje personajes; // <--- Cambiado a plural para que coincida con el XML

    public Esdla() {}

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public Personaje getPersonajes() { // <--- Get en plural
        return personajes;
    }

    public void setPersonajes(Personaje personajes) { // <--- Set en plural
        this.personajes = personajes;
    }
}