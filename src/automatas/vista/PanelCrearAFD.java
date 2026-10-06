/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.modelo.AFD;
import automatas.servicio.RepositorioAutomatas;
import automatas.util.ParserModo1AFD;
import automatas.util.ParserModo2AFD;
import automatas.validacion.ValidacionException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pantalla para crear un AFD por nombre. Soporta dos modos de ingreso de
 * transiciones:
 * <ul>
 *   <li><b>Modo 1</b>: una línea por transición
 *       {@code origen,destino,simbolo;bool,bool}.</li>
 *   <li><b>Modo 2</b>: matriz con línea de alfabeto, línea de estados
 *       (con aceptación opcional separada por {@code ;}) y una línea de
 *       destinos por terminal para cada estado.</li>
 * </ul>
 *
 * <p>El AFD creado se registra en {@link RepositorioAutomatas} y vuelve al
 * menú principal.
 */
public class PanelCrearAFD extends JPanel {

    private final Navegador nav;
    private final JTextField txtNombre = new JTextField(20);
    private final JTextArea txtModo1 = new JTextArea(12, 50);
    private final JTextArea txtModo2 = new JTextArea(12, 50);

    public PanelCrearAFD(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Cabecera: nombre + volver ----
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(new JLabel("Nombre del AFD:", SwingConstants.LEFT), BorderLayout.WEST);
        cabecera.add(txtNombre, BorderLayout.CENTER);
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        cabecera.add(btnVolver, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        // ---- Tabs modo 1 / modo 2 ----
        JTabbedPane tabs = new JTabbedPane();

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Una línea por transición: origen,destino,simbolo;bool,bool"),
                BorderLayout.NORTH);
        txtModo1.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        p1.add(new JScrollPane(txtModo1), BorderLayout.CENTER);
        tabs.addTab("Modo 1 (líneas)", p1);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(new JLabel(
                "Línea 1: alfabeto. Línea 2: estados (con ';' + aceptación). "
                + "Siguientes: destinos por terminal. Use '-' o vacío para sin transición.",
                SwingConstants.LEFT), BorderLayout.NORTH);
        txtModo2.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        p2.add(new JScrollPane(txtModo2), BorderLayout.CENTER);
        tabs.addTab("Modo 2 (matriz)", p2);

        add(tabs, BorderLayout.CENTER);

        // ---- Pie: crear + ayuda ----
        JPanel pie = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton btnCrear = new JButton("Crear AFD");
        btnCrear.addActionListener(e -> crear());
        pie.add(btnCrear);
        pie.add(BotonAyuda.crear(nav));
        add(pie, BorderLayout.SOUTH);
    }

    private void crear() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            nav.mostrarError("Falta nombre", "Ingresa un nombre para el AFD.");
            return;
        }
        AFD afd = new AFD(nombre);
        try {
            List<String> lineasModo1 = noVacias(txtModo1.getText());
            List<String> lineasModo2 = noVacias(txtModo2.getText());
            if (!lineasModo1.isEmpty() && !lineasModo2.isEmpty()) {
                nav.mostrarError("Modo doble",
                        "Use solo uno de los dos modos (líneas o matriz).");
                return;
            }
            if (lineasModo1.isEmpty() && lineasModo2.isEmpty()) {
                nav.mostrarError("Sin transiciones",
                        "Ingresa al menos una transición en uno de los dos modos.");
                return;
            }
            if (!lineasModo1.isEmpty()) {
                int n = 0;
                for (String l : lineasModo1) {
                    n++;
                    ParserModo1AFD.parsear(l, afd, n);
                }
                if (afd.getEstadoInicial() == null)
                    throw new ValidacionException(
                            "No se pudo inferir el estado inicial (¿ninguna transición?).");
                afd.setEstadoInicial(afd.getEstados().iterator().next());
            } else {
                ParserModo2AFD.parsear(lineasModo2, afd);
            }
            RepositorioAutomatas.getInstancia().registrar(afd);
            nav.mostrarInfo("AFD creado",
                    "AFD '" + nombre + "' registrado con "
                    + afd.getEstados().size() + " estados y "
                    + afd.getAlfabeto().size() + " símbolos.");
            nav.irA(VentanaPrincipal.MENU);
        } catch (ValidacionException ex) {
            nav.mostrarError("Error de validación", ex.getMessage());
        }
    }

    private static List<String> noVacias(String texto) {
        return Arrays.stream(texto.split("\\R"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}