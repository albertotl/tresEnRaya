/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Alberto
 */
public class CasillaVista extends JLabel{
    private TableroVista vista;
    
    private boolean seleccionada = false;
    private String codigo;
    private String tipo;
    
    private Font fuente;
    
    private Map atributos;
    private int tamanyoNormal;

    public final static String CIRCULO = "O";
    public final static String CRUZ = "X";
    
    CasillaVista(TableroVista vista, boolean recibeEventosRaton){
        this.vista = vista;
        //fuente = getFont();
        atributos = fuente.getAttributes();
        tamanyoNormal = fuente.getSize();
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setIcon(new ImageIcon("img/BLANCO.png"));
        
        if(recibeEventosRaton){
            recibirEventosRaton();
        }
    }

    private void recibirEventosRaton() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                CasillaVista casillaVista = (CasillaVista)e.getSource();
               
                if(casillaVista.obtenerCodigo() != null){
                   vista.seleccionarCasilla(casillaVista);
                }
            }
        });
    }
    
    public String obtenerCodigo(){
        return codigo;
    }
    
    public void ponerCodigo(String codigo){
        this.codigo = codigo;
    }
    
     /**
    * Obtiene la el codigo del asiento
    */
    public boolean estaOcupada(){
        return ocupada;
    }
    
    /**
    * Selecciona la casilla para ver su previsualización
    */
    public void seleccionar(enum Modelo.Juego.Jugador turno){
        if(turno.equals(Modelo.Juego.Jugador.CIRCULO)){
            setIcon(new ImageIcon("img/O_GRIS.png"));
        }else{
            setIcon(new ImageIcon("img/X_GRIS.png"));
        }
        seleccionada = true;
    }
    
    /**
    * Deja la casilla vacia
    */
    public void deseleccionar(enum Modelo.Juego.Jugador turno){
        setIcon(new ImageIcon("img/BLANCO.png"));
        seleccionada = false;
    }
    
    
    /*
    * Confirma donde queda puesta la ficha
    */
    public void confirmar(enum Modelo.Juego.Jugador turno){
        if(turno.equals(Modelo.Juego.Jugador.CIRCULO)){
            setIcon(new ImageIcon("img/O_BLANCO.png"));
            tipo = CIRCULO;
        }else{
            setIcon(new ImageIcon("img/X_BLANCO.png"));
            tipo = CRUZ;
        }
        seleccionada = false;
    }
}