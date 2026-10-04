/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import java.util.ArrayList;
import java.util.List;

/**
 * Evalúa una cadena sobre un AFD. Genera la ruta con el formato del enunciado:
 * "A, A, a; A, C, b; ..."  (origen, destino, símbolo; ...)
 */
public class EvaluadorAFD implements Evaluador {

    private final AFD afd;

    public EvaluadorAFD(AFD afd) {
        this.afd = afd;
    }

    @Override
    public ResultadoEvaluacion evaluar(String cadena) {
        List<String> pasos = new ArrayList<>();
        String actual = afd.getEstadoInicial();

        if (actual == null)
            return new ResultadoEvaluacion(false, "Ruta en AFD: (el AFD no tiene estado inicial)");

        for (char c : cadena.toCharArray()) {
            String simbolo = String.valueOf(c);
            String siguiente = afd.mover(actual, simbolo);
            if (siguiente == null) {
                pasos.add(actual + ", ?, " + simbolo);   // se bloquea: no hay transición
                return new ResultadoEvaluacion(false, "Ruta en AFD: " + String.join("; ", pasos));
            }
            pasos.add(actual + ", " + siguiente + ", " + simbolo);
            actual = siguiente;
        }
        boolean valida = afd.esAceptacion(actual);
        String ruta = pasos.isEmpty() ? "(cadena vacía)" : String.join("; ", pasos);
        return new ResultadoEvaluacion(valida, "Ruta en AFD: " + ruta);
    }
}