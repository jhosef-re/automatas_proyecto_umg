/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AFDTest {

    private AFD afd;

    @BeforeEach
    void setUp() {
        afd = new AFD("test");
    }

    @Test
    @DisplayName("agregarEstado: estado duplicado lanza excepción")
    void estadoDuplicado() throws ValidacionException {
        afd.agregarEstado("A");
        assertThrows(ValidacionException.class, () -> afd.agregarEstado("A"),
                "Segunda vez debe lanzar");
    }

    @Test
    @DisplayName("agregarEstado: estado vacío lanza excepción")
    void estadoVacio() {
        assertThrows(ValidacionException.class, () -> afd.agregarEstado(""));
        assertThrows(ValidacionException.class, () -> afd.agregarEstado("   "));
        assertThrows(ValidacionException.class, () -> afd.agregarEstado(null));
    }

    @Test
    @DisplayName("agregarSimbolo: epsilon rechazado en AFD")
    void simboloEpsilon() {
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo("epsilon"));
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo("EPSILON"));
    }

    @Test
    @DisplayName("agregarSimbolo: duplicado lanza excepción")
    void simboloDuplicado() throws ValidacionException {
        afd.agregarSimbolo("a");
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo("a"));
    }

    @Test
    @DisplayName("Estado y símbolo no pueden llamarse igual")
    void estadoIgualSimbolo() throws ValidacionException {
        afd.agregarEstado("A");
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo("A"));
        AFD afd2 = new AFD("test2");
        afd2.agregarSimbolo("a");
        assertThrows(ValidacionException.class, () -> afd2.agregarEstado("a"));
    }

    @Test
    @DisplayName("setEstadoInicial: estado inexistente lanza excepción")
    void inicialInexistente() {
        assertThrows(ValidacionException.class, () -> afd.setEstadoInicial("X"));
    }

    @Test
    @DisplayName("setEstadoInicial: reemplaza al anterior si se reingresa")
    void inicialReemplaza() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.setEstadoInicial("A");
        assertEquals("A", afd.getEstadoInicial());
        afd.setEstadoInicial("B");
        assertEquals("B", afd.getEstadoInicial());
    }

    @Test
    @DisplayName("agregarEstadoAceptacion: estado inexistente lanza excepción")
    void aceptacionInexistente() {
        assertThrows(ValidacionException.class, () -> afd.agregarEstadoAceptacion("X"));
    }

    @Test
    @DisplayName("agregarTransicion: origen/destino/símbolo inexistentes lanzan excepción")
    void transicionInvalida() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("X", "B", "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", "X", "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", "B", "z"));
    }

    @Test
    @DisplayName("agregarTransicion: dos transiciones mismo símbolo desde mismo estado → no determinismo")
    void noDeterminismo() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarEstado("C");
        afd.agregarSimbolo("a");
        afd.agregarTransicion("A", "B", "a");
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", "C", "a"));
    }

    @Test
    @DisplayName("agregarTransicion: epsilon rechazado")
    void transicionEpsilon() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", "B", "epsilon"));
    }

    @Test
    @DisplayName("mover: devuelve destino correcto o null si no hay")
    void mover() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        afd.agregarTransicion("A", "B", "a");
        assertEquals("B", afd.mover("A", "a"));
        assertNull(afd.mover("A", "b"));
        assertNull(afd.mover("Z", "a"));
    }

    @Test
    @DisplayName("esAceptacion: refleja el conjunto")
    void esAceptacion() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("D");
        afd.agregarEstadoAceptacion("D");
        assertFalse(afd.esAceptacion("A"));
        assertTrue(afd.esAceptacion("D"));
    }

    @Test
    @DisplayName("setAceptacion: usado al cargar archivos; última definición gana")
    void setAceptacionToggle() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstadoAceptacion("A");
        assertTrue(afd.esAceptacion("A"));
        afd.setAceptacion("A", false);
        assertFalse(afd.esAceptacion("A"));
        afd.setAceptacion("A", true);
        assertTrue(afd.esAceptacion("A"));
    }

    @Test
    @DisplayName("getNombre: devuelve el nombre asignado")
    void getNombre() {
        assertEquals("test", afd.getNombre());
    }

    // ---- Edge cases (refactor) ----

    @Test
    @DisplayName("agregarSimbolo: null/vacío/blanco lanza excepción")
    void simboloVacio() {
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo(null));
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo(""));
        assertThrows(ValidacionException.class, () -> afd.agregarSimbolo("   "));
    }

    @Test
    @DisplayName("setAceptacion: estado inexistente lanza excepción")
    void setAceptacionInexistente() {
        assertThrows(ValidacionException.class, () -> afd.setAceptacion("X", true));
        assertThrows(ValidacionException.class, () -> afd.setAceptacion("X", false));
    }

    @Test
    @DisplayName("agregarTransicion: argumentos null/vacíos/blanco se rechazan")
    void transicionArgsVacios() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion(null, "B", "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("", "B", "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("   ", "B", "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", null, "a"));
        assertThrows(ValidacionException.class, () -> afd.agregarTransicion("A", "B", null));
    }

    @Test
    @DisplayName("agregarTransicion: argumentos con espacios alrededor se normalizan")
    void transicionTrimNormaliza() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        afd.agregarTransicion(" A ", " B ", " a ");
        assertEquals("B", afd.mover("A", "a"));
    }

    @Test
    @DisplayName("getTransiciones: copia profundamente inmutable")
    void getTransicionesEsInmutable() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        afd.agregarTransicion("A", "B", "a");

        Map<String, Map<String, String>> t = afd.getTransiciones();
        assertThrows(UnsupportedOperationException.class, () -> t.put("X", null));
        Map<String, String> interno = t.get("A");
        assertNotNull(interno);
        assertThrows(UnsupportedOperationException.class, () -> interno.put("x", "y"));
    }

    @Test
    @DisplayName("getEstados/getAlfabeto/getEstadosAceptacion: son inmutables")
    void gettersSimplesInmutables() throws ValidacionException {
        afd.agregarEstado("A");
        afd.agregarSimbolo("a");
        afd.agregarEstadoAceptacion("A");
        assertThrows(UnsupportedOperationException.class, () -> afd.getEstados().add("X"));
        assertThrows(UnsupportedOperationException.class, () -> afd.getAlfabeto().add("x"));
        assertThrows(UnsupportedOperationException.class, () -> afd.getEstadosAceptacion().add("X"));
    }
}