/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import java.util.*;

/** Registro único de AFDs y gramáticas por nombre (Singleton). */
public class RepositorioAutomatas {

    private static final RepositorioAutomatas INSTANCIA = new RepositorioAutomatas();

    private final Map<String, AFD> afds = new LinkedHashMap<>();
    private final Map<String, Gramatica> gramaticas = new LinkedHashMap<>();

    private RepositorioAutomatas() { }

    public static RepositorioAutomatas getInstancia() { return INSTANCIA; }

    /** El nombre es único entre AFDs y gramáticas. */
    public boolean existe(String nombre) {
        return afds.containsKey(nombre) || gramaticas.containsKey(nombre);
    }

    public void registrar(AFD afd) throws ValidacionException {
        if (existe(afd.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + afd.getNombre() + "'.");
        afds.put(afd.getNombre(), afd);
    }

    public void registrar(Gramatica g) throws ValidacionException {
        if (existe(g.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + g.getNombre() + "'.");
        gramaticas.put(g.getNombre(), g);
    }

    public AFD obtenerAFD(String nombre) { return afds.get(nombre); }
    public Gramatica obtenerGramatica(String nombre) { return gramaticas.get(nombre); }

    /** Para tests que necesitan limpiar el estado entre casos. */
    public void limpiar() {
        afds.clear();
        gramaticas.clear();
    }
}