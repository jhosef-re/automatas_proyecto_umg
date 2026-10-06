/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.modelo.Produccion;
import automatas.validacion.ValidacionException;
import java.util.List;
import java.util.Set;

/**
 * Convierte una gramática regular <b>lineal por la derecha</b> en un AFD
 * equivalente.
 *
 * <p>Transformación aplicada:
 * <ul>
 *   <li>cada NT → un estado del AFD;</li>
 *   <li>cada terminal → un símbolo del alfabeto;</li>
 *   <li>el NT inicial → estado inicial;</li>
 *   <li>{@code A > epsilon} → A es estado de aceptación;</li>
 *   <li>{@code A > w B} con {@code |w| == 1} → transición {@code δ(A, w) = B};</li>
 *   <li>{@code A > w} con {@code |w| == 1} → transición {@code δ(A, w) = F},
 *       donde {@code F} es un <b>estado final extra</b> de aceptación.</li>
 * </ul>
 *
 * <p><b>Estado final extra (F):</b> solo se crea si la gramática tiene al
 * menos una producción terminal-only (sin NT en el lado derecho). El nombre
 * base es {@link #NOMBRE_ESTADO_FINAL}; si ese NT ya está declarado, se usan
 * las variantes {@code F#0}, {@code F#1}, … hasta encontrar un nombre libre.
 *
 * <p><b>Casos rechazados (fail-fast):</b>
 * <ul>
 *   <li>producción con varios terminales antes del NT ({@code A > t1 t2 B})
 *       → {@code ValidacionException};</li>
 *   <li>producción epsilon-transición ({@code A > B} sin terminales) →
 *       {@code ValidacionException};</li>
 *   <li>dos producciones que generarían la misma transición
 *       ({@code A > a B} y {@code A > a C}) → {@code ValidacionException}
 *       (no se implementa conversión por subconjuntos).</li>
 * </ul>
 */
public class ConversorGramaticaAFD {

    /** Nombre base para el estado final extra. */
    public static final String NOMBRE_ESTADO_FINAL = "F";

    private final Gramatica gramatica;

    /**
     * @param gramatica gramática regular lineal por la derecha (no null)
     * @throws IllegalArgumentException si {@code gramatica} es null
     * @throws ValidacionException      si alguna producción no es derecha pura
     */
    public ConversorGramaticaAFD(Gramatica gramatica) throws ValidacionException {
        if (gramatica == null)
            throw new IllegalArgumentException("La gramática no puede ser null");
        validarDerechaPura(gramatica);
        this.gramatica = gramatica;
    }

    /** Misma validación que {@code EvaluadorGramatica}. */
    private void validarDerechaPura(Gramatica g) throws ValidacionException {
        Set<String> terminales = g.getTerminales();
        Set<String> noTerminales = g.getNoTerminales();
        for (String nt : noTerminales) {
            for (Produccion p : g.getProduccionesDe(nt)) {
                List<String> der = p.getDerecho();
                if (der.isEmpty()) continue;
                int lastNTIdx = -1;
                for (int i = der.size() - 1; i >= 0; i--) {
                    if (noTerminales.contains(der.get(i))) { lastNTIdx = i; break; }
                }
                if (lastNTIdx == -1) {
                    for (String s : der) {
                        if (!terminales.contains(s)) {
                            throw new ValidacionException(
                                    "La producción '" + p + "' no es lineal por la derecha: "
                                    + "símbolo no declarado '" + s + "'.");
                        }
                    }
                } else {
                    if (lastNTIdx != der.size() - 1) {
                        throw new ValidacionException(
                                "La producción '" + p + "' no es lineal por la derecha: "
                                + "el no terminal debe estar al final.");
                    }
                    for (int i = 0; i < lastNTIdx; i++) {
                        if (!terminales.contains(der.get(i))) {
                            throw new ValidacionException(
                                    "La producción '" + p + "' no es lineal por la derecha: "
                                    + "los símbolos antes del no terminal deben ser terminales.");
                        }
                    }
                }
            }
        }
    }

