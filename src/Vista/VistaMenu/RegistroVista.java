/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Vista.VistaMenu;

import javax.swing.JFrame;
import Modelo.*;

import Controlador.OyenteVista;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;

public class RegistroVista extends JFrame implements PropertyChangeListener{
    private OyenteVista oyenteVista;
    private static RegistroVista instancia;
    private Juego juego;
    
    private static final String MENSAJE_EXITO = "Usuario registrado"
            + " con exito. ";
    private static final String MENSAJE_ERROR = "Usuario existente"
            + " cambie el nombre de usuario.";
    private static final String MENSAJE_CONTRASENAS = "Las claves"
            + " no coinciden";
    /**
     * Creates new form RegistroVista
     */
    private RegistroVista(OyenteVista oyenteVista, Juego juego) {
        this.oyenteVista = oyenteVista;
        this.juego = juego;
        juego.nuevoObservador(this);
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    /*
    * Patron singleton
    */
    public static synchronized RegistroVista
            instancia(OyenteVista oyenteIU, Juego juego) {
        if (instancia == null) {
            instancia = new RegistroVista(oyenteIU, juego);
        }
        return instancia;
    }
            
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nuevo usuario:");

        jLabel2.setText("Nueva contraseña:");

        jLabel3.setText("Repita contraseña:");

        jButton1.setText("Crear usuario");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Volver");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(87, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(63, 63, 63)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPasswordField2)
                            .addComponent(jPasswordField1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /*
    * Accion del boton registrarse
    */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String usuario = jTextField1.getText();
        String contrasena = String.valueOf(jPasswordField1.getPassword());
        String contrasena2 = String.valueOf(jPasswordField2.getPassword());
        
        if(contrasena.equals(contrasena2)){
            Tupla tupla = new Tupla<>(usuario, contrasena);
            oyenteVista.eventoProducido(OyenteVista.Evento.REGISTRARSE, tupla);
        }else{
            JOptionPane.showMessageDialog(this, MENSAJE_CONTRASENAS,
                    Juego.VERSION, JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    /*
     * Accion del boton atras
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        InicioSesionVista inicioSesion = InicioSesionVista.instancia(oyenteVista, juego);
        inicioSesion.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /*
    * Pone la ventana en la ultima ubicacion de la anterior       
    */
    public void ubicarVentana(Point p){
        this.setLocation(p);
    }        
            
    /*
    * Devuelve la ubicacion de la ventana      
    */
    public Point posicion(){
        return this.getLocation();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    /*
    * Recibe los cambios de en el modelo
    */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Juego.REGISTRARSE)) {
            boolean exito = (boolean)evt.getNewValue();
            if(exito){
                JOptionPane.showMessageDialog(this, MENSAJE_EXITO,
                    Juego.VERSION, JOptionPane.INFORMATION_MESSAGE);
                InicioSesionVista inicioSesion = InicioSesionVista.
                        instancia(oyenteVista, juego);
                inicioSesion.ubicarVentana(posicion());
                inicioSesion.setVisible(true);
                this.setVisible(false);
            }else{
                JOptionPane.showMessageDialog(this, MENSAJE_ERROR,
                    Juego.VERSION, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
