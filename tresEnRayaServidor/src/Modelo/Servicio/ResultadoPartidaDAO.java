/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Servicio;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Alberto
 */
public class ResultadoPartidaDAO {
    
    private Statement bd;
    
    public ResultadoPartidaDAO(Statement bd){
        this.bd = bd;
    }
    
    public String obtenerNuevoIdResultadoPartida(){
        try {
            bd.execute("SELECT COUNT(*) AS CUENTA FROM resultado_partida");
            ResultSet consulta = bd.getResultSet();
            if (consulta.next()) {
                int id = consulta.getInt("CUENTA");
                id++;
                return String.valueOf(id);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
    public boolean nuevoResultadoPartida(ResultadoPartidaVO resultado){
        String id = obtenerNuevoIdResultadoPartida();
        if (id != null) {
            Date fecha = new Date();
            SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-aaaa hh:mm:ss ");
            java.sql.Date date = new java.sql.Date(fecha.getTime());
            System.out.println(objSDF.format(fecha));
            try {
                bd.execute("INSERT INTO resultado_partida (cod, idUsuario1, "
                        + "idUsuario2, fichaUsuario1, fichaUsuario2, fecha,"
                        + " ganador) VALUES ('" + id + "','" + resultado.getUsuario1()
                        + "','" + resultado.getUsuario2() + "','"
                        + resultado.getFichaUsuario1() + "','"
                        + resultado.getFichaUsuario2() + "','" 
                        + objSDF.format(fecha) + "','"
                        + resultado.getGanador() + "')");
                
                bd.execute("INSERT INTO tiene (cod_resultado,"
                        + " idUsuario) VALUES ('" + id + "','"
                        + resultado.getUsuario1() + "')");

                bd.execute("INSERT INTO tiene (cod_resultado,"
                        + " idUsuario) VALUES ('" + id + "','"
                        + resultado.getUsuario2() + "')");

                    System.out.println("Se ha añadido un nuevo resultado");
                    return true;
                
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        System.out.println("Error al insertar el nuevo resultado");
        return false;
    }
    
    public List<String> obtenerResultados(String idUsuario){
        return null;
    }
}
