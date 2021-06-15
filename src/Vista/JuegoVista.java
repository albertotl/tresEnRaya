
package Vista;

import Controlador.OyenteVista;
import Modelo.*;
import Vista.VistaMenu.InicioVista;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 *
 * @author Alberto
 */
public class JuegoVista implements ActionListener, PropertyChangeListener{
    private OyenteVista oyenteVista;
    private Juego juego;
    private String codigoCasillaSeleccionada;
    
    private static JuegoVista instancia = null;
    private JFrame ventana;
    private JLabel etiquetaContrincante;
    private JButton botonConfirmar;
    private JButton botonSalir;
    private JButton botonAyuda;
    private TableroVista tableroVista; 
    private JTextArea texto;
    private String historialJugadas = "";
    private String contrincante = "";
    private CasillaVista casillaVistaSeleccionada;
    
    private static final String PONER_FICHA = "Poner ficha";
    private static final String ABANDONAR_PARTIDA = "Abandonar partida";
    private static final String CONTRINCANTE = "Contrincante: ";
    private static final String VACIO = "";
    private static final String GANADOR = "Has ganado la partida!";
    private static final String PERDEDOR = "Has perdido la partida..";
    private static final String SALIR = "Salir";
    private static final String AYUDA = "Ayuda";
    private static final String TEXTO_AYUDA = "Introduzca una "
            + "ficha en una casilla intentando juntar 3 fichas\n   "
            + "      iguales "
            + "en la misma fila, columna o diagonal.\n       "
            + "                            Turno de:  ";
    
   private JuegoVista(OyenteVista oyenteVista, Juego juego){
        this.tableroVista = new TableroVista(this, true);
        juego.nuevoObservador(this);
        this.oyenteVista = oyenteVista;
        this.juego = juego;
        crearVentana();
        contrincante = juego.devuelveContrincante();
        etiquetaContrincante.setText(CONTRINCANTE + contrincante);
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
    
    public void visualizarVentana(){
        ventana.setVisible(true);
    }
    
    /**
    * Devuelve la unica instancia posible de JuegoVista
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
        
        etiquetaContrincante = new JLabel(contrincante);
        etiquetaContrincante.setEnabled(true);
        panel.add(etiquetaContrincante);
        
        botonAyuda = crearBoton(AYUDA);
        barra.add(botonAyuda);
        botonAyuda.setEnabled(true);
        barra.add(new JToolBar.Separator());
        
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
        botonSalir = crearBoton(ABANDONAR_PARTIDA);
        panel.add(botonSalir);
    }
    
    private void crearPanelJugadas(JPanel panel){
        texto = new JTextArea(16,13);
        texto.setLineWrap(true);
        texto.setEditable(false);
        JScrollPane scroll = new JScrollPane(texto);
        panel.add(scroll);
    }
    
    private void mostrarTextoJugada(String jugada){
            historialJugadas = historialJugadas  + jugada + "\n";
            texto.setText(historialJugadas);
    }
    
    public void seleccionarCasillaVista(CasillaVista casillaVista) {
        if (casillaVistaSeleccionada != null) {
            casillaVistaSeleccionada.deseleccionar();
        }
        if (juego.devuelveTurno().equals(juego.devuelveUsuario())) {
            this.casillaVistaSeleccionada = casillaVista;
            this.codigoCasillaSeleccionada = casillaVista.obtenerCodigo();
            if (codigoCasillaSeleccionada != null) {
                casillaVistaSeleccionada.seleccionar(juego.devuelveFicha());
                if (casillaVistaSeleccionada.estaSeleccionada()) {
                    if (casillaVistaSeleccionada.estaConfirmada()) {
                        if (juego.completo()) {
                            activarBotonConfirmar(true);
                        } else {
                            activarBotonConfirmar(false);
                        }
                    } else {
                        activarBotonConfirmar(true);
                    }
                }
            }
        }
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
                mostrarTextoJugada(juego.mostrarTablero());
                tableroVista.ponerCasillas();
                break;
                
            case AYUDA:
                mostrarMensaje(TEXTO_AYUDA + juego.devuelveTurno());
                break;
                
            case ABANDONAR_PARTIDA:
                oyenteVista.eventoProducido(OyenteVista.Evento.ABANDONAR_PARTIDA, VACIO);
                break;
        }
    }
    private void mostrarMensaje(String mensaje){
     JOptionPane.showMessageDialog(ventana, mensaje, 
        Juego.VERSION, 
        JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void vaciarTodo(){
        historialJugadas = VACIO;
        texto.setText(historialJugadas);
        
        contrincante = VACIO;
        etiquetaContrincante.setText(contrincante);
        tableroVista.ponerTablero(juego.devuelveTablero());
        tableroVista.ponerCasillas();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(juego.ACABAR_PARTIDA)) {
            boolean ganador = (boolean) evt.getNewValue();
            if(ganador){
                mostrarMensaje(GANADOR);
            }else{
                mostrarMensaje(PERDEDOR);
            }
            vaciarTodo();
            InicioVista inicio = InicioVista.instancia(oyenteVista, juego);
            inicio.setVisible(true);
            ventana.setVisible(false);
            oyenteVista.eventoProducido(OyenteVista.Evento.PEDIR_HISTORIAL, evt);
            
        }else if(evt.getPropertyName().equals(juego.ACTUALIZAR_TABLERO)){
            tableroVista.setEnabled(true);
            botonConfirmar.setEnabled(true);
            tableroVista.ponerCasillas();
            
        }else if(evt.getPropertyName().equals(juego.PONER_FICHA)){
            botonConfirmar.setEnabled(false);
            tableroVista.setEnabled(false);
            
        }else if(evt.getPropertyName().equals(Juego.ENCUENTRA_PARTIDA)){
            contrincante = juego.devuelveContrincante();
            etiquetaContrincante.setText(CONTRINCANTE + contrincante);
        }
    }
    /*
     * Activa el boton de confirmar
    */
    private void activarBotonConfirmar(boolean activar){
        botonConfirmar.setEnabled(activar);
    }
}
