/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.ConversorAFDGramatica;
import automatas.servicio.ConversorGramaticaAFD;
import automatas.servicio.Evaluador;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.servicio.HistorialEvaluaciones;
import automatas.servicio.RepositorioAutomatas;
import automatas.servicio.ResultadoEvaluacion;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pantalla para evaluar una cadena contra un AFD o una gramática registrada.
 * Muestra la ruta (AFD) o la expansión (gramática) y registra la
 * evaluación en el {@link HistorialEvaluaciones}.
 *
 * <p>Botón adicional: <i>Convertir a...</i>, que aplica la conversión
 * AFD↔Gramática correspondiente y registra el resultado con un sufijo.
 */
public class PanelEvaluar extends JPanel {

    private final Navegador nav;
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtCadena = new JTextField(40);
    private final JTextArea  txtResultado = new JTextArea(10, 60);

    public PanelEvaluar(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(new JLabel("Nombre del modelo:", SwingConstants.LEFT), BorderLayout.WEST);
        cabecera.add(txtNombre, BorderLayout.CENTER);
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        cabecera.add(btnVolver, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        JPanel fila2 = new JPanel(new BorderLayout(5, 5));
        fila2.add(new JLabel("Cadena a evaluar:", SwingConstants.LEFT), BorderLayout.WEST);
        fila2.add(txtCadena, BorderLayout.CENTER);
        add(fila2, BorderLayout.CENTER);

        JPanel filaBotones = new JPanel(new GridLayout(1, 4, 8, 8));
        JButton btnEvaluar = new JButton("Evaluar");
        btnEvaluar.addActionListener(e -> evaluar());
        filaBotones.add(btnEvaluar);

        JButton btnConvertir = new JButton("Convertir a…");
        btnConvertir.addActionListener(e -> convertir());
        filaBotones.add(btnConvertir);

        filaBotones.add(BotonAyuda.crear(nav));
        filaBotones.add(new JLabel());   // placeholder

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(filaBotones, BorderLayout.NORTH);
        txtResultado.setEditable(false);
        txtResultado.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        centro.add(new JScrollPane(txtResultado), BorderLayout.CENTER);
        add(centro, BorderLayout.SOUTH);
    }

    private void evaluar() {
        String nombre = txtNombre.getText().trim();
        String cadena = txtCadena.getText();
        if (nombre.isEmpty()) {
            nav.mostrarError("Falta nombre", "Ingresa el nombre del modelo.");
            return;
        }
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        AFD afd = repo.obtenerAFD(nombre);
        Gramatica g = repo.obtenerGramatica(nombre);
        if (afd == null && g == null) {
            nav.mostrarError("No existe",
                    "No hay un AFD ni una gramática con nombre '" + nombre + "'.");
            return;
        }
        try {
            Evaluador ev;
            if (afd != null) ev = new EvaluadorAFD(afd);
            else            ev = new EvaluadorGramatica(g);
            ResultadoEvaluacion r = ev.evaluar(cadena);
            txtResultado.setText(r.toString());
            HistorialEvaluaciones.getInstancia().registrar(nombre, cadena, r.esValida());
        } catch (IllegalArgumentException ex) {
            nav.mostrarError("Error al evaluar", ex.getMessage());
        } catch (Exception ex) {
            nav.mostrarError("Error al evaluar", ex.getMessage());
        }
    }

    private void convertir() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            nav.mostrarError("Falta nombre", "Ingresa el nombre del modelo a convertir.");
            return;
        }
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        AFD afd = repo.obtenerAFD(nombre);
        Gramatica g = repo.obtenerGramatica(nombre);
        try {
            if (afd != null) {
                Gramatica g2 = new ConversorAFDGramatica(afd).convertir(nombre + "_gram");
                repo.registrar(g2);
                nav.mostrarInfo("Conversión", "Gramática '" + g2.getNombre()
                        + "' creada a partir del AFD '" + nombre + "'.");
                nav.irA(VentanaPrincipal.MENU);
            } else if (g != null) {
                AFD a2 = new ConversorGramaticaAFD(g).convertir(nombre + "_afd");
                repo.registrar(a2);
                nav.mostrarInfo("Conversión", "AFD '" + a2.getNombre()
                        + "' creado a partir de la gramática '" + nombre + "'.");
                nav.irA(VentanaPrincipal.MENU);
            } else {
                nav.mostrarError("No existe",
                        "No hay un AFD ni una gramática con nombre '" + nombre + "'.");
            }
        } catch (Exception ex) {
            nav.mostrarError("Error en conversión", ex.getMessage());
        }
    }
}