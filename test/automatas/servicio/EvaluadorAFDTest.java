/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorAFDTest {

    private AFD afd;

    @BeforeEach
    void setUp() throws ValidacionException {
        afd = new AFD("ejemplo");
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
    }

    @Test
    @DisplayName("Ejemplo del enunciado: aababb → válida con ruta exacta")
    void casoEnunciado() {
        ResultadoEvaluacion r = new EvaluadorAFD(afd).evaluar("aababb");
        assertTrue(r.esValida());
        assertEquals(
                "Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b",
                r.getDetalle());
    }

    @Test
    @DisplayName("Cadena inválida: cae en estado no aceptor")
    void cadenaInvalidaNoAceptacion() {
        ResultadoEvaluacion r = new EvaluadorAFD(afd).evaluar("aab");
        assertFalse(r.esValida());
        assertTrue(r.getDetalle().contains("Ruta en AFD:"));
    }

    @Test
    @DisplayName("Cadena inválida: no hay transición para el símbolo")
    void cadenaInvalidaBloqueada() {
        // Construimos un AFD sin transición desde A con 'c'
        AFD afd2 = new AFD("min");
        try {
            afd2.agregarEstado("A");
            afd2.agregarEstado("B");
            afd2.agregarSimbolo("a");
            afd2.setEstadoInicial("A");
            afd2.agregarEstadoAceptacion("B");
            afd2.agregarTransicion("A", "B", "a");
        } catch (ValidacionException e) {
            fail("setup falló: " + e.getMessage());
        }
        // 'c' no existe en el alfabeto; al pasar 'c' se bloquea
        ResultadoEvaluacion r = new EvaluadorAFD(afd2).evaluar("c");
        assertFalse(r.esValida());
        assertTrue(r.getDetalle().contains("?"),
                "Debe marcar paso bloqueado con '?'");
    }

    @Test
    @DisplayName("Cadena vacía: válida solo si inicial es de aceptación")
    void cadenaVacia() {
        // AFD del enunciado: inicial A, aceptación D → cadena vacía inválida
        ResultadoEvaluacion r = new EvaluadorAFD(afd).evaluar("");
        assertFalse(r.esValida());
        assertTrue(r.getDetalle().contains("(cadena vacía)"));

        // AFD donde inicial == aceptación
        AFD afd3 = new AFD("vacio");
        try {
            afd3.agregarEstado("A");
            afd3.agregarSimbolo("a");
            afd3.setEstadoInicial("A");
            afd3.agregarEstadoAceptacion("A");
            afd3.agregarTransicion("A", "A", "a");
        } catch (ValidacionException e) {
            fail("setup falló: " + e.getMessage());
        }
        ResultadoEvaluacion r3 = new EvaluadorAFD(afd3).evaluar("");
        assertTrue(r3.esValida());
    }
}