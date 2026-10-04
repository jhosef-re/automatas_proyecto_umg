/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GramaticaTest {

    private Gramatica g;

    @BeforeEach
    void setUp() {
        g = new Gramatica("test");
    }

    @Test
    @DisplayName("agregarNoTerminal: duplicado lanza excepción")
    void ntDuplicado() throws ValidacionException {
        g.agregarNoTerminal("A");
        assertThrows(ValidacionException.class, () -> g.agregarNoTerminal("A"));
    }

    @Test
    @DisplayName("agregarTerminal: 'epsilon' está reservado")
    void terminalReservado() {
        assertThrows(ValidacionException.class, () -> g.agregarTerminal("epsilon"));
        assertThrows(ValidacionException.class, () -> g.agregarTerminal("EPSILON"));
    }

    @Test
    @DisplayName("NT y terminal no pueden llamarse igual")
    void ntIgualTerminal() throws ValidacionException {
        g.agregarNoTerminal("A");
        assertThrows(ValidacionException.class, () -> g.agregarTerminal("A"));
        Gramatica g2 = new Gramatica("test2");
        g2.agregarTerminal("a");
        assertThrows(ValidacionException.class, () -> g2.agregarNoTerminal("a"));
    }

    @Test
    @DisplayName("setInicial: NT inexistente lanza excepción")
    void inicialInexistente() {
        assertThrows(ValidacionException.class, () -> g.setInicial("X"));
    }

    @Test
    @DisplayName("setInicial: reemplaza al anterior")
    void inicialReemplaza() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarNoTerminal("B");
        g.setInicial("A");
        assertEquals("A", g.getInicial());
        g.setInicial("B");
        assertEquals("B", g.getInicial());
    }

    @Test
    @DisplayName("agregarProduccion: formato inválido (sin >) lanza excepción")
    void produccionSinSimboloMayor() {
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("AaB"));
    }

    @Test
    @DisplayName("agregarProduccion: NT izquierdo no declarado lanza excepción")
    void produccionNtNoDeclarado() {
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("X > a"));
    }

    @Test
    @DisplayName("agregarProduccion: símbolo no declarado lanza excepción")
    void produccionSimboloNoDeclarado() throws ValidacionException {
        g.agregarNoTerminal("A");
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("A > x"));
    }

    @Test
    @DisplayName("agregarProduccion: alternativa vacía (entre |) lanza excepción")
    void produccionAlternativaVacia() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("A > a | | b"));
    }

    @Test
    @DisplayName("agregarProduccion: producción repetida lanza excepción")
    void produccionRepetida() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        g.agregarProduccion("A > a");
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("A > a"));
    }

    @Test
    @DisplayName("agregarProduccion: disyunción con | produce varias producciones")
    void produccionDisyuncionPipe() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarNoTerminal("B");
        g.agregarTerminal("a");
        g.agregarTerminal("b");
        g.agregarProduccion("A > a B | b");
        List<Produccion> lista = g.getProduccionesDe("A");
        assertEquals(2, lista.size());
        assertEquals("A>a B", lista.get(0).toString());
        assertEquals("A>b", lista.get(1).toString());
    }

    @Test
    @DisplayName("agregarProduccion: epsilon se reconoce como producción vacía")
    void produccionEpsilon() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarProduccion("A > epsilon");
        List<Produccion> lista = g.getProduccionesDe("A");
        assertEquals(1, lista.size());
        assertTrue(lista.get(0).esEpsilon());
    }

    // ---- Edge cases (refactor) ----

    @Test
    @DisplayName("agregarNoTerminal: null/vacío/blanco lanza excepción")
    void ntVacio() {
        assertThrows(ValidacionException.class, () -> g.agregarNoTerminal(null));
        assertThrows(ValidacionException.class, () -> g.agregarNoTerminal(""));
        assertThrows(ValidacionException.class, () -> g.agregarNoTerminal("   "));
    }

    @Test
    @DisplayName("agregarTerminal: null/vacío/blanco lanza excepción")
    void terminalVacio() {
        assertThrows(ValidacionException.class, () -> g.agregarTerminal(null));
        assertThrows(ValidacionException.class, () -> g.agregarTerminal(""));
        assertThrows(ValidacionException.class, () -> g.agregarTerminal("   "));
    }

    @Test
    @DisplayName("setInicial: null/vacío/blanco lanza excepción")
    void inicialVacio() {
        assertThrows(ValidacionException.class, () -> g.setInicial(null));
        assertThrows(ValidacionException.class, () -> g.setInicial(""));
        assertThrows(ValidacionException.class, () -> g.setInicial("   "));
    }

    @Test
    @DisplayName("getProduccionesDe: NT inexistente retorna lista vacía")
    void getProduccionesDeInexistente() throws ValidacionException {
        g.agregarNoTerminal("A");
        assertTrue(g.getProduccionesDe("X").isEmpty());
    }

    @Test
    @DisplayName("getProducciones: copia profundamente inmutable")
    void getProduccionesEsInmutable() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        g.agregarProduccion("A > a");

        var prods = g.getProducciones();
        assertThrows(UnsupportedOperationException.class, () -> prods.put("X", List.of()));
        List<Produccion> listaInterna = prods.get("A");
        assertNotNull(listaInterna);
        assertThrows(UnsupportedOperationException.class, () -> listaInterna.add(null));
        assertThrows(UnsupportedOperationException.class, () -> listaInterna.get(0).getDerecho().add("x"));
    }

    @Test
    @DisplayName("agregarProduccion: múltiples '>' se rechazan")
    void produccionMultiplesFlechas() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("b");
        g.agregarTerminal("c");
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("A > b > c"));
    }

    @Test
    @DisplayName("agregarProduccion: solo '>' sin NT ni producción lanza excepción")
    void produccionSoloFlecha() {
        assertThrows(ValidacionException.class, () -> g.agregarProduccion(">"));
    }

    @Test
    @DisplayName("agregarProduccion: espacios al alrededor se normalizan")
    void produccionTrimNormaliza() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        g.agregarNoTerminal("B");
        g.agregarProduccion("  A  >  a B  ");
        assertEquals(1, g.getProduccionesDe("A").size());
        assertEquals("A>a B", g.getProduccionesDe("A").get(0).toString());
    }

    @Test
    @DisplayName("agregarProduccion: pipe inicial produce alternativa vacía")
    void produccionPipeInicial() throws ValidacionException {
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        assertThrows(ValidacionException.class, () -> g.agregarProduccion("A > | a"));
    }
}