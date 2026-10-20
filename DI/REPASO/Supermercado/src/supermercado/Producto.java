package supermercado;

public class Producto {
    String nombre;
    int candidad;
    String categoria;

    public Producto(String nombre, int candidad, String categoria) {
        this.nombre = nombre;
        this.candidad = candidad;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCandidad() {
        return candidad;
    }

    public void setCandidad(int candidad) {
        this.candidad = candidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    
    
    
}
