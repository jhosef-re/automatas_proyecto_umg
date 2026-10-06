/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.validacion.ValidacionException;
import java.nio.file.Path;

/**
 * Fábrica que dispatcha por la extensión del path
 * (patrón <b>Factory</b>): {@code .afd} ↔ {@link LectorAFD}/{@link EscritorAFD},
 * {@code .gtk} ↔ {@link LectorGTK}/{@link EscritorGTK}.
 *
 * <p>Los métodos devuelven tipos comodín ({@code Lector<?>},
 * {@code Escritor<?>}) para forzar al consumidor a hacer un cast explícito
 * al modelo concreto.
 */
public final class ArchivoFactory {

    private ArchivoFactory() {}

    /**
     * @param archivo ruta cuyo archivo a leer (no null)
     * @return lector apropiado según la extensión del path
     * @throws ValidacionException si la extensión no es {@code .afd} ni
     *                             {@code .gtk}, o si la ruta es null
     */
    public static Lector<?> crearLector(Path archivo) throws ValidacionException {
        if (archivo == null)
            throw new ValidacionException("La ruta no puede ser null");
        String nombre = archivo.getFileName().toString().toLowerCase();
        if (nombre.endsWith(".afd")) return new LectorAFD();
        if (nombre.endsWith(".gtk")) return new LectorGTK();
        throw new ValidacionException(
                "Extensión no reconocida: se esperaba .afd o .gtk, fue '"
                + nombre + "'");
    }

    /**
     * @param archivo ruta destino de la escritura (no null)
     * @return escritor apropiado según la extensión del path
     * @throws ValidacionException si la extensión no es {@code .afd} ni
     *                             {@code .gtk}, o si la ruta es null
     */
    public static Escritor<?> crearEscritor(Path archivo) throws ValidacionException {
        if (archivo == null)
            throw new ValidacionException("La ruta no puede ser null");
        String nombre = archivo.getFileName().toString().toLowerCase();
        if (nombre.endsWith(".afd")) return new EscritorAFD();
        if (nombre.endsWith(".gtk")) return new EscritorGTK();
        throw new ValidacionException(
                "Extensión no reconocida: se esperaba .afd o .gtk, fue '"
                + nombre + "'");
    }
}