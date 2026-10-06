/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.modelo.Produccion;
import automatas.servicio.RepositorioAutomatas;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Pantalla de reportes. En Fase 5 solo implementa <b>Ver Detalle</b>:
 * muestra en un {@code JTextArea} la información del modelo seleccionado
 * (alfabeto, estados, inicial, aceptación, transiciones para AFD;
 * NT, terminales, inicial, producciones para gramática).
 *
 * <p>El botón "Generar PDF" está deshabilitado y se implementará en Fase 6.
 */
public class PanelReportes extends JPanel {

    private final Navegador nav;
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JList<String> listaModelos = new JList<>(modeloLista);
    private final JTextArea txtDetalle = new JTextArea(20, 60);

    public PanelReportes(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        refrescarLista();
        listaModelos.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        listaModelos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalle();
        });

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.add(new JLabel("Modelos registrados:", SwingConstants.LEFT),
                BorderLayout.NORTH);
        izquierda.add(new JScrollPane(listaModelos), BorderLayout.CENTER);
        add(izquierda, BorderLayout.WEST);

        JPanel derecha = new JPanel(new BorderLayout());
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        derecha.add(new JScrollPane(txtDetalle), BorderLayout.CENTER);

        JPanel pie = new JPanel(new GridLayout(1, 4, 8, 8));
        JButton btnVer = new JButton("Ver Detalle");
        btnVer.addActionListener(e -> mostrarDetalle());
        pie.add(btnVer);
        JButton btnPdf = new JButton("Generar PDF (próximamente)");
        btnPdf.setEnabled(false);
        pie.add(btnPdf);
        pie.add(BotonAyuda.crear(nav));
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        pie.add(btnVolver);
        derecha.add(pie, BorderLayout.SOUTH);
        add(derecha, BorderLayout.CENTER);
    }

    private void refrescarLista() {
        modeloLista.clear();
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        for (String nombre : repo.getNombres()) {
            String prefijo = (repo.obtenerAFD(nombre) != null) ? "[AFD] " : "[GTK]  ";
            modeloLista.addElement(prefijo + nombre);
        }
    }

    private void mostrarDetalle() {
        String sel = listaModelos.getSelectedValue();
        if (sel == null) {
            txtDetalle.setText("");
            return;
        }
        String nombre = sel.substring(4).trim();
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        AFD afd = repo.obtenerAFD(nombre);
        if (afd != null) {
            txtDetalle.setText(detalleAfd(afd));
            return;
        }
        Gramatica g = repo.obtenerGramatica(nombre);
        if (g != null) {
            txtDetalle.setText(detalleGramatica(g));
            return;
        }
        txtDetalle.setText("(modelo no encontrado)");
    }

    private static String detalleAfd(AFD afd) {
        StringBuilder sb = new StringBuilder();
        sb.append("AFD: ").append(afd.getNombre()).append('\n');
        sb.append("Estados (").append(afd.getEstados().size()).append("): ")
                .append(afd.getEstados()).append('\n');
        sb.append("Alfabeto (").append(afd.getAlfabeto().size()).append("): ")
                .append(afd.getAlfabeto()).append('\n');
        sb.append("Estado inicial: ").append(afd.getEstadoInicial()).append('\n');
        sb.append("Aceptación (").append(afd.getEstadosAceptacion().size()).append("): ")
                .append(afd.getEstadosAceptacion()).append("\n\n");
        sb.append("Transiciones:\n");
        afd.getTransiciones().forEach((o, m) -> m.forEach((s, d) ->
                sb.append("  δ(").append(o).append(", ").append(s)
                        .append(") = ").append(d).append('\n')));
        return sb.toString();
    }

    private static String detalleGramatica(Gramatica g) {
        StringBuilder sb = new StringBuilder();
        sb.append("Gramática: ").append(g.getNombre()).append('\n');
        sb.append("No terminales (").append(g.getNoTerminales().size()).append("): ")
                .append(g.getNoTerminales()).append('\n');
        sb.append("Terminales (").append(g.getTerminales().size()).append("): ")
                .append(g.getTerminales()).append('\n');
        sb.append("NT inicial: ").append(g.getInicial()).append("\n\n");
        sb.append("Producciones:\n");
        g.getNoTerminales().forEach(nt -> {
            for (Produccion p : g.getProduccionesDe(nt)) {
                sb.append("  ").append(p).append('\n');
            }
        });
        return sb.toString();
    }
}