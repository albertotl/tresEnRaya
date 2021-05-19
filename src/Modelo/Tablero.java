/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alberto
 */
public final class Tablero {
    public final int CASILLA_INICIAL = 1;
    public final int CASILLA_FINAL = 9;
    private Casilla tablero[][];
    
    public Tablero(){
        tablero = new Casilla[3][3];
        cargarTablero();
       
    }
    
    /*
    * Llena el tablero con 9 casillas con su respectivo codigo (1-9)
    */
    public void cargarTablero(){
        int cod = 1;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    Casilla aux = new Casilla(String.valueOf(cod));
                    tablero[i][j] = aux;
                    cod++;
                }
            }
            
            
        
    }
    
    /*
    * Introduce la ficha correpondiente en la casilla(codCasilla) del tablero
    
    public void introducirFicha(int fila, int columna, Ficha ficha){
        if(ficha != null){
            if(fila > 0 || fila <= 3 || columna > 0 || columna <= 3){
                Casilla casilla = tablero[fila-1][columna-1];
                if(casilla != null){
                    casilla.introducirFicha(ficha);
                }
            }
        }
    }
    */
    
    
    /*
    * Devuelve la casilla(codCasilla)
    */
    public Casilla devolverCasilla(int fila, int columna){
        if(fila > 0 || fila <= 3 || columna > 0 || columna <= 3){
            return tablero[fila][columna];
        }
        return null;
    }
    
    public String devolverTipoCasilla(int fila, int columna){
        if(fila > 0 || fila <= 3 || columna > 0 || columna <= 3){
            return tablero[fila][columna].devuelveTipo();
        }
        return null;
    }
}
