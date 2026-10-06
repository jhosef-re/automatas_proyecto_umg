/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatosCursoTest {

    @Test
    @DisplayName("nombresCompletos() incluye los 3 integrantes con su carné")
    void nombresCompletosListaLos3() {
        String nombres = DatosCurso.nombresCompletos();
        assertEquals(3, DatosCurso.NOMBRES.length);
        assertEquals(3, DatosCurso.CARNES.length);

        assertTrue(nombres.contains("Jhosef Estefano Reyes Román"),
                "Falta el integrante Jhosef");
        assertTrue(nombres.contains("Alejandro Leiva García"),
                "Falta el integrante Alejandro");
        assertTrue(nombres.contains("Oscar René Gonzales Rojas"),
                "Falta el integrante Oscar");

        assertTrue(nombres.contains("9390-24-4816"), "Falta carné de Jhosef");
        assertTrue(nombres.contains("9390-24-7148"), "Falta carné de Alejandro");
        assertTrue(nombres.contains("9390-24-8224"), "Falta carné de Oscar");
    }

    @Test
    @DisplayName("nombresCompletos() tiene exactamente 2 saltos de línea (3 líneas)")
    void nombresCompletosTresLineas() {
        String nombres = DatosCurso.nombresCompletos();
        long saltos = nombres.chars().filter(c -> c == '\n').count();
        assertEquals(2, saltos,
                "Esperaba 2 saltos (3 líneas, una por integrante)");
    }

    @Test
    @DisplayName("textoAyuda() incluye los 3 últimos dígitos del carnet")
    void textoAyudaUltimoDigito() {
        String ayuda = DatosCurso.textoAyuda();
        assertTrue(ayuda.contains("Jhosef Estefano Reyes Román"));
        assertTrue(ayuda.contains("Alejandro Leiva García"));
        assertTrue(ayuda.contains("Oscar René Gonzales Rojas"));
        assertTrue(ayuda.contains("→ 6"),  "Falta último dígito 6 (Jhosef)");
        assertTrue(ayuda.contains("→ 8"),  "Falta último dígito 8 (Alejandro)");
        assertTrue(ayuda.contains("→ 4"),  "Falta último dígito 4 (Oscar)");
    }

    @Test
    @DisplayName("textoAyuda() incluye el curso, la sección y el catedrático")
    void textoAyudaDatosCurso() {
        String ayuda = DatosCurso.textoAyuda();
        assertTrue(ayuda.contains("Autómatas y Lenguajes Formales"));
        assertTrue(ayuda.contains("Sección: A"));
        assertTrue(ayuda.contains("Inge. Alan G. Ucelo Morán"));
    }

    @Test
    @DisplayName("Constantes CURSO, SECCION, CATEDRATICO, CARNE no son placeholders")
    void constantesCompletas() {
        assertNotNull(DatosCurso.CURSO);
        assertNotNull(DatosCurso.SECCION);
        assertNotNull(DatosCurso.CARNE);
        assertNotNull(DatosCurso.CATEDRATICO);
        assertFalse(DatosCurso.CURSO.contains("<"),       "CURSO tiene placeholder");
        assertFalse(DatosCurso.SECCION.contains("<"),      "SECCION tiene placeholder");
        assertFalse(DatosCurso.CARNE.contains("<"),        "CARNE tiene placeholder");
        assertFalse(DatosCurso.CATEDRATICO.contains("<"),  "CATEDRATICO tiene placeholder");
        assertEquals("A", SECCION());
    }

    private static String SECCION() { return DatosCurso.SECCION; }
}