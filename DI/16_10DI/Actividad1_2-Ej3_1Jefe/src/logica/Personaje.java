package logica;

import java.util.Random;

public abstract class Personaje {
    private String nombre;
    private int fuerza;  // Aleatorio entre 3 y 10
    private int defensa; // Aleatorio entre 0 y 5
    private int vida;    
    private boolean flintcheado; // Si es true, pierde su próximo turno

    protected Random random = new Random();

    public Personaje(String nombre, int vida) {
        this.nombre = nombre;
        this.vida = vida;
        this.fuerza = random.nextInt(8) + 3; // 3 a 10
        this.defensa = random.nextInt(6);    // 0 a 5
        this.flintcheado = false;
    }

    public boolean vivo() {
        return this.vida > 0;
    }

    // Comprueba si está flintcheado y consume el estado
    public boolean estaFlintcheado() {
        if (this.flintcheado) {
            this.flintcheado = false; // Se recupera para el turno subsiguiente
            return true;
        }
        return false;
    }

    public abstract String ataca(Personaje defender);

    // Getters y Setters
    public boolean isFlintcheado() { return flintcheado; }
    public void setFlintcheado(boolean flintcheado) { this.flintcheado = flintcheado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { this.fuerza = fuerza; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = Math.max(0, vida); }
}