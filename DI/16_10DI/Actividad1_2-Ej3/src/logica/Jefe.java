package logica;

public class Jefe extends Personaje {

    public Jefe(String nombre) {
        super(nombre, 35);
        setDefensa(getDefensa() + 2);
    }

    @Override
    public String ataca(Personaje defender) {
        if (estaFlintcheado()) {
            return "¡El Jefe " + getNombre() + " ruge desorientado (flintcheado) y pierde el turno!";
        }

        // Dodge del jugador (El arquero esquiva con 35% de chance al Jefe; las demás clases un 15%)
        int probDodgeHeroe = (defender instanceof Arquero) ? 35 : 15;
        if (random.nextInt(100) < probDodgeHeroe) {
            return "¡" + defender.getNombre() + " logra ESQUIVAR (DODGE) el golpe del Jefe " + getNombre() + "!";
        }

        // Golpe torpe (55% acierto)
        if (random.nextInt(100) >= 55) {
            return "El Jefe " + getNombre() + " intenta aplastarte pero falla el golpe.";
        }

        int danoCalculado = (getFuerza() + 3) - defender.getDefensa();
        int danoReal = Math.max(1, danoCalculado);

        // Crítico devastador (20% prob, x2 daño)
        boolean esCritico = random.nextInt(100) < 20;
        if (esCritico) {
            danoReal *= 2;
        }

        defender.setVida(defender.getVida() - danoReal);

        String msg = "¡EL JEFE " + getNombre() + (esCritico ? " asesta un IMPACTO CRÍTICO DEVASTADOR a " : " ataca a ") 
                + defender.getNombre() + " causándole " + danoReal + " de daño!";

        // 35% probabilidad de flintchear al jugador
        if (random.nextInt(100) < 35 && defender.vivo()) {
            defender.setFlintcheado(true);
            msg += " ¡Te deja brutalmente FLINTCHEADO!";
        }

        return msg;
    }
}