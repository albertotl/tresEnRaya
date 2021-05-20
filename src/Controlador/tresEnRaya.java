
package Controlador;

import Modelo.Juego;
import java.util.Scanner;

/**
 *
 * @author Alberto
 */
public class tresEnRaya {
    
     public static void main(String[] args){
         Scanner scanner = new Scanner(System.in);
         Juego juego = new Juego();
         while(true){
             String codigo = scanner.next();
             juego.ponerFicha(codigo);
             System.out.println(juego.verificarTablero());
             System.out.println(juego.mostrarTablero());
         }
    }
}
