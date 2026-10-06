/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

/**
 * Estrategia de evaluación de cadenas (AFD o gramática).
 *
 * <p>Patrón <b>Strategy</b>: cada implementación ({@link EvaluadorAFD},
 * {@link EvaluadorGramatica}) produce un {@link ResultadoEvaluacion}
 * con el mismo contrato pero con detalles textuales distintos (ruta vs
 * expansión).
 */
public interface Evaluador {

    /**
     * Evalúa una cadena contra el modelo subyacente.
     *
     * @param cadena texto a evaluar (un carácter por símbolo; puede ser
     *               la cadena vacía)
     * @return resultado con la validez y la descripción textual
     *         (ruta para AFD, expansión para gramática)
     * @throws IllegalArgumentException si {@code cadena} es null
     */
    ResultadoEvaluacion evaluar(String cadena);
}