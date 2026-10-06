/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifica que las conversiones AFD↔Gramática preservan el lenguaje:
 * el modelo original y el convertido aceptan/rechazan las mismas cadenas
 * sobre un muestreo exhaustivo de longitud acotada.
 */
class EquivalenciaConversionTest {

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

    /** Genera todas las palabras sobre {@code alfabeto} de longitud ≤ {@code max}. */
    private List<String> todasPalabras(List<String> alfabeto, int max) {
        List<String> out = new ArrayList<>();
        out.add("");
        for (int len = 1; len <= max; len++) {
            agregarLongitud(out, "", len, alfabeto);
        }
        return out;
    }

    private void agregarLongitud(List<String> out, String prefijo, int restante,
                                  List<String> alfabeto) {
        if (restante == 0) { out.add(prefijo); return; }
        for (String s : alfabeto) {
            agregarLongitud(out, prefijo + s, restante - 1, alfabeto);
        }
    }

    private void assertEquivalencia(String descripcion, java.util.function.Predicate<String> acepta1,
                                     java.util.function.Predicate<String> acepta2,
                                     List<String> cadenas) {
        for (String c : cadenas) {
            assertEquals(acepta1.test(c), acepta2.test(c),
                    descripcion + " discrepancia en '" + c + "'");
        }
    }

    @Test
    @DisplayName("AFD del enunciado ≡ Gramática convertida (16 cadenas)")
    void afdAGramaticaEquivalencia() throws ValidacionException {
        AFD original = afdEnunciado();
        Gramatica convertida = new ConversorAFDGramatica(original).convertir("g");
        EvaluadorAFD evAfd = new EvaluadorAFD(original);
        EvaluadorGramatica evGram = new EvaluadorGramatica(convertida);

        List<String> palabras = todasPalabras(Arrays.asList("a", "b"), 4);
        assertEquivalencia("AFD vs Gram",
                c -> evAfd.evaluar(c).esValida(),
                c -> evGram.evaluar(c).esValida(),
                palabras);
    }

    @Test
    @DisplayName("Gramática (0|1)* ≡ AFD convertido")
    void gramaticaAAfdEquivalencia() throws ValidacionException {
        Gramatica original = new Gramatica("g");
        for (String nt : new String[]{"A", "B"}) original.agregarNoTerminal(nt);
        for (String t : new String[]{"0", "1"}) original.agregarTerminal(t);
        original.setInicial("A");
        original.agregarProduccion("A > 0 B");
        original.agregarProduccion("A > 1 A");
        original.agregarProduccion("A > epsilon");
        original.agregarProduccion("B > 0 B");
        original.agregarProduccion("B > 1 A");

        AFD convertido = new ConversorGramaticaAFD(original).convertir("afd");
        EvaluadorGramatica evGram = new EvaluadorGramatica(original);
        EvaluadorAFD evAfd = new EvaluadorAFD(convertido);

        List<String> palabras = todasPalabras(Arrays.asList("0", "1"), 4);
        assertEquivalencia("Gram vs AFD",
                c -> evGram.evaluar(c).esValida(),
                c -> evAfd.evaluar(c).esValida(),
                palabras);
    }

    @Test
    @DisplayName("Ida-vuelta AFD: AFD → Gram → AFD preserva el lenguaje")
    void idaVueltaAFD() throws ValidacionException {
        AFD original = afdEnunciado();
        Gramatica g = new ConversorAFDGramatica(original).convertir("g");
        AFD convertido = new ConversorGramaticaAFD(g).convertir("afd");

        EvaluadorAFD ev1 = new EvaluadorAFD(original);
        EvaluadorAFD ev2 = new EvaluadorAFD(convertido);
        List<String> palabras = todasPalabras(Arrays.asList("a", "b"), 4);
        assertEquivalencia("Ida-vuelta AFD",
                c -> ev1.evaluar(c).esValida(),
                c -> ev2.evaluar(c).esValida(),
                palabras);
    }

    @Test
    @DisplayName("Ida-vuelta Gramática: Gram → AFD → Gram preserva el lenguaje")
    void idaVueltaGram() throws ValidacionException {
        Gramatica original = new Gramatica("g");
        for (String nt : new String[]{"A", "B"}) original.agregarNoTerminal(nt);
        for (String t : new String[]{"0", "1"}) original.agregarTerminal(t);
        original.setInicial("A");
        original.agregarProduccion("A > 0 B");
        original.agregarProduccion("A > 1 A");
        original.agregarProduccion("A > epsilon");
        original.agregarProduccion("B > 0 B");
        original.agregarProduccion("B > 1 A");

        AFD afd = new ConversorGramaticaAFD(original).convertir("afd");
        Gramatica gram2 = new ConversorAFDGramatica(afd).convertir("g2");

        EvaluadorGramatica ev1 = new EvaluadorGramatica(original);
        EvaluadorGramatica ev2 = new EvaluadorGramatica(gram2);
        List<String> palabras = todasPalabras(Arrays.asList("0", "1"), 4);
        assertEquivalencia("Ida-vuelta Gram",
                c -> ev1.evaluar(c).esValida(),
                c -> ev2.evaluar(c).esValida(),
                palabras);
    }

    @Test
    @DisplayName("Múltiples AFDs distintos: la conversión preserva el lenguaje")
    void multiplesAFDsDistintos() throws ValidacionException {
        // AFD que acepta solo "a".
        AFD afd1 = new AFD("a");
        afd1.agregarEstado("S");
        afd1.agregarEstado("F");
        afd1.agregarSimbolo("a");
        afd1.setEstadoInicial("S");
        afd1.agregarEstadoAceptacion("F");
        afd1.agregarTransicion("S", "F", "a");

        // AFD que acepta "ab*".
        AFD afd2 = new AFD("ab*");
        afd2.agregarEstado("S");
        afd2.agregarEstado("A");
        afd2.agregarEstado("B");
        afd2.agregarSimbolo("a");
        afd2.agregarSimbolo("b");
        afd2.setEstadoInicial("S");
        afd2.agregarEstadoAceptacion("A");
        afd2.agregarTransicion("S", "A", "a");
        afd2.agregarTransicion("A", "B", "b");
        afd2.agregarTransicion("B", "B", "b");

        for (AFD original : new AFD[]{afd1, afd2}) {
            Gramatica convertida = new ConversorAFDGramatica(original).convertir("g");
            EvaluadorAFD evAfd = new EvaluadorAFD(original);
            EvaluadorGramatica evGram = new EvaluadorGramatica(convertida);
            List<String> alfabeto = new ArrayList<>(original.getAlfabeto());
            List<String> palabras = todasPalabras(alfabeto, 4);
            assertEquivalencia(original.getNombre(),
                    c -> evAfd.evaluar(c).esValida(),
                    c -> evGram.evaluar(c).esValida(),
                    palabras);
        }
    }
}