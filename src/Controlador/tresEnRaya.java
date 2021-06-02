
package Controlador;

import Modelo.Juego;
import Vista.*;
import java.io.IOException;

/**
 *
 * @author Alberto
 */
public class tresEnRaya implements OyenteVista{
    
    private Juego juego;
    private JuegoVista vista;
    
    public tresEnRaya(){
        juego = new Juego();
        vista = JuegoVista.instancia(this, juego);
    }
    
    public void eventoProducido(Evento evento, Object obj){
        switch(evento){
            case PONER_FICHA:
                juego.ponerFicha(String.valueOf(obj));
                break;
                
            case ACABAR_PARTIDA:
                juego.acabarPartida(String.valueOf(obj));
                break;
            
            case SALIR:
                System.exit(0);
                break;
        }
    }
    
     public static void main(String[] args) throws IOException{
        new tresEnRaya();
    }
}
