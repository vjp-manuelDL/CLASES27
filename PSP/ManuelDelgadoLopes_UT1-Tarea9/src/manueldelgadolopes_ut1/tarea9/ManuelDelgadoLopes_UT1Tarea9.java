package manueldelgadolopes_ut1.tarea9;

/**
 *
 * @author alumno
 */
public class ManuelDelgadoLopes_UT1Tarea9 {

    public static void main(String[] args) {

        Coche coche1 = new Coche(1);
        Coche coche2 = new Coche(2);
        Coche coche3 = new Coche(3);
        Coche coche4 = new Coche(4);

        Thread hilo1 = new Thread(coche1);
        Thread hilo2 = new Thread(coche2);
        Thread hilo3 = new Thread(coche3);
        Thread hilo4 = new Thread(coche4);
        
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

    }

}