    /**
     * @param nombreAFD nombre del AFD resultante
     * @return AFD equivalente a la gramática
     * @throws ValidacionException si la gramática no tiene NT inicial, tiene
     *         producciones multi-terminal o genera no-determinismo
     */
    public AFD convertir(String nombreAFD) throws ValidacionException {
        if (gramatica.getInicial() == null)
            throw new ValidacionException("La gramática no tiene NT inicial.");

        AFD afd = new AFD(nombreAFD);
        for (String nt : gramatica.getNoTerminales())
            afd.agregarEstado(nt);
        for (String t : gramatica.getTerminales())
            afd.agregarSimbolo(t);
        afd.setEstadoInicial(gramatica.getInicial());

        boolean necesitaEstadoFinal = tieneProduccionTerminalOnly(gramatica);
        String estadoFinal = null;
        if (necesitaEstadoFinal) {
            estadoFinal = obtenerOcrearEstadoFinal(afd, gramatica.getNoTerminales());
            afd.agregarEstadoAceptacion(estadoFinal);
        }

        for (String nt : gramatica.getNoTerminales()) {
            for (Produccion p : gramatica.getProduccionesDe(nt)) {
                procesarProduccion(afd, p, estadoFinal);
            }
        }
        return afd;
    }

    /** Devuelve {@code true} si existe alguna producción de la forma {@code A > t1 t2 ... tk} (sin NT). */
    private boolean tieneProduccionTerminalOnly(Gramatica g) {
        Set<String> nt = g.getNoTerminales();
        for (String izq : g.getNoTerminales()) {
            for (Produccion p : g.getProduccionesDe(izq)) {
                List<String> der = p.getDerecho();
                if (der.isEmpty()) continue;
                boolean tieneNT = false;
                for (String s : der) {
                    if (nt.contains(s)) { tieneNT = true; break; }
                }
                if (!tieneNT) return true;
            }
        }
        return false;
    }

    /** Elige un nombre libre para el estado final extra, agregándolo al AFD si hace falta. */
    private String obtenerOcrearEstadoFinal(AFD afd, Set<String> ntDeclarados)
            throws ValidacionException {
        if (!ntDeclarados.contains(NOMBRE_ESTADO_FINAL)) {
            afd.agregarEstado(NOMBRE_ESTADO_FINAL);
            return NOMBRE_ESTADO_FINAL;
        }
        for (int i = 0; i < 10_000; i++) {
            String candidato = NOMBRE_ESTADO_FINAL + "#" + i;
            if (!ntDeclarados.contains(candidato)) {
                afd.agregarEstado(candidato);
                return candidato;
            }
        }
        throw new ValidacionException("No hay nombres libres para el estado final extra.");
    }

    /**
     * Procesa una producción individual:
     * <ul>
     *   <li>epsilon → estado de aceptación;</li>
     *   <li>w (1 terminal, sin NT) → transición al estado final extra;</li>
     *   <li>w B (terminales + NT) → transición al NT.</li>
     * </ul>
     */
    private void procesarProduccion(AFD afd, Produccion p, String estadoFinal)
            throws ValidacionException {
        List<String> der = p.getDerecho();
        String origen = p.getIzquierdo();

        if (der.isEmpty()) {
            afd.agregarEstadoAceptacion(origen);
            return;
        }

        Set<String> nt = gramatica.getNoTerminales();
        boolean tieneNT = false;
        int lastNTIdx = -1;
        for (int i = der.size() - 1; i >= 0; i--) {
            if (nt.contains(der.get(i))) { tieneNT = true; lastNTIdx = i; break; }
        }

        if (!tieneNT) {
            // w = secuencia de terminales.
            if (der.size() != 1) {
                throw new ValidacionException(
                        "La producción '" + p + "' tiene múltiples terminales sin NT: "
                        + "no se puede convertir directamente a AFD.");
            }
            if (estadoFinal == null) {
                throw new ValidacionException(
                        "La producción '" + p + "' requiere estado final extra, pero la "
                        + "gramática no tiene producciones terminal-only.");
            }
            afd.agregarTransicion(origen, estadoFinal, der.get(0));
            return;
        }

        // w B = terminales + NT al final.
        if (lastNTIdx == 0) {
            // Solo un NT, sin terminales: "A > B" → epsilon-transición → no es AFD.
            throw new ValidacionException(
                    "La producción '" + p + "' es una ε-transición, no válida en AFD.");
        }
        if (lastNTIdx > 1) {
            throw new ValidacionException(
                    "La producción '" + p + "' tiene múltiples terminales antes del NT: "
                    + "no se puede convertir directamente a AFD.");
        }
        // Caso normal: A > t B  (lastNTIdx == 1).
        afd.agregarTransicion(origen, der.get(lastNTIdx), der.get(0));
    }
}