/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

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
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Alberto
 */
public class ConexionPush {
    private String idConexion;
    private String idUsuario;
    private String idJuego;
    private String direccionCliente;
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private CountDownLatch cierreConexion;
    
    /*
    * Construye conexión push con un cliente
    */
    ConexionPush(String idConexion, String direccionCliente,
            Socket socket, CountDownLatch cierreConexion)
            throws IOException{
        this.idConexion = idConexion;
        this.direccionCliente = direccionCliente;
        this.socket = socket;
        this.cierreConexion = cierreConexion;
        
        entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        
        // El true es autoflush
        salida = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                socket.getOutputStream())), true);
    }
    
    /*
    * Obtiene id de conexión
    */ 
    String obtenerIdConexion(){
        return idConexion;
    }
    
    public void ponerIdJuego(String idJuego){
        this.idJuego = idJuego;
    }
    
    /*
    * Obtiene id de juego
    */ 
    String obtenerIdJuego(){
        return idJuego;
    }
    
    /*
    * Obtiene id de cliente
    */
    String obtenerIdUsuario(){
        return idUsuario;
    }
    
    public void ponerIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
    }
    /*
    * Envia solicitud
    */
    private void enviar(PrimitivaComunicacion solicitud,
            int tiempoEspera, String parametros) throws IOException {
        socket.setSoTimeout(tiempoEspera);
        salida.println(solicitud.toString());
        
        if(parametros != null){
            salida.println(parametros);
        }
        
        salida.println(PrimitivaComunicacion.FIN);
    }
    
    /*
    * Recibe respuesta
    */
    private PrimitivaComunicacion recibir(List<String> resultados)
            throws IOException{
        PrimitivaComunicacion respuesta = PrimitivaComunicacion.NOK;
        
        String linea = entrada.readLine();
        if(linea != null){
            respuesta = PrimitivaComunicacion.nueva(
                    new Scanner(new StringReader(linea)));
            
            resultados.clear();
            
            while(entrada.ready()){
                resultados.add(entrada.readLine());
            }
        }
        return respuesta;
    }
    /*
    * Envia solicitud a Juego
    */
    synchronized PrimitivaComunicacion enviarSolicitud(
            PrimitivaComunicacion solicitud, int tiempoEspera,
            String parametros, List<String> resultados)
            throws IOException{
        enviar(solicitud, tiempoEspera, parametros);
        
        return recibir(resultados);
    }
    
    /*
    * Envia solicitud a Juego sin esperar resultados
    */
    synchronized PrimitivaComunicacion enviarSolicitud(
            PrimitivaComunicacion solicitud, int tiempoEspera,
            String parametros) throws IOException{
        return enviarSolicitud(solicitud, tiempoEspera,
                parametros, new ArrayList());
    }
    
    /*
    * Envia solicitud a Juego 
    * sin parámetros y sin esperar resultados
    */
    synchronized PrimitivaComunicacion enviarSolicitud(
            PrimitivaComunicacion solicitud, int tiempoEspera) 
            throws IOException{
        return enviarSolicitud(solicitud, tiempoEspera,
                null, new ArrayList());
    }
    
    /*
    * Cierra la conexión
    */
    synchronized void cerrar() throws IOException{
        entrada.close();
        salida.close();
        socket.close();
        
        cierreConexion.countDown();
    }
    
    public String toString(){
        return idUsuario + " " + idConexion;
   }
}
