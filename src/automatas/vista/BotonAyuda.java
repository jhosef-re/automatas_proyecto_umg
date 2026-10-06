/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import java.awt.Font;
import javax.swing.JButton;

/**
 * Helper para crear el botón de Ayuda estándar de la aplicación. Muestra
 * los datos del curso (sección, carné, catedrático) en un diálogo modal.
 *
 * <p>Uso típico:
 * <pre>{@code
 * JButton ayuda = BotonAyuda.crear(navegador);
 * panel.add(ayuda);
 * }</pre>
 */
public final class BotonAyuda {

    private BotonAyuda() {}

    /**
     * @param nav navegador sobre el que se mostrará el diálogo
     * @return botón configurado para abrir la ayuda al hacer clic
     */
    public static JButton crear(Navegador nav) {
        JButton b = new JButton("Ayuda");
        b.setFont(new Font("SansSerif", Font.PLAIN, 16));
        b.addActionListener(e -> nav.mostrarInfo("Ayuda", DatosCurso.textoAyuda()));
        return b;
    }
}