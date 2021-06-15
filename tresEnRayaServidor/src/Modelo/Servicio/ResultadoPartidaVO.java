/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.Servicio;

import Modelo.Juego;

/**
 *
 * @author Alberto
 */
public class ResultadoPartidaVO {
    private String usuario1;
    private String usuario2;
    private String ganador;
    private String fichaUsuario1;
    private String fichaUsuario2;
    private String fecha;

    public ResultadoPartidaVO(String usuario1, String usuario2, String ganador) {
        this.usuario1 = usuario1;
        this.fichaUsuario1 = Juego.CIRCULO;
        this.usuario2 = usuario2;
        this.fichaUsuario2 = Juego.CRUZ;
        this.ganador = ganador;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public String getGanador() {
        return ganador;
    }

    public String getFichaUsuario1() {
        return fichaUsuario1;
    }

    public String getFichaUsuario2() {
        return fichaUsuario2;
    }

    public String getFecha() {
        return fecha;
    }
    
    
    
}
