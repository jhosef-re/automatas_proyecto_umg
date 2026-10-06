/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Estrategia para leer un modelo (AFD o gramática) desde un archivo.
 *
 * @param <T> tipo del modelo leído ({@link automatas.modelo.AFD} o
 *            {@link automatas.modelo.Gramatica})
 */
public interface Lector<T> {

    /**
     * Lee el archivo y construye el modelo correspondiente.
     *
     * @param archivo ruta del archivo a leer (no puede ser null)
     * @return modelo recién creado (sin registrar en el repositorio)
     * @throws IOException          si el archivo no existe / no se puede leer
     * @throws ValidacionException  si el contenido es inválido para el modelo
     */
    T leer(Path archivo) throws IOException, ValidacionException;
}