package repaso_ut1_16;

/**
 *
 * @author alumno
 */
public class Persona extends Thread{
    String nombre;
    private Baño baño;

    public Persona(String nombre, Baño baño) {
        this.nombre = nombre;
        this.baño = baño;
    }

    @Override
    public void run(){
        for (int i = 0; i < 5; i++) {
        }
            try {
                baño.entrar(this.nombre);
                
                int tiempobaño = (int) (Math.random() * 20 +1);
                System.out.println("La persona " + this.nombre + " lleva " + tiempobaño + " ms en el baño");
                Thread.sleep(tiempobaño);
                
                baño.salir(this.nombre);
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    

