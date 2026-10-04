/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import automatas.validacion.ValidacionUtils;
import java.util.*;

/** Autómata Finito Determinista con validaciones del enunciado. */
public class AFD {

    private final String nombre;
    private final Set<String> estados = new LinkedHashSet<>();
    private final Set<String> alfabeto = new LinkedHashSet<>();
    private final Set<String> estadosAceptacion = new LinkedHashSet<>();
    // origen -> (símbolo -> destino)
    private final Map<String, Map<String, String>> transiciones = new LinkedHashMap<>();
    private String estadoInicial;

    public AFD(String nombre) {
        this.nombre = nombre;
    }

    /** Agrega un estado: no repetido y distinto de cualquier símbolo del alfabeto. */
    public void agregarEstado(String estado) throws ValidacionException {
        estado = ValidacionUtils.normalizar(estado, "estado");
        if (estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' ya existe.");
        if (alfabeto.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' es igual a un símbolo del alfabeto.");
        estados.add(estado);
    }

    /** Agrega un símbolo: no repetido y distinto de cualquier estado. */
    public void agregarSimbolo(String simbolo) throws ValidacionException {
        simbolo = ValidacionUtils.normalizar(simbolo, "símbolo");
        if (simbolo.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("epsilon no es válido en un AFD (solo en AFN).");
        if (alfabeto.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' ya existe.");
        if (estados.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' es igual a un estado.");
        alfabeto.add(simbolo);
    }

    /**
     * Define el estado inicial; si ya había uno, lo reemplaza.
     *
     * @throws ValidacionException si el estado es vacío o no fue declarado.
     */
    public void setEstadoInicial(String estado) throws ValidacionException {
        estado = ValidacionUtils.normalizar(estado, "estado inicial");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        this.estadoInicial = estado;
    }

    /** Marca un estado como de aceptación. No-op si ya estaba. */
    public void agregarEstadoAceptacion(String estado) throws ValidacionException {
        estado = ValidacionUtils.normalizar(estado, "estado de aceptación");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        estadosAceptacion.add(estado);
    }

    /** Usado al cargar archivos: la última definición de aceptación gana. */
    public void setAceptacion(String estado, boolean esAceptacion) throws ValidacionException {
        estado = ValidacionUtils.normalizar(estado, "estado");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        if (esAceptacion) estadosAceptacion.add(estado);
        else estadosAceptacion.remove(estado);
    }

    /**
     * Agrega una transición verificando que el AFD siga siendo determinista.
     * Los tres argumentos se normalizan con {@code trim()} antes de validar.
     *
     * @throws ValidacionException si origen/destino no existen, símbolo no
     *         está en el alfabeto, ya hay otra transición con el mismo símbolo
     *         desde el mismo origen (no determinismo), o el símbolo es epsilon.
     */
    public void agregarTransicion(String origen, String destino, String simbolo)
            throws ValidacionException {
        origen = ValidacionUtils.normalizar(origen, "origen");
        destino = ValidacionUtils.normalizar(destino, "destino");
        simbolo = ValidacionUtils.normalizar(simbolo, "símbolo");
        if (simbolo.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("Las transiciones con epsilon solo son posibles en AFN.");
        if (!estados.contains(origen))
            throw new ValidacionException("El estado origen '" + origen + "' no existe.");
        if (!estados.contains(destino))
            throw new ValidacionException("El estado destino '" + destino + "' no existe.");
        if (!alfabeto.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' no está en el alfabeto.");

        Map<String, String> salidas = transiciones.computeIfAbsent(origen, k -> new LinkedHashMap<>());
        if (salidas.containsKey(simbolo))
            throw new ValidacionException("Dos transiciones con el símbolo '" + simbolo
                    + "' desde '" + origen + "' solo son posibles en AFN (no determinísticos).");
        salidas.put(simbolo, destino);
    }

    /** Devuelve el destino de la transición desde {@code estado} con {@code simbolo}, o {@code null} si no hay. */
    public String mover(String estado, String simbolo) {
        Map<String, String> salidas = transiciones.get(estado);
        return salidas == null ? null : salidas.get(simbolo);
    }

    /** Indica si el estado es de aceptación. */
    public boolean esAceptacion(String estado) {
        return estadosAceptacion.contains(estado);
    }

    // ---- Getters (vistas inmutables, copia profunda donde aplica) ----

    /** @return el nombre del AFD (puede ser null si se construyó con null). */
    public String getNombre() { return nombre; }

    /** @return vista inmutable de los estados declarados. */
    public Set<String> getEstados() { return Collections.unmodifiableSet(estados); }

    /** @return vista inmutable del alfabeto. */
    public Set<String> getAlfabeto() { return Collections.unmodifiableSet(alfabeto); }

    /** @return vista inmutable de los estados de aceptación. */
    public Set<String> getEstadosAceptacion() { return Collections.unmodifiableSet(estadosAceptacion); }

    /** @return estado inicial o {@code null} si nunca se asignó. */
    public String getEstadoInicial() { return estadoInicial; }

    /**
     * @return copia **profundamente** inmutable del mapa de transiciones
     *         (origen → símbolo → destino). Ni el mapa externo ni los mapas
     *         internos ni los valores pueden ser mutados por el consumidor.
     */
    public Map<String, Map<String, String>> getTransiciones() {
        Map<String, Map<String, String>> copia = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> e : transiciones.entrySet()) {
            copia.put(e.getKey(), Collections.unmodifiableMap(new LinkedHashMap<>(e.getValue())));
        }
        return Collections.unmodifiableMap(copia);
    }
}