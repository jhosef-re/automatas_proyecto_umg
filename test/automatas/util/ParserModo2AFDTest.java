/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.servicio.EvaluadorAFD;
import automatas.validacion.ValidacionException;

import java.util.Arrays;
import java.util.List;

class ParserModo2AFDTest {

    /** Matriz del AFD del enunciado. Cada fila de transiciones tiene 1 columna por terminal. */
    private List<String> matrizEnunciadoValida() {
        return Arrays.asList(
                "a, b",
                "A, B, C, D; D",
                "A, C",          // transiciones de A: δ(A,a)=A, δ(A,b)=C
                "A, C",          // transiciones de B: δ(B,a)=A, δ(B,b)=C
                "B, D",          // transiciones de C: δ(C,a)=B, δ(C,b)=D
                "D, D"           // transiciones de D: δ(D,a)=D, δ(D,b)=D
        );
    }

    @Test
    @DisplayName("Matriz del enunciado produce un AFD que acepta 'aababb'")
    void matrizEnunciado() throws ValidacionException {
        AFD afd = new AFD("matriz");
        ParserModo2AFD.parsear(matrizEnunciadoValida(), afd);
        assertTrue(new EvaluadorAFD(afd).evaluar("aababb").esValida());
        assertFalse(new EvaluadorAFD(afd).evaluar("aab").esValida());
    }

    @Test
    @DisplayName("Con corchetes opcionales alrededor de cada línea")
    void corchetesOpcionales() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "[a, b]",
                "[A, B; B]",
                "[B, A]",
                "[A, B]"
        ), afd);
        assertEquals(2, afd.getEstados().size());
        assertEquals(2, afd.getAlfabeto().size());
        assertEquals("A", afd.getEstadoInicial());
        assertTrue(afd.esAceptacion("B"));
    }

    @Test
    @DisplayName("Sin aceptaciones explícitas (sin ';' en línea de estados)")
    void sinAceptaciones() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "a, b",
                "A, B, C",                // sin ;
                "B, A",
                "A, A",
                "C, C"
        ), afd);
        assertTrue(afd.getEstadosAceptacion().isEmpty());
    }

    @Test
    @DisplayName("Líneas vacías y comentarios se ignoran")
    void vaciosYComentarios() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "# comentario",
                "",
                "a",
                "  ",
                "A; A",
                "A"
        ), afd);
        assertEquals("A", afd.getEstadoInicial());
        assertEquals(1, afd.getEstados().size());
    }

@Test
    @DisplayName("Celdas vacías en la matriz = sin transición")
    void celdasVacias() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "a, b",
                "A, B; B",
                "B, -",          // δ(A,a)=B, δ(A,b)=sin transición
                "-, A"           // δ(B,a)=sin, δ(B,b)=A
        ), afd);
        assertEquals("B", afd.mover("A", "a"));
        assertNull(afd.mover("A", "b"));
        assertNull(afd.mover("B", "a"));
        assertEquals("A", afd.mover("B", "b"));
    }

    @Test
    @DisplayName("Fila con número incorrecto de columnas → ValidacionException")
    void columnasInconsistentes() {
        AFD afd = new AFD("m");
        assertThrows(ValidacionException.class,
                () -> ParserModo2AFD.parsear(Arrays.asList(
                        "a, b",                // 2 terminales
                        "A, B, C",             // 3 estados
                        "B, A",                // fila 1 (estado A): 2 cols OK
                        "A",                   // fila 2 (estado B): solo 1 col, debe ser 2
                        "A, B"                 // fila 3 (estado C): 2 cols OK
                ), afd));
    }

    @Test
    @DisplayName("Más filas que estados → ValidacionException")
    void masFilasQueEstados() {
        AFD afd = new AFD("m");
        assertThrows(ValidacionException.class,
                () -> ParserModo2AFD.parsear(Arrays.asList(
                        "a",
                        "A",                 // 1 estado
                        "B",                 // primera fila
                        "A"                  // segunda fila → sobrante
                ), afd));
    }

    @Test
    @DisplayName("Lista vacía → ValidacionException")
    void listaVacia() {
        AFD afd = new AFD("m");
        assertThrows(ValidacionException.class,
                () -> ParserModo2AFD.parsear(Arrays.asList(), afd));
    }

    @Test
    @DisplayName("Solo línea de alfabeto (sin estados) → ValidacionException")
    void sinEstados() {
        AFD afd = new AFD("m");
        assertThrows(ValidacionException.class,
                () -> ParserModo2AFD.parsear(Arrays.asList("a, b"), afd));
    }

    @Test
    @DisplayName("Estado inicial = primer estado de la línea 2")
    void estadoInicialEsElPrimero() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "a",
                "Z, A; A",               // Z es el primero → inicial Z
                "A",                     // δ(Z,a) = A
                "Z"                      // δ(A,a) = Z
        ), afd);
        assertEquals("Z", afd.getEstadoInicial());
    }

    @Test
    @DisplayName("Una transición: celda con destino, agrega al AFD")
    void unaTransicion() throws ValidacionException {
        AFD afd = new AFD("m");
        ParserModo2AFD.parsear(Arrays.asList(
                "a",
                "A, B; B",
                "B",
                "A"
        ), afd);
        assertEquals("B", afd.mover("A", "a"));
        assertEquals("A", afd.mover("B", "a"));
    }
}