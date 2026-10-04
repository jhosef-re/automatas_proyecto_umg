/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.validacion;

/** Utilidades de validación compartidas por el modelo. */
public final class ValidacionUtils {

    private ValidacionUtils() { }

    /**
     * Normaliza una cadena: si es {@code null} o queda vacía tras
     * {@code trim()}, lanza {@link ValidacionException}. En otro caso,
     * devuelve el valor sin espacios al inicio ni al final.
     *
     * @param valor  texto a normalizar
     * @param campo  nombre del campo (para el mensaje de error)
     * @return       valor con {@code trim()} aplicado
     * @throws ValidacionException si {@code valor} es null o queda vacío
     */
    public static String normalizar(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El " + campo + " no puede estar vacío.");
        return valor.trim();
    }
}