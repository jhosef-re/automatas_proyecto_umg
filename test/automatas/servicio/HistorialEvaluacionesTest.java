/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HistorialEvaluacionesTest {

    @AfterEach
    void limpiarTrasCadaTest() {
        HistorialEvaluaciones.getInstancia().limpiar();
    }

    @Test
    @DisplayName("Singleton: misma instancia siempre")
    void singleton() {
        assertSame(HistorialEvaluaciones.getInstancia(),
                   HistorialEvaluaciones.getInstancia());
    }

    @Test
    @DisplayName("Registrar y recuperar")
    void registrarYRecuperar() {
        HistorialEvaluaciones h = HistorialEvaluaciones.getInstancia();
        h.registrar("afd1", "a", true);
        h.registrar("afd1", "b", false);
        h.registrar("g1", "0011", true);
        assertEquals(3, h.getTodos().size());
        assertEquals("a", h.getTodos().get(0).getCadena());
        assertTrue(h.getTodos().get(0).esValida());
        assertEquals("afd1", h.getTodos().get(0).getNombreModelo());
    }

    @Test
    @DisplayName("Limpiar vacía el historial")
    void limpiar() {
        HistorialEvaluaciones h = HistorialEvaluaciones.getInstancia();
        h.registrar("x", "y", true);
        h.limpiar();
        assertEquals(0, h.getTodos().size());
    }

    @Test
    @DisplayName("Duplicado (mismo modelo + cadena) no se registra dos veces")
    void duplicadosNoSeRegistran() {
        HistorialEvaluaciones h = HistorialEvaluaciones.getInstancia();
        h.registrar("m", "c", true);
        h.registrar("m", "c", false);   // duplicado → debe ignorarse
        assertEquals(1, h.getTodos().size());
        assertTrue(h.getTodos().get(0).esValida());
    }

    @Test
    @DisplayName("registrar(null) lanza IllegalArgumentException")
    void registrarNullInvalido() {
        HistorialEvaluaciones h = HistorialEvaluaciones.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> h.registrar(null, "a", true));
        assertThrows(IllegalArgumentException.class,
                () -> h.registrar("m", null, true));
    }

    @Test
    @DisplayName("getTodos() devuelve vista inmutable")
    void vistaInmutable() {
        HistorialEvaluaciones h = HistorialEvaluaciones.getInstancia();
        h.registrar("m", "c", true);
        assertThrows(UnsupportedOperationException.class,
                () -> h.getTodos().add(new RegistroEvaluacion("x", "y", false)));
    }
}