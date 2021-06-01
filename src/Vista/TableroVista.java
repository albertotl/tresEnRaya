/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Casilla;
import Modelo.Tablero;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author Alberto
 */
public class TableroVista extends JPanel {
    private static final int ALTURA_FILA = 85;
    private static final int ANCHURA_COLUMNA = 85;
    private static final int DIMENSION = Modelo.Tablero.DIMENSION;
    
    private CasillaVista[][] casillasVista;
    private JuegoVista vista;
    
    private Tablero tablero;
    
    public static final boolean RECIBE_EVENTOS_RATON = true;
    public static final boolean NO_RECIBE_EVENTOS_RATON = false;
    
    TableroVista(JuegoVista vista, boolean recibeEventosRaton){
        this.vista = vista;
        casillasVista = new CasillaVista[DIMENSION][DIMENSION];
        crearCasillas(recibeEventosRaton);
        this.setPreferredSize(new Dimension(DIMENSION * ANCHURA_COLUMNA,
                DIMENSION * ALTURA_FILA));
        
    }
    
    private void crearCasillas(boolean recibeEventosRaton){
        setLayout(new GridLayout(DIMENSION, DIMENSION));
        casillasVista = new CasillaVista[DIMENSION][DIMENSION];
        
        for(int fila = 0; fila < DIMENSION; fila++){
            for(int columna = 0; columna < DIMENSION; columna++){
                casillasVista[fila][columna] = new CasillaVista(vista, recibeEventosRaton);
                add(casillasVista[fila][columna]);
            }
            
        }
    }
    
    public void ponerTablero(Tablero tablero){
        this.tablero = tablero;
    }
    
    private CasillaVista buscarCasillaVista(String codigo){
        for (int fila = 0; fila < DIMENSION; fila++) {
            for (int columna = 0; columna < DIMENSION; columna++) {

                String codigoCasilla = casillasVista[fila][columna].
                        obtenerCodigo();
                if(codigoCasilla != null){
                   if (codigoCasilla.equals(codigo)) {
                        return casillasVista[fila][columna];
                    } 
                }
            }
        }
        return null;
    }
    
    public void confirmarCasilla(String codigo, String tipo){
        CasillaVista casillaVista = buscarCasillaVista(codigo);
        
        if(casillaVista != null){
            casillaVista.confirmar(tipo);
        }
    }
    
    /**
     * Inicia vista del tablero
     */
    private void iniciarTableroVista() {
        for (int fila = 0; fila < DIMENSION; fila++) {
            for (int columna = 0; columna < DIMENSION; columna++) {
                casillasVista[columna][fila].iniciar();
            }
        }
    } 
    
    public void pintaCasilla(CasillaVista casillaVista, Casilla casilla){
        if(casilla.devuelveFicha != null){
            casillaVista.confirmar(casilla.devuelveTipo());
        }
    }
    
    public void ponerCasillas(){
        iniciarTableroVista();
        Casilla[][] casillas = tablero.devuelveCasillas();
        for (int fila = 0; fila < DIMENSION; fila++) {
            for (int columna = 0; columna < DIMENSION; columna++) {
                CasillaVista casillaVista = casillasVista[fila][columna];
                casillaVista.ponerCodigo(casillas[fila][columna].devuelveCodigo());
                pintaCasilla(casillaVista, casillas[fila][columna]);
            }
        }
    }
    
}
