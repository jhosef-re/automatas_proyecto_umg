/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.servicio.ResultadoEvaluacion;
import automatas.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 *
 * <p>Por defecto lanza la GUI Swing ({@link VentanaPrincipal}). Si se
 * invoca con el argumento {@code --demo}, ejecuta por consola los dos
 * ejemplos del enunciado (AFD con {@code aababb} y gramática con
 * {@code 0011}) y termina.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "--demo".equals(args[0])) {
            demoConsola();
            return;
        }
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    /** Reproduce los dos ejemplos del enunciado y verifica el formato exacto. */
    private static void demoConsola() throws Exception {
        // ---- AFD del enunciado ----
        AFD afd = new AFD("ejemplo");
        for (String s : new String[]{"A", "B", "C", "D"}) afd.agregarEstado(s);
        afd.agregarSimbolo("a");
        afd.agregarSimbolo("b");
        afd.setEstadoInicial("A");
        afd.agregarEstadoAceptacion("D");
        afd.agregarTransicion("A", "A", "a");
        afd.agregarTransicion("A", "C", "b");
        afd.agregarTransicion("B", "A", "a");
        afd.agregarTransicion("B", "C", "b");
        afd.agregarTransicion("C", "B", "a");
        afd.agregarTransicion("C", "D", "b");

        ResultadoEvaluacion rAFD = new EvaluadorAFD(afd).evaluar("aababb");
        System.out.println(rAFD);
        if (!rAFD.esValida() || !rAFD.getDetalle().equals(
                "Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b")) {
            throw new AssertionError("La salida del AFD no coincide con el ejemplo del enunciado.");
        }

        // ---- Gramática (Fase 2) ----
        Gramatica g = new Gramatica("g0011");
        for (String nt : new String[]{"A", "B"}) g.agregarNoTerminal(nt);
        for (String t : new String[]{"0", "1"}) g.agregarTerminal(t);
        g.setInicial("A");
        g.agregarProduccion("A > 0 B");
        g.agregarProduccion("A > 1 A");
        g.agregarProduccion("A > epsilon");
        g.agregarProduccion("B > 0 B");
        g.agregarProduccion("B > 1 A");

        ResultadoEvaluacion rG = new EvaluadorGramatica(g).evaluar("0011");
        System.out.println(rG);
        if (!rG.esValida() || !rG.getDetalle().equals(
                "Expansión Gramática: A> 0B> 00B> 001A> 0011A> 0011(epsilon)> 0011")) {
            throw new AssertionError("La salida de la gramática no coincide con el ejemplo del enunciado.");
        }
    }
}