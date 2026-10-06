/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

/**
 * Resultado de evaluar una cadena contra un AFD o gramática.
 *
 * <p>El campo {@link #detalle} contiene una descripción textual de la
 * evaluación con un prefijo según el modelo evaluado:
 * <ul>
 *   <li><b>AFD:</b> {@code "Ruta en AFD: A, A, a; A, C, b; ..."}. Cada paso
 *       tiene el formato {@code origen, destino, símbolo}; los pasos van
 *       separados por {@code ; }. Si el AFD se bloquea en una transición
 *       inexistente, el paso bloqueado lleva {@code ?, simbolo} como destino.</li>
 *   <li><b>Gramática:</b> {@code "Expansión Gramática: A>0B>00B>..."}.
 *       Cada paso lleva el <i>form</i> resultante de la aplicación de una
 *       producción al primer NT, separados por {@code >}. Cuando la
 *       producción aplicada es {@code ε}, el paso resultante lleva el
 *       sufijo {@code (epsilon)} para indicar que el NT fue consumido.
 *       Ejemplo completo: {@code "Expansión Gramática: A>0B>00B>001A>0011A>0011(epsilon)>0011"}.</li>
 * </ul>
 */
public class ResultadoEvaluacion {

    private final boolean valida;
    private final String detalle;   // "Ruta en AFD: ..." o "Expansión Gramática: ..."

    /**
     * @param valida  {@code true} si la cadena es aceptada por el modelo
     * @param detalle texto descriptivo (ver contrato arriba)
     */
    public ResultadoEvaluacion(boolean valida, String detalle) {
        this.valida = valida;
        this.detalle = detalle;
    }

    /** @return {@code true} si la cadena es válida según el modelo evaluado. */
    public boolean esValida() { return valida; }

    /** @return la descripción textual de la evaluación (ruta o expansión). */
    public String getDetalle() { return detalle; }

    @Override
    public String toString() {
        return detalle + "\nResultado: " + (valida ? "cadena válida" : "cadena inválida");
    }
}