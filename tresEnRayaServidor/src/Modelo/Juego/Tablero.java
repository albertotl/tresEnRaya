/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Alberto
 */
public final class Tablero {

    public final int CASILLA_INICIAL = 1;
    public final int CASILLA_FINAL = 9;
    public final static int DIMENSION = 3;
    private Casilla[][] casillas;

    public Tablero() {
        casillas = new Casilla[DIMENSION][DIMENSION];
        cargarTablero();

    }
    
    public Casilla[][] devuelveCasillas(){
        return casillas;
    }

    /*
    * Devuelve la fila y columna a partir del codigo de casilla
     */
    public int[] devuelveFilaColumna(String codigo) {
        int[] datos = new int[2];
        if (Integer.valueOf(codigo) <= 9 && Integer.valueOf(codigo) >= 1) {
            for (int i = 0; i < DIMENSION; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    if (casillas[i][j].devuelveCodigo().equals(codigo)) {
                        datos[0] = i;
                        datos[1] = j;
                    }
                }
            }
            return datos;
        }
        return null;
    }

    /*
    * Llena el tablero con 9 casillas con su respectivo codigo (1-9)
     */
    public void cargarTablero() {
        int cod = 1;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                casillas[i][j] = new Casilla(String.valueOf(cod));
                cod++;
            }
        }
    }

    /*
    * Devuelve la casilla a partir del codigo de casilla
     */
    public Casilla devolverCasilla(String codigo) {
        int[] datos = devuelveFilaColumna(codigo);
        if (datos != null) {
            return casillas[datos[0]][datos[1]];
        }
        return null;
    }

    /*
    * Devuelve el tipo de casilla a partir del codigo de la casilla 
    * (Para poner una ficha en una casilla)
     */
    public String devolverTipoCasilla(String codigo) {
        int[] datos = devuelveFilaColumna(codigo);
        if (datos != null) {
            return casillas[datos[0]][datos[1]].devuelveTipo();
        }
        return null;
    }

    /*
    * Devuelve el tipo de casilla a partir de la fila y la columna
    * (Para comprobar la partida y para mostrar en pantalla)
    * No hace falta comprobar las filas y columnas debido a que siempre son validas
     */
    public String devolverTipoCasilla(int fila, int columna) {
        return casillas[fila][columna].devuelveTipo();
    }


    /*
    * Devuelve verdadero en caso de que el tablero este completo
    */
    public boolean completo() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (casillas[i][j].devuelveTipo() == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void vaciar(){
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                casillas[i][j].vaciar();
            }
        }
    }
}
