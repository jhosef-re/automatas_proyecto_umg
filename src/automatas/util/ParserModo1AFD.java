/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.util;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;

/**
 * Parser de transiciones AFD en <b>modo 1</b>:
 * <pre>
 * origen, destino, simbolo; acept_origen, acept_destino
 * </pre>
 *
 * <p>Las llamadas son <b>idempotentes</b>: agregar el mismo estado/símbolo
 * dos veces no lanza excepción. Las reglas de validación del modelo se
 * delegan a {@link AFD#agregarEstado}, {@link AFD#agregarSimbolo},
 * {@link AFD#agregarTransicion} y {@link AFD#setAceptacion}; cualquier
 * {@link ValidacionException} del modelo se propaga.
 */
public final class ParserModo1AFD {

    private ParserModo1AFD() {}

    /**
     * @param linea   texto con el formato {@code A,B,a;false,true}
     * @param afd     AFD al que se aplica la transición
     * @param numLinea número de línea (1 solo, para mensajes de error)
     * @throws ValidacionException si el formato es inválido o el modelo rechaza los datos
     */
    public static void parsear(String linea, AFD afd, int numLinea) throws ValidacionException {
        if (linea == null) throw new ValidacionException("Línea " + numLinea + ": null");
        String t = linea.trim();
        if (t.isEmpty() || t.startsWith("#")) return;

        String[] partes = t.split(";", 2);
        if (partes.length != 2)
            throw new ValidacionException("Línea " + numLinea
                    + ": formato inválido (falta ';'): " + linea);

        String[] izq = partes[0].split(",", 3);
        if (izq.length != 3)
            throw new ValidacionException("Línea " + numLinea
                    + ": formato inválido (esperado origen,destino,simbolo): " + linea);

        String origen  = izq[0].trim();
        String destino = izq[1].trim();
        String simbolo = izq[2].trim();

        String[] acepts = partes[1].split(",", 2);
        if (acepts.length != 2)
            throw new ValidacionException("Línea " + numLinea
                    + ": formato inválido (esperado bool,bool): " + linea);
        boolean aceptOrigen  = parseBool(acepts[0].trim(), numLinea, linea);
        boolean aceptDestino = parseBool(acepts[1].trim(), numLinea, linea);

        declararEstadoSilencioso(afd, origen);
        declararEstadoSilencioso(afd, destino);
        declararSimboloSilencioso(afd, simbolo);

        afd.agregarTransicion(origen, destino, simbolo);
        afd.setAceptacion(origen,  aceptOrigen);
        afd.setAceptacion(destino, aceptDestino);
    }

    private static boolean parseBool(String s, int numLinea, String linea)
            throws ValidacionException {
        if (s.equalsIgnoreCase("true")) return true;
        if (s.equalsIgnoreCase("false")) return false;
        throw new ValidacionException("Línea " + numLinea
                + ": se esperaba true/false, obtuve '" + s + "' en: " + linea);
    }

    private static void declararEstadoSilencioso(AFD afd, String estado)
            throws ValidacionException {
        if (afd.getEstados().contains(estado)) return;
        afd.agregarEstado(estado);
    }

    private static void declararSimboloSilencioso(AFD afd, String simbolo)
            throws ValidacionException {
        if (afd.getAlfabeto().contains(simbolo)) return;
        afd.agregarSimbolo(simbolo);
    }
}