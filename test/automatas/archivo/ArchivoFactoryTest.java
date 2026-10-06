/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.archivo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;

import java.nio.file.Path;
import java.nio.file.Paths;

class ArchivoFactoryTest {

    @Test
    @DisplayName("crearLector dispatcha por extensión: .afd → LectorAFD")
    void creaLectorPorExtensionAfd() throws ValidacionException {
        Lector<?> l = ArchivoFactory.crearLector(Paths.get("cualquier.afd"));
        assertTrue(l instanceof LectorAFD, "Debe ser LectorAFD");
    }

    @Test
    @DisplayName("crearLector dispatcha por extensión: .gtk → LectorGTK")
    void creaLectorPorExtensionGtk() throws ValidacionException {
        Lector<?> l = ArchivoFactory.crearLector(Paths.get("cualquier.gtk"));
        assertTrue(l instanceof LectorGTK, "Debe ser LectorGTK");
    }

    @Test
    @DisplayName("crearLector con extensión desconocida → ValidacionException")
    void creaLectorExtensionDesconocida() {
        assertThrows(ValidacionException.class,
                () -> ArchivoFactory.crearLector(Paths.get("archivo.txt")));
    }

    @Test
    @DisplayName("crearEscritor dispatcha por extensión: .afd / .gtk")
    void creaEscritorPorExtension() throws ValidacionException {
        assertTrue(ArchivoFactory.crearEscritor(Paths.get("x.afd")) instanceof EscritorAFD);
        assertTrue(ArchivoFactory.crearEscritor(Paths.get("y.gtk")) instanceof EscritorGTK);
    }

    @Test
    @DisplayName("crearEscritor con extensión desconocida → ValidacionException")
    void creaEscritorExtensionDesconocida() {
        assertThrows(ValidacionException.class,
                () -> ArchivoFactory.crearEscritor(Paths.get("archivo.bin")));
    }

    @Test
    @DisplayName("crearLector con path null → ValidacionException")
    void crearLectorNull() {
        assertThrows(ValidacionException.class,
                () -> ArchivoFactory.crearLector(null));
    }

    @Test
    @DisplayName("crearEscritor con path null → ValidacionException")
    void crearEscritorNull() {
        assertThrows(ValidacionException.class,
                () -> ArchivoFactory.crearEscritor(null));
    }

    @Test
    @DisplayName("Round-trip por factory: cargar un .afd del enunciado y reescribirlo")
    @SuppressWarnings({"unchecked", "rawtypes"})
    void roundTripPorFactory() throws Exception {
        Path entrada = Paths.get("test/resources/enunciado.afd");
        Path salida = Paths.get("test/resources/_tmp_factory_test.afd");
        try {
            Lector<?> lector = ArchivoFactory.crearLector(entrada);
            AFD afd = (AFD) lector.leer(entrada);
            Escritor escritor = ArchivoFactory.crearEscritor(salida);
            escritor.escribir(afd, salida);
            AFD reLeido = (AFD) ArchivoFactory.crearLector(salida).leer(salida);
            assertEquals(afd.getEstadoInicial(), reLeido.getEstadoInicial());
            assertEquals(afd.getEstadosAceptacion(), reLeido.getEstadosAceptacion());
        } finally {
            java.nio.file.Files.deleteIfExists(salida);
        }
    }

    @Test
    @DisplayName("Round-trip por factory: gramática del enunciado")
    @SuppressWarnings({"unchecked", "rawtypes"})
    void roundTripPorFactoryGramatica() throws Exception {
        Path entrada = Paths.get("test/resources/enunciado.gtk");
        Path salida = Paths.get("test/resources/_tmp_factory_test.gtk");
        try {
            Lector<?> lector = ArchivoFactory.crearLector(entrada);
            Gramatica g = (Gramatica) lector.leer(entrada);
            Escritor escritor = ArchivoFactory.crearEscritor(salida);
            escritor.escribir(g, salida);
            Gramatica reLeida = (Gramatica) ArchivoFactory.crearLector(salida).leer(salida);
            assertEquals(g.getInicial(), reLeida.getInicial());
            assertEquals(g.getNoTerminales(), reLeida.getNoTerminales());
            assertEquals(g.getTerminales(), reLeida.getTerminales());
        } finally {
            java.nio.file.Files.deleteIfExists(salida);
        }
    }
}