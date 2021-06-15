/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Servicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alberto
 */
public class UsuarioDAO {
    
    private Statement bd;
    
    public UsuarioDAO(Statement bd){
        this.bd = bd;
    }
    
    public boolean iniciarSesion(UsuarioVO usuario) throws SQLException {
        //Se comprueba en la BD si existe algun usuario con ese id y contraseña
        bd.execute("SELECT * FROM usuario WHERE idUsuario = '" +
                usuario.devuelveNombre() + "' AND password = '" +
                usuario.devuelveContrasena() + "'");
        ResultSet consulta = bd.getResultSet();
        if(consulta.next()){
            bd.execute("UPDATE usuario SET ultimaIp = '" + 
                    usuario.devuelveIp() + "' WHERE idUsuario = '" +
                    usuario.devuelveNombre() + "'");
            System.out.println("El usuario " + usuario.devuelveNombre() +
                    " ha iniciado sesion");
            return true;
        }
        return false;
    }

    public boolean registrarse(UsuarioVO usuario) throws SQLException {
        //Consulta si exise ese idUsuario en la BD
        bd.execute("SELECT idUsuario FROM usuario WHERE"
                + " idUsuario = '" + usuario.devuelveNombre()+ "';");
        ResultSet consulta = bd.getResultSet();
        if(!consulta.next()){
            try{
                bd.execute("INSERT INTO usuario (idUsuario, password,"
                        + " ultimaIp) VALUES ('" + usuario.devuelveNombre()
                        + "','" + usuario.devuelveContrasena() + "','" +
                        usuario.devuelveIp() + "')");
                System.out.println("Se ha añadido el nuevo usuario");
                return true;
            }catch (SQLException e){
                System.out.println("Error al insetar el nuevo usuario");
            }
        }
        return false;
    }

    public List<String> obtenerHistorial(UsuarioVO usuario) {
        List<String> historial = new ArrayList<>();
        try{
            bd.execute("SELECT r.idUsuario1, r.idUsuario2, r.ganador, r.fecha "
                    + "FROM tiene t, resultado_partida r WHERE t.idUsuario = '"
                    + usuario.devuelveNombre() + "' AND cod = cod_resultado;");
            ResultSet resultado = bd.getResultSet();
            int i = 1;
            while(resultado.next()){
                String ganador = resultado.getString("ganador");
                String idUsuario1 = resultado.getString("idUsuario1");
                String idUsuario2 = resultado.getString("idUsuario2");
                String partida = "";
                if(ganador.equals(idUsuario1) &&
                        ganador.equals(usuario.devuelveNombre())){
                    partida = i + ". VICTORIA contra " 
                            + resultado.getString("idUsuario2") 
                            + " el día " + resultado.getString("fecha");
                }else if(ganador.equals(idUsuario2) &&
                        ganador.equals(usuario.devuelveNombre())){
                    partida = i + ". VICTORIA contra " 
                            + resultado.getString("idUsuario1") 
                            + " el día " + resultado.getString("fecha");
                }else if(ganador.equals(idUsuario1) &&
                        (!ganador.equals(usuario.devuelveNombre()))){
                    partida = i + ". DERROTA contra " 
                            + resultado.getString("idUsuario1") 
                            + " el día " + resultado.getString("fecha");
                }else if(ganador.equals(idUsuario2) &&
                        !(ganador.equals(usuario.devuelveNombre()))){
                    partida = i + ". DERROTA contra " 
                            + resultado.getString("idUsuario2") 
                            + " el día " + resultado.getString("fecha");
                }
                i++;
                historial.add(partida);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return historial;
    }
}
