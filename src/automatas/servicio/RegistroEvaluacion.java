/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

/**
 * Registro inmutable de una cadena evaluada por el usuario durante la sesión.
 * Lo usa la Fase 6 (reporte PDF) para incluir tanto las cadenas válidas/
 * inválidas generadas automáticamente como las que el usuario probó.
 */
public final class RegistroEvaluacion {

    private final String nombreModelo;
    private final String cadena;
    private final boolean valida;

    /**
     * @param nombreModelo nombre del AFD o gramática evaluado
     * @param cadena       cadena evaluada
     * @param valida       resultado de la evaluación
     * @throws IllegalArgumentException si nombreModelo o cadena son null
     */
    public RegistroEvaluacion(String nombreModelo, String cadena, boolean valida) {
        if (nombreModelo == null)
            throw new IllegalArgumentException("nombreModelo no puede ser null");
        if (cadena == null)
            throw new IllegalArgumentException("cadena no puede ser null");
        this.nombreModelo = nombreModelo;
        this.cadena = cadena;
        this.valida = valida;
    }

    /** @return el nombre del AFD o gramática evaluado. */
    public String getNombreModelo() { return nombreModelo; }

    /** @return la cadena que fue evaluada. */
    public String getCadena() { return cadena; }

    /** @return {@code true} si la cadena es aceptada por el modelo. */
    public boolean esValida() { return valida; }

    /**
     * @return representación textual con formato
     *         {@code "modelo :: 'cadena' -> VÁLIDA/INVÁLIDA"}.
     */
    @Override
    public String toString() {
        return nombreModelo + " :: '" + cadena + "' -> " + (valida ? "VÁLIDA" : "INVÁLIDA");
    }
}