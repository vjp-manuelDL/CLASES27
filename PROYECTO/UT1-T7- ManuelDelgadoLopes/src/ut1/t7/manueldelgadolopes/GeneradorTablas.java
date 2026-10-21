package ut1.t7.manueldelgadolopes;

public class GeneradorTablas {
    
    // Este es el método que te faltaba y por el que daba error en rojo
    public void crearTabla(int numero) {
        PintaTabla hilo = new PintaTabla(numero);
        hilo.start();
    }
}