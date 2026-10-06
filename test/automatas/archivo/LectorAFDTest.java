/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.servicio.EvaluadorAFD;
import automatas.validacion.ValidacionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class LectorAFDTest {

    private static final Path RECURSOS = Paths.get("test/resources");

    @Test
    @DisplayName("Lee el AFD del enunciado y evalúa 'aababb' como válida")
    void leeAFDEnunciado() throws IOException, ValidacionException {
        AFD afd = new LectorAFD().leer(RECURSOS.resolve("enunciado.afd"));
        assertEquals("enunciado", afd.getNombre());
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertTrue(ev.evaluar("aababb").esValida());
        assertFalse(ev.evaluar("aab").esValida());
    }

    @Test
    @DisplayName("Estado inicial = origen de la primera transición")
    void estadoInicialEsPrimero() throws IOException, ValidacionException {
        AFD afd = new LectorAFD().leer(RECURSOS.resolve("enunciado.afd"));
        assertEquals("A", afd.getEstadoInicial());
    }

    @Test
    @DisplayName("Última definición de aceptación gana")
    void ultimaAceptacionGana() throws IOException, ValidacionException {
        // enunciado.afd define C con false (líneas 5,6) y D con true (línea 6).
        AFD afd = new LectorAFD().leer(RECURSOS.resolve("enunciado.afd"));
        assertTrue(afd.getEstadosAceptacion().contains("D"));
        assertFalse(afd.getEstadosAceptacion().contains("C"));
        assertFalse(afd.getEstadosAceptacion().contains("A"));
    }

    @Test
    @DisplayName("Salta líneas vacías y comentarios")
    void skipComentariosYVacios() throws IOException, ValidacionException {
        AFD afd = new LectorAFD().leer(RECURSOS.resolve("con_comentarios.afd"));
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertTrue(ev.evaluar("aababb").esValida());
    }

    @Test
    @DisplayName("Archivo vacío → ValidacionException")
    void archivoVacio() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("vacio", ".afd");
        Files.writeString(tmp, "");
        try {
            assertThrows(ValidacionException.class,
                    () -> new LectorAFD().leer(tmp));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Solo comentarios → ValidacionException (sin transiciones)")
    void soloComentarios() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("comentarios", ".afd");
        Files.writeString(tmp, "# solo comentario\n\n# otro\n");
        try {
            assertThrows(ValidacionException.class,
                    () -> new LectorAFD().leer(tmp));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Archivo null → IllegalArgumentException")
    void archivoNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new LectorAFD().leer(null));
    }

    @Test
    @DisplayName("Extensión incorrecta → ValidacionException")
    void extensionIncorrecta() {
        Path p = RECURSOS.resolve("enunciado.gtk");   // es .gtk, no .afd
        assertThrows(ValidacionException.class,
                () -> new LectorAFD().leer(p));
    }

    @Test
    @DisplayName("Archivo inexistente → IOException")
    void archivoInexistente() {
        Path p = RECURSOS.resolve("no_existe.afd");
        assertThrows(IOException.class,
                () -> new LectorAFD().leer(p));
    }
}