/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GeneradorCadenasTest {

    private AFD afd;
    private EvaluadorAFD evaluador;
    private Set<String> alfabeto;

    /** AFD que acepta (a|b)+ que termina en 'b'. */
    @BeforeEach
    void setUp() throws ValidacionException {
        afd = new AFD("terminaEnB");
        afd.agregarEstado("A");
        afd.agregarEstado("B");
        afd.agregarSimbolo("a");
        afd.agregarSimbolo("b");
        afd.setEstadoInicial("A");
        afd.agregarEstadoAceptacion("B");
        afd.agregarTransicion("A", "B", "b");
        afd.agregarTransicion("A", "A", "a");
        afd.agregarTransicion("B", "A", "a");
        afd.agregarTransicion("B", "B", "b");
        evaluador = new EvaluadorAFD(afd);
        alfabeto = new HashSet<>(Arrays.asList("a", "b"));
    }

    @Test
    @DisplayName("generarValidas: ≥3 cadenas que efectivamente son válidas")
    void generar3Validas() {
        List<String> validas = new GeneradorCadenas(evaluador, alfabeto).generarValidas(3);
        assertTrue(validas.size() >= 3);
        for (String c : validas) {
            assertTrue(evaluador.evaluar(c).esValida(),
                    "La cadena '" + c + "' debería serlo");
        }
        // La primera (longitud 0) no es válida en este AFD.
        // Las válidas a longitud ≤4 incluyen "b", "ab", "bb", "aab", "abb", ...
        assertTrue(validas.contains("b"));
        assertTrue(validas.contains("ab"));
    }

    @Test
    @DisplayName("generarInvalidas: ≥3 cadenas que efectivamente son inválidas")
    void generar3Invalidas() {
        List<String> invalidas = new GeneradorCadenas(evaluador, alfabeto).generarInvalidas(3);
        assertTrue(invalidas.size() >= 3);
        for (String c : invalidas) {
            assertFalse(evaluador.evaluar(c).esValida(),
                    "La cadena '" + c + "' debería no serlo");
        }
        // "a" es inválida, "" también.
        assertTrue(invalidas.contains(""));
        assertTrue(invalidas.contains("a"));
    }

    @Test
    @DisplayName("cantidad 0 devuelve lista vacía")
    void cantidadCero() {
        GeneradorCadenas gen = new GeneradorCadenas(evaluador, alfabeto);
        assertEquals(0, gen.generarValidas(0).size());
        assertEquals(0, gen.generarInvalidas(0).size());
    }

    @Test
    @DisplayName("longitudMaxima 0 → solo evalúa la cadena vacía")
    void longitudMaxCero() {
        GeneradorCadenas gen = new GeneradorCadenas(evaluador, alfabeto);
        List<String> invalidas = gen.generarInvalidas(5, 0);
        assertEquals(1, invalidas.size());
        assertEquals("", invalidas.get(0));
    }

    @Test
    @DisplayName("alfabeto vacío → solo cadena vacía como candidato")
    void alfabetoVacio() {
        GeneradorCadenas gen = new GeneradorCadenas(evaluador, new HashSet<>());
        List<String> invalidas = gen.generarInvalidas(5);
        assertEquals(1, invalidas.size());
        assertEquals("", invalidas.get(0));
    }

    @Test
    @DisplayName("constructor(null) lanza IllegalArgumentException")
    void constructorNullInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new GeneradorCadenas(null, alfabeto));
    }

    @Test
    @DisplayName("Si pide más de las que existen, devuelve las que hay sin duplicar")
    void sinDuplicadosNiRelleno() {
        GeneradorCadenas gen = new GeneradorCadenas(evaluador, alfabeto);
        List<String> validas = gen.generarValidas(999, 2);   // sobre longitud 2 hay pocas válidas
        // Verifica que no haya duplicados
        Set<String> unicas = new HashSet<>(validas);
        assertEquals(validas.size(), unicas.size(), "No debe haber duplicados");
    }
}