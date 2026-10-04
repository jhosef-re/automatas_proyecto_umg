/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

/** Datos mostrados en la portada y en la opción Ayuda. TODO: completar con los reales. */
public final class DatosCurso {
    public static final String CURSO = "Autómatas y Lenguajes Formales";
    public static final String SECCION = "<sección>";
    public static final String CARNE = "<carné>";
    public static final String CATEDRATICO = "<nombre del catedrático>";

    private DatosCurso() { }

    public static String textoAyuda() {
        String ultimo = CARNE.isEmpty() ? "?" : CARNE.substring(CARNE.length() - 1);
        return "Curso: " + CURSO
             + "\nCatedrático: " + CATEDRATICO
             + "\nÚltimo dígito del carné: " + ultimo;
    }
}