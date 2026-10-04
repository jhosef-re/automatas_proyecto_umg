/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import java.util.*;

/** Una producción: NT > símbolo1 símbolo2 ...  (lista vacía = epsilon). */
public class Produccion {

    private final String izquierdo;
    private final List<String> derecho;

    public Produccion(String izquierdo, List<String> derecho) {
        this.izquierdo = izquierdo;
        this.derecho = Collections.unmodifiableList(new ArrayList<>(derecho));
    }

    public String getIzquierdo() { return izquierdo; }
    public List<String> getDerecho() { return derecho; }
    public boolean esEpsilon() { return derecho.isEmpty(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Produccion)) return false;
        Produccion p = (Produccion) o;
        return izquierdo.equals(p.izquierdo) && derecho.equals(p.derecho);
    }

    @Override
    public int hashCode() { return Objects.hash(izquierdo, derecho); }

    @Override
    public String toString() {
        return izquierdo + ">" + (esEpsilon() ? "epsilon" : String.join(" ", derecho));
    }
}