/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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