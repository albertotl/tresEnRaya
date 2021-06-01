
package Controlador;

import Modelo.Juego;
import Vista.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Alberto
 */
public class tresEnRaya implements OyenteVista{
    
    private Juego juego;
    private JuegoVista vista;
    
    public tresEnRaya(){
        juego = new Juego();
        vista = JuegoVista.instancia(this, juego);
        int dfdfg =0;
    }
    
    public void eventoProducido(Evento evento, Object obj){
        switch(evento){
            case PONER_FICHA:
                juego.ponerFicha("");
                break;
            
            case SALIR:
                System.exit(0);
                break;
        }
    }
    
     public static void main(String[] args) throws IOException{
        new tresEnRaya();
    }

    
}
