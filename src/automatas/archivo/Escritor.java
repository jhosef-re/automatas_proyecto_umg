/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Estrategia para escribir un modelo (AFD o gramática) a un archivo.
 *
 * @param <T> tipo del modelo a escribir ({@link automatas.modelo.AFD} o
 *            {@link automatas.modelo.Gramatica})
 */
public interface Escritor<T> {

    /**
     * Serializa el modelo al archivo indicado, sobrescribiendo si existe.
     *
     * @param modelo  modelo a escribir (no puede ser null)
     * @param archivo ruta destino (no puede ser null)
     * @throws IOException          si no se puede escribir el archivo
     * @throws ValidacionException  si el modelo no es escribible (falta estado
     *                              inicial, producciones inválidas, etc.)
     */
    void escribir(T modelo, Path archivo) throws IOException, ValidacionException;
}