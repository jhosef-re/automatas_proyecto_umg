/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.reporte;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.modelo.Produccion;
import automatas.servicio.Evaluador;
import automatas.servicio.EvaluadorAFD;
import automatas.servicio.EvaluadorGramatica;
import automatas.servicio.GeneradorCadenas;
import automatas.servicio.HistorialEvaluaciones;
import automatas.servicio.RegistroEvaluacion;
import automatas.validacion.ValidacionException;
import automatas.vista.DatosCurso;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * Genera el reporte PDF de un AFD o una gramática usando OpenPDF
 * ({@code com.lowagie.text.*}).
 *
 * <p>Contenido:
 * <ol>
 *   <li>Portada: título, nombre del modelo, datos del curso.</li>
 *   <li>Detalle del modelo (alfabeto / estados / NT / producciones /
 *       transiciones).</li>
 *   <li>Grafo (si Graphviz está disponible) o fallback con el DOT como
 *       texto.</li>
 *   <li>Cadenas válidas (≥3) e inválidas (≥3) generadas por
 *       {@link GeneradorCadenas}, más todas las cadenas evaluadas
 *       durante la sesión (vía {@link HistorialEvaluaciones}).</li>
 * </ol>
 *
 * <p>Si Graphviz falla, el PDF se genera igual (degradación elegante).
 */
public final class GeneradorPDF {

    private static final int LONGITUD_MAX_BFS = 4;
    private static final int CANTIDAD_CADENAS  = 3;

    private GeneradorPDF() {}

