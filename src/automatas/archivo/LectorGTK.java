/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee una gramática regular desde un archivo con el formato del enunciado:
 * <pre>
 * NT &gt; símbolo1 símbolo2 ... | alternativa | epsilon
 * </pre>
 * Cada línea es una producción (sin el operador {@code |}; para
 * alternativas se repite el NT en varias líneas). <b>Mayúsculas = NT,
 * minúsculas = terminales</b>; el <b>NT inicial</b> es el de la primera
 * línea. La palabra reservada {@code epsilon} representa el vacío.
 *
 * <p>Implementa una <b>pre-pasada</b>: en una primera lectura declara
 * todos los NT y terminales (respetando mayúsculas/minúsculas), y en la
 * segunda pasada invoca {@link Gramatica#agregarProduccion(String)}. Esto
 * resuelve el problema de declarar símbolos que aparecen por primera vez
 * en líneas posteriores.
 *
 * <p>Líneas vacías y comentarios (líneas que empiezan con {@code #}) se
 * ignoran.
 *
 * <p>La gramática resultante recibe el nombre del archivo sin extensión.
 */
public class LectorGTK implements Lector<Gramatica> {

    @Override
    public Gramatica leer(Path archivo) throws IOException, ValidacionException {
        if (archivo == null)
            throw new IllegalArgumentException("La ruta no puede ser null");
        if (!Files.exists(archivo))
            throw new IOException("No existe el archivo: " + archivo);
        if (!archivo.toString().toLowerCase().endsWith(".gtk"))
            throw new ValidacionException("El archivo debe tener extensión .gtk");

        String nombre = archivo.getFileName().toString();
        int punto = nombre.lastIndexOf('.');
        String nombreModelo = (punto >= 0) ? nombre.substring(0, punto) : nombre;

        List<String> lineasUtiles = new ArrayList<>();
        for (String cruda : Files.readAllLines(archivo)) {
            String t = cruda.trim();
            if (!t.isEmpty() && !t.startsWith("#")) lineasUtiles.add(t);
        }
        if (lineasUtiles.isEmpty())
            throw new ValidacionException("El archivo no contiene producciones.");

        Gramatica g = new Gramatica(nombreModelo);

        // Pre-pasada: declarar todos los NT y terminales.
        String primerIzq = null;
        for (String linea : lineasUtiles) {
            int pos = linea.indexOf('>');
            if (pos < 0 || pos != linea.lastIndexOf('>'))
                throw new ValidacionException("Formato inválido (se esperaba un solo '>'): " + linea);
            String izq = linea.substring(0, pos).trim();
            if (primerIzq == null) primerIzq = izq;
            agregarNTsiFalta(g, izq);
            String rhs = linea.substring(pos + 1).trim();
            if (!rhs.equalsIgnoreCase("epsilon")) {
                for (String s : rhs.split("\\s+")) {
                    if (s.isEmpty()) continue;
                    if (esMayuscula(s)) agregarNTsiFalta(g, s);
                    else agregarTerminalSiFalta(g, s);
                }
            }
        }

        g.setInicial(primerIzq);

        // Segunda pasada: agregar producciones (las validaciones del modelo se aplican).
        for (String linea : lineasUtiles) {
            g.agregarProduccion(linea);
        }
        return g;
    }

    private static boolean esMayuscula(String s) {
        return s.codePoints().allMatch(Character::isUpperCase);
    }

    private static void agregarNTsiFalta(Gramatica g, String nt) throws ValidacionException {
        if (g.getNoTerminales().contains(nt)) return;
        g.agregarNoTerminal(nt);
    }

    private static void agregarTerminalSiFalta(Gramatica g, String t) throws ValidacionException {
        if (g.getTerminales().contains(t)) return;
        g.agregarTerminal(t);
    }
}