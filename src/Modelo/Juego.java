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
    private enum Jugador{CIRCULO, CRUZ};
    
    private Tablero tablero;
    private Jugador turno;
    
    public Juego(){
        tablero = new Tablero();
        Random r = new Random();
        int valor = r.nextInt(1);  // Entre 0 y 1.
        
        if(valor == 0){
            turno = Jugador.CIRCULO;
        }else{
            turno = Jugador.CRUZ;
        }
    }
    
    /*
    * Intercambia turnos entre los jugadores y pone la ficha en la casilla correspondiente
    */
    public void ponerFicha(int fila, int columna){
        if (fila > 0 && fila <= 3 && columna > 0 && columna <= 3){
            Casilla casilla = tablero.devolverCasilla(fila-1, columna-1);
            
            if(casilla.devuelveTipo() == null || tablero.completo()){
                if(turno.equals(Jugador.CIRCULO)){
                    Circulo ficha = new Circulo();
                    casilla.introducirFicha(ficha);
                    turno = Jugador.CRUZ;
                }else{
                    Cruz ficha = new Cruz();
                    casilla.introducirFicha(ficha);
                    turno = Jugador.CIRCULO;
                }
            }
        }
    }
    
    public String mostrarTablero(){
        String cadena = "";
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cadena = cadena + " | " + tablero.devolverTipoCasilla(i, j) + " | ";
            }
        }
        return cadena;
    }
    
    /*
    * Verifica si hay ganador en las filas
    */
    public String verificarFilas(){
        boolean ganador = true;
        
        for(int i = 0; i < Tablero.DIMENSION; i++){
            String tipo = tablero.devolverTipoCasilla(i, 0);
           
            if(tipo != null){
                for(int j = 0; j < Tablero.DIMENSION; j++){
                    if(tipo != tablero.devolverTipoCasilla(i, j)){
                        ganador = false;
                    }
                }
                if(ganador){
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
    public String verificarColumnas(){
        boolean ganador = true;
        
        for(int i = 0; i < Tablero.DIMENSION; i++){
            String tipo = tablero.devolverTipoCasilla(0, i);
           
            if(tipo != null){
                for(int j = 0; j < Tablero.DIMENSION; j++){
                    if(tipo != tablero.devolverTipoCasilla(j, i)){
                        ganador = false;
                    }
                }
                if(ganador){
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
    public String verificarDiagonal1(){
        boolean ganador = true;
        
        String tipo = tablero.devolverTipoCasilla(0, 0);
        if(tipo != null){
            for(int i = 1; i < Tablero.DIMENSION; i++){
                if(tipo != tablero.devolverTipoCasilla(i, i)){
                    ganador = false;
                    break;
                }
            }
            if(ganador){
                return "Ganador " + tipo + " en diagonal \\";
            }
        }
        return null;
    }
    
    /*
    * Verifica si hay ganador en la diagonal 2 (/)
    */
    public String verificarDiagonal2(){
        
        String tipo2 = tablero.devolverTipoCasilla(0, 2);
        boolean ganador = true;
        if(tipo2 != null){
            for(int i = 1, j = 1; i < Tablero.DIMENSION; i++, j--){
                if(tipo2 != tablero.devolverTipoCasilla(i, j)){
                    ganador = false;
                    break;
                }
            }
            if(ganador){
                return "Ganador " + tipo2 + " en diagonal /";
            }
        }
        return null;  
    }
    
    /*
    * Verifica si hay ganador en el tablero
    */
    public String verificarTablero(){
        String filas = verificarFilas();
        if(filas != null) return filas;
        
        String columnas = verificarColumnas();
        if(columnas != null) return columnas;
        
        String diagonal1 = verificarDiagonal1();
        if(diagonal1 != null) return diagonal1;
        
        String diagonal2 = verificarDiagonal2();
        if(diagonal2 != null) return diagonal2;
        
        return "";
    }
}
