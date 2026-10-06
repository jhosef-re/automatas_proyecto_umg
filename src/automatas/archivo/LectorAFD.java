/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.modelo.AFD;
import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Lee un AFD desde un archivo con el formato del enunciado:
 * <pre>
 * A,B,simbolo;acept_origen,acept_destino
 * </pre>
 * Una línea por transición. El <b>estado inicial</b> es el origen de la
 * primera transición no-comentada. Si un estado aparece con valores de
 * aceptación distintos en distintas líneas, <b>gana la última</b>
 * (delegado a {@link AFD#setAceptacion(String, boolean)}).
 *
 * <p>Las líneas vacías y las que empiezan con {@code #} se ignoran.
 *
 * <p>El AFD resultante recibe el nombre del archivo sin extensión
 * (p.ej. {@code /ruta/ejemplo.afd → AFD("ejemplo")}).
 */
public class LectorAFD implements Lector<AFD> {

    /** @return el AFD leído del archivo (sin registrar en el repositorio). */
    @Override
    public AFD leer(Path archivo) throws IOException, ValidacionException {
        if (archivo == null)
            throw new IllegalArgumentException("La ruta no puede ser null");
        if (!Files.exists(archivo))
            throw new IOException("No existe el archivo: " + archivo);
        if (!archivo.toString().toLowerCase().endsWith(".afd"))
            throw new ValidacionException("El archivo debe tener extensión .afd");

        String nombre = archivo.getFileName().toString();
        int punto = nombre.lastIndexOf('.');
        String nombreModelo = (punto >= 0) ? nombre.substring(0, punto) : nombre;

        AFD afd = new AFD(nombreModelo);
        List<String> lineas = Files.readAllLines(archivo);
        int numLinea = 0;
        String primerOrigen = null;

        for (String cruda : lineas) {
            numLinea++;
            String linea = cruda.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] partes = linea.split(";", 2);
            if (partes.length != 2)
                throw new ValidacionException("Línea " + numLinea
                        + ": formato inválido (falta ';'): " + linea);

            String[] izq = partes[0].split(",", 3);
            if (izq.length != 3)
                throw new ValidacionException("Línea " + numLinea
                        + ": formato inválido (esperado origen,destino,simbolo): " + linea);

            String origen = izq[0].trim();
            String destino = izq[1].trim();
            String simbolo = izq[2].trim();

            String[] acepts = partes[1].split(",", 2);
            if (acepts.length != 2)
                throw new ValidacionException("Línea " + numLinea
                        + ": formato inválido (esperado bool,bool): " + linea);
            boolean aceptOrigen = parseBool(acepts[0].trim(), numLinea, linea);
            boolean aceptDestino = parseBool(acepts[1].trim(), numLinea, linea);

            declararEstadoSilencioso(afd, origen);
            declararEstadoSilencioso(afd, destino);
            declararSimboloSilencioso(afd, simbolo);

            afd.agregarTransicion(origen, destino, simbolo);
            afd.setAceptacion(origen, aceptOrigen);
            afd.setAceptacion(destino, aceptDestino);

            if (primerOrigen == null) primerOrigen = origen;
        }

        if (primerOrigen == null)
            throw new ValidacionException("El archivo no contiene transiciones.");
        afd.setEstadoInicial(primerOrigen);

        return afd;
    }

    private static boolean parseBool(String s, int numLinea, String linea)
            throws ValidacionException {
        if (s.equalsIgnoreCase("true")) return true;
        if (s.equalsIgnoreCase("false")) return false;
        throw new ValidacionException("Línea " + numLinea
                + ": se esperaba true/false, obtuve '" + s + "' en: " + linea);
    }

    /** {@code agregarEstado} lanza excepción si está repetido; aquí lo ignoramos. */
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