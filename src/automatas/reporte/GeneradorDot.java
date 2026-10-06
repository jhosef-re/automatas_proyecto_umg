/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import automatas.modelo.AFD;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Genera la representación DOT (lenguaje de Graphviz) de un AFD.
 *
 * <p>Características:
 * <ul>
 *   <li>Doble círculo para estados de aceptación.</li>
 *   <li>Flecha desde un nodo invisible hacia el estado inicial.</li>
 *   <li>Símbolos que comparten origen y destino se agrupan en una sola
 *       arista con label {@code "a,b,c"}.</li>
 * </ul>
 */
public final class GeneradorDot {

    private GeneradorDot() {}

    /**
     * @param afd AFD a serializar (no puede ser null)
     * @return String con el código DOT listo para pasar a {@code dot}
     */
    public static String generar(AFD afd) {
        if (afd == null) throw new IllegalArgumentException("AFD null");
        StringBuilder sb = new StringBuilder();

        sb.append("digraph AFD_").append(sanitize(afd.getNombre())).append(" {\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=circle];\n");

        // Estados: doble círculo para aceptación.
        for (String e : afd.getEstados()) {
            sb.append("    \"").append(sanitize(e)).append("\"");
            if (afd.esAceptacion(e)) {
                sb.append(" [shape=doublecircle]");
            }
            sb.append(";\n");
        }

        // Flecha de inicio: nodo invisible → estado inicial.
        String inicial = afd.getEstadoInicial();
        if (inicial != null) {
            sb.append("    \"_inicio\" [shape=point, style=invis];\n");
            sb.append("    \"_inicio\" -> \"").append(sanitize(inicial)).append("\";\n");
        }

        // Transiciones agrupadas (origen → destino → símbolos).
        // TreeMap para determinismo en la salida.
        Map<String, Map<String, List<String>>> agrupado = new TreeMap<>();
        for (var eOrigen : afd.getTransiciones().entrySet()) {
            String origen = eOrigen.getKey();
            for (var eSimbolo : eOrigen.getValue().entrySet()) {
                String destino = eSimbolo.getValue();
                agrupado
                        .computeIfAbsent(origen, k -> new TreeMap<>())
                        .computeIfAbsent(destino, k -> new ArrayList<>())
                        .add(eSimbolo.getKey());
            }
        }
        for (var eOrigen : agrupado.entrySet()) {
            for (var eDestino : eOrigen.getValue().entrySet()) {
                List<String> sims = eDestino.getValue();
                sims.sort(null);  // orden estable
                sb.append("    \"").append(sanitize(eOrigen.getKey()))
                        .append("\" -> \"").append(sanitize(eDestino.getKey()))
                        .append("\" [label=\"")
                        .append(String.join(",", sims))
                        .append("\"];\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    /** Escapa comillas para uso en strings DOT. */
    private static String sanitize(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}