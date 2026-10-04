/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import java.util.*;

/** Gramática regular con las validaciones del enunciado. */
public class Gramatica {

    private final String nombre;
    private final Set<String> noTerminales = new LinkedHashSet<>();
    private final Set<String> terminales = new LinkedHashSet<>();
    private final Map<String, List<Produccion>> producciones = new LinkedHashMap<>();
    private String inicial;

    public Gramatica(String nombre) {
        this.nombre = nombre;
    }

    public void agregarNoTerminal(String nt) throws ValidacionException {
        nt = limpiar(nt, "no terminal");
        if (noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' ya existe.");
        if (terminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' es igual a un terminal.");
        noTerminales.add(nt);
    }

    public void agregarTerminal(String t) throws ValidacionException {
        t = limpiar(t, "terminal");
        if (t.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("'epsilon' está reservado para el vacío.");
        if (terminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' ya existe.");
        if (noTerminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' es igual a un no terminal.");
        terminales.add(t);
    }

    /** Define el NT inicial; reemplaza al anterior. */
    public void setInicial(String nt) throws ValidacionException {
        nt = limpiar(nt, "no terminal inicial");
        if (!noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' no existe.");
        this.inicial = nt;
    }

    /**
     * Agrega una línea de producción. Admite disyunción con '|'.
     * Formato:  A > a B | b | epsilon   (símbolos separados por espacio)
     */
    public void agregarProduccion(String linea) throws ValidacionException {
        int pos = linea.indexOf('>');
        if (pos < 0)
            throw new ValidacionException("Formato inválido. Use:  NT > simbolos");
        String izq = linea.substring(0, pos).trim();
        if (!noTerminales.contains(izq))
            throw new ValidacionException("El no terminal '" + izq + "' no existe.");

        for (String alternativa : linea.substring(pos + 1).split("\\|")) {
            List<String> simbolos = new ArrayList<>();
            String alt = alternativa.trim();
            if (alt.isEmpty())
                throw new ValidacionException("Alternativa vacía; use 'epsilon' para el vacío.");
            if (!alt.equalsIgnoreCase("epsilon")) {
                for (String s : alt.split("\\s+")) {
                    if (!noTerminales.contains(s) && !terminales.contains(s))
                        throw new ValidacionException("El símbolo '" + s + "' no está declarado.");
                    simbolos.add(s);
                }
            }
            Produccion nueva = new Produccion(izq, simbolos);
            List<Produccion> lista = producciones.computeIfAbsent(izq, k -> new ArrayList<>());
            if (lista.contains(nueva))
                throw new ValidacionException("La producción '" + nueva + "' ya existe.");
            lista.add(nueva);
        }
    }

    public List<Produccion> getProduccionesDe(String nt) {
        return producciones.getOrDefault(nt, Collections.emptyList());
    }

    private String limpiar(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El " + campo + " no puede estar vacío.");
        return valor.trim();
    }

    // ---- Getters ----
    public String getNombre() { return nombre; }
    public Set<String> getNoTerminales() { return Collections.unmodifiableSet(noTerminales); }
    public Set<String> getTerminales() { return Collections.unmodifiableSet(terminales); }
    public String getInicial() { return inicial; }
    public Map<String, List<Produccion>> getProducciones() {
        return Collections.unmodifiableMap(producciones);
    }
}