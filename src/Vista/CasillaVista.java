/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Alberto
 */
public class CasillaVista extends JLabel {
    private JuegoVista vista;
    
    private boolean seleccionada = false;
    private boolean confirmada = false;
    private String codigo;
    
    private ImageIcon imagen;
    private Font fuente;
    private Map atributos;
    private final Color colorNoSeleccionado;
    
    private int tamanyoNormal;
    
    public final static String CIRCULO = "O";
    public final static String CRUZ = "X";
    private static final int INC_FUENTE_CODIGO = 8;
    private static final Color COLOR_SELECCIONADO = Color.YELLOW;
    private static final Color COLOR_ASIENTO_OCUPADO = Color.CYAN;
    
    public enum Formato {DESTACADO, NORMAL};
    
    CasillaVista(JuegoVista vista, boolean recibeEventosRaton){
        this.vista = vista;
        fuente = getFont();
        atributos = fuente.getAttributes();
        tamanyoNormal = fuente.getSize();
        colorNoSeleccionado = this.getBackground();
        setHorizontalAlignment(SwingConstants.CENTER);
        
        //setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //imagen = new ImageIcon("img/BLANCO.jpg");
        
       // this.setIcon(imagen);
        setVisible(true);
        if(recibeEventosRaton){
            recibirEventosRaton();
        }
    }
    
    public boolean estaConfirmada(){
        return confirmada;
    }
    
    public boolean estaSeleccionada(){
        return seleccionada;
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
    
    public void seleccionar(String turno){
        if(turno.equals(CIRCULO)){
            imagen = new ImageIcon("img/O_GRIS.jpg");
            setIcon(imagen);
            this.setVisible(true);
        }else{
            imagen = new ImageIcon("img/X_GRIS.jpg");
            setIcon(imagen);
            this.setVisible(true);
        }
        seleccionada = true; 
    }
    
    /**
     * Pone texto con formato
     */
    public void ponerTexto(String texto, Formato formato) {
        setText(texto);
        if (formato == Formato.DESTACADO) {
            atributos.put(TextAttribute.SIZE,
                    tamanyoNormal + INC_FUENTE_CODIGO);
        } else {
            atributos.put(TextAttribute.SIZE, tamanyoNormal);
        }
        setFont(fuente.deriveFont(atributos));
    }
    /**
     * Pone texto con formato normal
    */
    public void ponerTexto(String texto){
        ponerTexto(texto, Formato.NORMAL);
    }
     
    /**
    * Deja la casilla vacia
    */
    public void deseleccionar(){
        //setIcon(new ImageIcon("img/O_BLANCO.jpg"));
        //seleccionada = false;
        if (confirmada){
           setBackground(COLOR_ASIENTO_OCUPADO);
       }else{
           setBackground(colorNoSeleccionado);
       }
    }
    
    /*
    * Confirma donde queda puesta la ficha
    */
    public void confirmar(String tipo){
        if(tipo.equals(CIRCULO)){
            setIcon(new ImageIcon("img/O_BLANCO.jpg"));
            confirmada = true;
        }else{
            setIcon(new ImageIcon("img/X_BLANCO.jpg"));
            confirmada = true;
        }
        seleccionada = false;
    }

    void iniciar() {
        ponerTexto("");
        this.codigo = "";
        deseleccionar();
    }

    
}
