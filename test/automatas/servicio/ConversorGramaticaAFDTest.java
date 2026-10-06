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

class ConversorGramaticaAFDTest {

    /** Gramática (0|1)*: A→0B, A→1A, A→ε, B→0B, B→1A. */
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
    @DisplayName("Convierte gramática (0|1)* y evalúa cadenas según terminación en A")
    void convierteGramaticaEnunciadoYevalua() throws ValidacionException {
        AFD afd = new ConversorGramaticaAFD(gramaticaEnunciado()).convertir("afd_0011");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        // Solo A es de aceptación (por A>epsilon). Las válidas terminan en A o son vacías.
        assertTrue(ev.evaluar("0011").esValida());
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("1").esValida());
        assertTrue(ev.evaluar("111").esValida());
        assertTrue(ev.evaluar("010101").esValida());   // alterna y termina en A
        // Inválidas: quedan en B (no aceptación).
        assertFalse(ev.evaluar("0").esValida());
        assertFalse(ev.evaluar("00").esValida());
        assertFalse(ev.evaluar("10").esValida());
        assertFalse(ev.evaluar("1010").esValida());
        // "2" no está en alfabeto → inválida.
        assertFalse(ev.evaluar("2").esValida());
    }

    @Test
    @DisplayName("Producción epsilon → estado inicial y de aceptación")
    void produccionEpsilon() throws ValidacionException {
        Gramatica g = new Gramatica("epsilon");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > epsilon");
        g.agregarProduccion("S > a S");
        AFD afd = new ConversorGramaticaAFD(g).convertir("afd_e");
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertTrue(ev.evaluar("").esValida());
        assertTrue(ev.evaluar("aaa").esValida());
    }

    @Test
    @DisplayName("Producción terminal-only crea estado final 'F'")
    void produccionTerminalCreaF() throws ValidacionException {
        Gramatica g = new Gramatica("term");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > a");
        AFD afd = new ConversorGramaticaAFD(g).convertir("afd_term");
        assertTrue(afd.getEstados().contains(ConversorGramaticaAFD.NOMBRE_ESTADO_FINAL));
        assertTrue(afd.getEstadosAceptacion().contains(ConversorGramaticaAFD.NOMBRE_ESTADO_FINAL));
        assertEquals("F", afd.mover("S", "a"));
        EvaluadorAFD ev = new EvaluadorAFD(afd);
        assertTrue(ev.evaluar("a").esValida());
        assertFalse(ev.evaluar("").esValida());
    }

    @Test
    @DisplayName("Múltiples producciones epsilon → múltiples estados de aceptación")
    void multiplesProduccionesEpsilon() throws ValidacionException {
        Gramatica g = new Gramatica("multi");
        for (String nt : new String[]{"S", "T"}) g.agregarNoTerminal(nt);
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > epsilon");
        g.agregarProduccion("T > epsilon");
        g.agregarProduccion("S > a T");
        AFD afd = new ConversorGramaticaAFD(g).convertir("afd_multi");
        assertTrue(afd.getEstadosAceptacion().contains("S"));
        assertTrue(afd.getEstadosAceptacion().contains("T"));
    }

    @Test
    @DisplayName("Estado F ya declarado como NT → se usa 'F#0'")
    void estadoFEnConflicto() throws ValidacionException {
        Gramatica g = new Gramatica("conflicto");
        for (String nt : new String[]{"S", "F"}) g.agregarNoTerminal(nt);
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > a");
        g.agregarProduccion("F > epsilon");
        AFD afd = new ConversorGramaticaAFD(g).convertir("afd_c");
        assertTrue(afd.getEstados().contains("F"));
        assertTrue(afd.getEstados().contains("F#0"));
        // F#0 debe ser de aceptación.
        assertTrue(afd.getEstadosAceptacion().contains("F#0"));
        // 'a' desde S debe ir a F#0 (no a F).
        assertEquals("F#0", afd.mover("S", "a"));
    }

    @Test
    @DisplayName("No determinismo → ValidacionException")
    void noDeterminismo() throws ValidacionException {
        Gramatica g = new Gramatica("nd");
        for (String nt : new String[]{"A", "B", "C"}) g.agregarNoTerminal(nt);
        g.agregarTerminal("a");
        g.setInicial("A");
        g.agregarProduccion("A > a B");
        g.agregarProduccion("A > a C");
        assertThrows(ValidacionException.class,
                () -> new ConversorGramaticaAFD(g).convertir("afd_nd"));
    }

    @Test
    @DisplayName("Producción con múltiples terminales antes del NT → ValidacionException")
    void multiTerminalEnProduccion() throws ValidacionException {
        Gramatica g = new Gramatica("multi");
        for (String nt : new String[]{"A", "B"}) g.agregarNoTerminal(nt);
        g.agregarTerminal("a");
        g.agregarTerminal("b");
        g.setInicial("A");
        g.agregarProduccion("A > a b B");
        assertThrows(ValidacionException.class,
                () -> new ConversorGramaticaAFD(g).convertir("afd_m"));
    }

    @Test
    @DisplayName("Sin NT inicial → ValidacionException")
    void sinInicial() throws ValidacionException {
        Gramatica g = new Gramatica("sin");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        // no se llama setInicial
        g.agregarProduccion("S > a");
        assertThrows(ValidacionException.class,
                () -> new ConversorGramaticaAFD(g).convertir("afd_s"));
    }

    @Test
    @DisplayName("constructor(null) lanza IllegalArgumentException")
    void gramaticaNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConversorGramaticaAFD(null));
    }
}