/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author Alberto
 */
public interface OyenteVista {
    public enum Evento {PONER_FICHA, SALIR};
    
    public void eventoProducido(Evento evento, Object obj);
}
