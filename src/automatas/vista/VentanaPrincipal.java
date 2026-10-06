/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Ventana única de la aplicación. Cambia de pantalla con
 * {@link CardLayout}. Implementa {@link Navegador} para que los paneles
 * no necesiten referenciar la {@code JFrame} directamente.
 *
 * <p>Las constantes de pantalla ({@link #PORTADA}, {@link #MENU},
 * {@link #CREAR_AFD}, etc.) son los nombres registrados en el
 * {@code CardLayout}.
 */
public class VentanaPrincipal extends JFrame implements Navegador {

    public static final String PORTADA       = "portada";
    public static final String MENU          = "menu";
    public static final String CREAR_AFD     = "crear_afd";
    public static final String CREAR_GRAM    = "crear_gramatica";
    public static final String EVALUAR       = "evaluar";
    public static final String CARGAR        = "cargar";
    public static final String GUARDAR       = "guardar";
    public static final String REPORTES      = "reportes";

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);

    public VentanaPrincipal() {
        super("Proyecto Autómatas — " + DatosCurso.CURSO);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 640);
        setMinimumSize(new java.awt.Dimension(800, 560));
        setLocationRelativeTo(null);

        contenedor.add(new PanelPortada(this),         PORTADA);
        contenedor.add(new PanelMenu(this),            MENU);
        contenedor.add(new PanelCrearAFD(this),        CREAR_AFD);
        contenedor.add(new PanelCrearGramatica(this), CREAR_GRAM);
        contenedor.add(new PanelEvaluar(this),         EVALUAR);
        contenedor.add(new PanelCargar(this),          CARGAR);
        contenedor.add(new PanelGuardar(this),         GUARDAR);
        contenedor.add(new PanelReportes(this),        REPORTES);

        add(contenedor);
        irA(PORTADA);
    }

    /** {@inheritDoc} */
    @Override
    public void irA(String nombrePantalla) {
        cartas.show(contenedor, nombrePantalla);
    }

    /** Registra una pantalla adicional (uso interno por los paneles). */
    public void agregarPantalla(String nombre, JPanel panel) {
        contenedor.add(panel, nombre);
    }

    /** {@inheritDoc} */
    @Override
    public void mostrarInfo(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /** {@inheritDoc} */
    @Override
    public void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    /** {@inheritDoc} */
    @Override
    public boolean confirmar(String titulo, String mensaje) {
        int r = JOptionPane.showConfirmDialog(this, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }
}