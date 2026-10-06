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

class ParserModo1AFDTest {

    private AFD afd = new AFD("test");

    @Test
    @DisplayName("Parsea una línea del enunciado y reproduce 'aababb' válida")
    void parseaEnunciado() throws ValidacionException {
        ParserModo1AFD.parsear("A,A,a;false,false", afd, 1);
        ParserModo1AFD.parsear("A,C,b;false,false", afd, 2);
        ParserModo1AFD.parsear("B,A,a;false,false", afd, 3);
        ParserModo1AFD.parsear("B,C,b;false,false", afd, 4);
        ParserModo1AFD.parsear("C,B,a;false,false", afd, 5);
        ParserModo1AFD.parsear("C,D,b;false,true",  afd, 6);
        afd.setEstadoInicial("A");
        assertTrue(new EvaluadorAFD(afd).evaluar("aababb").esValida());
        assertFalse(new EvaluadorAFD(afd).evaluar("aab").esValida());
    }

    @Test
    @DisplayName("Líneas vacías y comentarios se ignoran")
    void lineasVaciasYComentarios() throws ValidacionException {
        ParserModo1AFD.parsear("", afd, 1);
        ParserModo1AFD.parsear("# comentario", afd, 2);
        ParserModo1AFD.parsear("   ", afd, 3);
        ParserModo1AFD.parsear("A,A,a;false,false", afd, 4);
        assertEquals(1, afd.getEstados().size());
    }

    @Test
    @DisplayName("Idempotencia: declarar estado/símbolo dos veces no falla")
    void idempotente() throws ValidacionException {
        ParserModo1AFD.parsear("A,B,a;false,false", afd, 1);
        ParserModo1AFD.parsear("B,C,b;false,false", afd, 2);
        // Declarar el mismo estado o símbolo en otra línea (con otro símbolo)
        // no debe lanzar excepción:
        ParserModo1AFD.parsear("A,C,b;false,false", afd, 3);
        // Estados declarados: A, B, C. Símbolos: a, b.
        assertEquals(3, afd.getEstados().size());
        assertEquals(2, afd.getAlfabeto().size());
    }

    @Test
    @DisplayName("Última aceptación gana (setAceptacion)")
    void ultimaAceptacionGana() throws ValidacionException {
        ParserModo1AFD.parsear("A,B,a;false,false", afd, 1);
        ParserModo1AFD.parsear("B,A,a;true,false",  afd, 2);
        assertTrue(afd.esAceptacion("B"));
        // Cambiamos la aceptación de B con otra transición (distinto símbolo
        // para no chocar con la anterior):
        ParserModo1AFD.parsear("B,C,b;false,false", afd, 3);
        assertFalse(afd.esAceptacion("B"),
                "La última línea con origen B fija false, sobrescribe el true anterior.");
    }

    @Test
    @DisplayName("Línea sin ';' → ValidacionException")
    void sinSeparador() {
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear("A,B,a false,false", afd, 1));
    }

    @Test
    @DisplayName("Línea sin coma de aceptación → ValidacionException")
    void sinComaAceptacion() {
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear("A,B,a;false", afd, 1));
    }

    @Test
    @DisplayName("Booleano inválido → ValidacionException")
    void booleanoInvalido() {
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear("A,B,a;yes,false", afd, 1));
    }

    @Test
    @DisplayName("Símbolo 'epsilon' → ValidacionException")
    void epsilonNoPermitido() {
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear("A,B,epsilon;false,false", afd, 1));
    }

    @Test
    @DisplayName("Transición duplicada → ValidacionException")
    void transicionDuplicada() throws ValidacionException {
        ParserModo1AFD.parsear("A,B,a;false,false", afd, 1);
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear("A,C,a;false,false", afd, 2));
    }

    @Test
    @DisplayName("Línea null → ValidacionException")
    void lineaNull() {
        assertThrows(ValidacionException.class,
                () -> ParserModo1AFD.parsear(null, afd, 1));
    }
}