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

/**
 * Prueba rápida del modelo: replica los ejemplos del enunciado (AFD con
 * "aababb" y gramática con "0011"). Reemplazar por la lógica del menú
 * principal cuando esté lista la UI.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // ---- Ejemplo AFD del enunciado ----
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

        // ---- Ejemplo Gramática (Fase 2) ----
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