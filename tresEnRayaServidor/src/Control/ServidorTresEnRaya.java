package Control;

import Modelo.*;
import Modelo.Servicio.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Alberto
 */
public class ServidorTresEnRaya extends Thread{
    private ServidorTresEnRaya servidor;
    private Map<String, Juego> partidas;
    private Map<String, ConexionPush> conexionesPush;
    private int numConexion = 0;
    private int numPartida = 0;
    
    private static String FICHERO_CONFIG_ERRONEO = 
          "Config file is wrong. Set default values";
    public static String VERSION = "Tres en Raya 1.0";
    private static String ESPERANDO_SOLICITUD_USUARIO = 
          "Waiting for users requests...";
    private static String ERROR_EJECUCION_SERVIDOR =
            "Error: Server running in ";   
    private static String ERROR_CREANDO_CONEXION_USUARIO = 
          "Failed to create user-server connection";
   private static String OK = "OK";
   private static String SEPARADOR = "'";
   private static String PARTIDA_GANADA = "partida_ganada";
   private static String PARTIDA_PERDIDA = "partida_perdida";
    
    private static int TIEMPO_TEST_CONEXIONES = 10 * 1500;
    public static int TIEMPO_ESPERA_CLIENTE = 1000;
    
    /** Configuración sockets **/
    private Properties propiedades;
    private static final String FICHERO_CONFIG = "config.properties";
    private static final String NUM_THREADS = "threadsNumber";
    private int numThreads = 20;
    private static final String PUERTO_SERVIDOR = "serverPort";
    private int puertoServidor = 25000;
    
    /** Configuracion conexion BD **/
    private static Connection conexion;
    private Statement bd;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "";
    private static final String url = "jdbc:mysql://localhost:3306/tresenraya_bd";
    
    /*
    * Contruye el servidor de venta billetes
    */
    public ServidorTresEnRaya() throws IOException{
        servidor = this;
        conexionesPush = new ConcurrentHashMap<>();
        partidas = new HashMap<>();
        leerConfiguracion();
        conectarBD();
        envioTestConexionPeriodicoPush();
        start();
    }
    
    /*
    * Conecta con la base de datos
    */
    public void conectarBD() {
        conexion = null;
        try{
            Class.forName(driver);
            conexion = (Connection) DriverManager.
                    getConnection(url, usuario, contrasena);
            if (conexion != null){
                bd = conexion.createStatement();
                System.out.println("Conexion con base de datos exitosa");
            }
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("Error al conectar con la base de datos: " +  e);
        }
    }
    
    /*
    * Lee la configuracion del servidor
    */
    private void leerConfiguracion() throws 
            FileNotFoundException, IOException{
        try{
            propiedades = new Properties();
            propiedades.load(new FileInputStream(FICHERO_CONFIG));
            numThreads = Integer.parseInt(
                    propiedades.getProperty(NUM_THREADS));
            puertoServidor = Integer.parseInt(
                    propiedades.getProperty(PUERTO_SERVIDOR));
        } catch(Exception e){
            System.out.println(FICHERO_CONFIG_ERRONEO);
            System.out.println(NUM_THREADS + " = " + numThreads);
            System.out.println(PUERTO_SERVIDOR + " = " + puertoServidor);
        }
    }
    
    /*
    * Ejecuta bucle en espera de conexiones
    */
    public void run(){
        System.out.println(VERSION);
        try{
            ExecutorService poolThreads = 
                    Executors.newFixedThreadPool(numThreads);
            ServerSocket serverSocket = new ServerSocket(puertoServidor);
            
            while(true){
                System.out.println(ESPERANDO_SOLICITUD_USUARIO);
                Socket socket = serverSocket.accept();
                poolThreads.execute(
                        new ServicioConexion(this, socket));
            }
        } catch (BindException e){
           System.out.println(ERROR_EJECUCION_SERVIDOR + puertoServidor);
        } catch (IOException ex) {
           System.out.println(ERROR_CREANDO_CONEXION_USUARIO);
        }
    }
    
    /*
    * Envia test periodicos para mantener lista las conexiones push
    */
    private void envioTestConexionPeriodicoPush(){
        new Timer().scheduleAtFixedRate(new TimerTask(){
            public void run(){
              for(ConexionPush conexionPush :
                        conexionesPush.values()){
                try{
                    conexionPush.enviarSolicitud(
                        PrimitivaComunicacion.TEST,TIEMPO_TEST_CONEXIONES);
                        
                } catch(IOException e1){
                    System.out.println(ServicioConexion.
                            ERROR_CONEXION + " " + conexionPush.toString());
                    acabarPartidaDesconexion(conexionPush);
                    conexionesPush.remove(conexionPush.obtenerIdConexion());
                    try{
                        conexionPush.cerrar();
                    } catch(IOException e2){
                        // No se hace nada porque ya se cerro
                        // la conexión
                    }
                }
              }
            }
        }, TIEMPO_TEST_CONEXIONES, TIEMPO_TEST_CONEXIONES);
    }
    
