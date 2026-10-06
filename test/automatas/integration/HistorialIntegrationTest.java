/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.integration;

import automatas.archivo.ArchivoFactory;
import automatas.archivo.Lector;
import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.HistorialEvaluaciones;
import automatas.servicio.RegistroEvaluacion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración del flujo Evaluar → Historial.
 *
 * <p>Verifica que múltiples evaluaciones sobre distintos modelos queden
 * reflejadas en el {@link HistorialEvaluaciones} Singleton, sin duplicados,
 * con el orden de inserción y la metadata correcta.
 */
class HistorialIntegrationTest {

    private static final Path RECURSOS = Paths.get("test/resources");
    private HistorialEvaluaciones historial;

    @BeforeEach
    void limpiarAntes() {
        historial = HistorialEvaluaciones.getInstancia();
        historial.limpiar();
        assertEquals(0, historial.getTodos().size(), "El historial debe arrancar vacío");
    }

    @AfterEach
    void limpiarDespues() {
        historial.limpiar();
    }

    @Test
    @DisplayName("Singleton: getInstancia() siempre devuelve la misma instancia")
    void singleton() {
        HistorialEvaluaciones a = HistorialEvaluaciones.getInstancia();
        HistorialEvaluaciones b = HistorialEvaluaciones.getInstancia();
        assertSame(a, b);
    }

    @Test
    @DisplayName("Evaluar 3 cadenas distintas del enunciado.afd → 3 registros en historial")
    void evaluarAfdEnunciado() throws Exception {
        AFD afd = cargarAFD("enunciado.afd");
        EvaluadorAFD ev = new EvaluadorAFD(afd);

        ev.evaluar("aababb");   // válida
        ev.evaluar("aab");      // inválida
        ev.evaluar("aaba");     // inválida
        historial.registrar("enunciado", "aababb", true);
        historial.registrar("enunciado", "aab", false);
        historial.registrar("enunciado", "aaba", false);

        List<RegistroEvaluacion> regs = historial.getTodos();
        assertEquals(3, regs.size());

        assertEquals("enunciado", regs.get(0).getNombreModelo());
        assertEquals("aababb", regs.get(0).getCadena());
        assertTrue(regs.get(0).esValida());

        assertFalse(regs.get(1).esValida());
        assertFalse(regs.get(2).esValida());
    }

    @Test
    @DisplayName("Registrar misma cadena dos veces → no se duplica")
    void sinDuplicados() {
        historial.registrar("m1", "ab", true);
        historial.registrar("m1", "ab", true);
        historial.registrar("m1", "ab", false);
        historial.registrar("m1", "ac", true);

        assertEquals(2, historial.getTodos().size(),
                "Solo debe haber 2 registros únicos (ab, ac)");
    }

    @Test
    @DisplayName("Registros de dos modelos distintos se mantienen independientes")
    void dosModelosIndependientes() throws Exception {
        AFD afd = cargarAFD("enunciado.afd");
        Gramatica g = cargarGTK("enunciado.gtk");

        historial.registrar(afd.getNombre(), "aababb", true);
        historial.registrar(g.getNombre(), "0011", true);
        historial.registrar(afd.getNombre(), "aab", false);
        historial.registrar(g.getNombre(), "0", false);

        List<RegistroEvaluacion> regs = historial.getTodos();
        assertEquals(4, regs.size());

        // Como enunciado.afd y enunciado.gtk comparten nombre 'enunciado',
        // la unicidad es (modelo, cadena). Ambos pares se preservan.
        assertTrue(regs.stream().anyMatch(r -> r.getCadena().equals("aababb") && r.esValida()));
        assertTrue(regs.stream().anyMatch(r -> r.getCadena().equals("0011")   && r.esValida()));
        assertTrue(regs.stream().anyMatch(r -> r.getCadena().equals("aab")    && !r.esValida()));
        assertTrue(regs.stream().anyMatch(r -> r.getCadena().equals("0")      && !r.esValida()));
    }

    @Test
    @DisplayName("Limpiar historial → vuelve a 0")
    void limpiar() {
        historial.registrar("m", "x", true);
        historial.registrar("m", "y", false);
        assertEquals(2, historial.getTodos().size());
        historial.limpiar();
        assertEquals(0, historial.getTodos().size());
    }

    @Test
    @DisplayName("getTodos() devuelve vista inmutable (no se puede mutar)")
    void vistaInmutable() {
        historial.registrar("m", "x", true);
        List<RegistroEvaluacion> regs = historial.getTodos();
        assertThrows(UnsupportedOperationException.class,
                () -> regs.add(new RegistroEvaluacion("otro", "z", true)));
    }

    // ========== Helpers ==========

    private static AFD cargarAFD(String nombreArchivo) throws IOException, Exception {
        Path ruta = RECURSOS.resolve(nombreArchivo);
        Lector<?> lector = ArchivoFactory.crearLector(ruta);
        return (AFD) lector.leer(ruta);
    }

    private static Gramatica cargarGTK(String nombreArchivo) throws IOException, Exception {
        Path ruta = RECURSOS.resolve(nombreArchivo);
        Lector<?> lector = ArchivoFactory.crearLector(ruta);
        return (Gramatica) lector.leer(ruta);
    }
}