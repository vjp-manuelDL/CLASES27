package miscomponentes;

import java.io.Serializable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * @author alumno
 */
public class PanelGestionAlumnos extends javax.swing.JPanel implements Serializable {

    private DefaultTableModel modelo;

    public PanelGestionAlumnos() {
        initComponents();

        // modelo de la tabla con sus dos columnas
        modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Curso");
        tablaAlumnos.setModel(modelo);
    }

    //métodos públicos para el JFrame
    public int getTotalAlumnos() {
        return modelo.getRowCount();
    }

    public void vaciarTabla() {
        modelo.setRowCount(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDatos = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblCurso = new javax.swing.JLabel();
        txtCurso = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaAlumnos = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        lblNombre.setText("Nombre:");
        panelDatos.add(lblNombre);

        txtNombre.setColumns(12);
        panelDatos.add(txtNombre);

        lblCurso.setText("Curso:");
        panelDatos.add(lblCurso);

        txtCurso.setColumns(8);
        panelDatos.add(txtCurso);

        btnAnadir.setText("Añadir");
        btnAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnadirActionPerformed(evt);
            }
        });
        panelDatos.add(btnAnadir);

        btnBorrar.setText("Borrar seleccionado");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        panelDatos.add(btnBorrar);

        add(panelDatos, java.awt.BorderLayout.NORTH);

        scrollTabla.setViewportView(tablaAlumnos);

        add(scrollTabla, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // se ejecuta al pulsar "Añadir"
    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
        String nombre = txtNombre.getText();
        String curso = txtCurso.getText();

        if (nombre.isEmpty() || curso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellena el nombre y el curso");
            return;
        }

        modelo.addRow(new Object[]{nombre, curso}); // pasamos los datos a la tabla

        txtNombre.setText("");
        txtCurso.setText("");
    }//GEN-LAST:event_btnAnadirActionPerformed

    // se ejecuta al pulsar "Borrar seleccionado"
    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        int fila = tablaAlumnos.getSelectedRow(); 
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno de la tabla");
        } else {
            modelo.removeRow(fila);
        }
    }//GEN-LAST:event_btnBorrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTextField txtCurso;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
