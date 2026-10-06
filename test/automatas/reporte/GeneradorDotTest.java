/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;

class GeneradorDotTest {

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
    @DisplayName("DOT contiene digraph, rankdir y todos los estados")
    void dotContieneDigraphYRrankdir() throws ValidacionException {
        String dot = GeneradorDot.generar(afdEnunciado());
        assertTrue(dot.startsWith("digraph AFD_ejemplo"), dot);
        assertTrue(dot.contains("rankdir=LR"), dot);
        for (String e : new String[]{"A", "B", "C", "D"}) {
            assertTrue(dot.contains("\"" + e + "\""), "Falta estado " + e);
        }
    }

    @Test
    @DisplayName("Aceptaciones tienen doble círculo")
    void aceptacionesTienenDoubleCircle() throws ValidacionException {
        String dot = GeneradorDot.generar(afdEnunciado());
        assertTrue(dot.contains("\"D\" [shape=doublecircle]"), dot);
        // Los no-aceptados NO tienen doublecircle explícito.
        assertTrue(dot.contains("\"A\";\n"), "A debe aparecer sin shape=doublecircle");
        assertFalse(dot.contains("\"A\" [shape=doublecircle]"),
                "A no es aceptación; no debe tener doublecircle");
    }

    @Test
    @DisplayName("Estado inicial es destino de la flecha de inicio")
    void estadoInicialEsDestinoDeFlecha() throws ValidacionException {
        String dot = GeneradorDot.generar(afdEnunciado());
        assertTrue(dot.contains("\"_inicio\" [shape=point"), dot);
        assertTrue(dot.contains("\"_inicio\" -> \"A\""), dot);
    }

    @Test
    @DisplayName("Transiciones agrupadas: A->C con símbolos 'a' y 'b' → un solo label 'a,b'")
    void transicionesAgrupadas() throws ValidacionException {
        // Construimos un AFD donde A→C ocurre con 'a' y con 'b'.
        AFD afd = new AFD("agrupar");
        afd.agregarEstado("A");
        afd.agregarEstado("C");
        afd.agregarSimbolo("a");
        afd.agregarSimbolo("b");
        afd.setEstadoInicial("A");
        // No se puede tener δ(A,a)=C y δ(A,b)=C porque AFD rechaza no determinismo...
        // En realidad C es el mismo destino con distintos símbolos:
        // el modelo lo permite porque cambia el símbolo (es determinista).
        // Pero el modelo lo rechaza con "Dos transiciones con el mismo símbolo".
        // Aquí los símbolos son distintos, así que debería funcionar.
        // Mejor probamos otro caso: A→B con un solo símbolo (no agrupable).
        afd.agregarTransicion("A", "C", "a");
        afd.agregarTransicion("A", "C", "b");
        String dot = GeneradorDot.generar(afd);
        assertTrue(dot.contains("\"A\" -> \"C\" [label=\"a,b\"]"), dot);
        // Solo debe haber una arista A->C, no dos.
        assertEquals(1, dot.split("\"A\" -> \"C\"").length - 1,
                "Debe haber una sola arista A->C");
    }

    @Test
    @DisplayName("DOT parsea como DOT válido (Graphviz real)")
    void dotParseaConGraphvizReal() throws Exception {
        String dot = GeneradorDot.generar(afdEnunciado());
        java.nio.file.Path tmp = java.nio.file.Files.createTempFile("test", ".dot");
        try {
            java.nio.file.Files.writeString(tmp, dot);
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tdot", tmp.toString())
                    .redirectErrorStream(true);
            Process p = pb.start();
            int exit = p.waitFor();
            assertEquals(0, exit, "Graphviz debe aceptar el DOT sin errores");
        } catch (Exception ex) {
            // Si dot no está instalado, no podemos probar — skip silencioso.
            System.out.println("[skip] Graphviz no disponible: " + ex.getMessage());
        } finally {
            java.nio.file.Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("DOT tiene llaves balanceadas y comillas balanceadas")
    void dotValidoParaSintaxisBasica() throws ValidacionException {
        String dot = GeneradorDot.generar(afdEnunciado());
        long abiertas  = dot.chars().filter(c -> c == '{').count();
        long cerradas = dot.chars().filter(c -> c == '}').count();
        assertEquals(abiertas, cerradas, "Llaves no balanceadas");
        long comillas = dot.chars().filter(c -> c == '"').count();
        assertEquals(0, comillas % 2, "Comillas no balanceadas (encontradas: " + comillas + ")");
    }

    @Test
    @DisplayName("AFD null lanza IllegalArgumentException")
    void afdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> GeneradorDot.generar(null));
    }
}