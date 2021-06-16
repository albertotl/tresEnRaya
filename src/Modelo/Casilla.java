/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Modelo;

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
    
    public Ficha devuelveFicha(){
        return ficha;
    }
    
    public void introducirFicha(Ficha ficha){
        this.ficha = ficha;
    }
    
    public void vaciar(){
        this.ficha = null;
    }
    
    public String devuelveTipo(){
        if(ficha != null){
            return ficha.devolverTipo();
        }
        return null;
    }

   
}


