/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;

class EvaluadorGramaticaTest {

    /**
     * Gramática que genera (0|1)*: cada paso suma exactamente un terminal.
     * Derivación de "0011":
     *   A→0B, B→0B, B→1A, A→1A, A→ε.
     */
    private Gramatica gramatica0011() throws ValidacionException {
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
    @DisplayName("Ejemplo del enunciado: 0011 → válida con expansión exacta")
    void casoEnunciado0011() throws ValidacionException {
        ResultadoEvaluacion r = new EvaluadorGramatica(gramatica0011()).evaluar("0011");
        assertTrue(r.esValida());
        assertEquals(
                "Expansión Gramática: A> 0B> 00B> 001A> 0011A> 0011(epsilon)> 0011",
                r.getDetalle());
    }

    @Test
    @DisplayName("Cadena inválida: no existe derivación")
    void cadenaInvalidaPorNoDerivacion() throws ValidacionException {
        // (0|1)* acepta cualquier cadena de 0/1, así que usamos una gramática
        // más restrictiva: solo 'a's en cantidad par.
        Gramatica g = new Gramatica("pares");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > a a S");
        g.agregarProduccion("S > epsilon");
        ResultadoEvaluacion r = new EvaluadorGramatica(g).evaluar("aaa");
        assertFalse(r.esValida());
        assertTrue(r.getDetalle().contains("sin derivación"));
    }

    @Test
    @DisplayName("Cadena vacía válida cuando existe S > epsilon")
    void cadenaVaciaValida() throws ValidacionException {
        Gramatica g = new Gramatica("epsilon");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > epsilon");
        g.agregarProduccion("S > a S");
        ResultadoEvaluacion r = new EvaluadorGramatica(g).evaluar("");
        assertTrue(r.esValida());
    }

    @Test
    @DisplayName("Cadena vacía inválida cuando no existe S > epsilon")
    void cadenaVaciaInvalida() throws ValidacionException {
        Gramatica g = new Gramatica("sinEpsilon");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > a S");
        g.agregarProduccion("S > a");
        ResultadoEvaluacion r = new EvaluadorGramatica(g).evaluar("");
        assertFalse(r.esValida());
    }

    @Test
    @DisplayName("evaluar(null) lanza IllegalArgumentException")
    void evaluarCadenaNull() throws ValidacionException {
        assertThrows(IllegalArgumentException.class,
                () -> new EvaluadorGramatica(gramatica0011()).evaluar(null));
    }

    @Test
    @DisplayName("constructor(null) lanza IllegalArgumentException")
    void constructorGramaticaNull() {
        assertThrows(IllegalArgumentException.class, () -> new EvaluadorGramatica(null));
    }

    @Test
    @DisplayName("Gramática sin NT inicial → inválida con mensaje")
    void sinInicial() throws ValidacionException {
        Gramatica g = new Gramatica("x");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        // no se llama setInicial
        g.agregarProduccion("S > a");
        ResultadoEvaluacion r = new EvaluadorGramatica(g).evaluar("a");
        assertFalse(r.esValida());
        assertTrue(r.getDetalle().contains("sin NT inicial"));
    }

    @Test
    @DisplayName("Producción con dos NT (no derecha pura) → ValidacionException al construir")
    void rechazarProduccionNoDerecha() throws ValidacionException {
        Gramatica g = new Gramatica("mala");
        g.agregarNoTerminal("A");
        g.agregarNoTerminal("B");
        g.agregarTerminal("a");
        g.agregarTerminal("b");
        g.setInicial("A");
        // a B b → derecha puro. Mejor ejemplo no válido: A → a B B (dos NT al final)
        // pero la validación rechaza cualquier cosa que no sea 0 o 1 NT al final.
        // Usamos A → a b B B (dos NT consecutivos) que no es derecha pura.
        g.agregarProduccion("A > a b B B");
        assertThrows(ValidacionException.class, () -> new EvaluadorGramatica(g));
    }

    @Test
    @DisplayName("Producción con NT antes de terminal (no derecha pura) → ValidacionException al construir")
    void rechazarProduccionNoDerechaMixta() throws ValidacionException {
        Gramatica g = new Gramatica("mala2");
        g.agregarNoTerminal("A");
        g.agregarNoTerminal("B");
        g.agregarTerminal("a");
        g.setInicial("A");
        g.agregarProduccion("A > B a");   // NT primero, terminal después → no derecha pura
        assertThrows(ValidacionException.class, () -> new EvaluadorGramatica(g));
    }

    @Test
    @DisplayName("Ciclo A > A no cuelga la evaluación (poda por visitados)")
    void cicloNoCuelga() throws ValidacionException {
        Gramatica g = new Gramatica("ciclo");
        g.agregarNoTerminal("A");
        g.agregarTerminal("a");
        g.setInicial("A");
        g.agregarProduccion("A > A");
        g.agregarProduccion("A > a");
        EvaluadorGramatica ev = new EvaluadorGramatica(g);
        ResultadoEvaluacion r = assertDoesNotThrow(() -> ev.evaluar("a"));
        assertTrue(r.esValida());
        assertEquals("Expansión Gramática: A> a", r.getDetalle());
    }
}