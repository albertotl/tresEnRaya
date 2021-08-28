/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Vista;

import Modelo.Casilla;
import Modelo.Tablero;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

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
    /*
    * Carga la matriz de casillasVista
    */
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
    /*
    * Actualiza el tablero actual por uno nuevo
    */
    public void ponerTablero(Tablero tablero){
        this.tablero = tablero;
        iniciarTableroVista();
    }
    
    /*
    * Devuelve una casillaVista por un codigo de casilla
    */
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
    
    /*
    * Confirma la vista de una casilla en el tablero
    */
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
    
    /*
    * Pinta una casilla segun sus caracteristticas
    */
    public void pintaCasilla(CasillaVista casillaVista, Casilla casilla){
        if(casilla.devuelveTipo() != null){
            casillaVista.confirmar(casilla.devuelveTipo());
        }
    }
    
    /*
    * Pone todas las casillas con sus respectivas caracteristicas
    */
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
