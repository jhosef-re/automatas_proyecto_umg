/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorGramatica;
import automatas.validacion.ValidacionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class LectorGTKTest {

    private static final Path RECURSOS = Paths.get("test/resources");

    @Test
    @DisplayName("Lee la gramática del enunciado y evalúa '0011' como válida")
    void leeGramaticaEnunciado() throws IOException, ValidacionException {
        Gramatica g = new LectorGTK().leer(RECURSOS.resolve("enunciado.gtk"));
        assertEquals("enunciado", g.getNombre());
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("0011").esValida());
        assertTrue(ev.evaluar("").esValida());
    }

    @Test
    @DisplayName("NT inicial = NT de la primera línea")
    void inicialEsPrimero() throws IOException, ValidacionException {
        Gramatica g = new LectorGTK().leer(RECURSOS.resolve("enunciado.gtk"));
        assertEquals("A", g.getInicial());
    }

    @Test
    @DisplayName("epsilon se reconoce como producción vacía")
    void epsilonSeReconoce() throws IOException, ValidacionException {
        Gramatica g = new LectorGTK().leer(RECURSOS.resolve("con_epsilon.gtk"));
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("aaaa").esValida());
    }

    @Test
    @DisplayName("Salta líneas vacías y comentarios")
    void skipComentariosYVacios() throws IOException, ValidacionException {
        Gramatica g = new LectorGTK().leer(RECURSOS.resolve("con_comentarios.gtk"));
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("0011").esValida());
    }

    @Test
    @DisplayName("Archivo vacío → ValidacionException")
    void archivoVacio() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("vacio", ".gtk");
        Files.writeString(tmp, "");
        try {
            assertThrows(ValidacionException.class,
                    () -> new LectorGTK().leer(tmp));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Solo comentarios → ValidacionException")
    void soloComentarios() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("comentarios", ".gtk");
        Files.writeString(tmp, "# solo comentario\n");
        try {
            assertThrows(ValidacionException.class,
                    () -> new LectorGTK().leer(tmp));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Archivo null → IllegalArgumentException")
    void archivoNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new LectorGTK().leer(null));
    }

    @Test
    @DisplayName("Extensión incorrecta → ValidacionException")
    void extensionIncorrecta() {
        Path p = RECURSOS.resolve("enunciado.afd");
        assertThrows(ValidacionException.class,
                () -> new LectorGTK().leer(p));
    }

    @Test
    @DisplayName("Archivo inexistente → IOException")
    void archivoInexistente() {
        Path p = RECURSOS.resolve("no_existe.gtk");
        assertThrows(IOException.class,
                () -> new LectorGTK().leer(p));
    }
}