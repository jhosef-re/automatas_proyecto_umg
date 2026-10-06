/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.modelo.Gramatica;
import automatas.modelo.Produccion;
import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializa una gramática al formato del enunciado:
 * <pre>
 * NT &gt; símbolo1 símbolo2 ...
 * </pre>
 * Una línea por producción (sin operador {@code |}; para alternativas se
 * repite el NT en varias líneas). El vacío se escribe como la palabra
 * reservada {@code epsilon}. El NT inicial es el primero en aparecer
 * (orden de declaración).
 */
public class EscritorGTK implements Escritor<Gramatica> {

    @Override
    public void escribir(Gramatica gramatica, Path archivo)
            throws IOException, ValidacionException {
        if (gramatica == null)
            throw new IllegalArgumentException("La gramática no puede ser null");
        if (archivo == null)
            throw new IllegalArgumentException("La ruta no puede ser null");
        if (!archivo.toString().toLowerCase().endsWith(".gtk"))
            throw new ValidacionException("El archivo debe tener extensión .gtk");
        if (gramatica.getInicial() == null)
            throw new ValidacionException("La gramática no tiene NT inicial.");

        Path padre = archivo.getParent();
        if (padre != null) Files.createDirectories(padre);

        List<String> lineas = new ArrayList<>();
        for (String nt : gramatica.getNoTerminales()) {
            for (Produccion p : gramatica.getProduccionesDe(nt)) {
                StringBuilder sb = new StringBuilder(nt).append('>');
                if (p.esEpsilon()) {
                    sb.append("epsilon");
                } else {
                    sb.append(String.join(" ", p.getDerecho()));
                }
                lineas.add(sb.toString());
            }
        }
        Files.write(archivo, lineas);
    }
}