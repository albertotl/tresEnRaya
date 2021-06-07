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
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
    public static final String REGISTRARSE = "registrarse";
    public static final String ENCUENTRA_PARTIDA = "encuentra_partida";
    public static final String ACABAR_PARTIDA = "acabar_partida";
    public static final String INICIAR_SESION = "iniciar_sesion";
    public static final String PROPIEDAD_CONECTADO = "Conectado";
    public static final String SEPARADOR = "'";
    
    
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
    
    //private String ficha;  //Tipo de ficha que asiganara el servidor para una partida
    //private boolean turno; //Si el cliente tiene o no el turno de jugada
    
    
    private boolean conectado;
    private String idConexion;
    
    private String turno;
    private String usuario = "Alberto";
    private String contrincante;
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
    
    public String devuelveUsuario(){
        return usuario;
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
    * Intercambia turnos entre los jugadores y pone la ficha en la casilla correspondiente
     */
    public void ponerFicha(String codigo) {
        Casilla casilla = tablero.devolverCasilla(codigo);

        if (casilla != null) {
            if (casilla.devuelveTipo() == null || tablero.completo()) {
                if (turno.equals(CIRCULO)) {
                    Circulo ficha = new Circulo();
                    casilla.introducirFicha(ficha);
                    turno = CRUZ;
                } else {
                    Cruz ficha = new Cruz();
                    casilla.introducirFicha(ficha);
                    turno = CIRCULO;
                }
            }
        }
        if(!verificarTablero().equals(VACIO)){
            this.observadores.firePropertyChange(GANADOR,
                    null, verificarTablero());
            
        }
    }

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
    * Verifica si hay ganador en las filas
     */
    public String verificarFilas() {
        boolean ganador = true;
        for (int i = 0; i < Tablero.DIMENSION; i++) {
            String tipo = tablero.devolverTipoCasilla(i, 0);
            if (tipo != null) {
                for (int j = 0; j < Tablero.DIMENSION; j++) {
                    if (tipo != tablero.devolverTipoCasilla(i, j)) {
                        ganador = false;
                    }
                }
                if (ganador) {
                    return "Ganador " + tipo + " en fila " + (i + 1);
                }
                ganador = true;
            }
        }
        return null;
    }

    /*
    * Verifica si hay ganador en las columnas
     */
    public String verificarColumnas() {
        boolean ganador = true;
        for (int i = 0; i < Tablero.DIMENSION; i++) {
            String tipo = tablero.devolverTipoCasilla(0, i);
            if (tipo != null) {
                for (int j = 0; j < Tablero.DIMENSION; j++) {
                    if (tipo != tablero.devolverTipoCasilla(j, i)) {
                        ganador = false;
                    }
                }
                if (ganador) {
                    return "Ganador " + tipo + " en columna " + (i + 1);
                }
                ganador = true;
            }
        }
        return null;
    }

    /*
    * Verifica si hay ganador en la diagonal 1 (\)
     */
    public String verificarDiagonal1() {
        boolean ganador = true;
        String tipo = tablero.devolverTipoCasilla(0, 0);
        if (tipo != null) {
            for (int i = 1; i < Tablero.DIMENSION; i++) {
                if (tipo != tablero.devolverTipoCasilla(i, i)) {
                    ganador = false;
                    break;
                }
            }
            if (ganador) {
                return "Ganador " + tipo + " en diagonal \\";
            }
        }
        return null;
    }

    /*
    * Verifica si hay ganador en la diagonal 2 (/)
     */
    public String verificarDiagonal2() {

        String tipo2 = tablero.devolverTipoCasilla(0, 2);
        boolean ganador = true;
        if (tipo2 != null) {
            for (int i = 1, j = 1; i < Tablero.DIMENSION; i++, j--) {
                if (tipo2 != tablero.devolverTipoCasilla(i, j)) {
                    ganador = false;
                    break;
                }
            }
            if (ganador) {
                return "Ganador " + tipo2 + " en diagonal /";
            }
        }
        return null;
    }

    /*
    * Verifica si hay ganador en el tablero
     */
    public String verificarTablero() {
        String filas = verificarFilas();
        if (filas != null) {
            return filas;
        }
        String columnas = verificarColumnas();
        if (columnas != null) {
            return columnas;
        }
        String diagonal1 = verificarDiagonal1();
        if (diagonal1 != null) {
            return diagonal1;
        }
        String diagonal2 = verificarDiagonal2();
        if (diagonal2 != null) {
            return diagonal2;
        }
        return VACIO;
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
    * Envia el resultado al servidor y limpia el tablero
    */
    public void acabarPartida(){
        // Enviar resultado a servidor
        this.tablero.vaciar();
         this.observadores.firePropertyChange(ACABAR_PARTIDA,
                    null, null);
        
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
    public void iniciarSesion(String usuario, String contrasena)
            throws IOException {
        if (conectado) {
            //Se cifra la contraseña antes de enviarla
            String contrasenaCifrada = cifrarContrasena(contrasena);
            if (contrasenaCifrada != null) {
                historial = new ArrayList<>();
                String credenciales = idConexion + SEPARADOR
                        + usuario + SEPARADOR + contrasenaCifrada;
                cliente.enviarSolicitud(PrimitivaComunicacion.INICIAR_SESION,
                        Cliente.TIEMPO_ESPERA_SERVIDOR,
                        credenciales, historial);
                if(historial.get(0).equals(ERROR_CONEXION)){
                    Tupla tupla = new Tupla<>(ERROR, ERROR);
                    this.observadores.firePropertyChange(INICIAR_SESION,
                            null, tupla);
                }else{
                    if (historial.isEmpty()) {
                        Tupla tupla = new Tupla<>(this.usuario, VACIO);
                        this.observadores.firePropertyChange(INICIAR_SESION,
                            null, tupla);
                    }else{
                        Tupla tupla = new Tupla<>(this.usuario, this.historial);
                        this.observadores.firePropertyChange(INICIAR_SESION,
                            null, tupla);
                    }
                }
            }
        }
    }


    /*
    * Hace una peticion al servidor para registrar un nuevo usuario
    */
    public void registrar(String usuario, String contrasena) 
            throws IOException{
        if(conectado){
            boolean exito = false;
            String contrasenaCifrada = cifrarContrasena(contrasena);
            String datos = usuario + SEPARADOR + contrasenaCifrada;
            PrimitivaComunicacion respuesta = 
                    cliente.enviarSolicitud(PrimitivaComunicacion.REGISTRARSE,
                    Cliente.TIEMPO_ESPERA_SERVIDOR, datos);
            if(respuesta == PrimitivaComunicacion.OK){
                exito = true;
            }
            this.observadores.firePropertyChange(REGISTRARSE,
                    null, exito);
        }
    }
    
    private void solicitudServidorPonerFicha(){
        
    } 
    
    public void buscarPartida(){
        
        boolean exito = true;
        
        if(exito){
            Tupla tupla = new Tupla<>(contrincante, turno);
             this.observadores.firePropertyChange(ENCUENTRA_PARTIDA,
                    null, contrincante);
        }
    }

    @Override
    public boolean solicitudServidorProducida(PrimitivaComunicacion solicitud,
            List<String> resultados) throws IOException {
        if(resultados.isEmpty()){
            return false;
        }
        switch(solicitud){
            case NUEVO_ID_CONEXION:
                return solicitudServidorNuevoIdConexion(resultados);
            
            default:
                return false;
        }
    }
}
