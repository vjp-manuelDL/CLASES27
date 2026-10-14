package logica;

import java.util.Random;

public class Personaje {
    private String nombre;
    private int fuerza;  // Aleatorio entre 3 y 10
    private int defensa; // Aleatorio entre 0 y 5
    private int vida;    // Inicia en 10

    private Random random = new Random();

    public Personaje(String nombre) {
        this.nombre = nombre;
        this.vida = 10;
        this.fuerza = random.nextInt(8) + 3; // 3 a 10
        this.defensa = random.nextInt(6);    // 0 a 5
    }

    public boolean vivo() {
        return this.vida > 0;
    }

    public String ataca(Personaje defender) {
        // 70% probabilidad de acierto
        boolean acierta = random.nextInt(100) < 70;

        if (acierta) {
            int danoCalculado = this.fuerza - defender.getDefensa();
            int danoReal = Math.max(1, danoCalculado); // Mínimo 1 punto de daño

            defender.setVida(defender.getVida() - danoReal);

            return this.nombre + " ataca a " + defender.getNombre() + " y le quita " + danoReal + " puntos de vida.";
        } else {
            return this.nombre + " ataca pero falla el golpe.";
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, vida);
    }
}