
package miscomponentes;

import javax.swing.JOptionPane; // para sacar mensajes en una ventanita

/**
 * 
 *
 * @author alumno
 */
public class VentanaPruebaEmail extends javax.swing.JFrame {

    // Constructor: crea la ventana
    public VentanaPruebaEmail() {
        initComponents();            
        this.setSize(800,800);       
        setLocationRelativeTo(null); 
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblEmail = new javax.swing.JLabel();
        campoEmail1 = new miscomponentes.CampoEmail();
        btnComprobar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ejercicio 2 - Componente compuesto");
        getContentPane().setLayout(new java.awt.FlowLayout());

        lblEmail.setText("Email:");
        getContentPane().add(lblEmail);
        getContentPane().add(campoEmail1);

        btnComprobar.setText("Comprobar");
        btnComprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprobarActionPerformed(evt);
            }
        });
        getContentPane().add(btnComprobar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // se ejecuta al pulsar el botón "Comprobar"
    private void btnComprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprobarActionPerformed
        // Aquí usamos desde FUERA el método isEmailValido() del componente
        if (campoEmail1.isEmailValido()) {
            JOptionPane.showMessageDialog(this, "El email es válido");
        } else {
            JOptionPane.showMessageDialog(this, "El email NO es válido");
        }
    }//GEN-LAST:event_btnComprobarActionPerformed

   
    public static void main(String args[]) {
        // Crea la ventana y la hace visible
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPruebaEmail().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComprobar;
    private miscomponentes.CampoEmail campoEmail1;
    private javax.swing.JLabel lblEmail;
    // End of variables declaration//GEN-END:variables
}
