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
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Alberto
 */
public class ServicioConexion implements Runnable{
    public static final String SEPARADOR = "'";
    public static final String OK = "OK";
    public static String ERROR_CONEXION = "Closed conexion";
    private static final String ENTRADA = "Recibe: ";
    private static final String NUEVA_CONEXION =
            "Nueva conexion con cliente:  ";
    private static final String DESCONEXION = 
            "Desconectado: ";
    private static final String USUARIO = "   usuario: ";
    private static final String ACABAR_PARTIDA = "Solicitud de abandono de partida";
    private static final String HISTORIAL_VACIO = "El historial esta vacio";
    private static final String INICIA_SESION = "Ha iniciado sesion el usuario: ";
    private static final String ERROR_INICIA_SESION = "Ha fallado la autenticacion del usuario: ";
    private static final String HISTORIAL = "Se envia el historial: ";
    private static final String ID_CONEXION = "  iDConexion: ";  
    private static final String PONER_FICHA = " Solicitud de poner ficha";
    private static final String BUSCAR_PARTIDA = "Solicitud de buscar partida";
    private static final String CERRAR_SESION = "Solicitud de cerrar sesion";
    private static final String INICIAR_SESION = "Solicitud de iniciar sesion";
    private static final String PEDIR_HISTORIAL = "Solicitud de historial";
    private static final String ENVIO = "Envío: ";
    private static final String ERROR = "ERROR";
    private static final String REGISTRO = "Solicitud de registro";
    private static final String REGISTRARSE = "Se ha registrado el usuario: ";
    private static final String CONEXION_PUSH = "Solicitud de conexión";
    private static final String DIRECCION = "  direccion ip: ";
    private static final String DESCONEXION_PUSH = "Solicitud desconexion";
    
    
    
