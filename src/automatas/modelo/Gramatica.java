/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import automatas.validacion.ValidacionUtils;
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

    /**
     * Agrega un no terminal: no repetido y distinto de cualquier terminal.
     *
     * @throws ValidacionException si está vacío o coincide con un terminal existente.
     */
    public void agregarNoTerminal(String nt) throws ValidacionException {
        nt = ValidacionUtils.normalizar(nt, "no terminal");
        if (noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' ya existe.");
        if (terminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' es igual a un terminal.");
        noTerminales.add(nt);
    }

    /**
     * Agrega un terminal: no repetido y distinto de cualquier no terminal.
     * La palabra "epsilon" está reservada para representar el vacío.
     *
     * @throws ValidacionException si está vacío, es "epsilon" o coincide con un NT.
     */
    public void agregarTerminal(String t) throws ValidacionException {
        t = ValidacionUtils.normalizar(t, "terminal");
        if (t.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("'epsilon' está reservado para el vacío.");
        if (terminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' ya existe.");
        if (noTerminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' es igual a un no terminal.");
        terminales.add(t);
    }

    /**
     * Define el NT inicial; si ya había uno, lo reemplaza.
     *
     * @throws ValidacionException si el NT es vacío o no fue declarado.
     */
    public void setInicial(String nt) throws ValidacionException {
        nt = ValidacionUtils.normalizar(nt, "no terminal inicial");
        if (!noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' no existe.");
        this.inicial = nt;
    }

    /**
     * Agrega una línea de producción. Admite disyunción con '|'.
     * Formato: {@code NT > simbolo1 simbolo2 ... | alternativa2 | epsilon}.
     * Los símbolos se separan por espacio; el vacío se representa con {@code epsilon}.
     *
     * <p>Cada alternativa genera una {@link Produccion} independiente. Las
     * alternativas duplicadas se rechazan.
     *
     * @throws ValidacionException si falta {@code >}, el NT no existe, algún
     *         símbolo no está declarado, hay una alternativa vacía o la
     *         producción ya existe.
     */
    public void agregarProduccion(String linea) throws ValidacionException {
        int pos = linea.indexOf('>');
        if (pos < 0 || pos != linea.lastIndexOf('>'))
            throw new ValidacionException("Formato inválido. Use:  NT > simbolos");
        String izq = ValidacionUtils.normalizar(linea.substring(0, pos), "no terminal izquierdo");
        if (!noTerminales.contains(izq))
            throw new ValidacionException("El no terminal '" + izq + "' no existe.");

        for (String alternativa : linea.substring(pos + 1).split("\\|")) {
            String alt = alternativa.trim();
            if (alt.isEmpty())
                throw new ValidacionException("Alternativa vacía; use 'epsilon' para el vacío.");
            List<String> simbolos = new ArrayList<>();
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

    /** @return lista de producciones del NT, o lista vacía si el NT no existe o no tiene producciones. */
    public List<Produccion> getProduccionesDe(String nt) {
        return Collections.unmodifiableList(producciones.getOrDefault(nt, Collections.emptyList()));
    }

    // ---- Getters (vistas inmutables, copia profunda donde aplica) ----

    /** @return el nombre de la gramática (puede ser null). */
    public String getNombre() { return nombre; }

    /** @return vista inmutable de los no terminales declarados. */
    public Set<String> getNoTerminales() { return Collections.unmodifiableSet(noTerminales); }

    /** @return vista inmutable de los terminales declarados. */
    public Set<String> getTerminales() { return Collections.unmodifiableSet(terminales); }

    /** @return NT inicial o {@code null} si nunca se asignó. */
    public String getInicial() { return inicial; }

    /**
     * @return copia **profundamente** inmutable del mapa de producciones
     *         (NT → lista de Producciones). El mapa externo, las listas y las
     *         producciones mismas no pueden ser mutadas por el consumidor.
     */
    public Map<String, List<Produccion>> getProducciones() {
        Map<String, List<Produccion>> copia = new LinkedHashMap<>();
        for (Map.Entry<String, List<Produccion>> e : producciones.entrySet()) {
            copia.put(e.getKey(), Collections.unmodifiableList(new ArrayList<>(e.getValue())));
        }
        return Collections.unmodifiableMap(copia);
    }
}