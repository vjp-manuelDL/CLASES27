/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Cazatesoros;

/**
 *
 * @author Manue
 */
public class VentanaCazatesoros extends javax.swing.JFrame {

    private boolean turnoUsuario = true;
    private int puntosUsuario = 0;
    private int puntosCPU = 0;
    private int tesorosRestantes = 5;
    private java.util.Random random = new java.util.Random();

    /**
     * Creates new form VentanaCazatesoros
     */
    public VentanaCazatesoros() {
        initComponents();
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);

        // 1. Esconder los 5 tesoros de forma aleatoria
        esconderTesoros();

        // 2. Crear un único escuchador para los 16 botones
        java.awt.event.ActionListener oyenteBotones = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPulsado(evt);
            }
        };

        // Asignar el escuchador a cada botón del panel central
        for (java.awt.Component comp : jPanel2.getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                ((javax.swing.JButton) comp).addActionListener(oyenteBotones);
            }
        }
    }

    private void botonPulsado(java.awt.event.ActionEvent evt) {
        if (!turnoUsuario) {
            return; // Si la CPU está pensando, ignoramos los clics
        }
        javax.swing.JButton boton = (javax.swing.JButton) evt.getSource();

        // Si el botón ya está deshabilitado, no hacemos nada
        if (!boton.isEnabled()) {
            return;
        }

        realizarJugada(boton, true); // true indica que es el turno del usuario

        if (tesorosRestantes == 0) {
            comprobarFinPartida();
        } else {
            turnoUsuario = false; // Bloquea al usuario mientras piensa la CPU
            juegaCPU();
        }
    }

    private void esconderTesoros() {
        java.awt.Component[] componentes = jPanel2.getComponents();
        int tesorosColocados = 0;

        while (tesorosColocados < 5) {
            int indiceAleatorio = random.nextInt(componentes.length);
            javax.swing.JButton boton = (javax.swing.JButton) componentes[indiceAleatorio];

            // Solo ponemos el tesoro si el botón no lo tiene ya
            if (!"tesoro".equals(boton.getName())) {
                boton.setName("tesoro");
                tesorosColocados++;
            }
        }
    }

    private void realizarJugada(javax.swing.JButton boton, boolean esUsuario) {
        if ("tesoro".equals(boton.getName())) {
            boton.setBackground(java.awt.Color.YELLOW);
            boton.setOpaque(true);
            boton.setText("T"); // Opcional: marca visual

            if (esUsuario) {
                puntosUsuario++;
                ptsuser.setText("Puntos Usuario: " + puntosUsuario);
            } else {
                puntosCPU++;
                jLabel2.setText("Puntos CPU: " + puntosCPU);
            }
            tesorosRestantes--;
        } else {
            boton.setBackground(java.awt.Color.GRAY);
            boton.setOpaque(true);
        }

        boton.setEnabled(false); // Deshabilita el botón que se acaba de pulsar
    }

    private void juegaCPU() {
        // Temporizador de 1 segundo (1000 ms) para simular que la CPU "piensa"
        javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                java.awt.Component[] componentes = jPanel2.getComponents();
                javax.swing.JButton botonElegido = null;
                boolean botonValido = false;

                // La CPU busca un botón al azar que siga habilitado
                while (!botonValido && tesorosRestantes > 0) {
                    int indiceAleatorio = random.nextInt(componentes.length);
                    botonElegido = (javax.swing.JButton) componentes[indiceAleatorio];
                    if (botonElegido.isEnabled()) {
                        botonValido = true;
                    }
                }

                if (botonElegido != null) {
                    realizarJugada(botonElegido, false);
                }

                if (tesorosRestantes == 0) {
                    comprobarFinPartida();
                } else {
                    turnoUsuario = true; // Devuelve el turno al usuario
                }
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void comprobarFinPartida() {
        // Deshabilitar cualquier botón restante en el tablero
        for (java.awt.Component comp : jPanel2.getComponents()) {
            comp.setEnabled(false);
        }

        String mensaje;
        if (puntosUsuario > puntosCPU) {
            mensaje = "¡Has ganado!";
        } else if (puntosCPU > puntosUsuario) {
            mensaje = "¡Ha ganado la máquina!";
        } else {
            mensaje = "¡Empate!";
        }

        javax.swing.JOptionPane.showMessageDialog(this, mensaje, "Fin del juego", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ptscpu = new javax.swing.JPanel();
        ptsuser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ptsuser.setText("Puntos Usuario: 0");
        ptscpu.add(ptsuser);

        jLabel2.setText("Puntos CPU: 0");
        ptscpu.add(jLabel2);

        getContentPane().add(ptscpu, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.GridLayout(4, 4));
        jPanel2.add(jButton1);
        jPanel2.add(jButton2);
        jPanel2.add(jButton3);
        jPanel2.add(jButton4);
        jPanel2.add(jButton5);
        jPanel2.add(jButton6);
        jPanel2.add(jButton7);
        jPanel2.add(jButton8);
        jPanel2.add(jButton9);
        jPanel2.add(jButton10);
        jPanel2.add(jButton11);
        jPanel2.add(jButton12);
        jPanel2.add(jButton13);
        jPanel2.add(jButton14);
        jPanel2.add(jButton15);
        jPanel2.add(jButton16);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaCazatesoros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaCazatesoros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaCazatesoros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaCazatesoros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaCazatesoros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel ptscpu;
    private javax.swing.JLabel ptsuser;
    // End of variables declaration//GEN-END:variables
}
