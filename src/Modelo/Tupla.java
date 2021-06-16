/**
* Proyecto Software 2020-2021
* 
* Proyecto: Tres en raya 
* 
* Autor: Alberto Tena Litauszky (770079)
*/
package Modelo;

/**
 *  Tupla genérica de cuatro objetos
 */
public class Tupla<A, B> {
  public final A a;
  public final B b;

  /**
   *  Construye una tupla
   */   
  public Tupla(A a, B b) { 
    this.a = a; 
    this.b = b; 
  }
} 