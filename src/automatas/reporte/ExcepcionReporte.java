/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

/**
 * Excepción unchecked para errores durante la generación de reportes
 * (Graphviz no disponible, falló la ejecución externa, etc.).
 * Permite que la UI degrade con elegancia (PDF sin imagen) sin propagar
 * una checked exception.
 */
public class ExcepcionReporte extends RuntimeException {

    public ExcepcionReporte(String mensaje) {
        super(mensaje);
    }

    public ExcepcionReporte(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}