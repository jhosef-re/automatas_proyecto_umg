/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Invoca el binario externo {@code dot} de Graphviz para convertir
 * código DOT en una imagen PNG.
 *
 * <p>Resolución del ejecutable:
 * <ol>
 *   <li>Propiedad del sistema {@code automatas.graphviz.path}.</li>
 *   <li>Default: {@code "dot"} (búsqueda en {@code PATH}).</li>
 * </ol>
 *
 * <p>Si la ejecución falla por cualquier motivo (no existe, código de
 * salida ≠ 0, DOT inválido) lanza {@link ExcepcionReporte}.
 */
public final class GeneradorGraphviz {

    /** Propiedad del sistema para sobreescribir el ejecutable de Graphviz. */
    public static final String PROPIEDAD_PATH = "automatas.graphviz.path";

    /** Nombre del ejecutable por defecto (búsqueda en PATH). */
    public static final String EJECUTABLE_POR_DEFECTO = "dot";

    private GeneradorGraphviz() {}

    /**
     * Renderiza el DOT y devuelve los bytes PNG resultantes.
     *
     * @param dotFuente código DOT a renderizar
     * @return bytes PNG del grafo
     * @throws ExcepcionReporte si {@code dot} falla o no está disponible
     */
    public static byte[] renderizar(String dotFuente) {
        if (dotFuente == null)
            throw new ExcepcionReporte("El código DOT no puede ser null.");
        Path tmp = null;
        try {
            tmp = Files.createTempFile("automata_", ".png");
            renderizar(dotFuente, tmp);
            return Files.readAllBytes(tmp);
        } catch (IOException ex) {
            throw new ExcepcionReporte(
                    "Error de E/S al generar el grafo: " + ex.getMessage(), ex);
        } finally {
            try {
                if (tmp != null) Files.deleteIfExists(tmp);
            } catch (IOException ignored) {
                // best-effort cleanup
            }
        }
    }

    /**
     * Renderiza el DOT y escribe el PNG directamente en el destino.
     *
     * @param dotFuente código DOT a renderizar
     * @param destinoPng ruta destino del archivo PNG
     * @throws ExcepcionReporte si {@code dot} falla o no está disponible
     */
    public static void renderizar(String dotFuente, Path destinoPng) {
        if (dotFuente == null)
            throw new ExcepcionReporte("El código DOT no puede ser null.");
        String ejecutable = resolverEjecutable();
        Path stdin = null;
        try {
            // Pasamos el DOT por stdin; dot lee desde "-" si se omite el archivo.
            stdin = Files.createTempFile("automata_dot_", ".dot");
            Files.writeString(stdin, dotFuente, StandardCharsets.UTF_8);

            ProcessBuilder pb = new ProcessBuilder(
                    ejecutable, "-Tpng", "-o", destinoPng.toString(), stdin.toString());
            pb = pb.redirectErrorStream(true);
            Process proceso = pb.start();

            // Capturamos stdout/stderr para diagnóstico.
            StringBuilder out = new StringBuilder();
            try (InputStream is = proceso.getInputStream()) {
                byte[] buf = new byte[1024];
                int n;
                while ((n = is.read(buf)) > 0) {
                    out.append(new String(buf, 0, n, StandardCharsets.UTF_8));
                }
            }
            int exit = proceso.waitFor();
            if (exit != 0) {
                throw new ExcepcionReporte(
                        "Graphviz salió con código " + exit + ". Salida: " + out);
            }
            if (!Files.exists(destinoPng) || Files.size(destinoPng) == 0) {
                throw new ExcepcionReporte(
                        "Graphviz no produjo archivo PNG. Salida: " + out);
            }
        } catch (IOException ex) {
            throw new ExcepcionReporte(
                    "No se pudo invocar '" + ejecutable + "': "
                    + ex.getMessage() + ". ¿Está instalado Graphviz y en el PATH?", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ExcepcionReporte("Interrumpido mientras se generaba el grafo.", ex);
        } finally {
            try {
                if (stdin != null) Files.deleteIfExists(stdin);
            } catch (IOException ignored) {
                // best-effort cleanup
            }
        }
    }

    private static String resolverEjecutable() {
        String prop = System.getProperty(PROPIEDAD_PATH);
        if (prop != null && !prop.isBlank()) return prop;
        return EJECUTABLE_POR_DEFECTO;
    }
}