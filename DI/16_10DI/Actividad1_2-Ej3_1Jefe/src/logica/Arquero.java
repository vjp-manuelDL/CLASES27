package logica;

public class Arquero extends Personaje {

    public Arquero(String nombre) {
        super(nombre, 16);
    }

    @Override
    public String ataca(Personaje defender) {
        if (estaFlintcheado()) {
            return getNombre() + " está aturdido (flintcheado) y pierde el turno.";
        }

        int probDodge = (defender instanceof Arquero) ? 25 : 10;
        if (random.nextInt(100) < probDodge) {
            return defender.getNombre() + " ha logrado ESQUIVAR la flecha de " + getNombre() + "!";
        }

        // Gran precisión (90% acierto)
        if (random.nextInt(100) >= 90) {
            return getNombre() + " dispara pero la flecha se desvía.";
        }

        int danoCalculado = getFuerza() - defender.getDefensa();
        int danoReal = Math.max(1, danoCalculado);

        // Crítico a punto débil (30% prob, x2.5 daño)
        boolean esCritico = random.nextInt(100) < 30;
        if (esCritico) {
            danoReal = (int) (danoReal * 2.5);
        }

        defender.setVida(defender.getVida() - danoReal);

        String msg = getNombre() + (esCritico ? " asesta un TIRO CRÍTICO A LA CABEZA de " : " dispara a ") 
                + defender.getNombre() + " causando " + danoReal + " de daño.";

        // 20% probabilidad de flintchear
        if (random.nextInt(100) < 20 && defender.vivo()) {
            defender.setFlintcheado(true);
            msg += " ¡La flecha hace tambalearse al rival!";
        }

        return msg;
    }
}