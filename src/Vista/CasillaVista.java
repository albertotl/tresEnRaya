/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    
    public final static String CIRCULO = "O";
    public final static String CRUZ = "X";
    
    public enum Formato {DESTACADO, NORMAL};
    
    CasillaVista(JuegoVista vista, boolean recibeEventosRaton){
        this.vista = vista;
        setHorizontalAlignment(SwingConstants.CENTER);
        
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        imagen = new ImageIcon("img/BLANCO.jpg");
        this.setIcon(imagen);
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

    private void recibirEventosRaton(){
       addMouseListener(new MouseAdapter() {
           public void mousePressed(MouseEvent e){
               CasillaVista casillaVista = (CasillaVista)e.getSource();
               
               if(casillaVista.obtenerCodigo() != null){
                   vista.seleccionarCasillaVista(casillaVista);
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
        }else{
            imagen = new ImageIcon("img/X_GRIS.jpg");
            setIcon(imagen);
        }
        seleccionada = true; 
    }
    
    /**
    * Deja la casilla vacia
    */
    public void deseleccionar(){
        setIcon(new ImageIcon("img/BLANCO.jpg"));
        seleccionada = false;
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
        this.codigo = "";
        deseleccionar();
    }

    
}
