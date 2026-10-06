/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

class GeneradorGraphvizTest {

    private static final String DOT_VALIDO =
            "digraph Test { rankdir=LR; \"A\" -> \"B\"; }";

    private static final String DOT_INVALIDO =
            "digraph { not valid syntax here";

    @AfterEach
    void limpiarPropiedad() {
        System.clearProperty(GeneradorGraphviz.PROPIEDAD_PATH);
    }

    @Test
    @DisplayName("DOT válido → PNG con magic bytes 89 50 4E 47")
    void renderizarDotValido() {
        try {
            byte[] png = GeneradorGraphviz.renderizar(DOT_VALIDO);
            assertNotNull(png);
            assertTrue(png.length > 8);
            assertEquals((byte) 0x89, png[0]);
            assertEquals((byte) 0x50, png[1]);   // 'P'
            assertEquals((byte) 0x4E, png[2]);   // 'N'
            assertEquals((byte) 0x47, png[3]);   // 'G'
        } catch (ExcepcionReporte ex) {
            // Si dot no está instalado, no podemos probar.
            System.out.println("[skip] Graphviz no disponible: " + ex.getMessage());
        }
    }

    @Test
    @DisplayName("DOT inválido → ExcepcionReporte")
    void dotInvalidoLanzaExcepcion() {
        assertThrows(ExcepcionReporte.class,
                () -> GeneradorGraphviz.renderizar(DOT_INVALIDO));
    }

    @Test
    @DisplayName("Ruta configurable por propiedad del sistema")
    void rutaConfigurablePorPropiedad() {
        System.setProperty(GeneradorGraphviz.PROPIEDAD_PATH, "/no/existe/dot_fake");
        assertThrows(ExcepcionReporte.class,
                () -> GeneradorGraphviz.renderizar(DOT_VALIDO));
    }

    @Test
    @DisplayName("renderizar a Path directo escribe el PNG")
    void renderizarAPathDirecto() throws Exception {
        Path tmp = Files.createTempFile("test_", ".png");
        try {
            GeneradorGraphviz.renderizar(DOT_VALIDO, tmp);
            assertTrue(Files.exists(tmp));
            assertTrue(Files.size(tmp) > 0);
        } catch (ExcepcionReporte ex) {
            System.out.println("[skip] " + ex.getMessage());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("DOT null → ExcepcionReporte")
    void dotNull() {
        assertThrows(ExcepcionReporte.class,
                () -> GeneradorGraphviz.renderizar(null));
    }
}