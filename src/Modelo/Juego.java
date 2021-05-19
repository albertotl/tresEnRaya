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
    private Jugador ganador;
    
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
    
    public String mostrarTablero(){
        String cadena = "";
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                cadena = cadena + " | " + tablero.devolverTipoCasilla(i, j) + " | ";
            }
        }
        return cadena;
    }
}
