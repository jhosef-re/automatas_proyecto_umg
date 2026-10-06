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

class EscritorAFDTest {

    private AFD afdEnunciado() throws ValidacionException {
        AFD afd = new AFD("ejemplo");
        for (String s : new String[]{"A", "B", "C", "D"}) afd.agregarEstado(s);
        afd.agregarSimbolo("a");
        afd.agregarSimbolo("b");
        afd.setEstadoInicial("A");
        afd.agregarEstadoAceptacion("D");
        afd.agregarTransicion("A", "A", "a");
        afd.agregarTransicion("A", "C", "b");
        afd.agregarTransicion("B", "A", "a");
        afd.agregarTransicion("B", "C", "b");
        afd.agregarTransicion("C", "B", "a");
        afd.agregarTransicion("C", "D", "b");
        return afd;
    }

    @Test
    @DisplayName("Ida-vuelta: escribir → leer → evaluar produce el mismo resultado")
    void escribeYLecturaRedonda() throws IOException, ValidacionException {
        AFD original = afdEnunciado();
        Path tmp = Files.createTempFile("afd_round", ".afd");
        try {
            new EscritorAFD().escribir(original, tmp);
            AFD leido = new LectorAFD().leer(tmp);
            EvaluadorAFD evOrig = new EvaluadorAFD(original);
            EvaluadorAFD evLeido = new EvaluadorAFD(leido);
            assertEquals(evOrig.evaluar("aababb").esValida(),
                         evLeido.evaluar("aababb").esValida());
            assertEquals(evOrig.evaluar("aab").esValida(),
                         evLeido.evaluar("aab").esValida());
            assertEquals(evOrig.evaluar("").esValida(),
                         evLeido.evaluar("").esValida());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Preserva múltiples estados de aceptación")
    void preservaAceptacion() throws IOException, ValidacionException {
        AFD afd = new AFD("multi");
        for (String s : new String[]{"S", "F"}) afd.agregarEstado(s);
        afd.agregarSimbolo("a");
        afd.setEstadoInicial("S");
        afd.agregarEstadoAceptacion("S");
        afd.agregarEstadoAceptacion("F");
        afd.agregarTransicion("S", "F", "a");

        Path tmp = Files.createTempFile("multi", ".afd");
        try {
            new EscritorAFD().escribir(afd, tmp);
            AFD leido = new LectorAFD().leer(tmp);
            assertTrue(leido.getEstadosAceptacion().contains("S"));
            assertTrue(leido.getEstadosAceptacion().contains("F"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Preserva el estado inicial")
    void preservaInicial() throws IOException, ValidacionException {
        AFD afd = afdEnunciado();
        Path tmp = Files.createTempFile("inicial", ".afd");
        try {
            new EscritorAFD().escribir(afd, tmp);
            AFD leido = new LectorAFD().leer(tmp);
            assertEquals("A", leido.getEstadoInicial());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("AFD null → IllegalArgumentException")
    void escribeNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new EscritorAFD().escribir(null, null));
    }

    @Test
    @DisplayName("Extensión incorrecta → ValidacionException")
    void extensionIncorrecta() throws ValidacionException {
        AFD afd = afdEnunciado();
        Path tmp = Path.of("/tmp/archivo_equivocado.txt");
        assertThrows(ValidacionException.class,
                () -> new EscritorAFD().escribir(afd, tmp));
    }
}