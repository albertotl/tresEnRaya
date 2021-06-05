
package Controlador;

import Modelo.*;
import Vista.*;
import Vista.VistaMenu.InicioSesionVista;
import java.io.IOException;

/**
 *
 * @author Alberto
 */
public class tresEnRaya implements OyenteVista{
    
    private Juego juego;
    private InicioSesionVista vista;
    
    public tresEnRaya(){
        juego = new Juego();
        //vista = JuegoVista.instancia(this, juego);
        vista = InicioSesionVista.instancia(this, juego);
    }
    
    public void eventoProducido(Evento evento, Object obj){
        switch(evento){
            case PONER_FICHA:
                juego.ponerFicha(String.valueOf(obj));
                break;
                
            case ACABAR_PARTIDA:
                juego.acabarPartida();
                break;
            
            case INICIAR_SESION:
                Tupla<String, String> tupla = (Tupla<String, String>)obj;
                juego.iniciarSesion(tupla.a, tupla.b);
                break;
                
            case REGISTRARSE:
                Tupla<String, String> tupla2 = (Tupla<String, String>)obj;
                juego.registrar(tupla2.a, tupla2.b);
                break;
            
            case SALIR:
                System.exit(0);
                break;
            
            case BUSCAR_PARTIDA:
                juego.buscarPartida();
                break;
        }
    }
    
     public static void main(String[] args) throws IOException{
        new tresEnRaya();
    }
}
