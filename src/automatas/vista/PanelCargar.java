/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import automatas.archivo.ArchivoFactory;
import automatas.archivo.Lector;
import automatas.servicio.RepositorioAutomatas;
import automatas.validacion.ValidacionException;
import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Pantalla para cargar un AFD o gramática desde un archivo {@code .afd}
 * o {@code .gtk} y registrarlo en el {@link RepositorioAutomatas}.
 */
public class PanelCargar extends JPanel {

    private final Navegador nav;
    private final JTextField txtRuta = new JTextField(40);

    public PanelCargar(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(new JLabel("Ruta del archivo:", SwingConstants.LEFT), BorderLayout.WEST);
        cabecera.add(txtRuta, BorderLayout.CENTER);
        JButton btnExaminar = new JButton("Examinar…");
        btnExaminar.addActionListener(e -> examinar());
        cabecera.add(btnExaminar, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        JPanel pie = new JPanel(new BorderLayout(10, 10));
        JButton btnCargar = new JButton("Cargar y registrar");
        btnCargar.addActionListener(e -> cargar());
        pie.add(btnCargar, BorderLayout.WEST);
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> nav.irA(VentanaPrincipal.MENU));
        pie.add(btnVolver, BorderLayout.CENTER);
        pie.add(BotonAyuda.crear(nav), BorderLayout.EAST);
        add(pie, BorderLayout.SOUTH);
    }

    private void examinar() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(
                "Archivos AFD/GTK (*.afd, *.gtk)", "afd", "gtk"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRuta.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void cargar() {
        String rutaTxt = txtRuta.getText().trim();
        if (rutaTxt.isEmpty()) {
            nav.mostrarError("Falta ruta", "Selecciona un archivo.");
            return;
        }
        Path ruta = Path.of(rutaTxt);
        try {
            Lector lector = ArchivoFactory.crearLector(ruta);
            Object modelo = lector.leer(ruta);
            if (modelo instanceof automatas.modelo.AFD a) {
                RepositorioAutomatas.getInstancia().registrar(a);
                nav.mostrarInfo("Cargado", "AFD '" + a.getNombre()
                        + "' con " + a.getEstados().size() + " estados.");
            } else if (modelo instanceof automatas.modelo.Gramatica g) {
                RepositorioAutomatas.getInstancia().registrar(g);
                nav.mostrarInfo("Cargado", "Gramática '" + g.getNombre()
                        + "' con " + g.getNoTerminales().size() + " NT.");
            } else {
                nav.mostrarError("Tipo desconocido",
                        "El archivo no es un AFD ni una gramática.");
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