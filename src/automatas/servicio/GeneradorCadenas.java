/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Genera ejemplos de cadenas <b>válidas</b> e <b>inválidas</b> para un AFD
 * o gramática, usando fuerza bruta sobre el alfabeto.
 *
 * <p>Estrategia: enumera todas las cadenas de longitud 0, 1, 2, …
 * hasta {@link #LONGITUD_MAXIMA_POR_DEFECTO} y evalúa cada una con el
 * {@link Evaluador} provisto. Devuelve las primeras {@code cantidad} que
 * cumplen el criterio (válida o inválida).
 *
 * <p>Funciona para AFDs y gramáticas: solo necesita un {@code Evaluador}
 * (Strategy) y el alfabeto sobre el que iterar. Las cadenas que contengan
 * símbolos fuera del alfabeto simplemente no se generan.
 *
 * <p>Si no se alcanzan {@code cantidad} cadenas válidas/inválidas dentro
 * del límite, devuelve las que encontró (sin rellenar).
 */
public class GeneradorCadenas {

    /** Longitud máxima por defecto al enumerar cadenas (suficiente para alfabetos típicos). */
    public static final int LONGITUD_MAXIMA_POR_DEFECTO = 4;

    private final Evaluador evaluador;
    private final Set<String> alfabeto;

    /**
     * @param evaluador estrategia de evaluación (AFD o gramática)
     * @param alfabeto  conjunto de símbolos (terminales) sobre el que iterar;
     *                  si es vacío, el único candidato es la cadena vacía
     * @throws IllegalArgumentException si el evaluador es null
     */
    public GeneradorCadenas(Evaluador evaluador, Set<String> alfabeto) {
        if (evaluador == null)
            throw new IllegalArgumentException("El evaluador no puede ser null");
        this.evaluador = evaluador;
        this.alfabeto = (alfabeto == null)
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(alfabeto);
    }

    /** @return hasta {@code cantidad} cadenas válidas (longitud máx = por defecto). */
    public List<String> generarValidas(int cantidad) {
        return generarValidas(cantidad, LONGITUD_MAXIMA_POR_DEFECTO);
    }

    /** @return hasta {@code cantidad} cadenas inválidas (longitud máx = por defecto). */
    public List<String> generarInvalidas(int cantidad) {
        return generarInvalidas(cantidad, LONGITUD_MAXIMA_POR_DEFECTO);
    }

    /** @return hasta {@code cantidad} cadenas válidas con longitud ≤ {@code longitudMaxima}. */
    public List<String> generarValidas(int cantidad, int longitudMaxima) {
        return generar(cantidad, true, longitudMaxima);
    }

    /** @return hasta {@code cantidad} cadenas inválidas con longitud ≤ {@code longitudMaxima}. */
    public List<String> generarInvalidas(int cantidad, int longitudMaxima) {
        return generar(cantidad, false, longitudMaxima);
    }

    /**
     * Algoritmo común. Itera por longitud y por orden lexicográfico estable
     * del alfabeto. Si {@code cantidad <= 0} devuelve lista vacía.
     */
    private List<String> generar(int cantidad, boolean filtroValidas, int longitudMaxima) {
        List<String> resultado = new ArrayList<>();
        if (cantidad <= 0 || longitudMaxima < 0) return resultado;

        List<String> simbolos = new ArrayList<>(alfabeto);
        for (int longitud = 0; longitud <= longitudMaxima; longitud++) {
            List<String> candidatos = palabrasDeLongitud(longitud, new StringBuilder(), simbolos);
            for (String c : candidatos) {
                boolean esValida = evaluador.evaluar(c).esValida();
                if (esValida == filtroValidas) {
                    resultado.add(c);
                    if (resultado.size() >= cantidad) return resultado;
                }
            }
        }
        return resultado;
    }

    /** Genera todas las palabras de la longitud indicada por extensión iterativa. */
    private List<String> palabrasDeLongitud(int longitudObjetivo, StringBuilder prefijo,
                                            List<String> simbolos) {
        List<String> out = new ArrayList<>();
        if (longitudObjetivo == 0) {
            out.add(prefijo.toString());
            return out;
        }
        for (String s : simbolos) {
            prefijo.append(s);
            out.addAll(palabrasDeLongitud(longitudObjetivo - 1, prefijo, simbolos));
            prefijo.deleteCharAt(prefijo.length() - 1);
        }
        return out;
    }
}