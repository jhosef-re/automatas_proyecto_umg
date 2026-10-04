/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioAutomatasTest {

    private RepositorioAutomatas repo;

    @BeforeEach
    void setUp() {
        repo = RepositorioAutomatas.getInstancia();
        repo.limpiar();
    }

    @Test
    @DisplayName("Singleton: getInstancia devuelve siempre la misma")
    void singleton() {
        assertSame(repo, RepositorioAutomatas.getInstancia());
    }

    @Test
    @DisplayName("Registrar AFD y gramática y recuperarlos por nombre")
    void registrar() throws ValidacionException {
        AFD afd = new AFD("a1");
        repo.registrar(afd);
        Gramatica g = new Gramatica("g1");
        repo.registrar(g);
        assertTrue(repo.existe("a1"));
        assertTrue(repo.existe("g1"));
        assertSame(afd, repo.obtenerAFD("a1"));
        assertSame(g, repo.obtenerGramatica("g1"));
        assertNull(repo.obtenerAFD("g1"));
        assertNull(repo.obtenerGramatica("a1"));
    }

    @Test
    @DisplayName("Conflicto de nombre: un AFD y una gramática no pueden llamarse igual")
    void conflictoNombre() throws ValidacionException {
        repo.registrar(new AFD("x"));
        assertThrows(ValidacionException.class, () -> repo.registrar(new Gramatica("x")));
        assertThrows(ValidacionException.class, () -> repo.registrar(new AFD("x")));
    }

    // ---- Edge cases (refactor) ----

    @Test
    @DisplayName("limpiar() borra todos los registros")
    void limpiarBorraTodo() throws ValidacionException {
        repo.registrar(new AFD("a1"));
        repo.registrar(new Gramatica("g1"));
        assertTrue(repo.existe("a1"));
        assertTrue(repo.existe("g1"));
        repo.limpiar();
        assertFalse(repo.existe("a1"));
        assertFalse(repo.existe("g1"));
        assertNull(repo.obtenerAFD("a1"));
        assertNull(repo.obtenerGramatica("g1"));
    }

    @Test
    @DisplayName("existe(null) retorna false sin lanzar")
    void existeNull() {
        assertFalse(repo.existe(null));
        assertNull(repo.obtenerAFD(null));
        assertNull(repo.obtenerGramatica(null));
    }
}