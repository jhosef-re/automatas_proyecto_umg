/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.archivo.ArchivoFactory;
import automatas.archivo.Escritor;
import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.RepositorioAutomatas;
import automatas.validacion.ValidacionException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Pantalla para guardar un AFD o gramática en un archivo
 * {@code .afd} o {@code .gtk}.
 */
public class PanelGuardar extends JPanel {

    private final Navegador nav;
    private final JList<String> listaModelos;
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JTextField txtRuta = new JTextField(40);

    public PanelGuardar(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        listaModelos = new JList<>(modeloLista);
        listaModelos.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        refrescarLista();

        JPanel seleccion = new JPanel(new BorderLayout());
        seleccion.add(new JLabel("Modelo registrado (doble clic para usar):",
                SwingConstants.LEFT), BorderLayout.NORTH);
        seleccion.add(new JScrollPane(listaModelos), BorderLayout.CENTER);
        listaModelos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String s = listaModelos.getSelectedValue();
                if (s != null) txtRuta.setText(s + extension(s));
            }
        });
        add(seleccion, BorderLayout.NORTH);

        JPanel filaRuta = new JPanel(new BorderLayout());
        filaRuta.add(new JLabel("Destino:", SwingConstants.LEFT), BorderLayout.WEST);
        filaRuta.add(txtRuta, BorderLayout.CENTER);
        JButton btnExaminar = new JButton("Examinar…");
        btnExaminar.addActionListener(e -> examinar());
        filaRuta.add(btnExaminar, BorderLayout.EAST);
        add(filaRuta, BorderLayout.CENTER);

        JPanel pie = new JPanel(new GridLayout(1, 3, 8, 8));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        pie.add(btnGuardar);
        pie.add(BotonAyuda.crear(nav));
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        pie.add(btnVolver);
        add(pie, BorderLayout.SOUTH);
    }

    private void refrescarLista() {
        modeloLista.clear();
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        for (String nombre : repo.getNombres()) {
            String prefijo = (repo.obtenerAFD(nombre) != null) ? "[AFD] " : "[GTK]  ";
            modeloLista.addElement(prefijo + nombre);
        }
    }

    private static String extension(String prefijoNombre) {
        if (prefijoNombre.startsWith("[AFD]")) return ".afd";
        if (prefijoNombre.startsWith("[GTK]")) return ".gtk";
        return ".txt";
    }

    private void examinar() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(
                "Archivos AFD/GTK (*.afd, *.gtk)", "afd", "gtk"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRuta.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void guardar() {
        String seleccion = listaModelos.getSelectedValue();
        if (seleccion == null) {
            nav.mostrarError("Sin selección", "Elige un modelo de la lista.");
            return;
        }
        String nombre = seleccion.substring(4).trim();
        String rutaTxt = txtRuta.getText().trim();
        if (rutaTxt.isEmpty()) {
            nav.mostrarError("Falta ruta", "Indica la ruta destino.");
            return;
        }
        Path ruta = Path.of(rutaTxt);
        RepositorioAutomatas repo = RepositorioAutomatas.getInstancia();
        AFD afd = repo.obtenerAFD(nombre);
        Gramatica g = repo.obtenerGramatica(nombre);
        try {
            Escritor escritor = ArchivoFactory.crearEscritor(ruta);
            if (afd != null) {
                escritor.escribir(afd, ruta);
                nav.mostrarInfo("Guardado",
                        "AFD '" + nombre + "' guardado en " + ruta);
            } else if (g != null) {
                escritor.escribir(g, ruta);
                nav.mostrarInfo("Guardado",
                        "Gramática '" + nombre + "' guardada en " + ruta);
            } else {
                nav.mostrarError("No existe",
                        "No hay un AFD ni una gramática con nombre '" + nombre + "'.");
                return;
            }
            nav.irA(VentanaPrincipal.MENU);
        } catch (ValidacionException ex) {
            nav.mostrarError("Error de validación", ex.getMessage());
        } catch (IOException ex) {
            nav.mostrarError("Error de E/S", ex.getMessage());
        }
    }
}