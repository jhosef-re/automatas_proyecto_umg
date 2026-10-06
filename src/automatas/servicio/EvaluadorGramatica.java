/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.Gramatica;
import automatas.modelo.Produccion;
import automatas.validacion.ValidacionException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Evalúa una cadena contra una gramática regular <b>lineal por la derecha</b>.
 *
 * <p>La validación de la convención derecha se hace en el constructor: toda
 * producción debe tener la forma {@code A → a1 a2 ... an} donde
 * {@code a1..an-1} son terminales y {@code an} es un no terminal (o bien la
 * producción es {@code A → ε}). Si alguna producción no respeta esa forma se
 * lanza {@link ValidacionException}.
 *
 * <p>El algoritmo es <b>backtracking forward</b>: parte del símbolo inicial y
 * va reemplazando el NT más a la izquierda por cada una de sus producciones
 * hasta que el <i>form</i> resultante coincide con la cadena objetivo. La
 * poda se hace con:
 * <ul>
 *   <li>un conjunto de visitados (clave = NT + longitud del form) que evita
 *       ciclos como {@code A → ε} aplicado dos veces;</li>
 *   <li>un tope {@link #MAX_PROFUNDIDAD} que garantiza terminación aún con
 *       gramáticas maliciosas.</li>
 * </ul>
 *
 * <p>El detalle del resultado tiene el formato del enunciado:
 * {@code "Expansión Gramática: A>0B>00B>001A>0011A>0011(epsilon)>0011"}.
 * Cuando una producción aplicada es ε, el paso resultante lleva el sufijo
 * {@code (epsilon)} para señalar que el NT fue consumido.
 */
public class EvaluadorGramatica implements Evaluador {

    /** Tope de recursión para evitar explosión combinatoria / bucles infinitos. */
    public static final int MAX_PROFUNDIDAD = 100;

private final Gramatica gramatica;

    /**
     * @param gramatica gramática regular lineal por la derecha (no null)
     * @throws IllegalArgumentException si {@code gramatica} es null
     * @throws ValidacionException      si alguna producción no es derecha pura
     */
    public EvaluadorGramatica(Gramatica gramatica) throws ValidacionException {
        if (gramatica == null)
            throw new IllegalArgumentException("La gramática no puede ser null");
        validarDerechaPura(gramatica);
        this.gramatica = gramatica;
    }

    /** @return la gramática subyacente (útil para los reportes PDF). */
    public Gramatica getGramatica() {
        return gramatica;
    }

    /**
     * Verifica que cada producción sea lineal por la derecha. Una producción
     * es derecha pura si:
     * <ul>
     *   <li>es epsilon (lado derecho vacío), o</li>
     *   <li>tiene solo terminales (producción terminal), o</li>
     *   <li>tiene una secuencia de terminales seguida de exactamente un
     *       no terminal al final.</li>
     * </ul>
     * Cualquier NT en posición intermedia, o dos NTs, o un NT seguido de
     * un terminal viola la convención.
     *
     * @throws ValidacionException si alguna producción viola la convención.
     */
    private void validarDerechaPura(Gramatica g) throws ValidacionException {
        Set<String> terminales = g.getTerminales();
        Set<String> noTerminales = g.getNoTerminales();
        for (String nt : noTerminales) {
            for (Produccion p : g.getProduccionesDe(nt)) {
                List<String> der = p.getDerecho();
                if (der.isEmpty()) continue;

                int lastNTIdx = -1;
                for (int i = der.size() - 1; i >= 0; i--) {
                    if (noTerminales.contains(der.get(i))) {
                        lastNTIdx = i;
                        break;
                    }
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
     * @param cadena cadena a derivar desde el NT inicial (un carácter por símbolo)
     * @return resultado con la expansión y la validez
     * @throws IllegalArgumentException si {@code cadena} es null
     */
    @Override
    public ResultadoEvaluacion evaluar(String cadena) {
        if (cadena == null)
            throw new IllegalArgumentException("La cadena no puede ser null");

        String inicial = gramatica.getInicial();
        if (inicial == null)
            return new ResultadoEvaluacion(false,
                    "Expansión Gramática: (gramática sin NT inicial)");

        List<String> pasos = new ArrayList<>();
        pasos.add(inicial);
        boolean exito = backtrack(inicial, pasos, cadena, new HashSet<>(), 0);
        if (exito)
            return new ResultadoEvaluacion(true,
                    "Expansión Gramática: " + String.join("> ", pasos));
        return new ResultadoEvaluacion(false,
                "Expansión Gramática: (sin derivación para '" + cadena + "')");
    }

    /**
     * Búsqueda recursiva con poda. Construye la derivación en {@code pasos}
     * mediante push/pop y devuelve {@code true} cuando el {@code form}
     * coincide con la cadena objetivo. Compara siempre el form <b>sin</b>
     * la anotación {@code (epsilon)}.
     *
     * <p>Poda por estado {@code (NT, prefijo)}: si el prefijo del form ya
     * excede la longitud de la cadena objetivo, o si la cadena no comienza
     * con ese prefijo, la rama no puede llevar a una derivación exitosa.
     * La clave {@code (NT, prefijo)} evita re-explorar la misma situación.
     */
    private boolean backtrack(String form, List<String> pasos, String cadena,
                              Set<String> visitados, int profundidad) {
        if (profundidad > MAX_PROFUNDIDAD) return false;
        if (form.equals(cadena)) {
            if (!cadena.isEmpty()
                    && (pasos.isEmpty() || !pasos.get(pasos.size() - 1).equals(cadena))) {
                pasos.add(cadena);
            }
            return true;
        }

        int posNT = indiceUltimoNT(form);
        if (posNT < 0) return false;

        String nt = form.substring(posNT, posNT + 1);
        String prefix = form.substring(0, posNT);

        if (prefix.length() > cadena.length()) return false;
        if (!cadena.startsWith(prefix)) return false;

        String clave = nt + "|" + prefix;
        if (visitados.contains(clave)) return false;
        visitados.add(clave);

        for (Produccion p : gramatica.getProduccionesDe(nt)) {
            String rhs = p.esEpsilon() ? "" : String.join("", p.getDerecho());
            StringBuilder sb = new StringBuilder(form);
            sb.replace(posNT, posNT + 1, rhs);
            String nuevo = sb.toString();

            String pasoLog;
            if (p.esEpsilon() && !nuevo.endsWith("(epsilon)")) {
                pasoLog = nuevo + "(epsilon)";
            } else {
                pasoLog = nuevo;
            }
            pasos.add(pasoLog);

            if (backtrack(nuevo, pasos, cadena, visitados, profundidad + 1))
                return true;
            pasos.remove(pasos.size() - 1);
        }

        visitados.remove(clave);
        return false;
    }

    /**
     * Devuelve el índice del <b>último</b> no terminal presente en la cadena
     * (en gramáticas derechas siempre hay a lo sumo un NT y está al final),
     * o -1 si no hay.
     */
    private int indiceUltimoNT(String form) {
        Set<String> nt = gramatica.getNoTerminales();
        for (int i = form.length() - 1; i >= 0; i--) {
            if (nt.contains(form.substring(i, i + 1)))
                return i;
        }
        return -1;
    }
}