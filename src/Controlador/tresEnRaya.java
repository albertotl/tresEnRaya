
package Controlador;

import Modelo.*;
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
        juego.conectar();
        vista = InicioSesionVista.instancia(this, juego);
        juego.nuevoObservador(vista);
    }
    
    public void eventoProducido(Evento evento, Object obj){
        switch(evento){
            case PONER_FICHA:
                juego.ponerFicha(String.valueOf(obj));
                break;
                
            case ABANDONAR_PARTIDA:
                juego.abandonarPartida();
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
                
            case PEDIR_HISTORIAL:
                juego.pedirHistorial();
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
