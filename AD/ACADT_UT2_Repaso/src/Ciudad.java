public class Ciudad {
    private String nombre;
    private String faccion;

    public Ciudad(String nombre, String faccion) {
        this.nombre = nombre;
        this.faccion = faccion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFaccion() { return faccion; }
    public void setFaccion(String faccion) { this.faccion = faccion; }
}