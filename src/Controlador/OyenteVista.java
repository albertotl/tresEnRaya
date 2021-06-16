/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Controlador;


public interface OyenteVista {
    public enum Evento {PONER_FICHA, ABANDONAR_PARTIDA, PEDIR_HISTORIAL,
    INICIAR_SESION, CERRAR_SESION, BUSCAR_PARTIDA, REGISTRARSE, SALIR};
    
    public void eventoProducido(Evento evento, Object obj);
}
