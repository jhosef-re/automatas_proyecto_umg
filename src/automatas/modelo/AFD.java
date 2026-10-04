/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
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
        estado = limpiar(estado, "estado");
        if (estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' ya existe.");
        if (alfabeto.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' es igual a un símbolo del alfabeto.");
        estados.add(estado);
    }

    /** Agrega un símbolo: no repetido y distinto de cualquier estado. */
    public void agregarSimbolo(String simbolo) throws ValidacionException {
        simbolo = limpiar(simbolo, "símbolo");
        if (simbolo.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("epsilon no es válido en un AFD (solo en AFN).");
        if (alfabeto.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' ya existe.");
        if (estados.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' es igual a un estado.");
        alfabeto.add(simbolo);
    }

    /** Define el estado inicial; reemplaza al anterior si ya había uno. */
    public void setEstadoInicial(String estado) throws ValidacionException {
        estado = limpiar(estado, "estado inicial");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        this.estadoInicial = estado;
    }

    public void agregarEstadoAceptacion(String estado) throws ValidacionException {
        estado = limpiar(estado, "estado de aceptación");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        estadosAceptacion.add(estado);
    }

    /** Usado al cargar archivos: la última definición de aceptación gana. */
    public void setAceptacion(String estado, boolean esAceptacion) throws ValidacionException {
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        if (esAceptacion) estadosAceptacion.add(estado);
        else estadosAceptacion.remove(estado);
    }

    /** Agrega una transición verificando que el AFD siga siendo determinista. */
    public void agregarTransicion(String origen, String destino, String simbolo)
            throws ValidacionException {
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

    /** Devuelve el destino o null si no hay transición. */
    public String mover(String estado, String simbolo) {
        Map<String, String> salidas = transiciones.get(estado);
        return salidas == null ? null : salidas.get(simbolo);
    }

    public boolean esAceptacion(String estado) {
        return estadosAceptacion.contains(estado);
    }

    private String limpiar(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El " + campo + " no puede estar vacío.");
        return valor.trim();
    }

    // ---- Getters (copias de solo lectura) ----
    public String getNombre() { return nombre; }
    public Set<String> getEstados() { return Collections.unmodifiableSet(estados); }
    public Set<String> getAlfabeto() { return Collections.unmodifiableSet(alfabeto); }
    public Set<String> getEstadosAceptacion() { return Collections.unmodifiableSet(estadosAceptacion); }
    public String getEstadoInicial() { return estadoInicial; }
    public Map<String, Map<String, String>> getTransiciones() {
        return Collections.unmodifiableMap(transiciones);
    }
}