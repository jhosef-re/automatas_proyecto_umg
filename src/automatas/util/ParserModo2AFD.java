/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.util;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;
import java.util.List;

/**
 * Parser de transiciones AFD en <b>modo 2</b> (matriz):
 * <pre>
 * [a, b]                  &lt;- línea 1: terminales (alfabeto)
 * [A, B, C, D; B, D]      &lt;- línea 2: estados (antes de ;) + aceptación (después)
 * [B, D, A, A]            &lt;- líneas 3..N+1: destinos por terminal (orden = alfabeto)
 * [A, A, C, B]
 * </pre>
 *
 * <p>Cada celda de la matriz representa el destino de la transición
 * {@code δ(estado, terminal)}. Una línea de estados puede no incluir
 * aceptación (sin {@code ;}); si la incluye, los NT después del
 * {@code ;} son aceptaciones.
 *
 * <p>El <b>estado inicial</b> es el primer estado declarado en la línea 2.
 */
public final class ParserModo2AFD {

    private ParserModo2AFD() {}

    /**
     * @param lineas contenido de la matriz (cada elemento es una línea sin saltos)
     * @param afd   AFD al que se vuelcan los datos
     * @throws ValidacionException si el formato es inválido o el modelo rechaza los datos
     */
    public static void parsear(List<String> lineas, AFD afd) throws ValidacionException {
        if (lineas == null || lineas.isEmpty())
            throw new ValidacionException("La entrada está vacía.");

        int fila = 0;
        String[] terminales = null;
        String[] estados = null;
        String[] aceptacion = new String[0];

        for (String cruda : lineas) {
            fila++;
            String t = cruda.trim();
            if (t.isEmpty() || t.startsWith("#")) continue;

            // Quitar corchetes opcionales
            if (t.startsWith("[")) t = t.substring(1);
            if (t.endsWith("]"))   t = t.substring(0, t.length() - 1);
            t = t.trim();
            if (t.isEmpty()) continue;

            // ¿Contiene ';'? Significa "estados ; aceptacion"
            String[] dos = t.split("\\s*;\\s*", 2);
            String lhs = dos[0].trim();
            String rhs = (dos.length > 1) ? dos[1].trim() : "";

            String[] lhsPartes = splitIgnorandoVacios(lhs);

            if (terminales == null) {
                // Primera línea no-vacía: alfabeto
                terminales = lhsPartes;
                for (String t0 : terminales) {
                    if (afd.getAlfabeto().contains(t0)) continue;
                    afd.agregarSimbolo(t0);
                }
                continue;
            }
            if (estados == null) {
                // Segunda línea no-vacía: estados (y aceptación si hay rhs)
                estados = lhsPartes;
                String[] rhsPartes = splitIgnorandoVacios(rhs);
                for (String e : estados) {
                    if (afd.getEstados().contains(e)) continue;
                    afd.agregarEstado(e);
                }
                aceptacion = rhsPartes;
                for (String a : aceptacion) {
                    if (a.isEmpty()) continue;
                    afd.agregarEstadoAceptacion(a);
                }
                afd.setEstadoInicial(estados[0]);
                continue;
            }
            // Resto de líneas: destinos
            if (lhsPartes.length != terminales.length) {
                throw new ValidacionException("Fila " + fila
                        + ": se esperaban " + terminales.length + " columnas, hay "
                        + lhsPartes.length + ".");
            }
            // ¿De qué estado es esta fila? índice = (líneas no-vacías previas) - 2 (alfa + estados)
            int idxEstado = contarFilasProcesadas(lineas, fila - 1) - 2;
            if (idxEstado < 0 || idxEstado >= estados.length) {
                throw new ValidacionException("Fila " + fila
                        + ": hay más filas de transiciones que estados declarados.");
            }
            String origen = estados[idxEstado];
            for (int i = 0; i < terminales.length; i++) {
                String destino = lhsPartes[i];
                // Si la celda está vacía, no hay transición.
                if (destino.isEmpty() || "-".equals(destino)) continue;
                if (destino.equalsIgnoreCase("trap") || destino.equalsIgnoreCase("-")) continue;
                afd.agregarTransicion(origen, destino, terminales[i]);
            }
        }

        if (estados == null)
            throw new ValidacionException("Falta la línea de estados.");
    }

    private static String[] splitIgnorandoVacios(String s) {
        String[] r = s.split("\\s*,\\s*");
        for (int i = 0; i < r.length; i++) r[i] = r[i].trim();
        return r;
    }

    /** Cuenta cuántas líneas no-vacías se procesaron hasta (e incluida) la actual. */
    private static int contarFilasProcesadas(List<String> lineas, int hastaFila1Based) {
        int n = 0;
        for (int i = 0; i < hastaFila1Based; i++) {
            String t = lineas.get(i).trim();
            if (!t.isEmpty() && !t.startsWith("#")) n++;
        }
        return n;
    }
}