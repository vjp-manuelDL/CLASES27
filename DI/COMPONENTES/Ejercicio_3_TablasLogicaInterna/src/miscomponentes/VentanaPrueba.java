package miscomponentes;

import javax.swing.JOptionPane;

/**
 *
 *
 * @author alumno
 */
public class VentanaPrueba extends javax.swing.JFrame {

    // Constructor: crea la ventana
    public VentanaPrueba() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBotones = new javax.swing.JPanel();
        btnTotal = new javax.swing.JButton();
        btnVaciar = new javax.swing.JButton();
        panelGestionAlumnos1 = new miscomponentes.PanelGestionAlumnos();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ejercicio 3 - Gestión de alumnos");

        btnTotal.setText("Ver total de alumnos");
        btnTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalActionPerformed(evt);
            }
        });
        panelBotones.add(btnTotal);

        btnVaciar.setText("Vaciar tabla");
        btnVaciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaciarActionPerformed(evt);
            }
        });
        panelBotones.add(btnVaciar);

        getContentPane().add(panelBotones, java.awt.BorderLayout.SOUTH);
        getContentPane().add(panelGestionAlumnos1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // se ejecuta al pulsar "Ver total de alumnos"
    private void btnTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalActionPerformed
        // Aquí usamos desde FUERA el método getTotalAlumnos() del componente
        int total = panelGestionAlumnos1.getTotalAlumnos();
        JOptionPane.showMessageDialog(this, "Hay " + total + " alumnos");
    }//GEN-LAST:event_btnTotalActionPerformed

    // se ejecuta al pulsar "Vaciar tabla"
    private void btnVaciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaciarActionPerformed
        // Aquí usamos desde FUERA el método vaciarTabla() del componente
        panelGestionAlumnos1.vaciarTabla();
        JOptionPane.showMessageDialog(this, "Tabla vaciada");
    }//GEN-LAST:event_btnVaciarActionPerformed

    public static void main(String args[]) {
        // Crea la ventana y la hace visible
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrueba().setVisible(true);
            }
        });
    }

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTotal;
    private javax.swing.JButton btnVaciar;
    private javax.swing.JPanel panelBotones;
    private miscomponentes.PanelGestionAlumnos panelGestionAlumnos1;
    // End of variables declaration//GEN-END:variables
}
