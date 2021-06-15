
package Control;


import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public enum PrimitivaComunicacion {
  CONECTAR_PUSH("connect"), 
  DESCONECTAR_PUSH("disconnect"), 
  NUEVO_ID_CONEXION("new_id_conection"),
  TEST("test"),
  PONER_FICHA("put_file"),
  INICIAR_SESION("log_in"),
  REGISTRARSE("register_on"),
  BUSCAR_PARTIDA("search_game"),
  ACABAR_PARTIDA("finish_game"),
  CERRAR_SESION("log_out"),
  OBTENER_TABLERO("get_board"),
  PARTIDA_ENCONTRADA("find_game"),
  PEDIR_HISTORIAL("get_historial"),
  FIN("end"),
  OK("ok"),
  NOK("nok");
  
  private String simbolo;
  private static final Pattern expresionRegular =      
           Pattern.compile(CONECTAR_PUSH.toString() + "|" +
                           DESCONECTAR_PUSH.toString() + "|" +
                           NUEVO_ID_CONEXION + "|" +
                           PONER_FICHA.toString() + "|"+
                           BUSCAR_PARTIDA.toString() + "|"+
                           ACABAR_PARTIDA.toString() + "|" +
                           REGISTRARSE.toString() + "|"+
                           INICIAR_SESION.toString() + "|"+
                           OBTENER_TABLERO.toString() + "|"+
                           TEST.toString() + "|"+
                           CERRAR_SESION.toString() + "|"+
                           PARTIDA_ENCONTRADA.toString() + "|" +
                           PEDIR_HISTORIAL.toString() + "|" +
                           FIN.toString() + "|" +               
                           OK.toString() + "|" +
                           NOK.toString());
  
  /*
   * Construye una primitiva
  */
  PrimitivaComunicacion(String simbolo){
      this.simbolo = simbolo;
  }
  
  /*
  * Devuelve una nueva primitiva leída de un scanner
  */
  public static PrimitivaComunicacion nueva(Scanner scanner) 
          throws InputMismatchException {
    String token = scanner.next(expresionRegular);
      
    if(token.equals(CONECTAR_PUSH.toString())){
       return CONECTAR_PUSH;
    }
    else if (token.equals(DESCONECTAR_PUSH.toString())) {
      return DESCONECTAR_PUSH; 
    }
    else if (token.equals(TEST.toString())) {
      return TEST; 
    } 
    else if (token.equals(NUEVO_ID_CONEXION.toString())) {
      return NUEVO_ID_CONEXION; 
    }
    else if (token.equals(PONER_FICHA.toString())) {
      return PONER_FICHA;    
    }
    else if (token.equals(BUSCAR_PARTIDA.toString())) {
      return BUSCAR_PARTIDA;   
    } 
    else if (token.equals(INICIAR_SESION.toString())) {
      return INICIAR_SESION;    
    }
    else if (token.equals(REGISTRARSE.toString())) {
      return REGISTRARSE;    
    }
    else if (token.equals(OBTENER_TABLERO.toString())) {
      return OBTENER_TABLERO;    
    }
    else if (token.equals(ACABAR_PARTIDA.toString())) {
      return ACABAR_PARTIDA;    
    }
    else if (token.equals(PARTIDA_ENCONTRADA.toString())) {
      return PARTIDA_ENCONTRADA;    
    }
    else if (token.equals(PEDIR_HISTORIAL.toString())) {
      return PEDIR_HISTORIAL;    
    }
    else if (token.equals(CERRAR_SESION.toString())) {
      return CERRAR_SESION;    
    }
    else if (token.equals(FIN.toString())) {
      return FIN;    
    } 
    else if (token.equals(OK.toString())) {
      return OK;    
    } 
    else {
      return NOK;    
    }
  }
      
  @Override
   public String toString(){
       return simbolo;
   }
}