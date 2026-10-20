// Archivo: Empleado.java
public class Empleado implements Comparable<Empleado> {
    private String nombre;
    private String apellido;
    private int sueldo;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getSueldo() { return sueldo; }
    public void setSueldo(int sueldo) { this.sueldo = sueldo; }

    // Este metodo decide como se ordenan (restando los sueldos)
    @Override
    public int compareTo(Empleado otro) {
        return this.sueldo - otro.sueldo;
    }
}