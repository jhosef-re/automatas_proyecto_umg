/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

/** Resultado de evaluar una cadena: validez + ruta/expansión como texto. */
public class ResultadoEvaluacion {

    private final boolean valida;
    private final String detalle;   // "Ruta en AFD: ..." o "Expansión Gramática: ..."

    public ResultadoEvaluacion(boolean valida, String detalle) {
        this.valida = valida;
        this.detalle = detalle;
    }

    public boolean esValida() { return valida; }
    public String getDetalle() { return detalle; }

    @Override
    public String toString() {
        return detalle + "\nResultado: " + (valida ? "cadena válida" : "cadena inválida");
    }
}