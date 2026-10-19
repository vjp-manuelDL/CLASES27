package manueldelgadolopes_ut1.tarea11;

/**
 *
 *
 */
public class Repartidores implements Runnable {

    int flyers = 0;
    String nombre;
    static int suma = 0;

    public Repartidores(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        while (suma < 1001) {
            synchronized (Repartidores.class) {
                System.out.println("El repartidor " + nombre
                        + " ha repartido " + flyers + " flyers.");
                flyers++;
                suma++;

            }

        }
     

    }

    public int getFlyers() {
        return flyers;
    }

    public void setFlyers(int flyers) {
        this.flyers = flyers;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        Repartidores.suma = suma;
    }

}
