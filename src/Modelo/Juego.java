/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Modelo.TresEnRayaEnLinea.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alberto
 */
public class Juego implements OyenteServidor {
    
    public static final String VERSION = "Tres en Raya 1.0";
    public static final String CIRCULO = "O";
    public static final String CRUZ = "X";
    public static final String VACIO = "";
    public static final String ERROR_CONEXION = "ERROR";
    public static final String ERROR = "Error---";
    public static final String GANADOR = "poner_ficha";
    public static final String PARTIDA_GANADA = "partida_ganada";
    public static final String PARTIDA_PERDIDA = "partida_perdida";
    public static final String REGISTRARSE = "registrarse";
    public static final String ENCUENTRA_PARTIDA = "encuentra_partida";
    public static final String PEDIR_HISTORIAL = "pedir_historial";
    public static final String CERRAR_SESION = "cerrar_sesion";
    public static final String ACABAR_PARTIDA = "acabar_partida";
    public static final String INICIAR_SESION = "iniciar_sesion";
    public static final String PROPIEDAD_CONECTADO = "Conectado";
    public static final String SEPARADOR = "'";
    public static final String ACTUALIZAR_TABLERO = "actualizar_tablero";
    public static final String PONER_FICHA = "poner_fichaa";
    
    public static final String ID_USUARIO = "idUsuario";
    public static final String NOMBRE = "nombre";
    public static final String URL_SERVIDOR = "URLServidor";
    private String URLServidor = "localhost";
    public static final String PUERTO_SERVIDOR = "puertoServidor";
    private int puertoServidor = 25000;
    public static final String ERROR_GUARDAR_CONFIGURACION = 
            "No se ha guardado la configuracion";
    
    private Cliente cliente;
    private OyenteServidor oyenteServidor;
    private PropertyChangeSupport observadores;
    private Tablero tablero;
   
    private boolean conectado;
    private String idConexion;
    private String idJuego;
    private String idUsuario;
    private String contrincante;
    private String ficha; // Tipo de ficha que se le asigna al jugador
    private String turno; // Tipo de ficha la cual tiene el turno
    
    private List<String> historial;

    public Juego() {
        tablero = new Tablero();
        oyenteServidor = this;
        conectado = false;
        observadores = new PropertyChangeSupport(this);
        
        cliente = new Cliente(URLServidor, puertoServidor);
    }
    
    public Tablero devuelveTablero(){
        return tablero;
    }
    
    public String devuelveTurno(){
        return turno;
    }
    
    public String devuelveFicha(){
        return ficha;
    }
    
    public String devuelveUsuario(){
        return idUsuario;
    }
    
    public String devuelveContrincante(){
        return contrincante;
    }
    public List<String> devuelveHistorial(){
        return historial;
    }
    
    public boolean estaConectado(){
        return conectado;
    }
    
    /*
    * Obtiene identificacion de conexion
    */
    public String obtenerIdConexion(){
        return conectado ? idConexion : "---";
    }
    
    /*
    * Conecta con el servidor mediante long polling
    */
    public void conectar(){
        new Thread(){
            public void run(){
                Cliente cliente = new Cliente(
                        URLServidor, puertoServidor);
                while(true){
                    try{
                        cliente.enviarSolicitudLongPolling(
                                PrimitivaComunicacion.CONECTAR_PUSH, 
                                cliente.TIEMPO_ESPERA_LARGA_ENCUESTA,
                                oyenteServidor);
                        
                    } catch(Exception e){
                        conectado = false;
                        observadores.firePropertyChange(
                                PROPIEDAD_CONECTADO, null, conectado);
                        // Se reintenta la conexión
                        try {
                          sleep(
                            cliente.TIEMPO_REINTENTO_CONEXION_SERVIDOR);
                        } catch (InterruptedException e2){
                            // Propagamos a la máquina virtual
                            new RuntimeException();
                        }
                    }
                }
            }
        }.start();
    }
    
    /*
    * Desconecta del servidor
    */
    public void desconectar() throws Exception{
        if(! conectado){
            return;
        }
        cliente.enviarSolicitud(PrimitivaComunicacion.DESCONECTAR_PUSH,
                Cliente.TIEMPO_ESPERA_SERVIDOR, idConexion);
    }
    
    /*
    * Recibe solicitud del servidor de nuevo idConexion
    */
    private boolean solicitudServidorNuevoIdConexion(
            List<String> resultados) throws IOException{
        idConexion = resultados.get(0);
        if(idConexion == null){
            return false;
        }
        conectado = true;
        observadores.firePropertyChange(
                PROPIEDAD_CONECTADO, null, conectado);
        return true;
    }

