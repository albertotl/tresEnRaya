/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Servicio;


import java.util.List;


/**
 *
 * @author Alberto
 */
public class UsuarioVO{
    private String nombre;
    private String contrasena;
    private String direccionIp;
    
    public UsuarioVO(String nombre, String contrasena, String ip) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.direccionIp = ip;
    }
    
    public UsuarioVO(String nombre){
        this.nombre = nombre;
    }
    
    public String devuelveIp(){
        return direccionIp;
    }
    
    public void ponerUltimaIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }
    
    public String devuelveNombre() {
        return nombre;
    }

    public String devuelveContrasena() {
        return contrasena;
    }
    
}