    public static void generar(AFD afd, Path destino) throws IOException {
        Path padre = destino.getParent();
        if (padre != null) Files.createDirectories(padre);
        Document doc = new Document(PageSize.A4, 40, 40, 50, 50);
        try (FileOutputStream fos = new FileOutputStream(destino.toFile())) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            escribirPortada(doc, "AFD", afd.getNombre());
            escribirDetalleAfd(doc, afd);
            escribirGrafo(doc, GeneradorDot.generar(afd),
                    "AFD_" + sanitize(afd.getNombre()));
            escribirCadenas(doc, new EvaluadorAFD(afd), afd.getNombre());

            doc.close();
        } catch (com.lowagie.text.DocumentException ex) {
            throw new IOException("Error al generar PDF: " + ex.getMessage(), ex);
        }
    }

    public static void generar(Gramatica g, Path destino) throws IOException {
        Path padre = destino.getParent();
        if (padre != null) Files.createDirectories(padre);
        Document doc = new Document(PageSize.A4, 40, 40, 50, 50);
        try (FileOutputStream fos = new FileOutputStream(destino.toFile())) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            escribirPortada(doc, "Gramática", g.getNombre());
            escribirDetalleGramatica(doc, g);
            try {
                escribirCadenas(doc, new EvaluadorGramatica(g), g.getNombre());
            } catch (ValidacionException ve) {
                escribirCadenasVacias(doc,
                        "Cadenas no disponibles: gramática no es derecha pura ("
                        + ve.getMessage() + ")");
            }

            doc.close();
        } catch (com.lowagie.text.DocumentException ex) {
            throw new IOException("Error al generar PDF: " + ex.getMessage(), ex);
        }
    }

    // ========== secciones ==========

    private static void escribirPortada(Document doc, String tipo, String nombre)
            throws com.lowagie.text.DocumentException {
        Font h1 = new Font(Font.HELVETICA, 22, Font.BOLD);
        Font h2 = new Font(Font.HELVETICA, 14, Font.NORMAL);

        Paragraph titulo = new Paragraph("Reporte — " + tipo, h1);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        Paragraph pNombre = new Paragraph("Modelo: " + nombre, h2);
        pNombre.setAlignment(Element.ALIGN_CENTER);
        doc.add(pNombre);

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));

        Paragraph curso = new Paragraph(DatosCurso.CURSO, h2);
        curso.setAlignment(Element.ALIGN_CENTER);
        doc.add(curso);
        doc.add(new Paragraph("Sección: " + DatosCurso.SECCION, h2));
        doc.add(new Paragraph("Carné: " + DatosCurso.CARNE, h2));
        doc.add(new Paragraph("Catedrático: " + DatosCurso.CATEDRATICO, h2));
        doc.add(new Paragraph("Fecha: " + LocalDate.now(), h2));

        doc.newPage();
    }

    private static void escribirDetalleAfd(Document doc, AFD afd)
            throws com.lowagie.text.DocumentException {
        Font h2 = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font mono = new Font(Font.COURIER, 10, Font.NORMAL);

        doc.add(encabezadoConTitulo("Detalle del AFD"));
        doc.add(linea("Nombre: " + afd.getNombre(), normal));
        doc.add(linea("Estados (" + afd.getEstados().size() + "): "
                + String.join(", ", afd.getEstados()), normal));
        doc.add(linea("Alfabeto (" + afd.getAlfabeto().size() + "): "
                + String.join(", ", afd.getAlfabeto()), normal));
        doc.add(linea("Estado inicial: " + afd.getEstadoInicial(), normal));
        doc.add(linea("Aceptación (" + afd.getEstadosAceptacion().size() + "): "
                + String.join(", ", afd.getEstadosAceptacion()), normal));
        doc.add(new Paragraph(" "));
        doc.add(linea("Transiciones:", h2));
        afd.getTransiciones().forEach((o, m2) ->
                        m2.forEach((s, d) ->
                                linea("  δ(" + o + ", " + s + ") = " + d, mono)));
        doc.newPage();
    }

    private static void escribirDetalleGramatica(Document doc, Gramatica g)
            throws com.lowagie.text.DocumentException {
        Font h2 = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font mono = new Font(Font.COURIER, 10, Font.NORMAL);

        doc.add(encabezadoConTitulo("Detalle de la gramática"));
        doc.add(linea("Nombre: " + g.getNombre(), normal));
        doc.add(linea("No terminales (" + g.getNoTerminales().size() + "): "
                + String.join(", ", g.getNoTerminales()), normal));
        doc.add(linea("Terminales (" + g.getTerminales().size() + "): "
                + String.join(", ", g.getTerminales()), normal));
        doc.add(linea("NT inicial: " + g.getInicial(), normal));
        doc.add(new Paragraph(" "));
        doc.add(linea("Producciones:", h2));
        g.getNoTerminales().forEach(nt -> {
            for (Produccion p : g.getProduccionesDe(nt)) {
                linea("  " + p, mono);
            }
        });
        doc.newPage();
    }

    private static void escribirGrafo(Document doc, String dot, String nombreSugerido)
            throws com.lowagie.text.DocumentException {
        Font h2 = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font mono = new Font(Font.COURIER, 9, Font.NORMAL);

        doc.add(encabezadoConTitulo("Grafo"));
        try {
            byte[] png = GeneradorGraphviz.renderizar(dot);
            Image img = Image.getInstance(png);
            // Escalar al ancho útil (A4 con márgenes 40+40 = 515 pts).
            float maxAncho = PageSize.A4.getWidth() - 80;
            if (img.getScaledWidth() > maxAncho) {
                img.scaleToFit(maxAncho, PageSize.A4.getHeight() - 100);
            }
            img.setAlignment(Element.ALIGN_CENTER);
            doc.add(img);
        } catch (java.io.IOException | ExcepcionReporte ex) {
            doc.add(linea("No se pudo generar el grafo:", h2));
            doc.add(linea(ex.getMessage(), normal));
            doc.add(linea("Se incluye el DOT como texto de fallback:", normal));
            doc.add(new Paragraph(" "));
            for (String l : dot.split("\\R")) {
                doc.add(linea(l, mono));
            }
        }
        doc.newPage();
    }

    private static void escribirCadenas(Document doc, Evaluador ev, String nombreModelo)
            throws com.lowagie.text.DocumentException {
        Font h2 = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font mono = new Font(Font.COURIER, 10, Font.NORMAL);

        java.util.Set<String> alfabeto = alfabetoDe(ev);
        doc.add(encabezadoConTitulo("Cadenas de ejemplo"));

        GeneradorCadenas gen = new GeneradorCadenas(ev, alfabeto);

        List<String> validas = gen.generarValidas(CANTIDAD_CADENAS, LONGITUD_MAX_BFS);
        List<String> invalidas = gen.generarInvalidas(CANTIDAD_CADENAS, LONGITUD_MAX_BFS);

        doc.add(linea("Válidas (≥ " + CANTIDAD_CADENAS + ", BFS longitud ≤ "
                + LONGITUD_MAX_BFS + "):", h2));
        if (validas.isEmpty()) doc.add(linea("  (ninguna encontrada)", normal));
        else for (String c : validas) doc.add(linea("  ✓ " + c, mono));

        doc.add(linea("Inválidas (≥ " + CANTIDAD_CADENAS + ", BFS longitud ≤ "
                + LONGITUD_MAX_BFS + "):", h2));
        if (invalidas.isEmpty()) doc.add(linea("  (ninguna encontrada)", normal));
        else for (String c : invalidas) doc.add(linea("  ✗ " + c, mono));

        doc.add(new Paragraph(" "));
        doc.add(linea("Evaluadas durante la sesión:", h2));
        List<RegistroEvaluacion> regs = filtrarHistorial(nombreModelo);
        if (regs.isEmpty()) doc.add(linea("  (sin historial)", normal));
        else for (RegistroEvaluacion r : regs) {
            doc.add(linea("  " + (r.esValida() ? "✓" : "✗") + " '" + r.getCadena() + "'",
                    mono));
        }
    }

    private static void escribirCadenasVacias(Document doc, String motivo)
            throws com.lowagie.text.DocumentException {
        Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);
        doc.add(encabezadoConTitulo("Cadenas de ejemplo"));
        doc.add(linea(motivo, normal));
    }

    // ========== helpers ==========

    private static Paragraph encabezadoConTitulo(String titulo) {
        Font h = new Font(Font.HELVETICA, 16, Font.BOLD);
        h.setColor(new Color(30, 41, 59));
        Paragraph p = new Paragraph(titulo, h);
        p.setAlignment(Element.ALIGN_LEFT);
        return p;
    }

    private static Paragraph linea(String texto, Font f) {
        return new Paragraph(texto == null ? "" : texto, f);
    }

    private static java.util.Set<String> alfabetoDe(Evaluador ev) {
        if (ev instanceof EvaluadorAFD e) return e.getAfd().getAlfabeto();
        if (ev instanceof EvaluadorGramatica e) return e.getGramatica().getTerminales();
        return java.util.Collections.emptySet();
    }

    private static List<RegistroEvaluacion> filtrarHistorial(String nombreModelo) {
        return HistorialEvaluaciones.getInstancia().getTodos().stream()
                .filter(r -> r.getNombreModelo().equals(nombreModelo))
                .collect(java.util.stream.Collectors.toList());
    }

    private static String sanitize(String s) {
        return s == null ? "" : s.replaceAll("[^A-Za-z0-9_]", "_");
    }

    // ====================================================================
    // EXPOSICIONES PARA LOS TESTS (paquete-visible)
    // ====================================================================

    /** Expuesto para tests: generaDOT no es público por API pero es invocable desde paquete. */
    static String dotParaPruebas(AFD afd) {
        return GeneradorDot.generar(afd);
    }

    /** Construye el AFD del enunciado para tests de integración. */
    public static AFD afdEnunciadoParaPruebas() throws ValidacionException {
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
}