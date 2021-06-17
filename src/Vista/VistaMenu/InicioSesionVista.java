/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Vista.VistaMenu;

import Controlador.OyenteVista;
import Modelo.*;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;

public class InicioSesionVista extends JFrame implements PropertyChangeListener {
    
    private static final String MENSAJE_ERROR = "Creedenciales "
            + "introducidas incorrectas";
    private OyenteVista oyenteVista;
    private static InicioSesionVista instancia;
    private static final String ERROR = Juego.ERROR;
    private static final String DESCONECTADO = "Desconectado";
    private static final String CONECTADO = "Conectado";
    private String conectado;
    private Juego juego;
    /**
     * Creates new form InicioSesion
     */
    private InicioSesionVista(OyenteVista oyenteVista, Juego juego) {
        initComponents();
        this.juego = juego;
        if(juego.estaConectado()){
            conectado = CONECTADO;
            jLabel4.setEnabled(true);
            
        }else{
            conectado = DESCONECTADO;
            jLabel4.setEnabled(false);
        }
        jLabel4.setText(conectado);
        ponerIcono();
        juego.nuevoObservador(this);
        this.oyenteVista = oyenteVista;
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    /*
    * Patron singleton
    */
    public static synchronized InicioSesionVista
            instancia(OyenteVista oyenteIU, Juego juego) {
        if (instancia == null) {
            instancia = new InicioSesionVista(oyenteIU, juego);
        }
        return instancia;
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Usuario:");

        jLabel2.setText("Contraseña:");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Iniciar sesión");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Registrarse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText(conectado);

        jLabel5.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(64, 64, 64))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(37, 37, 37)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /*
    * Accion del boton de iniciar sesion
    */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String usuario = jTextField1.getText();
        String contrasena = String.valueOf(jPasswordField1.getPassword());
        Tupla tupla = new Tupla<>(usuario, contrasena);
        oyenteVista.eventoProducido(OyenteVista.Evento.INICIAR_SESION, tupla);
    }//GEN-LAST:event_jButton1ActionPerformed
    /*
    * Accion del boton registrarse
    */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        RegistroVista registro = RegistroVista.instancia(oyenteVista, juego);
        registro.ubicarVentana(posicion());
        registro.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        
    }//GEN-LAST:event_jPasswordField1ActionPerformed
    
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
    
    /*
    * Pone el icono de la aplicacion
    */
    public void ponerIcono(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage("img/icono.png");
        this.setIconImage(img);
        Image img2 = kit.createImage("img/fondo.png");
        jLabel5.setIcon(new ImageIcon(img2));
        this.setTitle(Juego.VERSION);
    }
    @Override
    /*
    * Notificaciones de cambios en el modelo
    */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Juego.INICIAR_SESION)) {
            Tupla tupla = (Tupla)evt.getNewValue();
            Tupla<String, String> datos = (Tupla<String, String>)tupla;
            
            if(datos.a.equals(ERROR)){
                JOptionPane.showMessageDialog(this, MENSAJE_ERROR, 
                    Juego.VERSION, JOptionPane.INFORMATION_MESSAGE);
            }else{
                InicioVista inicio = InicioVista.instancia(oyenteVista, juego);
                inicio.ubicarVentana(posicion());
                inicio.setVisible(true);
                this.setVisible(false);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

   
}