    /*
    * Borra la partina haciendo las notificaciones pertinentes
    */
    synchronized void acabarPartidaDesconexion(ConexionPush conexionPush) {
        if (conexionPush.obtenerIdJuego() != null) {
            Juego juego = partidas.get(conexionPush.obtenerIdJuego());
            if (juego != null) {
                if (juego.devuelveUsuario().equals(
                        conexionPush.obtenerIdUsuario())) {
                    ConexionPush conexion = obtenerConexionPush(
                            juego.devuelveIdConexionUsuario2());
                    conexion.ponerIdJuego(null);
                    System.out.println("Se notifica a usuario: " + juego.devuelveIdConexionUsuario2() + " que se cierra el juego");
                    try {
                        notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                                PARTIDA_GANADA, juego.devuelveIdConexionUsuario2());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else {
                    ConexionPush conexion = obtenerConexionPush(
                            juego.devuelveIdConexionUsuario1());
                    conexion.ponerIdJuego(null);
                    try {
                        notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                                PARTIDA_GANADA, juego.devuelveIdConexionUsuario1());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
                partidas.remove(conexionPush.obtenerIdJuego());
            }
        }
    }
    
    public void acabarPartida(String idConexion){
        ConexionPush conexion = conexionesPush.get(idConexion);
        String idJuego = conexion.obtenerIdJuego();
        verificarPartida(idJuego, true);
    }
    
    /*
    * Asocia un idConexion con un idUsuario
    */
    public void ponerUsuarioConexionPush(String idConexion,
            String idUsuario){
        ConexionPush conexion = conexionesPush.get(idConexion);
        
        if(conexion != null){
            conexion.ponerIdUsuario(idUsuario);
        }
    }
    
    /*
    * Genera nuevo identificador de conexión de un cliente
    */
    synchronized String generarIdConexionPush(){
        return String.valueOf(++numConexion);
    }
    
    /*
    * Genera nuevo identificador de partida
    */
    public String generarIdPartida(){
        return String.valueOf(++numPartida);
    }
    
    public boolean cerrarSesion(String idConexion) throws IOException{
        ConexionPush conexion = conexionesPush.get(idConexion);
        if(conexion != null){
            String idJuego = conexion.obtenerIdJuego();
            if(partidas.get(idJuego) != null){
                partidas.remove(idJuego);
            }
            conexionesPush.remove(idConexion);
            return true;
        }
        return false;
    }
    /*
    * Obtiene conexionPush por su id de conexión
    */
    ConexionPush obtenerConexionPush(String idConexion){
        ConexionPush conexionPush = 
                conexionesPush.get(idConexion);
        
        if(conexionPush != null){
            return conexionPush;
        }
        return null;
    }
    
    /*
    * Nueva partida
    */
    synchronized String nuevaPartida(String idUsuario, String idConexion){
        for (Map.Entry<String, Juego> partida : partidas.entrySet()) {
            if(partida.getValue().devuelveUsuario().equals(idUsuario)){
                return null;
            }
        }
        ConexionPush conexion = obtenerConexionPush(idConexion);
        if(conexion != null){
            Juego juego = new Juego(idUsuario, idConexion);
            String idJuego = generarIdPartida();
            conexion.ponerIdJuego(idJuego);
            partidas.put(idJuego, juego);
            return idJuego;
        }
        return null;
    }
    
    synchronized boolean ponerFicha(String idUsuario, String idJuego, String posicion){
        Juego juego = partidas.get(idJuego);
        if(juego.ponerFicha(idUsuario, posicion)){
            String informacion = posicion + SEPARADOR + juego.devuelveTurnoUsuario();
            if(juego.devuelveTurnoUsuario().equals(juego.devuelveUsuario())){
                String idConexion1 = juego.devuelveIdConexionUsuario1();
                try{
                   notificarPush(PrimitivaComunicacion.PONER_FICHA,
                        informacion, idConexion1); 
                   System.out.println("Se notifica a usuario: " + idConexion1 + " que se ha puesto ficha");
                   return true;
                }catch(IOException e){
                    System.out.println(e);
                }
            }else{
                String idConexion2 = juego.devuelveIdConexionUsuario2();
                try{
                   notificarPush(PrimitivaComunicacion.PONER_FICHA,
                        informacion, idConexion2);
                  System.out.println("Se notifica a usuario: " + idConexion2 + " que se ha puesto ficha");
                   return true;
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
        return false;
    }
    
    synchronized boolean verificarPartida(String idJuego, boolean abandono){
        Juego juego = partidas.get(idJuego);
        if(juego.verificarTablero() != Juego.VACIO || 
                juego.abandonoPartida(idJuego, abandono)){
            String idConexionGanador = juego.devulveIdConexionGanador();
            
            ResultadoPartidaVO resultado = new ResultadoPartidaVO(
                    juego.devuelveUsuario(), juego.devuelveUsuario2(),
                    juego.devuelveGanador());
            ResultadoPartidaDAO resultadoDAO = new ResultadoPartidaDAO(bd);
            resultadoDAO.nuevoResultadoPartida(resultado);
            
            if(idConexionGanador.equals(juego.devuelveIdConexionUsuario1())){
                try{
                    notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                        PARTIDA_GANADA, juego.devuelveIdConexionUsuario1());
                    notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                        PARTIDA_PERDIDA, juego.devuelveIdConexionUsuario2());
                }catch(IOException e){
                    System.out.println(e);
                }
            }else{
                try{
                    notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                        PARTIDA_GANADA, juego.devuelveIdConexionUsuario2());
                    notificarPush(PrimitivaComunicacion.ACABAR_PARTIDA,
                        PARTIDA_PERDIDA, juego.devuelveIdConexionUsuario1());
                }catch(IOException e){
                    System.out.println(e);
                }
            }
            partidas.remove(idJuego);
            return true;
        }
        return false;
    }
    
    public String buscarJuegoLibre() {
        if (!partidas.isEmpty()) {
            for (Map.Entry<String, Juego> partida : partidas.entrySet()) {
                if (partida.getValue() != null) {
                    Juego juego = partida.getValue();
                    if (juego.devuelveUsuario2() == null) {
                        return partida.getKey();
                    }
                }
            }
        }
        return null;
    }

    synchronized String unirseAPartida(String idUsuario, String idConexion)
            throws IOException {
        String idJuego = buscarJuegoLibre();
        if (idJuego != null) {
            ConexionPush conexion = conexionesPush.get(idConexion);
            if (conexion != null) {
                Juego juego = partidas.get(idJuego);
                conexion.ponerIdJuego(idJuego);
                juego.ponerUsuario2(idUsuario);
                juego.ponerIdConexionUsuario2(idConexion);
                String idConexionUsuario1 = juego.devuelveIdConexionUsuario1();
                
                juego.asignarTurnos();
                String turnoUsuario = juego.devuelveTurnoUsuario();
                String informacion = idJuego + SEPARADOR + turnoUsuario +
                            SEPARADOR + Juego.CIRCULO + SEPARADOR +
                            idUsuario;
                notificarPush(PrimitivaComunicacion.PARTIDA_ENCONTRADA,
                    informacion, idConexionUsuario1);
                
                return idJuego + SEPARADOR + turnoUsuario + SEPARADOR + juego.devuelveUsuario() + SEPARADOR + Juego.CRUZ;
            }
        }
        return null;
    }
    /*
    private String buscarIdConexion(String idUsuario){
        for(ConexionPush conexion : conexionesPush.values()){
            if(conexion.obtenerIdUsuario().equals(idUsuario)){
                return conexion.obtenerIdConexion();
            }
        }
        return null;
    }
    */
    
    /*
    * Nueva conexion push
    */
    synchronized void nuevaConexionPush(ConexionPush conexionPush){
            conexionesPush.put(conexionPush.obtenerIdConexion(), conexionPush);
    }
    
    /*
    * Elimina conexion pushBilletes
    */
    synchronized boolean eliminarConexionPush(String idConexion)
            throws IOException{
        ConexionPush conexionPush = 
                conexionesPush.get(idConexion);
        
        if(conexionPush == null){
            return false;
        }
        conexionPush.cerrar();
        conexionesPush.remove(idConexion);
        return false;
    }
    
    /*
    * Notifica a otro usuario el cambio en el juego
    */
    private void notificarPush(PrimitivaComunicacion primitiva,
            String parametros, String idConexion) throws IOException{
        ConexionPush conexion = conexionesPush.get(idConexion);
        if(conexion != null){
            conexion.enviarSolicitud(primitiva, 
                    TIEMPO_ESPERA_CLIENTE, parametros);
        }
    }
    
    synchronized boolean iniciarSesion(String idUsuario,
            String contrasena, String ip, String idConexion) 
            throws IOException, SQLException{
        for(ConexionPush conexion : conexionesPush.values()){
            if(conexion.obtenerIdUsuario() != null){
                if(conexion.obtenerIdUsuario().equals(idUsuario)){
                    return false;
                }
            }
        }
        if(bd != null){
            UsuarioVO usuario = new UsuarioVO(idUsuario, contrasena, ip);
            UsuarioDAO usuarioDAO = new UsuarioDAO(bd);
            return usuarioDAO.iniciarSesion(usuario);
        }
        return false;
    }
    
    synchronized List<String> obtenerHistorial(String idUsuario){
        if(bd != null){
            UsuarioVO usuario = new UsuarioVO(idUsuario);
            UsuarioDAO usuarioDAO = new UsuarioDAO(bd);
            return usuarioDAO.obtenerHistorial(usuario);
        }
        return null;
    }
    
    
    
    synchronized boolean registrar(String idUsuario,
            String contrasena, String ip) throws SQLException{
        if(bd != null){
            UsuarioVO usuario = new UsuarioVO(idUsuario, contrasena, ip);
            UsuarioDAO usuarioDAO = new UsuarioDAO(bd);
            return usuarioDAO.registrarse(usuario);
        }
        return false;
    }
    
    public static void main(String args[]) throws  IOException{
        new ServidorTresEnRaya();
    }
}
