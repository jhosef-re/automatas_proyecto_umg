/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import java.util.*;

/**
 * Registro único de AFDs y gramáticas por nombre (patrón Singleton).
 *
 * <p>El nombre es único entre AFDs y gramáticas: no puede haber un AFD y una
 * gramática con el mismo nombre registrado simultáneamente. Esto simplifica
 * que las pantallas "Evaluar" y "Reportes" busquen por nombre sin distinguir
 * el tipo.
 *
 * <p><b>Thread-safety:</b> la instancia se inicializa atómicamente vía el
 * classloader. Las operaciones de registro/obtención <i>no</i> son
 * thread-safe (uso típico: UI Swing single-threaded).
 */
public class RepositorioAutomatas {

    private static final RepositorioAutomatas INSTANCIA = new RepositorioAutomatas();

    private final Map<String, AFD> afds = new LinkedHashMap<>();
    private final Map<String, Gramatica> gramaticas = new LinkedHashMap<>();

    private RepositorioAutomatas() { }

    /** @return la instancia única del repositorio. */
    public static RepositorioAutomatas getInstancia() { return INSTANCIA; }

    /**
     * Verifica si hay algo registrado con el nombre dado (AFD o gramática).
     *
     * @param nombre nombre a buscar (puede ser null → false)
     * @return {@code true} si existe
     */
    public boolean existe(String nombre) {
        return afds.containsKey(nombre) || gramaticas.containsKey(nombre);
    }

    /**
     * Registra un AFD en el repositorio.
     *
     * @throws ValidacionException si ya hay un AFD o gramática con el mismo nombre.
     */
    public void registrar(AFD afd) throws ValidacionException {
        if (existe(afd.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + afd.getNombre() + "'.");
        afds.put(afd.getNombre(), afd);
    }

    /**
     * Registra una gramática en el repositorio.
     *
     * @throws ValidacionException si ya hay un AFD o gramática con el mismo nombre.
     */
    public void registrar(Gramatica g) throws ValidacionException {
        if (existe(g.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + g.getNombre() + "'.");
        gramaticas.put(g.getNombre(), g);
    }

    /** @return el AFD con ese nombre, o {@code null} si no existe o es una gramática. */
    public AFD obtenerAFD(String nombre) { return afds.get(nombre); }

    /** @return la gramática con ese nombre, o {@code null} si no existe o es un AFD. */
    public Gramatica obtenerGramatica(String nombre) { return gramaticas.get(nombre); }

    /**
     * Elimina todas las entradas registradas. Reservado para uso en tests
     * (<code>@BeforeEach</code>); no invocar en código de producción.
     */
    public void limpiar() {
        afds.clear();
        gramaticas.clear();
    }
}