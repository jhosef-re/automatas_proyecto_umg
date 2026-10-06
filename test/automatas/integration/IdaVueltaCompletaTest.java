/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.integration;

import automatas.archivo.ArchivoFactory;
import automatas.archivo.Escritor;
import automatas.archivo.Lector;
import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.ConversorAFDGramatica;
import automatas.servicio.ConversorGramaticaAFD;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.validacion.ValidacionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración end-to-end de ida-vuelta completa:
 * modelo → convertir → guardar → leer → evaluar → comparar.
 *
 * <p>Recorre los 4 escenarios del enunciado §1.3:
 * <ul>
 *   <li>AFD → save .afd → load → evalúa igual</li>
 *   <li>Gramática → save .gtk → load → evalúa igual</li>
 *   <li>AFD → Gramática → AFD → save → load → evalúa equivalente al original</li>
 *   <li>Gramática → AFD → Gramática → save → load → evalúa equivalente al original</li>
 * </ul>
 */
class IdaVueltaCompletaTest {

    private static final Path RECURSOS = Paths.get("test/resources");

    @Test
    @DisplayName("AFD → save .afd → load → evalúa las mismas cadenas")
    void afdSaveLoad(@TempDir Path tmp) throws Exception {
        AFD original = afdEnunciado();
        Path archivo = tmp.resolve("afd_prueba.afd");
        guardar(original, archivo);

        AFD recargado = cargarAFD(archivo);

        assertEquals(original.getEstadoInicial(), recargado.getEstadoInicial());
        assertEquals(original.getEstados(), recargado.getEstados());
        assertEquals(original.getAlfabeto(), recargado.getAlfabeto());
        assertEquals(original.getEstadosAceptacion(), recargado.getEstadosAceptacion());
        assertEquals(original.getTransiciones(), recargado.getTransiciones());

        EvaluadorAFD ev1 = new EvaluadorAFD(original);
        EvaluadorAFD ev2 = new EvaluadorAFD(recargado);
        for (String c : Arrays.asList("", "a", "b", "ab", "aababb", "aaaa", "bbbb")) {
            assertEquals(ev1.evaluar(c).esValida(), ev2.evaluar(c).esValida(),
                    "Discrepancia en '" + c + "'");
        }
    }

    @Test
    @DisplayName("Gramática → save .gtk → load → evalúa las mismas cadenas")
    void gramaticaSaveLoad(@TempDir Path tmp) throws Exception {
        Gramatica original = gramaticaEnunciado();
        Path archivo = tmp.resolve("gram_prueba.gtk");
        guardar(original, archivo);

        Gramatica recargada = cargarGTK(archivo);

        assertEquals(original.getInicial(), recargada.getInicial());
        assertEquals(original.getNoTerminales(), recargada.getNoTerminales());
        assertEquals(original.getTerminales(), recargada.getTerminales());

        EvaluadorGramatica ev1 = new EvaluadorGramatica(original);
        EvaluadorGramatica ev2 = new EvaluadorGramatica(recargada);
        for (String c : Arrays.asList("", "0", "1", "00", "01", "0011", "010101", "abc")) {
            assertEquals(ev1.evaluar(c).esValida(), ev2.evaluar(c).esValida(),
                    "Discrepancia en '" + c + "'");
        }
    }

    @Test
    @DisplayName("AFD → Gramática → AFD → save → load → equivalente al original")
    void afdGramAfdRoundTrip(@TempDir Path tmp) throws Exception {
        AFD original = afdEnunciado();
        Gramatica g = new ConversorAFDGramatica(original).convertir("g");
        AFD convertido = new ConversorGramaticaAFD(g).convertir("afd");

        Path archivo = tmp.resolve("convertido.afd");
        guardar(convertido, archivo);

        AFD recargado = cargarAFD(archivo);

        EvaluadorAFD evOrig = new EvaluadorAFD(original);
        EvaluadorAFD evCarg = new EvaluadorAFD(recargado);

        List<String> palabras = todasPalabras(new ArrayList<>(original.getAlfabeto()), 4);
        for (String c : palabras) {
            assertEquals(evOrig.evaluar(c).esValida(), evCarg.evaluar(c).esValida(),
                    "Discrepancia en '" + c + "'");
        }
    }

