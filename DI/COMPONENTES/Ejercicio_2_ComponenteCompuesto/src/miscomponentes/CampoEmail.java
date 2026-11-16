/*
 * Ejercicio 2: Componente compuesto
 */
package miscomponentes;

import java.awt.Color;              
import java.io.Serializable;        
import javax.swing.BorderFactory;   

/*
 * @author alumno
 */
public class CampoEmail extends javax.swing.JPanel implements Serializable {
    private Color colorValido = Color.GREEN;   
    private Color colorInvalido = Color.RED;   
    private boolean emailValido = false;      

   
 
    public CampoEmail() {
        initComponents(); 
        comprobarEmail(); }
    


    // Aquí se comprueba si el texto es un email y se pinta del color que toque
    private void comprobarEmail() {
        String texto = txtEmail.getText();

      
        if (texto.trim().matches(".+@.+\\..+")) {
            emailValido = true;
            txtEmail.setBorder(BorderFactory.createLineBorder(colorValido, 2)); 
            lblEstado.setForeground(colorValido);                            
            lblEstado.setText("Válido");
        } else {
            emailValido = false;
            txtEmail.setBorder(BorderFactory.createLineBorder(colorInvalido, 2)); 
            lblEstado.setForeground(colorInvalido);                             
            lblEstado.setText("No válido");
        }
    }

    
    //getters y setters
    public Color getColorValido() {
        return colorValido;
    }

    public void setColorValido(Color colorValido) {
        this.colorValido = colorValido;
        comprobarEmail(); // repintamos para que se vea el color nuevo
    }

    public Color getColorInvalido() {
        return colorInvalido;
    }

    public void setColorInvalido(Color colorInvalido) {
        this.colorInvalido = colorInvalido;
        comprobarEmail(); // repintamos para que se vea el color nuevo
    }

    // Solo tiene 
    public boolean isEmailValido() {
        return emailValido;
    }

    //el texto lo pide
    public String getTexto() {
        return txtEmail.getText();
    }

    //  pasa el texto al JTextField de dentro
    public void setTexto(String texto) {
        txtEmail.setText(texto);
        comprobarEmail();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtEmail = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();

        txtEmail.setColumns(20);
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
            }
        });
        add(txtEmail);

        lblEstado.setText("No válido");
        add(lblEstado);
    }// </editor-fold>//GEN-END:initComponents

    
    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
        comprobarEmail();
    }//GEN-LAST:event_txtEmailKeyReleased


  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblEstado;
    private javax.swing.JTextField txtEmail;
    // End of variables declaration//GEN-END:variables
}
