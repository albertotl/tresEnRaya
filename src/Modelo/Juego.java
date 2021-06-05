/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

/**
 *
 * @author Alberto
 */
public class Juego {
    
    public static final String VERSION = "Tres en Raya 1.0";
    public static final String CIRCULO = "O";
    public static final String CRUZ = "X";
    public static final String VACIO = "";
    public static final String ERROR = "Error---";
    public static final String GANADOR = "poner_ficha";
    public static final String REGISTRARSE = "registrarse";
    public static final String ENCUENTRA_PARTIDA = "encuentra_partida";
    public static final String ACABAR_PARTIDA = "acabar_partida";
    public static final String INICIAR_SESION = "iniciar_sesion";

    private PropertyChangeSupport observadores;
    private Tablero tablero;
    
    //private String ficha;  //Tipo de ficha que asiganara el servidor para una partida
    //private boolean turno; //Si el cliente tiene o no el turno de jugada
    
    private String turno;
    private boolean conectado;
    private String usuario;
    private String contrincante;
    private String historial;

    public Juego() {
        tablero = new Tablero();
        observadores = new PropertyChangeSupport(this);
        Random r = new Random();
        int valor = r.nextInt(2);  // Entre 0 y 1.

        if (valor == 0) {
            turno = CIRCULO;
        } else {
            turno = CRUZ;
        }
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
    public String devuelveHistorial(){
        return historial;
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
    
    public void acabarPartida(){
        // Enviar resultado a servidor
        this.tablero.vaciar();
         this.observadores.firePropertyChange(ACABAR_PARTIDA,
                    null, null);
        
    }
    
    public void solicitarUsuario(){
        usuario = "hola";
    }
    
    public void solicitarHistorial(){
        historial = "dsfsd";
    }
    
    public void iniciarSesion(String usuario, String contraseña){
        
        //Proceso de inicio de sesion con el servidor
        solicitarUsuario();
        solicitarHistorial();
        conectado = true;
        if(conectado){
            if(!historial.equals("null")){
                Tupla tupla = new Tupla<>(this.usuario, this.historial);
                this.observadores.firePropertyChange(INICIAR_SESION,
                    null, tupla);
            }else{
                 Tupla tupla = new Tupla<>(this.usuario, VACIO);
                 this.observadores.firePropertyChange(INICIAR_SESION,
                    null, tupla);
            }
        }else{
            Tupla tupla = new Tupla<>(ERROR, VACIO);
            this.observadores.firePropertyChange(INICIAR_SESION,
                    null, tupla);
        }
    }
    
    public void registrar(String usuario, String contrasena){
        conectado = true;
        if(conectado){
            // Solicitud de registro al servidor
            
            boolean exito = true;
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
}
