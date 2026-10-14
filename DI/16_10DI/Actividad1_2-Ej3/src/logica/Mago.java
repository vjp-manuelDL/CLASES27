package logica;

public class Mago extends Personaje {

    public Mago(String nombre) {
        super(nombre, 12);
    }

    @Override
    public String ataca(Personaje defender) {
        if (estaFlintcheado()) {
            return getNombre() + " (Mago) está aturdido (flintcheado) y no puede lanzar hechizos.";
        }

        // Dodge del rival (La magia es casi imposible de esquivar: solo 5%)
        if (random.nextInt(100) < 5) {
            return defender.getNombre() + " esquiva (DODGE) milagrosamente la magia de " + getNombre() + "!";
        }

        if (random.nextInt(100) >= 80) {
            return getNombre() + " (Mago) falla al canalizar su hechizo.";
        }

        // Ignora defensa + Alta probabilidad de Crítico (35%)
        int danoReal = Math.max(1, getFuerza());
        boolean esCritico = random.nextInt(100) < 35;

        if (esCritico) {
            danoReal *= 2;
        }

        defender.setVida(defender.getVida() - danoReal);

        String msg = getNombre() + (esCritico ? " lanza una EXPLOSIÓN MÁGICA CRÍTICA sobre " : " ataca con magia a ") 
                + defender.getNombre() + " ignorando su defensa y quitándole " + danoReal + " de vida.";

        // 15% de probabilidad de congelar (Flintchear)
        if (random.nextInt(100) < 15 && defender.vivo()) {
            defender.setFlintcheado(true);
            msg += " ¡La magia congela y deja FLINTCHEADO al objetivo!";
        }

        return msg;
    }
}