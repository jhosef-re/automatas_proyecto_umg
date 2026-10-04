/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

/** Estrategia de evaluación de cadenas (AFD o gramática). */
public interface Evaluador {
    ResultadoEvaluacion evaluar(String cadena);
}