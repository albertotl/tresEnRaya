/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Modelo.TresEnRayaEnLinea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    // Tiempos en ms  
    public static int TIEMPO_ESPERA_LARGA_ENCUESTA = 0;   // infinito
    public static int TIEMPO_ESPERA_SERVIDOR = 1000;      
    public static int TIEMPO_REINTENTO_CONEXION_SERVIDOR = 10 * 1000; 
    
    private String URLServidor;
    private int puertoServidor;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    public Cliente(String URLServidor, int puertoServidor){
        this.URLServidor = URLServidor;
        this.puertoServidor = puertoServidor;
    }
    
    /*
    * Envia solicitud al servidor
    */
    private synchronized void enviar(PrimitivaComunicacion solicitud,
            int tiempoEspera, String parametros) throws IOException{
        socket = new Socket(URLServidor, puertoServidor);
        socket.setSoTimeout(tiempoEspera);
        
        entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        // El true es el autoflush
        salida = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        
        salida.println(solicitud.toString());
        if (parametros != null) {
            salida.println(parametros);
        }
    }
    
    /*
    * Recibe respuesta servidor
    */
    private void recibirRespuestaServidor(List<String> resultados)
            throws IOException{
        String linea = "";
        while((linea = entrada.readLine()) != null) {
            resultados.add(linea);
        }
    }
    
    /*
    * Recibe solicitud del servidor
    */
    private void recibirSolicitudServidor(List<String> resultados)
            throws IOException{
        String resultado = entrada.readLine();
        while(! resultado.equals(PrimitivaComunicacion.
                FIN.toString())){
            resultados.add(resultado);
            resultado = entrada.readLine();
        }
    }
    
    /*
    * Recibe respuesta o solicitud del servidor
    */
    private synchronized PrimitivaComunicacion recibir(
            List<String> resultados, boolean solicitudServidor) 
            throws IOException{
        // Esperamos la solicitud del servidor
        PrimitivaComunicacion respuesta = PrimitivaComunicacion.nueva(
                new Scanner(new StringReader(entrada.readLine())));
        
        // Servidor envia test de conexion
        if(respuesta == PrimitivaComunicacion.TEST){
            entrada.readLine(); // Salta a FIN
            salida.println(PrimitivaComunicacion.OK);
            return respuesta;
        }
        
        // En el resto de casos recibimos, si los hay, 
        // resultados en las lineas siguientes
        resultados.clear();
        
        if(! solicitudServidor){
            recibirRespuestaServidor(resultados);
        }else{
            recibirSolicitudServidor(resultados);
        }
        salida.println(PrimitivaComunicacion.OK);
        return respuesta;
    }
    
    /*
    * Recibe RESPUESTA del servidor
    */
    private synchronized PrimitivaComunicacion recibir(
            List<String> resultados) throws IOException{
        return recibir(resultados, false);
    }
    
    /*
    * Envia una solicitud al servidor devolviendo resultados
    */
    public synchronized PrimitivaComunicacion enviarSolicitud(
            PrimitivaComunicacion solicitud, int tiempoEspera,
            String parametros, List<String> resultados) 
            throws IOException{
        
        enviar(solicitud, tiempoEspera, parametros);
        PrimitivaComunicacion respuesta = recibir(resultados);
        
        entrada.close();
        salida.close();
        socket.close();
        
        return respuesta;
    }
    
    /*
    * Envía una solicitud al servidor sin devolver resultados
    */
    public synchronized PrimitivaComunicacion enviarSolicitud(
            PrimitivaComunicacion solicitud, int tiempoEspera,
            String parametros) throws IOException{
        return enviarSolicitud(solicitud, tiempoEspera, parametros,
                new ArrayList<String>());
    }
    
    /*
    * Envia solicitud long polling al servidor
    */
    public synchronized void enviarSolicitudLongPolling(
            PrimitivaComunicacion solicitud, int tiempoEspera, 
            OyenteServidor oyenteServidor) throws Exception{
        
         
        enviar(solicitud, tiempoEspera, null);
        
        List<String> resultados = new ArrayList<>();
        
        do{
            PrimitivaComunicacion respuesta = recibir(resultados, true);
            
            if(respuesta != PrimitivaComunicacion.TEST){
                if(oyenteServidor.solicitudServidorProducida(
                        respuesta, resultados)){
                    salida.println(PrimitivaComunicacion.OK);
                }else{
                    salida.println(PrimitivaComunicacion.NOK);
                }
            }
        } while(true);
    }
}