/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;

/**
 * Convierte un AFD en una gramática regular equivalente.
 *
 * <p>La gramática producida es lineal por la derecha (un NT por estado del
 * AFD). La transformación es:
 * <ul>
 *   <li>cada estado {@code E} del AFD → un NT de la gramática;</li>
 *   <li>cada símbolo del alfabeto → un terminal;</li>
 *   <li>el estado inicial → NT inicial;</li>
 *   <li>cada transición {@code δ(A, a) = B} → producción {@code A > a B};</li>
 *   <li>cada estado de aceptación {@code F} → producción {@code F > epsilon}.</li>
 * </ul>
 *
 * <p>El AFD no necesita ser mínimo ni completo: si tiene estados
 * inalcanzables la gramática resultante tendrá NTs sin producciones, lo cual
 * es sintácticamente válido para {@link Gramatica}.
 *
 * <p>No registra la gramática en {@link RepositorioAutomatas}; eso lo hace
 * la UI (Fase 5).
 */
public class ConversorAFDGramatica {

    private final AFD afd;

    /**
     * @param afd AFD a convertir (no puede ser null)
     * @throws IllegalArgumentException si {@code afd} es null
     */
    public ConversorAFDGramatica(AFD afd) {
        if (afd == null)
            throw new IllegalArgumentException("El AFD no puede ser null");
        this.afd = afd;
    }

    /**
     * @param nombreGramatica nombre de la gramática resultante
     * @return gramática regular derecha-lineal equivalente al AFD
     * @throws ValidacionException si el AFD está vacío, no tiene estado
     *         inicial, o si alguna producción no se puede crear
     */
    public Gramatica convertir(String nombreGramatica) throws ValidacionException {
        if (afd.getEstados().isEmpty())
            throw new ValidacionException("El AFD no tiene estados.");
        if (afd.getAlfabeto().isEmpty())
            throw new ValidacionException("El AFD no tiene símbolos en el alfabeto.");
        if (afd.getEstadoInicial() == null)
            throw new ValidacionException("El AFD no tiene estado inicial.");

        Gramatica g = new Gramatica(nombreGramatica);
        for (String estado : afd.getEstados())
            g.agregarNoTerminal(estado);
        for (String simbolo : afd.getAlfabeto())
            g.agregarTerminal(simbolo);
        g.setInicial(afd.getEstadoInicial());

        for (var entryOrigen : afd.getTransiciones().entrySet()) {
            String origen = entryOrigen.getKey();
            for (var entrySimbolo : entryOrigen.getValue().entrySet()) {
                String simbolo = entrySimbolo.getKey();
                String destino = entrySimbolo.getValue();
                g.agregarProduccion(origen + " > " + simbolo + " " + destino);
            }
        }

        for (String acept : afd.getEstadosAceptacion())
            g.agregarProduccion(acept + " > epsilon");

        return g;
    }
}