    private ServidorTresEnRaya servidor;
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    /*
    * Construye un servicio para un el juego de un usuario
    */
    ServicioConexion(ServidorTresEnRaya servidor,
            Socket socket) throws IOException{
        this.servidor = servidor;
        this.socket = socket;
        
        entrada = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
    
        // Autoflush!!
        salida = new PrintWriter(new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream())), true);
    }
    
    /*
    * Cierra conexión
    */
    private void cerrarConexion() throws IOException{
        entrada.close();
        salida.close();
        socket.close();
    }
    
    @Override
    public void run(){
        try{
            PrimitivaComunicacion solicitud = PrimitivaComunicacion.nueva(
                    new Scanner(new StringReader(entrada.readLine())));
            
            switch(solicitud){
                case CONECTAR_PUSH:
                    System.out.println(ENTRADA + CONEXION_PUSH);
                    conectarPush();
                    break;
                    
                case DESCONECTAR_PUSH:
                    System.out.println(ENTRADA + DESCONEXION_PUSH);
                    desconectarPush();
                    break;
                
                case REGISTRARSE:
                    System.out.println(ENTRADA + REGISTRO);
                    registrar();
                    break;
                
                case INICIAR_SESION:
                    System.out.println(ENTRADA + INICIAR_SESION);
                    iniciarSesion();
                    break;
                    
                case BUSCAR_PARTIDA:
                    System.out.println(ENTRADA + BUSCAR_PARTIDA);
                    buscarPartida();
                    break;
                
                case PONER_FICHA:
                    System.out.println(ENTRADA + PONER_FICHA);
                    ponerFicha();
                    break;
                    
                case PEDIR_HISTORIAL:
                    System.out.println(ENTRADA + PEDIR_HISTORIAL);
                    devolverHistorial();
                    break;
                    
                case ACABAR_PARTIDA:
                    System.out.println(ENTRADA + ACABAR_PARTIDA);
                    acabarPartida();
                    break;
                    
                case CERRAR_SESION:
                    System.out.println(ENTRADA + CERRAR_SESION);
                    cerrarSesion();
                    break;
                    
            }
        } catch (IOException e){
            System.out.println(ERROR_CONEXION + 
                    ": " + e.toString() + "IOException");
        }catch (InterruptedException ex) {
            System.out.println(ERROR_CONEXION + 
                    ": " + ex.toString() + "InterruptedException");
        } catch (SQLException ex) {
           
        }
    }
    
    
    private void conectarPush() throws IOException, InterruptedException{
        String direccionCliente = String.valueOf(socket.
                getRemoteSocketAddress());
        
        CountDownLatch cierreConexion = new CountDownLatch(1);
        
        String idConexion = servidor.generarIdConexionPush();
        
        ConexionPush conexionPush = new ConexionPush(idConexion, direccionCliente,
            socket, cierreConexion);
        
        PrimitivaComunicacion respuesta = conexionPush.
            enviarSolicitud(PrimitivaComunicacion.NUEVO_ID_CONEXION,
            servidor.TIEMPO_ESPERA_CLIENTE, idConexion);
        
        if(respuesta.equals(PrimitivaComunicacion.OK)){
            servidor.nuevaConexionPush(conexionPush);
            System.out.println(NUEVA_CONEXION +
                    DIRECCION + direccionCliente + ID_CONEXION + idConexion);
            cierreConexion.await();
        }else{
            conexionPush.cerrar();
        }
    }
    
    /*
    * Desconecta tresEnRaya push
    */
    private void desconectarPush() throws IOException{
        String idConexion = entrada.readLine();
        
        if(servidor.eliminarConexionPush(idConexion)){
            salida.println(PrimitivaComunicacion.OK);
            System.out.println(DESCONEXION + ID_CONEXION + idConexion);
        }else{
            salida.println(PrimitivaComunicacion.NOK);
        }
        cerrarConexion();
    }
    
    
    private void iniciarSesion() throws IOException, SQLException{
        
        String informacion = entrada.readLine();
        String[] credenciales = informacion.split(SEPARADOR);
        salida.println(PrimitivaComunicacion.INICIAR_SESION);
        if(credenciales.length < 4){
            String idConexion = credenciales[0];
            String usuario = credenciales[1];
            String contrasena = credenciales[2];
            
            if(usuario != null && contrasena != null){
                String direccionCliente = String.valueOf(socket.
                    getRemoteSocketAddress());
                if(servidor.iniciarSesion(usuario, contrasena,
                        direccionCliente, idConexion)){
                    System.out.println(INICIA_SESION + usuario);
                    servidor.ponerUsuarioConexionPush(idConexion, usuario);
                    List<String> historial = servidor.obtenerHistorial(usuario);
                    if(!historial.isEmpty()){
                        System.out.println(HISTORIAL);
                        for(String datos : historial){
                            salida.println(datos);
                        }
                    }else{
                        salida.println(HISTORIAL_VACIO);
                    }
                }else{
                    System.out.println(ERROR_INICIA_SESION + usuario);
                    salida.println(ERROR);
                }
            }else{
                salida.println(ERROR);
            }
        }else{
            salida.println(ERROR);
        }
        cerrarConexion();
    } 
    
    private void buscarPartida() throws IOException{
        String informacion = entrada.readLine();
        String[] datos = informacion.split(SEPARADOR);
        salida.println(PrimitivaComunicacion.BUSCAR_PARTIDA);
        if(datos.length < 3){
            String idUsuario = datos[0];
            String idConexion = datos[1];
            if(idUsuario != null && idConexion != null){
                String info = servidor.unirseAPartida(idUsuario, idConexion);
                if(info == null){
                    String idJuego = servidor.nuevaPartida(idUsuario, idConexion);
                    System.out.println("El usuario " + idUsuario 
                            + " ha creado una nueva partida" 
                            + " con id: " + idJuego);
                }else{
                    String[] inf = info.split(SEPARADOR);
                    
                    System.out.println("El usuario " + idUsuario 
                            + " se ha unido a la partida" 
                            + " con id: " + inf[0] + " con el usuario " + inf[2]);
                    salida.println(inf[0]);
                    salida.println(inf[1]);
                    salida.println(inf[2]);
                    salida.println(inf[3]);
                } 
            }
        }
        cerrarConexion();
    }
    
    private void acabarPartida() throws IOException{
        String idUsuario = entrada.readLine();
        servidor.acabarPartida(idUsuario);
        salida.println(PrimitivaComunicacion.OK);
        cerrarConexion();
    }
    
    private void devolverHistorial() throws IOException{
        String idUsuario = entrada.readLine();
        if(idUsuario != null){
            salida.println(PrimitivaComunicacion.PEDIR_HISTORIAL);
            List<String> historial = servidor.obtenerHistorial(idUsuario);
            if(historial != null){
                System.out.println(HISTORIAL);
                for(String datos : historial){
                    salida.println(datos);
                }
            }
        }
        cerrarConexion();
    }
    
    private void ponerFicha() throws IOException{
        String informacion = entrada.readLine();
        String[] datos = informacion.split(SEPARADOR);
        
        if(datos.length < 4){
            String idUsuario = datos[0];
            String idJuego = datos[1];
            String posicion = datos[2];
            if(servidor.ponerFicha(idUsuario, idJuego, posicion)){
                System.out.println("El usuario: '" + idUsuario + "'"
                        + " ha puesto la ficha en: '" + posicion 
                        + "' del tablero '" + idJuego + "'");
                salida.println(PrimitivaComunicacion.OK);
                if(servidor.verificarPartida(idJuego, false)){
                    System.out.println("Se notifica a los usuarios"
                            + " que se ha acabado la partida");
                }
            }
        }
        cerrarConexion();
    }
    
    private void cerrarSesion() throws IOException{
        String idConexion = entrada.readLine();
        if(idConexion != null){
            if(servidor.cerrarSesion(idConexion)){
                System.out.println("El usuario: '" + idConexion + "' ha cerrado sesion");
                salida.println(PrimitivaComunicacion.OK);
            }else{
                salida.println(PrimitivaComunicacion.NOK);
            }
        }else{
            salida.println(PrimitivaComunicacion.NOK);
        }
        cerrarConexion();
    }
    
    private void registrar() throws IOException, SQLException{
        PrimitivaComunicacion respuesta = PrimitivaComunicacion.NOK;
        String informacion = entrada.readLine();
        String[] credenciales = informacion.split(SEPARADOR);
        if(credenciales.length < 3){
            String usuario = credenciales[0];
            String contrasena = credenciales[1];
            if(usuario != null && contrasena != null){
                String direccionCliente = String.valueOf(socket.
                    getRemoteSocketAddress());
                if(servidor.registrar(usuario, contrasena, direccionCliente)){
                    System.out.println(REGISTRARSE + usuario);
                    respuesta = PrimitivaComunicacion.OK;
                }
            }
        }
        salida.println(respuesta);
        cerrarConexion();
    }
}
