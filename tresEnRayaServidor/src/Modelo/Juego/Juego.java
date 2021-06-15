/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
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

    private Tablero tablero;
    private String turno;
    private String turnoUsuario;
    private String idUsuario;
    private String idUsuario2;
    private String idConexionUsuario1;
    private String idConexionUsuario2;
    private String idConexionGanador;
    private String ganador;

    public Juego(String idUsuario, String idConexionUsuario1) {
        this.idUsuario = idUsuario;
        this.idConexionUsuario1 = idConexionUsuario1;
        tablero = new Tablero();
        
    }
    
    public void asignarTurnos(){
        Random r = new Random();
        int valor = r.nextInt(2);  // Entre 0 y 1.

        if (valor == 0) {
            turno = CIRCULO;
            turnoUsuario = idUsuario;
        } else {
            turno = CRUZ;
            turnoUsuario = idUsuario2;
        }
    }
    
    public String devuelveGanador(){
        return ganador;
    }
    
    public String devulveIdConexionGanador(){
        return idConexionGanador;
    }
    
    public Tablero devuelveTablero(){
        return tablero;
    }
    
    public String devuelveTurnoUsuario(){
        return turnoUsuario;
    }
    
    public String devuelveTurno(){
        return turno;
    }
    
    public String devuelveUsuario(){
        return idUsuario;
    }
    
    public String devuelveUsuario2(){
        return idUsuario2;
    }
    
    
    public String devuelveIdConexionUsuario1(){
        return idConexionUsuario1;
    }
    
    public String devuelveIdConexionUsuario2(){
        return idConexionUsuario2;
    }
    
    public void ponerUsuario2(String idUsuario){
        this.idUsuario2 = idUsuario;
    }
    public void ponerIdConexionUsuario2(String idConexion){
        this.idConexionUsuario2 = idConexion;
    }

    /*
    * Intercambia turnos entre los jugadores y pone la ficha en la casilla correspondiente
     */
    public boolean ponerFicha(String idUsuario, String codigo) {
        if(turnoUsuario.equals(idUsuario)){
            Casilla casilla = tablero.devolverCasilla(codigo);
            if (casilla != null) {
                if (casilla.devuelveTipo() == null || tablero.completo()) {
                    if (turno.equals(CIRCULO)) {
                        Circulo ficha = new Circulo();
                        casilla.introducirFicha(ficha);
                        turno = CRUZ;
                        turnoUsuario = idUsuario2;
                    } else {
                        Cruz ficha = new Cruz();
                        casilla.introducirFicha(ficha);
                        turno = CIRCULO;
                        turnoUsuario = this.idUsuario;
                    }
                    return true;
                }
            }
        }
        return false;
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
                    if(tipo.equals(Juego.CIRCULO)){
                        this.ganador = idUsuario;
                        this.idConexionGanador = idConexionUsuario1;
                    }else{
                        this.ganador = idUsuario2;
                        this.idConexionGanador = idConexionUsuario2;
                    } 
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
                    if(tipo.equals(Juego.CIRCULO)){
                        this.ganador = idUsuario;
                        this.idConexionGanador = idConexionUsuario1;
                    }else{
                        this.ganador = idUsuario2;
                        this.idConexionGanador = idConexionUsuario2;
                    } 
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
                if(tipo.equals(Juego.CIRCULO)){
                    this.ganador = idUsuario;
                    this.idConexionGanador = idConexionUsuario1;
                }else{
                    this.ganador = idUsuario2;
                    this.idConexionGanador = idConexionUsuario2;
                } 
                return "Ganador " + tipo + " en diagonal \\";
            }
        }
        return null;
    }

    /*
    * Verifica si hay ganador en la diagonal 2 (/)
     */
    public String verificarDiagonal2() {
        String tipo = tablero.devolverTipoCasilla(0, 2);
        boolean ganador = true;
        if (tipo != null) {
            for (int i = 1, j = 1; i < Tablero.DIMENSION; i++, j--) {
                if (tipo != tablero.devolverTipoCasilla(i, j)) {
                    ganador = false;
                    break;
                }
            }
            if (ganador) {
                if(tipo.equals(Juego.CIRCULO)){
                    this.ganador = idUsuario;
                    this.idConexionGanador = idConexionUsuario1;
                }else{
                    this.ganador = idUsuario2;
                    this.idConexionGanador = idConexionUsuario2;
                } 
                return "Ganador " + tipo + " en diagonal /";
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
    
    public boolean abandonoPartida(String idConexion, boolean abandono){
        if (abandono) {
            if (idConexion.equals(idConexionUsuario1)) {
                ganador = idUsuario2;
                this.idConexionGanador = idConexionUsuario2;
            } else {
                this.idConexionGanador = idConexionUsuario1;
                ganador = idUsuario;
            }
            return true;
        }
        return false;
    }
    
    public boolean completo(){
        if(tablero != null){
            return tablero.completo();
        }
        return false;
    }
}