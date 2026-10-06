/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Menú principal con las 8 opciones del enunciado. Cada botón navega a su
 * pantalla correspondiente mediante el {@link Navegador}.
 */
public class PanelMenu extends JPanel {

    public PanelMenu(Navegador nav) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titulo = new JLabel("Menú principal", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        JPanel grilla = new JPanel(new GridLayout(0, 1, 8, 8));
        grilla.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        grilla.setBackground(new Color(245, 245, 245));

        grilla.add(crearBoton("Crear AFD",                  () -> nav.irA(VentanaPrincipal.CREAR_AFD)));
        grilla.add(crearBoton("Crear Gramática",             () -> nav.irA(VentanaPrincipal.CREAR_GRAM)));
        grilla.add(crearBoton("Evaluar Cadenas",             () -> nav.irA(VentanaPrincipal.EVALUAR)));
        grilla.add(crearBoton("Reportes",                    () -> nav.irA(VentanaPrincipal.REPORTES)));
        grilla.add(crearBoton("Cargar archivo de entrada",   () -> nav.irA(VentanaPrincipal.CARGAR)));
        grilla.add(crearBoton("Guardar",                     () -> nav.irA(VentanaPrincipal.GUARDAR)));
        grilla.add(BotonAyuda.crear(nav));
        grilla.add(crearBoton("Salir",                       () -> System.exit(0)));
        add(grilla, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 16));
        b.addActionListener(e -> accion.run());
        return b;
    }
}