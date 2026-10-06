/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.modelo.Gramatica;
import automatas.servicio.RepositorioAutomatas;
import automatas.validacion.ValidacionException;
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
 * Pantalla para crear una gramática regular. El usuario ingresa:
 * <ul>
 *   <li>Nombre.</li>
 *   <li>No terminales (separados por coma).</li>
 *   <li>Terminales (separados por coma).</li>
 *   <li>NT inicial (debe coincidir con uno declarado).</li>
 *   <li>Producciones, una por línea con formato
 *       {@code NT > simbolos separados por espacio}. El vacío se escribe
 *       como la palabra reservada {@code epsilon}.</li>
 * </ul>
 */
public class PanelCrearGramatica extends JPanel {

    private final Navegador nav;
    private final JTextField txtNombre     = new JTextField(20);
    private final JTextField txtNT         = new JTextField(40);
    private final JTextField txtTerminales = new JTextField(40);
    private final JTextField txtInicial    = new JTextField(10);
    private final JTextArea  txtProd       = new JTextArea(10, 50);

    public PanelCrearGramatica(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Cabecera: nombre + volver ----
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(new JLabel("Nombre:", SwingConstants.LEFT), BorderLayout.WEST);
        cabecera.add(txtNombre, BorderLayout.CENTER);
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        cabecera.add(btnVolver, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        // ---- Centro: campos + producciones ----
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(3, 2, 5, 5));
        campos.add(new JLabel("No terminales (separados por coma):"));
        campos.add(txtNT);
        campos.add(new JLabel("Terminales (separados por coma):"));
        campos.add(txtTerminales);
        campos.add(new JLabel("NT inicial:"));
        campos.add(txtInicial);
        centro.add(campos, BorderLayout.NORTH);

        JPanel bloqueProds = new JPanel(new BorderLayout(5, 5));
        bloqueProds.add(new JLabel("Producciones (una por línea: NT > símbolos | ε):"),
                BorderLayout.NORTH);
        txtProd.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        bloqueProds.add(new JScrollPane(txtProd), BorderLayout.CENTER);
        centro.add(bloqueProds, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // ---- Pie: crear + ayuda ----
        JPanel pie = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton btnCrear = new JButton("Crear gramática");
        btnCrear.addActionListener(e -> crear());
        pie.add(btnCrear);
        pie.add(BotonAyuda.crear(nav));
        add(pie, BorderLayout.SOUTH);
    }

    private void crear() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) throw new ValidacionException("Ingresa un nombre.");
            Gramatica g = new Gramatica(nombre);
            for (String nt : txtNT.getText().split(",")) {
                String t = nt.trim();
                if (!t.isEmpty()) g.agregarNoTerminal(t);
            }
            for (String term : txtTerminales.getText().split(",")) {
                String t = term.trim();
                if (!t.isEmpty()) g.agregarTerminal(t);
            }
            String ini = txtInicial.getText().trim();
            if (!ini.isEmpty()) g.setInicial(ini);
            for (String linea : txtProd.getText().split("\\R")) {
                String l = linea.trim();
                if (l.isEmpty() || l.startsWith("#")) continue;
                g.agregarProduccion(l);
            }
            RepositorioAutomatas.getInstancia().registrar(g);
            nav.mostrarInfo("Gramática creada",
                    "Gramática '" + nombre + "' registrada con "
                    + g.getNoTerminales().size() + " NT y "
                    + g.getTerminales().size() + " terminales.");
            nav.irA(VentanaPrincipal.MENU);
        } catch (ValidacionException ex) {
            nav.mostrarError("Error de validación", ex.getMessage());
        }
    }
}