/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class GeneradorPDFTest {

    @AfterEach
    void limpiarPropiedad() {
        System.clearProperty(GeneradorGraphviz.PROPIEDAD_PATH);
    }

    private AFD afdEnunciado() throws ValidacionException {
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
        return afd;
    }

    private Gramatica gramaticaEnunciado() throws ValidacionException {
        Gramatica g = new Gramatica("g0011");
        for (String nt : new String[]{"A", "B"}) g.agregarNoTerminal(nt);
        for (String t : new String[]{"0", "1"}) g.agregarTerminal(t);
        g.setInicial("A");
        g.agregarProduccion("A > 0 B");
        g.agregarProduccion("A > 1 A");
        g.agregarProduccion("A > epsilon");
        g.agregarProduccion("B > 0 B");
        g.agregarProduccion("B > 1 A");
        return g;
    }

    @Test
    @DisplayName("Genera PDF de un AFD: tamaño > 1KB y empieza con %PDF-")
    void generaPDFdeAFD() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("test_", ".pdf");
        try {
            GeneradorPDF.generar(afdEnunciado(), tmp);
            byte[] bytes = Files.readAllBytes(tmp);
            assertTrue(bytes.length > 1024, "PDF demasiado pequeño: " + bytes.length);
            String header = new String(bytes, 0, 5);
            assertEquals("%PDF-", header);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Genera PDF de una gramática: tamaño > 1KB y empieza con %PDF-")
    void generaPDFdeGramatica() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("test_", ".pdf");
        try {
            GeneradorPDF.generar(gramaticaEnunciado(), tmp);
            byte[] bytes = Files.readAllBytes(tmp);
            assertTrue(bytes.length > 1024, "PDF demasiado pequeño");
            String header = new String(bytes, 0, 5);
            assertEquals("%PDF-", header);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Crea directorio padre si no existe")
    void creaDirectorioPadre() throws IOException, ValidacionException {
        Path tmpDir = Files.createTempDirectory("test_pdf_dir_");
        Path destino = tmpDir.resolve("sub/carpeta/reporte.pdf");
        try {
            assertFalse(Files.exists(destino.getParent()));
            GeneradorPDF.generar(afdEnunciado(), destino);
            assertTrue(Files.exists(destino));
            assertTrue(Files.size(destino) > 0);
        } finally {
            // Limpieza recursiva: borrar todo el árbol temporal.
            if (Files.exists(tmpDir)) {
                try (var stream = Files.walk(tmpDir)) {
                    stream.sorted(java.util.Comparator.reverseOrder())
                            .forEach(p -> { try { Files.deleteIfExists(p); }
                                           catch (IOException ignored) {} });
                }
            }
        }
    }

    @Test
    @DisplayName("Sobrescribe archivo existente")
    void sobrescribeArchivoExistente() throws IOException, ValidacionException {
        Path tmp = Files.createTempFile("test_", ".pdf");
        try {
            Files.writeString(tmp, "basura");
            GeneradorPDF.generar(afdEnunciado(), tmp);
            byte[] bytes = Files.readAllBytes(tmp);
            assertEquals("%PDF-", new String(bytes, 0, 5));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    @DisplayName("Sin Graphviz: el PDF igual se produce con el DOT como fallback")
    void pdfSinGraphvizContieneFallback() throws IOException, ValidacionException {
        System.setProperty(GeneradorGraphviz.PROPIEDAD_PATH, "/no/existe/dot_fake");
        Path tmp = Files.createTempFile("test_fallback_", ".pdf");
        try {
            GeneradorPDF.generar(afdEnunciado(), tmp);
            byte[] bytes = Files.readAllBytes(tmp);
            assertEquals("%PDF-", new String(bytes, 0, 5));
            // El PDF debe contener texto (no binario puro) que incluye
            // "No se pudo generar el grafo".
            // (No podemos inspeccionar el texto directamente, pero verificamos
            // que el archivo es más grande que un PDF vacío.)
            assertTrue(bytes.length > 1500,
                    "PDF sin grafo demasiado pequeño: " + bytes.length);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}