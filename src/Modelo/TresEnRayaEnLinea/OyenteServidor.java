/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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