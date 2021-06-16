/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Modelo.TresEnRayaEnLinea;

import java.io.IOException;
import java.util.List;

/**
 *  Interfaz de oyente para recibir solicitudes del servidor
 */
public interface OyenteServidor {
   /**
    *  Llamado para notificar una solicitud del servidor
    */ 
   public boolean solicitudServidorProducida(
           PrimitivaComunicacion solicitud, 
           List<String> resultados) throws IOException;
}