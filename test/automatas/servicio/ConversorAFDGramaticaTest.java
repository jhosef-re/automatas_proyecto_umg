/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;

class ConversorAFDGramaticaTest {

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
    @DisplayName("Convierte AFD del enunciado y evalúa 'aababb' como válida")
    void convierteAFDEnunciadoYevalua() throws ValidacionException {
        Gramatica g = new ConversorAFDGramatica(afdEnunciado()).convertir("g_afd");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("aababb").esValida());
        assertFalse(ev.evaluar("aab").esValida());
    }

    @Test
    @DisplayName("AFD con un solo estado (inicial = aceptación) → gramática {S > epsilon}")
    void afdUnSoloEstado() throws ValidacionException {
        AFD afd = new AFD("vacio");
        afd.agregarEstado("S");
        afd.agregarSimbolo("a");
        afd.setEstadoInicial("S");
        afd.agregarEstadoAceptacion("S");
        afd.agregarTransicion("S", "S", "a");
        Gramatica g = new ConversorAFDGramatica(afd).convertir("g_vacio");
        assertEquals(1, g.getNoTerminales().size());
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("aaa").esValida());
    }

    @Test
    @DisplayName("AFD con varios estados de aceptación → una producción epsilon por cada uno")
    void variosEstadosAceptacion() throws ValidacionException {
        AFD afd = new AFD("multi");
        for (String s : new String[]{"A", "B"}) afd.agregarEstado(s);
        afd.agregarSimbolo("a");
        afd.setEstadoInicial("A");
        afd.agregarEstadoAceptacion("A");
        afd.agregarEstadoAceptacion("B");
        afd.agregarTransicion("A", "B", "a");
        Gramatica g = new ConversorAFDGramatica(afd).convertir("g_multi");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("a").esValida());
    }

    @Test
    @DisplayName("constructor(null) lanza IllegalArgumentException")
    void afdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConversorAFDGramatica(null));
    }

    @Test
    @DisplayName("AFD sin estado inicial → ValidacionException")
    void afdSinInicial() throws ValidacionException {
        AFD afd = new AFD("sinIni");
        afd.agregarEstado("A");
        afd.agregarSimbolo("a");
        assertThrows(ValidacionException.class,
                () -> new ConversorAFDGramatica(afd).convertir("g_x"));
    }

    @Test
    @DisplayName("AFD sin estados → ValidacionException")
    void afdVacio() {
        AFD afd = new AFD("vacio");
        assertThrows(ValidacionException.class,
                () -> new ConversorAFDGramatica(afd).convertir("g_x"));
    }
}