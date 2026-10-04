/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import java.util.*;

/**
 * Una producción: {@code NT > símbolo1 símbolo2 ...}. Si el lado derecho
 * está vacío, representa la producción {@code epsilon}.
 */
public class Produccion {

    private final String izquierdo;
    private final List<String> derecho;

    /**
     * @param izquierdo NT al lado izquierdo (no puede ser null)
     * @param derecho   lista de símbolos al lado derecho; lista vacía = epsilon
     *                  (no puede ser null)
     * @throws IllegalArgumentException si {@code izquierdo} o {@code derecho} son null.
     */
    public Produccion(String izquierdo, List<String> derecho) {
        if (izquierdo == null)
            throw new IllegalArgumentException("izquierdo no puede ser null");
        if (derecho == null)
            throw new IllegalArgumentException("derecho no puede ser null");
        this.izquierdo = izquierdo;
        this.derecho = Collections.unmodifiableList(new ArrayList<>(derecho));
    }

    /** @return el no terminal al lado izquierdo de la producción. */
    public String getIzquierdo() { return izquierdo; }

    /** @return vista inmutable de los símbolos al lado derecho (vacía si es epsilon). */
    public List<String> getDerecho() { return derecho; }

    /** @return {@code true} si la producción representa el vacío (lista derecha vacía). */
    public boolean esEpsilon() { return derecho.isEmpty(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Produccion p)) return false;
        return izquierdo.equals(p.izquierdo) && derecho.equals(p.derecho);
    }

    @Override
    public int hashCode() { return Objects.hash(izquierdo, derecho); }

    @Override
    public String toString() {
        return izquierdo + ">" + (esEpsilon() ? "epsilon" : String.join(" ", derecho));
    }
}