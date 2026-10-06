/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.tools;

import automatas.vista.VentanaPrincipal;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * Captura PNG de las 8 pantallas de la app Swing sin interferir con
 * otros diálogos del sistema: posiciona la ventana en una zona "vacía"
 * del display (lejos de cualquier popup del escritorio) y usa
 * {@link java.awt.Robot#createScreenCapture(java.awt.Rectangle)}.
 *
 * <p>Salida: {@code docs/capturas/} (relativo al directorio de trabajo).
 *
 * <p>Uso:
 * <pre>
 *   ./probar.sh compile
 *   java -cp "build/classes:lib/openpdf-1.3.43.jar" \
 *        automatas.tools.CapturadorPantallas [salida]
 * </pre>
 */
public class CapturadorPantallas {

    private static final String[][] PANTALLAS = {
        { "01_portada",          VentanaPrincipal.PORTADA  },
        { "02_menu",             VentanaPrincipal.MENU     },
        { "03_crear_afd",        VentanaPrincipal.CREAR_AFD },
        { "04_crear_gramatica",  VentanaPrincipal.CREAR_GRAM },
        { "05_evaluar",          VentanaPrincipal.EVALUAR  },
        { "06_cargar",           VentanaPrincipal.CARGAR   },
        { "07_guardar",          VentanaPrincipal.GUARDAR  },
        { "08_reportes",         VentanaPrincipal.REPORTES }
    };

    private static final int W = 960;
    private static final int H = 640;
    /** Coordenada donde se posiciona la ventana: esquina superior izquierda
     *  en una zona del display donde no hay otros elementos del escritorio. */
    private static final int OFFSCREEN_X = -2000;
    private static final int OFFSCREEN_Y = -200;

    public static void main(String[] args) throws Exception {
        Path salida = Paths.get(args.length > 0 ? args[0] : "docs/capturas");
        Files.createDirectories(salida);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Robot robot = new Robot();
        robot.setAutoDelay(50);

        VentanaPrincipal[] holder = new VentanaPrincipal[1];
        SwingUtilities.invokeAndWait(() -> {
            VentanaPrincipal v = new VentanaPrincipal();
            v.setSize(W, H);
            v.setLocation(OFFSCREEN_X, OFFSCREEN_Y);
            v.setVisible(true);
            holder[0] = v;
        });
        VentanaPrincipal ventana = holder[0];
        Thread.sleep(800);

        for (String[] e : PANTALLAS) {
            String archivo = e[0];
            String constante = e[1];

            SwingUtilities.invokeAndWait(() -> ventana.irA(constante));
            Thread.sleep(500);

            BufferedImage img = capturar(robot, ventana);
            Path destino = salida.resolve(archivo + ".png");
            ImageIO.write(img, "png", destino.toFile());
            System.out.println("Capturado: " + destino.toAbsolutePath());
        }

        SwingUtilities.invokeAndWait(() -> ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE));
        ventana.dispose();
    }

    /**
     * Captura el contenido de la ventana usando
     * {@link Component#paint(java.awt.Graphics)} sobre un {@link BufferedImage}
     * fuera del frame real. Esto evita que cualquier diálogo del escritorio
     * aparezca en la captura.
     */
    private static BufferedImage capturar(Robot robot, JFrame ventana) throws Exception {
        BufferedImage destino = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = destino.createGraphics();
        try {
            ventana.paintAll(g);
        } finally {
            g.dispose();
        }
        return destino;
    }
}