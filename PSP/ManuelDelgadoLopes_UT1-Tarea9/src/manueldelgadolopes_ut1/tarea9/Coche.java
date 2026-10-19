package manueldelgadolopes_ut1.tarea9;

/**
 *
 * @author alumno
 */
public class Coche implements Runnable {

    int km = 1;
    int num = 1;
    private static final Object Puente = new Object();

    public Coche(int num) {
        this.num = num;
    }

    public int getKm() {
        return km;
    }
    
    public void pausa() {
    try {
        Thread.sleep(500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

    @Override
    public void run() {

        for (km = 0; km < 6; km++) {
            System.out.println("El coche " + num + " va por el KM:" + km);
            pausa();
        }

        synchronized (Puente) {
            for (km = 6; km <= 9; km++) {
                System.out.println("El coche" + num + " va por el km " + km + " del puente");
                pausa();
            }
        }

        for (km =10 ; km < 13; km++) {
            System.out.println("El coche" + num + " va por el km " + km + " del puente");
        }

    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getNum() {
        return num;

    }

    public void setNum(int num) {
        this.num = num;

    }

}
