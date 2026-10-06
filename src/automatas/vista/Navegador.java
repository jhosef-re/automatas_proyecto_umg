/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

/**
 * Contrato mínimo que cualquier panel Swing necesita para navegar entre
 * pantallas y mostrar mensajes al usuario. Lo implementa
 * {@link VentanaPrincipal}; permite que los paneles sean testeables sin
 * necesidad de instanciar la {@code JFrame} completa.
 */
public interface Navegador {

    /** Cambia la pantalla visible al nombre registrado en el navegador. */
    void irA(String nombrePantalla);

    /** Muestra un diálogo modal con un mensaje informativo. */
    void mostrarInfo(String titulo, String mensaje);

    /** Muestra un diálogo modal con un mensaje de error (icono de stop). */
    void mostrarError(String titulo, String mensaje);

    /**
     * Pide confirmación SÍ con respuestas (Sí/No), por defecto NO.
     *
     * @return {@code true} si el usuario eligió Sí
     */
    boolean confirmar(String titulo, String mensaje);
}