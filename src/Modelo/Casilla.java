/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Alberto
 */
public class Casilla {
    private String codigo;
    private Ficha ficha;
    
    public Casilla(String codigo){
        this.codigo = codigo;
        this.ficha = null;
    }
    
    public String devuelveCodigo(){
        return codigo;
    }
    
    public void introducirFicha(Ficha ficha){
        this.ficha = ficha;
    }
    
    public String devuelveTipo(){
        if(this.ficha != null){
            return ficha.devolverTipo();
        }
        return null;
    }
}


