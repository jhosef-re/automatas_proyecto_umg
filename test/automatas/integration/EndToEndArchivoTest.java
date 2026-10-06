/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.integration;

import automatas.archivo.ArchivoFactory;
import automatas.archivo.Lector;
import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.servicio.ResultadoEvaluacion;
import automatas.validacion.ValidacionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración end-to-end del flujo Cargar → Evaluar.
 *
 * <p>Recorre los 5 archivos del enunciado + los 6 archivos de prueba
 * propios de Fase 0, verifica el formato exacto de salida (ruta para AFD,
 * expansión para gramática) y la validez/invalidez de cadenas
 * representativas.
 */
class EndToEndArchivoTest {

    private static final Path RECURSOS = Paths.get("test/resources");

    // ========== AFD: enunciado.afd ==========

    @Test
    @DisplayName("Cargar enunciado.afd + Factory → evaluar 'aababb' produce ruta exacta")
    void cargarEnunciadoAfdYEvaluar() throws Exception {
        AFD afd = cargarAFD("enunciado.afd");
        assertEquals("enunciado", afd.getNombre());
        assertEquals("A", afd.getEstadoInicial());
        assertTrue(afd.esAceptacion("D"));

        ResultadoEvaluacion r = new EvaluadorAFD(afd).evaluar("aababb");
        assertTrue(r.esValida());
        assertEquals(
                "Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b",
                r.getDetalle());
    }

    @Test
    @DisplayName("AFD enunciado: 'aab' es inválida, 'aababb' es válida, '' es inválida")
    void afdEnunciadoVariasCadenas() throws Exception {
        AFD afd = cargarAFD("enunciado.afd");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertFalse(ev.evaluar("aab").esValida());
        assertTrue(ev.evaluar("aababb").esValida());
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("aaaa").esValida());
    }

    // ========== AFD: archivos propios ==========

    @Test
    @DisplayName("un_estado.afd acepta cualquier cadena, incluso la vacía")
    void unEstadoAceptaTodo() throws Exception {
        AFD afd = cargarAFD("un_estado.afd");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("a").esValida());
        assertTrue(ev.evaluar("b").esValida());
        assertTrue(ev.evaluar("ababab").esValida());
    }

    @Test
    @DisplayName("sin_validas.afd rechaza cualquier cadena")
    void sinValidasRechazaTodo() throws Exception {
        AFD afd = cargarAFD("sin_validas.afd");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("a").esValida());
        assertFalse(ev.evaluar("aaaa").esValida());
        assertFalse(ev.evaluar("bbbb").esValida());
    }

    @Test
    @DisplayName("lenguaje_ab.afd acepta solo 'ab'")
    void lenguajeAb() throws Exception {
        AFD afd = cargarAFD("lenguaje_ab.afd");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("a").esValida());
        assertTrue(ev.evaluar("ab").esValida());
        assertFalse(ev.evaluar("ba").esValida());
        assertFalse(ev.evaluar("aba").esValida());
        assertFalse(ev.evaluar("abb").esValida());
    }

    // ========== GTK: enunciado.gtk ==========

    @Test
    @DisplayName("Cargar enunciado.gtk + Factory → evaluar '0011' produce expansión exacta")
    void cargarEnunciadoGtkYEvaluar() throws Exception {
        Gramatica g = cargarGTK("enunciado.gtk");
        assertEquals("enunciado", g.getNombre());
        assertEquals("A", g.getInicial());

        ResultadoEvaluacion r = new EvaluadorGramatica(g).evaluar("0011");
        assertTrue(r.esValida());
        assertEquals(
                "Expansión Gramática: A> 0B> 00B> 001A> 0011A> 0011(epsilon)> 0011",
                r.getDetalle());
    }

    @Test
    @DisplayName("GTK enunciado: epsilon válido, '0011' válido, '0' inválido, 'abc' inválido")
    void gtkEnunciadoVariasCadenas() throws Exception {
        Gramatica g = cargarGTK("enunciado.gtk");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("0011").esValida());
        assertTrue(ev.evaluar("010101").esValida());
        assertFalse(ev.evaluar("0").esValida());
        assertFalse(ev.evaluar("abc").esValida());
    }

    // ========== GTK: archivos propios ==========

    @Test
    @DisplayName("solo_terminales.gtk: S → ab genera exactamente 'ab'")
    void soloTerminales() throws Exception {
        Gramatica g = cargarGTK("solo_terminales.gtk");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("ab").esValida());
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("a").esValida());
        assertFalse(ev.evaluar("abc").esValida());
        assertFalse(ev.evaluar("ba").esValida());
    }

    @Test
    @DisplayName("disyuncion.gtk: S → ab | ac genera {ab, ac}")
    void disyuncion() throws Exception {
        Gramatica g = cargarGTK("disyuncion.gtk");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("ab").esValida());
        assertTrue(ev.evaluar("ac").esValida());
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("a").esValida());
        assertFalse(ev.evaluar("abc").esValida());
        assertFalse(ev.evaluar("ad").esValida());
    }

    @Test
    @DisplayName("multi_terminal.gtk: S → abC, C → c genera exactamente 'abc'")
    void multiTerminal() throws Exception {
        Gramatica g = cargarGTK("multi_terminal.gtk");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("abc").esValida());
        assertFalse(ev.evaluar("").esValida());
        assertFalse(ev.evaluar("ab").esValida());
        assertFalse(ev.evaluar("abcd").esValida());
        assertFalse(ev.evaluar("abcc").esValida());
    }

    // ========== Helpers ==========

    private static AFD cargarAFD(String nombreArchivo) throws IOException, ValidacionException {
        Path ruta = RECURSOS.resolve(nombreArchivo);
        Lector<?> lector = ArchivoFactory.crearLector(ruta);
        AFD afd = (AFD) lector.leer(ruta);
        assertNotNull(afd);
        return afd;
    }

    private static Gramatica cargarGTK(String nombreArchivo) throws IOException, ValidacionException {
        Path ruta = RECURSOS.resolve(nombreArchivo);
        Lector<?> lector = ArchivoFactory.crearLector(ruta);
        Gramatica g = (Gramatica) lector.leer(ruta);
        assertNotNull(g);
        return g;
    }
}