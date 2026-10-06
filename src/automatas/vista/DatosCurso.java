/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

/**
 * Datos del curso y del equipo mostrados en la portada y en la opción Ayuda.
 * Los valores están completos (sin placeholders).
 */
public final class DatosCurso {

    public static final String CURSO = "Autómatas y Lenguajes Formales";
    public static final String SECCION = "A";
    public static final String CARNE = "9390-24-4816";
    public static final String CATEDRATICO = "Inge. Alan G. Ucelo Morán";

    /** Carnés completos de los integrantes del equipo, en el mismo orden que {@link #NOMBRES}. */
    public static final String[] CARNES = {
        "9390-24-4816",
        "9390-24-7148",
        "9390-24-8224"
    };

    /** Nombres completos de los integrantes del equipo, en el mismo orden que {@link #CARNES}. */
    public static final String[] NOMBRES = {
        "Jhosef Estefano Reyes Román",
        "Alejandro Leiva García",
        "Oscar René Gonzales Rojas"
    };

    private DatosCurso() { }

    /**
     * Texto mostrado por el botón Ayuda en cada panel. Lista el curso, la
     * sección, el catedrático y el último dígito del carné de cada
     * integrante del equipo.
     *
     * @return texto multilínea listo para mostrar en un {@code JOptionPane}
     */
    public static String textoAyuda() {
        StringBuilder sb = new StringBuilder();
        sb.append("Curso: ").append(CURSO).append('\n');
        sb.append("Sección: ").append(SECCION).append('\n');
        sb.append("Catedrático: ").append(CATEDRATICO).append('\n');
        sb.append("Integrantes (último dígito del carné):\n");
        for (int i = 0; i < NOMBRES.length; i++) {
            String ultDig = CARNES[i].substring(CARNES[i].length() - 1);
            sb.append("  • ").append(NOMBRES[i]).append(" → ").append(ultDig).append('\n');
        }
        return sb.toString();
    }

    /**
     * Lista los integrantes del equipo con su carné completo, uno por línea,
     * en el formato {@code "Nombre Apellido (Carné 9390-24-XXXX)"}.
     *
     * <p>Usado por la Portada y por la portada del PDF de reporte, donde se
     * quiere mostrar los 3 integrantes del equipo (no solo el carné principal).
     *
     * @return texto con los 3 integrantes en el orden de
     *         {@link #NOMBRES}/{@link #CARNES}
     */
    public static String nombresCompletos() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NOMBRES.length; i++) {
            sb.append(NOMBRES[i]).append(" (Carné ").append(CARNES[i]).append(')');
            if (i < NOMBRES.length - 1) sb.append('\n');
        }
        return sb.toString();
    }
}