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
    public void ponerFicha(String codCasilla){
        Casilla casilla = tablero.devolverCasilla(codCasilla);
        
        if(casilla != null){
            if(turno.equals(Jugador.CIRCULO)){
                Circulo ficha = new Circulo();
                tablero.introducirFicha(codCasilla, ficha);
                turno = Jugador.CRUZ;
            }else{
                Cruz ficha = new Cruz();
                tablero.introducirFicha(codCasilla, ficha);
                turno = Jugador.CIRCULO;
            }
        }
    }
}
