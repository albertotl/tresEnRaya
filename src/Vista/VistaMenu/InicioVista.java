package Vista.VistaMenu;

import java.beans.PropertyChangeListener;
import Controlador.OyenteVista;
import Modelo.*;
import Vista.JuegoVista;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Alberto
 */
public class InicioVista extends JFrame implements PropertyChangeListener{
    
    private String usuario;
    private List<String> historial;
    private Juego juego;
    private OyenteVista oyenteVista;
    private static InicioVista instancia;
    
    private static final String NUEVA_PARTIDA = "Nueva partida";
    private static final String ESPERANDO_ADVERSARIO = 
            "Esperando adversario...";
    private static final String SALTO = "\n---------------------------"
            + "----------------------------------------------------\n";
    private static final String VACIO = "";
    /**
     * Creates new form InicioVista
     */
    private InicioVista(OyenteVista oyenteVista, Juego juego) {
        this.oyenteVista = oyenteVista;
        this.juego = juego;
        historial = new ArrayList<>();
        ponerDatos();
        juego.nuevoObservador(this);
        initComponents();
        jLabel3.setEnabled(false);
        this.setLocationRelativeTo(null);
    }
    
    /*
    * Patron singleton
    */
    public static synchronized InicioVista
            instancia(OyenteVista oyenteIU, Juego juego) {
        if (instancia == null) {
            instancia = new InicioVista(oyenteIU, juego);
        }
        return instancia;
    }
    
    private void ponerDatos(){
        String usuario = juego.devuelveUsuario();
        historial = juego.devuelveHistorial();
        if(usuario != null){
            this.usuario = usuario;
        }
    }
    
    private void mostrarMensaje(String mensaje){
     JOptionPane.showMessageDialog(this, mensaje, 
        Juego.VERSION, 
        JOptionPane.INFORMATION_MESSAGE);
    }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText(usuario);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        String informacion = "";
        for(String linea: historial){
            informacion = linea + SALTO + informacion;

        }
        jTextArea1.setText(informacion);
        jTextArea1.setEditable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Historial de partidas");

        jButton1.setText("Buscar partida");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Desconectar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Buscando partida..");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(32, 32, 32)
                                .addComponent(jButton1)
                                .addGap(30, 30, 30))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        oyenteVista.eventoProducido(OyenteVista.Evento.BUSCAR_PARTIDA, VACIO);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        InicioSesionVista inicioSesion = InicioSesionVista.instancia(oyenteVista, juego);
        inicioSesion.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Juego.ENCUENTRA_PARTIDA)) {
            boolean exito = (boolean)evt.getNewValue();
            if(exito){
                mostrarMensaje(NUEVA_PARTIDA);
                JuegoVista juegoVista =  JuegoVista.instancia(oyenteVista, juego);
                juegoVista.visualizarVentana();
                jLabel3.setEnabled(false);
                this.setVisible(false);
            }else{
                mostrarMensaje(ESPERANDO_ADVERSARIO);
                jButton1.setEnabled(false);
                jLabel3.setEnabled(true);
            }
        }else if (evt.getPropertyName().equals(juego.ACABAR_PARTIDA)) {
            jButton1.setEnabled(true);
            jLabel3.setEnabled(false);
        }
    }    
}
