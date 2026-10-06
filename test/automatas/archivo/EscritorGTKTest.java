/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorGramatica;
import automatas.validacion.ValidacionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class EscritorGTKTest {

    private Gramatica gramaticaEnunciado() throws ValidacionException {
        Gramatica g = new Gramatica("g");
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

    private Gramatica gramaticaConEpsilon() throws ValidacionException {
        Gramatica g = new Gramatica("eps");
        g.agregarNoTerminal("S");
        g.agregarTerminal("a");
        g.setInicial("S");
        g.agregarProduccion("S > epsilon");
        g.agregarProduccion("S > a S");
        return g;
    }

    @Test
    @DisplayName("Ida-vuelta: escribir → leer → evaluar produce el mismo resultado")
    void escribeYLecturaRedonda() throws IOException, ValidacionException {
        Gramatica original = gramaticaEnunciado();
        Path tmp = Files.createTempFile("gram_round", ".gtk");
        try {
            new EscritorGTK().escribir(original, tmp);
            Gramatica leida = new LectorGTK().leer(tmp);
            EvaluadorGramatica evOrig = new EvaluadorGramatica(original);
            EvaluadorGramatica evLeido = new EvaluadorGramatica(leida);
            assertTrue(evOrig.evaluar("0011").esValida());
            assertTrue(evLeido.evaluar("0011").esValida());
            assertEquals(evOrig.evaluar("0011").esValida(),
                         evLeido.evaluar("0011").esValida());
            assertEquals(evOrig.evaluar("0").esValida(),
                         evLeido.evaluar("0").esValida());
            assertEquals(evOrig.evaluar("").esValida(),
                         evLeido.evaluar("").esValida());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Preserva producciones epsilon")
    void preservaEpsilon() throws IOException, ValidacionException {
        Gramatica g = gramaticaConEpsilon();
        Path tmp = Files.createTempFile("gram_eps", ".gtk");
        try {
            new EscritorGTK().escribir(g, tmp);
            String contenido = Files.readString(tmp);
            assertTrue(contenido.contains("S>epsilon"),
                    "El archivo debe contener 'S>epsilon', fue: " + contenido);
            Gramatica leida = new LectorGTK().leer(tmp);
            assertTrue(new EvaluadorGramatica(leida).evaluar("").esValida());
            assertTrue(new EvaluadorGramatica(leida).evaluar("aaa").esValida());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Preserva el NT inicial")
    void preservaInicial() throws IOException, ValidacionException {
        Gramatica g = gramaticaEnunciado();
        Path tmp = Files.createTempFile("gram_ini", ".gtk");
        try {
            new EscritorGTK().escribir(g, tmp);
            Gramatica leida = new LectorGTK().leer(tmp);
            assertEquals("A", leida.getInicial());
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Gramática null → IllegalArgumentException")
    void escribeNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new EscritorGTK().escribir(null, null));
    }

    @Test
    @DisplayName("Extensión incorrecta → ValidacionException")
    void extensionIncorrecta() throws ValidacionException {
        Gramatica g = gramaticaEnunciado();
        Path tmp = Path.of("/tmp/archivo_equivocado.txt");
        assertThrows(ValidacionException.class,
                () -> new EscritorGTK().escribir(g, tmp));
    }
}