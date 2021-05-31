/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

/**
 *
 * @author Alberto
 */
public class TableroVista extends JPanel {
    private static final int ALTURA_FILA = 50;
    private static final int ANCHURA_COLUMNA = 50;
    private static final int DIMENSION = Modelo.Tablero.DIMENSION;
    
    private CasillaVista[][] casillasVista;
    private JuegoVista vista;
    
    
    private Tablero tablero;
    
    public static final boolean RECIBE_EVENTOS_RATON = true;
    public static final boolean NO_RECIBE_EVENTOS_RATON = false;
    
    TableroVista(JuegoVista vista, Tablero tablero, boolean recibeEventosRaton){
        this.vista = vista;
        this.tablero = tablero;
        
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
            }
            add(casillasVista[fila][columna]);
        }
    }
    
    public void ponerTablero(Tablero tablero){
        this.tablero = tablero;
    }
    
    
    
    
}
