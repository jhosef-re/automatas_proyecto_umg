/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas;

import automatas.modelo.AFD;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.ResultadoEvaluacion;

/**
 * Prueba rápida del modelo: replica el ejemplo del enunciado (aababb).
 * Reemplazar por la lógica del menú principal cuando esté lista la UI.
 */
public class Main {

    public static void main(String[] args) throws Exception {
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

        ResultadoEvaluacion r = new EvaluadorAFD(afd).evaluar("aababb");
        System.out.println(r);
        if (!r.esValida() || !r.getDetalle().equals(
                "Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b")) {
            throw new AssertionError("La salida no coincide con el ejemplo del enunciado.");
        }
    }
}