    @Test
    @DisplayName("Gramática → AFD → Gramática → save → load → equivalente al original")
    void gramAfdGramRoundTrip(@TempDir Path tmp) throws Exception {
        Gramatica original = gramaticaEnunciado();
        AFD afd = new ConversorGramaticaAFD(original).convertir("afd");
        Gramatica gram2 = new ConversorAFDGramatica(afd).convertir("g2");

        Path archivo = tmp.resolve("convertida.gtk");
        guardar(gram2, archivo);

        Gramatica recargada = cargarGTK(archivo);

        EvaluadorGramatica evOrig = new EvaluadorGramatica(original);
        EvaluadorGramatica evCarg = new EvaluadorGramatica(recargada);

        List<String> palabras = todasPalabras(new ArrayList<>(original.getTerminales()), 4);
        for (String c : palabras) {
            assertEquals(evOrig.evaluar(c).esValida(), evCarg.evaluar(c).esValida(),
                    "Discrepancia en '" + c + "'");
        }
    }

    @Test
    @DisplayName("Factory dispatchea correctamente cada uno de los 11 archivos de recursos")
    void factoryDispatchaTodosLosArchivos() throws Exception {
        for (String nombre : Arrays.asList(
                "enunciado.afd", "enunciado.gtk",
                "con_comentarios.afd", "con_comentarios.gtk",
                "con_epsilon.gtk",
                "un_estado.afd", "sin_validas.afd", "lenguaje_ab.afd",
                "solo_terminales.gtk", "disyuncion.gtk", "multi_terminal.gtk")) {
            Path ruta = RECURSOS.resolve(nombre);
            assertTrue(Files.exists(ruta), "Falta el recurso: " + nombre);
            Lector<?> lector = ArchivoFactory.crearLector(ruta);
            Object modelo = lector.leer(ruta);
            assertNotNull(modelo, "El factory devolvió null para " + nombre);
            if (nombre.endsWith(".afd")) {
                assertInstanceOf(AFD.class, modelo, "Factory devolvió tipo incorrecto para " + nombre);
            } else if (nombre.endsWith(".gtk")) {
                assertInstanceOf(Gramatica.class, modelo, "Factory devolvió tipo incorrecto para " + nombre);
            }
        }
    }

    // ========== Helpers ==========

    private static AFD afdEnunciado() throws ValidacionException {
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

    private static Gramatica gramaticaEnunciado() throws ValidacionException {
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

    private static List<String> todasPalabras(List<String> alfabeto, int max) {
        List<String> out = new ArrayList<>();
        out.add("");
        for (int len = 1; len <= max; len++) {
            agregarLongitud(out, "", len, alfabeto);
        }
        return out;
    }

    private static void agregarLongitud(List<String> out, String prefijo, int restante,
                                        List<String> alfabeto) {
        if (restante == 0) { out.add(prefijo); return; }
        for (String s : alfabeto) {
            agregarLongitud(out, prefijo + s, restante - 1, alfabeto);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void guardar(Object modelo, Path archivo) throws IOException, ValidacionException {
        Escritor<?> esc = ArchivoFactory.crearEscritor(archivo);
        Escritor raw = (Escritor) esc;
        raw.escribir(modelo, archivo);
    }

    private static AFD cargarAFD(Path archivo) throws IOException, ValidacionException {
        Lector<?> lector = ArchivoFactory.crearLector(archivo);
        return (AFD) lector.leer(archivo);
    }

    private static Gramatica cargarGTK(Path archivo) throws IOException, ValidacionException {
        Lector<?> lector = ArchivoFactory.crearLector(archivo);
        return (Gramatica) lector.leer(archivo);
    }
}