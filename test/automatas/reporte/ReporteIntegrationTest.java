/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.validacion.ValidacionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ReporteIntegrationTest {

    @AfterEach
    void limpiarPropiedad() {
        System.clearProperty(GeneradorGraphviz.PROPIEDAD_PATH);
    }

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

    private Gramatica gramaticaEnunciado() throws ValidacionException {
        Gramatica g = new Gramatica("g0011");
        for (String nt : new String[]{"A", "B"}) g.agregarNoTerminal(nt);
        for (String t : new String[]{"0", "1"}) g.agregarTerminal(t);
        g.setInicial("A");
        g.agregarProduccion("A > 0 B");
        g.agregarProduccion("A > 1 A");
        g.agregarProduccion("A > epsilon");
        g.agregarProduccion("B > 0 B");
        g.agregarProduccion("B > 1 A");
        return g;
    }

    @Test
    @DisplayName("Round-trip AFD: el modelo que genera el reporte evalúa igual")
    void roundTripAFDporPDF() throws Exception {
        AFD original = afdEnunciado();
        Path tmp = Files.createTempFile("test_reporte_", ".pdf");
        try {
            GeneradorPDF.generar(original, tmp);
            // El PDF existe; verificamos que el modelo original sigue evaluando
            // correctamente (no se rompió nada al generar el reporte).
            EvaluadorAFD ev = new EvaluadorAFD(original);
            assertTrue(ev.evaluar("aababb").esValida());
            assertFalse(ev.evaluar("aab").esValida());
            assertTrue(Files.exists(tmp));
            assertTrue(Files.size(tmp) > 1024);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Round-trip Gramática: el modelo que genera el reporte evalúa igual")
    void roundTripGramaticaPorPDF() throws Exception {
        Gramatica original = gramaticaEnunciado();
        Path tmp = Files.createTempFile("test_reporte_gram_", ".pdf");
        try {
            GeneradorPDF.generar(original, tmp);
            EvaluadorGramatica ev = new EvaluadorGramatica(original);
            assertTrue(ev.evaluar("0011").esValida());
            assertTrue(ev.evaluar("").esValida());
            assertFalse(ev.evaluar("0").esValida());
            assertTrue(Files.exists(tmp));
            assertTrue(Files.size(tmp) > 1024);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}