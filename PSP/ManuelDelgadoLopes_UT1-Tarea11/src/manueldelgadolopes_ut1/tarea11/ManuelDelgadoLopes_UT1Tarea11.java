package manueldelgadolopes_ut1.tarea11;

/**
 *
 * @author alumno
 */
public class ManuelDelgadoLopes_UT1Tarea11 {

    public static void main(String[] args) {
        try {
            Repartidores rep1 = new Repartidores("1");
            Repartidores rep2 = new Repartidores("2");
            Repartidores rep3 = new Repartidores("3");
            Repartidores rep4 = new Repartidores("4");
            Repartidores rep5 = new Repartidores("5");

            Thread hilo1 = new Thread(rep1);
            Thread hilo2 = new Thread(rep2);
            Thread hilo3 = new Thread(rep3);
            Thread hilo4 = new Thread(rep4);
            Thread hilo5 = new Thread(rep5);

            hilo1.start();
            hilo2.start();
            hilo3.start();
            hilo4.start();
            hilo5.start();

            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
            hilo5.join();

        } catch (Exception e) {
        }

        System.out.println("La suma total son 1000 Flyers");
        System.out.println("Al final quedan 0 flyers");
    }

}