    /*
    * Solicita al servidor poner una ficha en la casilla correspondiente
     */
    public void ponerFicha(String codigo){
        if(conectado){
            if(turno.equals(idUsuario)){
                Casilla casilla = tablero.devolverCasilla(codigo);
                if (casilla != null) {
                    if (casilla.devuelveTipo() == null || completo()) {
                        String informacion = idUsuario + SEPARADOR + 
                                    idJuego + SEPARADOR + codigo;
                        try{
                            PrimitivaComunicacion respuesta = cliente.
                                enviarSolicitud(PrimitivaComunicacion.
                                PONER_FICHA, Cliente.TIEMPO_ESPERA_SERVIDOR,
                                informacion);
                            if(respuesta.equals(PrimitivaComunicacion.OK)){
                                if (ficha.equals(CIRCULO)) {
                                    Circulo ficha = new Circulo();
                                    casilla.introducirFicha(ficha);
                                } else {
                                    Cruz ficha = new Cruz();
                                    casilla.introducirFicha(ficha);
                                }
                                turno = contrincante;
                                observadores.firePropertyChange(
                                        PONER_FICHA, null, null);
                            }
                        }catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }
    /*
    * Muestra el tablero actual con una cadena de caracteres
    */
    public String mostrarTablero() {
        String cadena = "";
        String tipo = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tipo = tablero.devolverTipoCasilla(i, j);
                if (tipo == null) {
                    tipo = " - ";
                }
                cadena = cadena + " |  " + tipo + "  | ";
            }
            cadena = cadena + "\n";
        }
        return cadena;
    }
    
    /*
    * Devuelve verdadero si el tablero esta completo
    */
    public boolean completo(){
        if(tablero != null){
            return tablero.completo();
        }
        return false;
    }
    
    /*
    * Añade un observador al juego
    */
    public void nuevoObservador(PropertyChangeListener observador){
        this.observadores.addPropertyChangeListener(observador);
    }
    
    /*
    * Limpia el tablero
    */
    public void acabarPartida(){
        this.tablero = new Tablero();
        this.idJuego = "";
        this.ficha = "";
        this.contrincante = "";
        this.turno = "";
    }
    
    /*
    * Cifra la contraseña con SHA-256 para enviarla hasta el servidor
    */
    private String cifrarContrasena(String contrasena){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(contrasena.getBytes());
        StringBuffer contrasenaCifrada = new StringBuffer();
        for (byte b : hash) {
            contrasenaCifrada.append(String.format("%02x", b));
        }
        return contrasenaCifrada.toString();
    }
    
    /*
    * Hace una peticion al servidor de iniciar sesion 
     */
    public void iniciarSesion(String usuario, String contrasena){
        if (conectado) {
            //Se cifra la contraseña antes de enviarla
            String contrasenaCifrada = cifrarContrasena(contrasena);
            if (contrasenaCifrada != null) {
                historial = new ArrayList<>();
                String credenciales = idConexion + SEPARADOR
                        + usuario + SEPARADOR + contrasenaCifrada;
                try {
                    cliente.enviarSolicitud(PrimitivaComunicacion.
                            INICIAR_SESION, Cliente.TIEMPO_ESPERA_SERVIDOR,
                            credenciales, historial);
                    if (historial.get(0).equals(ERROR_CONEXION)) {
                        Tupla tupla = new Tupla<>(ERROR, ERROR);
                        this.observadores.firePropertyChange(INICIAR_SESION,
                                null, tupla);
                    } else {
                        idUsuario = usuario;
                        if (historial.get(0).equals("OK")) {
                            Tupla tupla = new Tupla<>(this.idUsuario, VACIO);
                            this.observadores.firePropertyChange(INICIAR_SESION,
                                    null, tupla);
                        } else {
                            Tupla tupla = new Tupla<>(this.
                                    idUsuario, this.historial);
                            this.observadores.firePropertyChange(INICIAR_SESION,
                                    null, tupla);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
    
    
    /*
    * Hace una peticion al servidor para registrar un nuevo idUsuario
    */
    public void registrar(String usuario, String contrasena){
        if(conectado){
            boolean exito = false;
            String contrasenaCifrada = cifrarContrasena(contrasena);
            String datos = usuario + SEPARADOR + contrasenaCifrada;
            try{
                PrimitivaComunicacion respuesta = 
                    cliente.enviarSolicitud(PrimitivaComunicacion.REGISTRARSE,
                    Cliente.TIEMPO_ESPERA_SERVIDOR, datos);
                if(respuesta == PrimitivaComunicacion.OK){
                    exito = true;
                }
                this.observadores.firePropertyChange(REGISTRARSE,
                    null, exito);
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    
    /*
    * Hace una peticion al servidor para desconectar al usuario
    */
    public void cerrarSesion(){
         if(conectado){
              try{
                PrimitivaComunicacion respuesta = 
                    cliente.enviarSolicitud(PrimitivaComunicacion.CERRAR_SESION,
                    Cliente.TIEMPO_ESPERA_SERVIDOR, idConexion);
                if(respuesta == PrimitivaComunicacion.OK){
                    idUsuario = null;
                    contrincante = null;
                    idJuego = null;
                }
                this.observadores.firePropertyChange(CERRAR_SESION,
                    null, null);
            }catch(IOException e){
                System.out.println(e);
            }
         }
    }
    
    /*
    * Hace una peticion al servidor solicitando el historial actual
    */
    public void pedirHistorial(){
        if(conectado){
            String datos = idUsuario;
            try {
                cliente.enviarSolicitud(PrimitivaComunicacion.PEDIR_HISTORIAL,
                        Cliente.TIEMPO_ESPERA_SERVIDOR, datos, historial);
                this.observadores.firePropertyChange(PEDIR_HISTORIAL,
                                    null, historial);
                
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    
    /*
    * Hace una solicitud al servidor para encontrar una partida
    */
    public void buscarPartida(){
        if(conectado){
            boolean exito = false;
            String datos = idUsuario + SEPARADOR + idConexion;
            List<String> resultados = new ArrayList<>();
            try{
                cliente.enviarSolicitud(PrimitivaComunicacion.BUSCAR_PARTIDA,
                    Cliente.TIEMPO_ESPERA_SERVIDOR, datos, resultados);
            }catch(IOException e){
                System.out.println(e);
            }
            if(!resultados.isEmpty()){
                exito = true;
                idJuego = resultados.get(0);
                turno = resultados.get(1);
                contrincante = resultados.get(2);
                ficha = resultados.get(3);
            }
            this.observadores.firePropertyChange(ENCUENTRA_PARTIDA,
                    null, exito);
        }
    }
    
    /*
    * Hace una solicitud al servidor para abandonar una partida
    */
    public void abandonarPartida(){
        if(conectado){
            String datos = idConexion;
            try{
                PrimitivaComunicacion respuesta = cliente.enviarSolicitud(
                        PrimitivaComunicacion.ACABAR_PARTIDA, 
                        Cliente.TIEMPO_ESPERA_SERVIDOR, datos);
                if(respuesta == PrimitivaComunicacion.OK){
                    this.observadores.firePropertyChange(ACABAR_PARTIDA,
                        null, false);
                }
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    /*
    * Recibe del servidor la solicitud de que ha concluido una partida
    */
    public boolean solicitudServidorAcabarPartida(List<String> resultados){
        if(resultados.get(0).equals(PARTIDA_GANADA)){
            acabarPartida();
            this.observadores.firePropertyChange(ACABAR_PARTIDA,
                 null, true);
            return true;
        }else if(resultados.get(0).equals(PARTIDA_PERDIDA)){
            acabarPartida();
            this.observadores.firePropertyChange(ACABAR_PARTIDA,
                 null, false);
            return true;
        }
        return false;
    }
    
    /*
    * Recibe del servidor la solicitud de que se ha encontrado una partida
    */
    public boolean solicitudServidorNuevaPartida(List<String> resultados){
        String informacion = resultados.get(0);
        
        String[] datos = informacion.split(SEPARADOR);
        if(datos.length < 5){
            idJuego = datos[0];
            turno = datos[1];
            ficha = datos[2];
            contrincante = datos[3];
            this.observadores.firePropertyChange(ENCUENTRA_PARTIDA,
                    null, true);
            return true;
        }
        return false;
    }
    
    /*
    * Recibe del servidor una solicitud de que se ha puesto una ficha
    */
    public boolean solicitudServidorPonerFicha(List<String> resultados){
            String informacion = resultados.get(0);
            String[] datos = informacion.split(SEPARADOR);
            
            String codigo = datos[0];
            turno = datos[1];
            Casilla casilla = tablero.devolverCasilla(codigo);
            if(casilla != null){
                if(ficha.equals(CIRCULO)){
                    Ficha ficha = new Cruz();
                    casilla.introducirFicha(ficha);
                }else{
                    Ficha ficha = new Circulo();
                    casilla.introducirFicha(ficha);
                }
                this.observadores.firePropertyChange(ACTUALIZAR_TABLERO,
                    null, null);
                return true;
            }
        return false;
    }

    @Override
    /*
    * Recibe todas las solitudes del servidor
    */
    public boolean solicitudServidorProducida(PrimitivaComunicacion solicitud,
            List<String> resultados) throws IOException {
        if(resultados.isEmpty()){
            return false;
        }
        switch(solicitud){
            case NUEVO_ID_CONEXION:
                return solicitudServidorNuevoIdConexion(resultados);
            
            case PARTIDA_ENCONTRADA:
                return solicitudServidorNuevaPartida(resultados);
                
            case ACABAR_PARTIDA:
                return solicitudServidorAcabarPartida(resultados);
                
            case PONER_FICHA:
                return solicitudServidorPonerFicha(resultados);
                
            default:
                return false;
        }
    }
}
