package ut1_p2_t7_manuel_delgado_lopes;

import gui.Interfaz;
import javax.swing.SwingUtilities;

public class UT1_P2_T7_Manuel_Delgado_Lopes {

    public static void main(String[] args) {
        // Lanza la interfaz gráfica principal ubicada en el paquete gui
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }
}