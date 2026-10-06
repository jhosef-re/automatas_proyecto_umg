/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Historial global de cadenas evaluadas por el usuario (patrón Singleton).
 *
 * <p>Cada vez que la UI evalúa una cadena contra un AFD o gramática, registra
 * el resultado para que el reporte PDF (Fase 6) pueda incluir tanto las
 * cadenas válidas/inválidas <i>automáticas</i> como las que el usuario
 * probó manualmente.
 *
 * <p><b>Thread-safety:</b> las operaciones están sincronizadas a nivel de
 * método para tolerar evaluaciones concurrentes (p.ej. pruebas).
 */
public class HistorialEvaluaciones {

    private static final HistorialEvaluaciones INSTANCIA = new HistorialEvaluaciones();

    private final List<RegistroEvaluacion> registros = new ArrayList<>();

    private HistorialEvaluaciones() {}

    /** @return la única instancia del historial. */
    public static HistorialEvaluaciones getInstancia() {
        return INSTANCIA;
    }

    /**
     * Registra una evaluación. Si ya existe un registro idéntico
     * (mismo modelo + misma cadena), se conserva el primero.
     *
     * @throws IllegalArgumentException si nombreModelo o cadena son null
     */
    public synchronized void registrar(String nombreModelo, String cadena, boolean valida) {
        if (nombreModelo == null)
            throw new IllegalArgumentException("nombreModelo no puede ser null");
        if (cadena == null)
            throw new IllegalArgumentException("cadena no puede ser null");
        for (RegistroEvaluacion r : registros) {
            if (r.getNombreModelo().equals(nombreModelo) && r.getCadena().equals(cadena))
                return;     // duplicado, no registrar de nuevo
        }
        registros.add(new RegistroEvaluacion(nombreModelo, cadena, valida));
    }

    /** @return vista inmutable de todos los registros en orden de inserción. */
    public synchronized List<RegistroEvaluacion> getTodos() {
        return Collections.unmodifiableList(new ArrayList<>(registros));
    }

    /** Elimina todos los registros del historial. */
    public synchronized void limpiar() {
        registros.clear();
    }
}