/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Serializa un AFD al formato del enunciado:
 * <pre>
 * A,B,simbolo;acept_origen,acept_destino
 * </pre>
 * Una línea por transición, en orden estable (origen → símbolo). El estado
 * inicial y los de aceptación se reconstruyen al releer el archivo
 * (última definición gana). Crea los directorios padre si no existen.
 */
public class EscritorAFD implements Escritor<AFD> {

    @Override
    public void escribir(AFD afd, Path archivo) throws IOException, ValidacionException {
        if (afd == null)
            throw new IllegalArgumentException("El AFD no puede ser null");
        if (archivo == null)
            throw new IllegalArgumentException("La ruta no puede ser null");
        if (!archivo.toString().toLowerCase().endsWith(".afd"))
            throw new ValidacionException("El archivo debe tener extensión .afd");
        if (afd.getEstadoInicial() == null)
            throw new ValidacionException("El AFD no tiene estado inicial.");

        Path padre = archivo.getParent();
        if (padre != null) Files.createDirectories(padre);

        List<String> lineas = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> eOrigen : afd.getTransiciones().entrySet()) {
            String origen = eOrigen.getKey();
            for (Map.Entry<String, String> eSimbolo : eOrigen.getValue().entrySet()) {
                String simbolo = eSimbolo.getKey();
                String destino = eSimbolo.getValue();
                lineas.add(String.format("%s,%s,%s;%s,%s",
                        origen, destino, simbolo,
                        afd.esAceptacion(origen),
                        afd.esAceptacion(destino)));
            }
        }
        Files.write(archivo, lineas);
    }
}