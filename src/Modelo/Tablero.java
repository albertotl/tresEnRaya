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
    private Map<String, Casilla> tablero;
    
    public Tablero(){
        tablero = new HashMap<>();
        cargarTablero();
       
    }
    
    /*
    * Llena el tablero con 9 casillas con su respectivo codigo (1-9)
    */
    public void cargarTablero(){
        for(int cod = CASILLA_INICIAL; cod <= CASILLA_FINAL; cod++){
            Casilla aux = new Casilla(String.valueOf(cod));
            if(tablero.get(aux.devuelveCodigo()) == null){
                tablero.put(aux.devuelveCodigo(), aux);
            }
        }
    }
    
    /*
    * Introduce la ficha correpondiente en la casilla(codCasilla) del tablero
    */
    public void introducirFicha(String codCasilla, Ficha ficha){
        if(ficha != null){
            if(codCasilla != null){
                Casilla casilla = tablero.get(codCasilla);
                if(casilla != null){
                    casilla.introducirFicha(ficha);
                }
            }
        }
    }
    
    public Map<String,Casilla> devolverTablero(){
        return tablero;
    }
    
    /*
    * Devuelve la casilla(codCasilla)
    */
    public Casilla devolverCasilla(String codCasilla){
        if(codCasilla != null){
            return tablero.get(codCasilla);
        }
        return null;
    }
    
    public String devolverTipoCasilla(String codCasilla){
        if(codCasilla != null){
            Casilla casilla = tablero.get(codCasilla);
            if(casilla != null){
                return casilla.devuelveTipo();
            }
        }
        return null;
    }
}
