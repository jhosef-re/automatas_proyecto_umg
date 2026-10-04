/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ProduccionTest {

    @Test
    @DisplayName("toString: producción normal une símbolos con espacio")
    void toStringNormal() {
        Produccion p = new Produccion("A", Arrays.asList("a", "B"));
        assertEquals("A>a B", p.toString());
    }

    @Test
    @DisplayName("toString: producción vacía muestra 'epsilon'")
    void toStringEpsilon() {
        Produccion p = new Produccion("A", Arrays.asList());
        assertEquals("A>epsilon", p.toString());
    }

    @Test
    @DisplayName("equals: dos producciones con mismo NT y derecho son iguales")
    void equalsIguales() {
        Produccion p1 = new Produccion("A", Arrays.asList("a", "B"));
        Produccion p2 = new Produccion("A", Arrays.asList("a", "B"));
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("equals: NT distinto o derecho distinto NO son iguales")
    void equalsDistintos() {
        Produccion p1 = new Produccion("A", Arrays.asList("a", "B"));
        Produccion p2 = new Produccion("B", Arrays.asList("a", "B"));
        Produccion p3 = new Produccion("A", Arrays.asList("a"));
        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}