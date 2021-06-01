
package Vista;

import Controlador.OyenteVista;
import Modelo.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author Alberto
 */
public class JuegoVista implements ActionListener, PropertyChangeListener{
    private OyenteVista oyenteVista;
    private Juego juego;
    private boolean conectado;
    private String codigoCasillaSeleccionada;
    
    private static JuegoVista instancia = null;
    private JFrame ventana;
    private JLabel etiquetaConectado;
    private JButton botonConfirmar;
    private JButton botonSalir;
    private JButton botonAyuda;
    private TableroVista tableroVista; 
    private JTextArea texto;
    private String historialJugadas = "";
    private CasillaVista casillaVistaSeleccionada;
    
    private static final String ESTADO_CONECTADO = "Conectado";
    private static final String ESTADO_DESCONECTADO = "Desconectado";
    private static final String PONER_FICHA = "Poner ficha";
    private static final String SALIR = "Salir";
    private static final String AYUDA = "Ayuda";
    
    private JuegoVista(OyenteVista oyenteVista, Juego juego){
        this.tableroVista = new TableroVista(this, true);
        this.oyenteVista = oyenteVista;
        this.juego = juego;
        crearVentana();
    }
    
    private void crearVentana() {
        //String idUsuario = juego.obtenerIdUsuario();
        ventana = new JFrame(Juego.VERSION);

        /*
      * Captura windowsClosing y produce el evento Salir
         */
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
                } catch (Exception ex) {
                  //  mensajeError(ERROR_SALIR);
                }
            }
        });
        
        ventana.getContentPane().setLayout(new BorderLayout());
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new FlowLayout());
        crearBarraHerramientas(panelNorte);
        ventana.getContentPane().add(panelNorte, BorderLayout.NORTH);
        
        JPanel panelOeste = new JPanel();
        crearPanelTablero(panelOeste);
        ventana.getContentPane().add(panelOeste, BorderLayout.WEST);
        
        JPanel panelCentral = new JPanel();
        crearPanelBotones(panelCentral);
        ventana.getContentPane().add(panelCentral, BorderLayout.SOUTH);
        
        JPanel panelEste = new JPanel();
        crearPanelJugadas(panelEste);
        ventana.getContentPane().add(panelEste, BorderLayout.CENTER);
        
        
        //No permite la maximizacion de la ventana
        ventana.setResizable(false);
        // Ajusta la ventana y componenetes
        ventana.pack();
        ventana.setVisible(true);
        // Centra la ventana en pantalla
        ventana.setLocationRelativeTo(null); 
    }
    
    /**
    * Devuelve la unica instancia posible de VentaBilletesVista
    * (patrón singleton)
    */        
    public static synchronized JuegoVista
            instancia(OyenteVista oyenteIU, Juego juego) {
        if (instancia == null) {
            instancia = new JuegoVista(oyenteIU, juego);
        }
        return instancia;
    }

    /**
     * Crea botón barra de herramientas
     */
    private JButton crearBoton(String etiqueta) {
        JButton boton = new JButton(etiqueta);
        boton.addActionListener(this);
        boton.setActionCommand(etiqueta);

        return boton;
    }
    
    private void crearBarraHerramientas(JPanel panel){
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        
        botonAyuda = crearBoton(AYUDA);
        barra.add(botonAyuda);
        botonAyuda.setEnabled(true);
        barra.add(new JToolBar.Separator());
        
        etiquetaConectado = new JLabel(ESTADO_DESCONECTADO);
        etiquetaConectado.setEnabled(false);
        panel.add(etiquetaConectado);
        panel.add(barra);
    }
    
    private void crearPanelTablero(JPanel panel){
        panel.setLayout(new FlowLayout());
        tableroVista.ponerTablero(juego.devuelveTablero());
        
        tableroVista.ponerCasillas();
        panel.add(tableroVista);
    }
    
    private void crearPanelBotones(JPanel panel){
        panel.setLayout(new FlowLayout());
        botonConfirmar = crearBoton(PONER_FICHA);
        panel.add(botonConfirmar);
        botonConfirmar.setEnabled(false);
        botonSalir = crearBoton(SALIR);
        panel.add(botonSalir);
    }
    
    private void crearPanelJugadas(JPanel panel){
        texto = new JTextArea(16,12);
        texto.setLineWrap(true);
        texto.setEditable(false);
        JScrollPane scroll = new JScrollPane(texto);
        panel.add(scroll);
    }
    
    private void mostrarTextoJugada(String jugada){
            historialJugadas = historialJugadas  + jugada + "\n";
            texto.setText(historialJugadas);
    }
  
    @Override
    public void actionPerformed(ActionEvent e) {
        notificacionAControl(e.getActionCommand());
    }
    
    private void notificacionAControl(String evento){
        switch(evento){
            case PONER_FICHA:
                oyenteVista.eventoProducido(OyenteVista.Evento.PONER_FICHA,
                        codigoCasillaSeleccionada);
                tableroVista.ponerCasillas();
                break;
                
            case SALIR:
                
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
     * Activa el boton de asignar
    */
    private void activarBotonConfirmar(boolean activar){
        botonConfirmar.setEnabled(activar);
    }

    public void seleccionarCasillaVista(CasillaVista casillaVista) {
        if (casillaVistaSeleccionada != null) {
            casillaVistaSeleccionada.deseleccionar();
        }

        this.casillaVistaSeleccionada = casillaVista;
        this.codigoCasillaSeleccionada = casillaVista.obtenerCodigo();
        
        if (codigoCasillaSeleccionada != null) {
            casillaVistaSeleccionada.seleccionar(juego.devuelveTurno());
            if (casillaVistaSeleccionada.estaSeleccionada()) {
                if (casillaVistaSeleccionada.estaConfirmada()) {
                    if (juego.completo()) {
                        activarBotonConfirmar(true);
                    }
                    activarBotonConfirmar(false);
                } else {
                    activarBotonConfirmar(true);
                }
            }
        }
    }
    
}
