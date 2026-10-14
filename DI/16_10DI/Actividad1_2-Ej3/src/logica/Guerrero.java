package logica;

public class Guerrero extends Personaje {

    public Guerrero(String nombre) {
        super(nombre, 25);
    }

    @Override
    public String ataca(Personaje defender) {
        // 1. Control de Flintcheo
        if (estaFlintcheado()) {
            return getNombre() + " (Guerrero) está aturdido (flintcheado) y pierde el turno.";
        }

        // 2. Control de Dodge (Esquiva del enemigo)
        int probDodge = (defender instanceof Arquero) ? 30 : 10;
        if (random.nextInt(100) < probDodge) {
            return defender.getNombre() + " ha logrado ESQUIVAR (DODGE) el mandoble de " + getNombre() + "!";
        }

        // 3. Probabilidad de acierto
        if (random.nextInt(100) >= 75) {
            return getNombre() + " (Guerrero) falla su potente ataque.";
        }

        // 4. Daño base + Crítico (20% probabilidad)
        int danoBase = (getFuerza() + 2) - defender.getDefensa();
        int danoReal = Math.max(1, danoBase);
        boolean esCritico = random.nextInt(100) < 20;

        if (esCritico) {
            danoReal *= 2;
        }

        defender.setVida(defender.getVida() - danoReal);

        String msg = getNombre() + (esCritico ? " asesta un GOLPE CRÍTICO a " : " golpea a ") 
                + defender.getNombre() + " infligiendo " + danoReal + " de daño.";

        // 5. Alta probabilidad de provocar Flintcheo por impacto pesado (30%)
        if (random.nextInt(100) < 30 && defender.vivo()) {
            defender.setFlintcheado(true);
            msg += " ¡El impacto deja al enemigo FLINTCHEADO!";
        }

        return msg;
    }
}