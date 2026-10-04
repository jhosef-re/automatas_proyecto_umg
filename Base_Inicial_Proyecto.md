# Base inicial del proyecto – Autómatas y Lenguajes Formales

Guía para dejar el proyecto **compilando y corriendo** el primer día: estructura, modelo base, evaluador de AFD y ventana con portada + menú.

> Los nombres de paquetes siguen el plan de trabajo. Todo el código usa `String` para estados/símbolos (más simple y suficiente). Se asume **un carácter por símbolo** al evaluar cadenas.

---

## 1. Crear el proyecto en NetBeans

1. `File > New Project > Java with Ant > Java Application` (o Maven si lo prefieren).
2. Nombre: `ProyectoAutomatas` · Paquete principal: `automatas` · desmarcar "Create Main Class" (la crearemos nosotros).
3. JDK 17 o superior.
4. Inicializar Git:
   ```bash
   git init
   git add .
   git commit -m "Proyecto base"
   ```
5. `.gitignore` mínimo:
   ```
   build/
   dist/
   nbproject/private/
   target/
   *.class
   ```
6. Verificar Graphviz en la terminal:
   ```bash
   dot -V
   ```

---

## 2. Estructura de paquetes

```
src/automatas/
├── Main.java
├── modelo/
│   ├── AFD.java
│   ├── Gramatica.java
│   └── Produccion.java
├── validacion/
│   └── ValidacionException.java
├── servicio/
│   ├── RepositorioAutomatas.java
│   ├── Evaluador.java
│   ├── ResultadoEvaluacion.java
│   └── EvaluadorAFD.java
├── archivo/        (fase 4)
├── reporte/        (fase 6)
└── vista/
    ├── DatosCurso.java
    ├── VentanaPrincipal.java
    ├── PanelPortada.java
    └── PanelMenu.java
```

> Crear los paquetes vacíos `archivo` y `reporte` desde ya para mantener el orden.

---

## 3. Excepción de validación

**`validacion/ValidacionException.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.validacion;

/** Error de validación mostrado al usuario (duplicados, inexistentes, etc.). */
public class ValidacionException extends Exception {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
```

---

## 4. Modelo

### 4.1 AFD

**`modelo/AFD.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import java.util.*;

/** Autómata Finito Determinista con validaciones del enunciado. */
public class AFD {

    private final String nombre;
    private final Set<String> estados = new LinkedHashSet<>();
    private final Set<String> alfabeto = new LinkedHashSet<>();
    private final Set<String> estadosAceptacion = new LinkedHashSet<>();
    // origen -> (símbolo -> destino)
    private final Map<String, Map<String, String>> transiciones = new LinkedHashMap<>();
    private String estadoInicial;

    public AFD(String nombre) {
        this.nombre = nombre;
    }

    /** Agrega un estado: no repetido y distinto de cualquier símbolo del alfabeto. */
    public void agregarEstado(String estado) throws ValidacionException {
        estado = limpiar(estado, "estado");
        if (estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' ya existe.");
        if (alfabeto.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' es igual a un símbolo del alfabeto.");
        estados.add(estado);
    }

    /** Agrega un símbolo: no repetido y distinto de cualquier estado. */
    public void agregarSimbolo(String simbolo) throws ValidacionException {
        simbolo = limpiar(simbolo, "símbolo");
        if (simbolo.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("epsilon no es válido en un AFD (solo en AFN).");
        if (alfabeto.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' ya existe.");
        if (estados.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' es igual a un estado.");
        alfabeto.add(simbolo);
    }

    /** Define el estado inicial; reemplaza al anterior si ya había uno. */
    public void setEstadoInicial(String estado) throws ValidacionException {
        estado = limpiar(estado, "estado inicial");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        this.estadoInicial = estado;
    }

    public void agregarEstadoAceptacion(String estado) throws ValidacionException {
        estado = limpiar(estado, "estado de aceptación");
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        estadosAceptacion.add(estado);
    }

    /** Usado al cargar archivos: la última definición de aceptación gana. */
    public void setAceptacion(String estado, boolean esAceptacion) throws ValidacionException {
        if (!estados.contains(estado))
            throw new ValidacionException("El estado '" + estado + "' no existe.");
        if (esAceptacion) estadosAceptacion.add(estado);
        else estadosAceptacion.remove(estado);
    }

    /** Agrega una transición verificando que el AFD siga siendo determinista. */
    public void agregarTransicion(String origen, String destino, String simbolo)
            throws ValidacionException {
        if (simbolo.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("Las transiciones con epsilon solo son posibles en AFN.");
        if (!estados.contains(origen))
            throw new ValidacionException("El estado origen '" + origen + "' no existe.");
        if (!estados.contains(destino))
            throw new ValidacionException("El estado destino '" + destino + "' no existe.");
        if (!alfabeto.contains(simbolo))
            throw new ValidacionException("El símbolo '" + simbolo + "' no está en el alfabeto.");

        Map<String, String> salidas = transiciones.computeIfAbsent(origen, k -> new LinkedHashMap<>());
        if (salidas.containsKey(simbolo))
            throw new ValidacionException("Dos transiciones con el símbolo '" + simbolo
                    + "' desde '" + origen + "' solo son posibles en AFN (no determinísticos).");
        salidas.put(simbolo, destino);
    }

    /** Devuelve el destino o null si no hay transición. */
    public String mover(String estado, String simbolo) {
        Map<String, String> salidas = transiciones.get(estado);
        return salidas == null ? null : salidas.get(simbolo);
    }

    public boolean esAceptacion(String estado) {
        return estadosAceptacion.contains(estado);
    }

    private String limpiar(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El " + campo + " no puede estar vacío.");
        return valor.trim();
    }

    // ---- Getters (copias de solo lectura) ----
    public String getNombre() { return nombre; }
    public Set<String> getEstados() { return Collections.unmodifiableSet(estados); }
    public Set<String> getAlfabeto() { return Collections.unmodifiableSet(alfabeto); }
    public Set<String> getEstadosAceptacion() { return Collections.unmodifiableSet(estadosAceptacion); }
    public String getEstadoInicial() { return estadoInicial; }
    public Map<String, Map<String, String>> getTransiciones() {
        return Collections.unmodifiableMap(transiciones);
    }
}
```

### 4.2 Producción

**`modelo/Produccion.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.modelo;

import java.util.*;

/** Una producción: NT > símbolo1 símbolo2 ...  (lista vacía = epsilon). */
public class Produccion {

    private final String izquierdo;
    private final List<String> derecho;

    public Produccion(String izquierdo, List<String> derecho) {
        this.izquierdo = izquierdo;
        this.derecho = Collections.unmodifiableList(new ArrayList<>(derecho));
    }

    public String getIzquierdo() { return izquierdo; }
    public List<String> getDerecho() { return derecho; }
    public boolean esEpsilon() { return derecho.isEmpty(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Produccion)) return false;
        Produccion p = (Produccion) o;
        return izquierdo.equals(p.izquierdo) && derecho.equals(p.derecho);
    }

    @Override
    public int hashCode() { return Objects.hash(izquierdo, derecho); }

    @Override
    public String toString() {
        return izquierdo + ">" + (esEpsilon() ? "epsilon" : String.join(" ", derecho));
    }
}
```

### 4.3 Gramática

**`modelo/Gramatica.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.modelo;

import automatas.validacion.ValidacionException;
import java.util.*;

/** Gramática regular con las validaciones del enunciado. */
public class Gramatica {

    private final String nombre;
    private final Set<String> noTerminales = new LinkedHashSet<>();
    private final Set<String> terminales = new LinkedHashSet<>();
    private final Map<String, List<Produccion>> producciones = new LinkedHashMap<>();
    private String inicial;

    public Gramatica(String nombre) {
        this.nombre = nombre;
    }

    public void agregarNoTerminal(String nt) throws ValidacionException {
        nt = limpiar(nt, "no terminal");
        if (noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' ya existe.");
        if (terminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' es igual a un terminal.");
        noTerminales.add(nt);
    }

    public void agregarTerminal(String t) throws ValidacionException {
        t = limpiar(t, "terminal");
        if (t.equalsIgnoreCase("epsilon"))
            throw new ValidacionException("'epsilon' está reservado para el vacío.");
        if (terminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' ya existe.");
        if (noTerminales.contains(t))
            throw new ValidacionException("El terminal '" + t + "' es igual a un no terminal.");
        terminales.add(t);
    }

    /** Define el NT inicial; reemplaza al anterior. */
    public void setInicial(String nt) throws ValidacionException {
        nt = limpiar(nt, "no terminal inicial");
        if (!noTerminales.contains(nt))
            throw new ValidacionException("El no terminal '" + nt + "' no existe.");
        this.inicial = nt;
    }

    /**
     * Agrega una línea de producción. Admite disyunción con '|'.
     * Formato:  A > a B | b | epsilon   (símbolos separados por espacio)
     */
    public void agregarProduccion(String linea) throws ValidacionException {
        int pos = linea.indexOf('>');
        if (pos < 0)
            throw new ValidacionException("Formato inválido. Use:  NT > simbolos");
        String izq = linea.substring(0, pos).trim();
        if (!noTerminales.contains(izq))
            throw new ValidacionException("El no terminal '" + izq + "' no existe.");

        for (String alternativa : linea.substring(pos + 1).split("\\|")) {
            List<String> simbolos = new ArrayList<>();
            String alt = alternativa.trim();
            if (alt.isEmpty())
                throw new ValidacionException("Alternativa vacía; use 'epsilon' para el vacío.");
            if (!alt.equalsIgnoreCase("epsilon")) {
                for (String s : alt.split("\\s+")) {
                    if (!noTerminales.contains(s) && !terminales.contains(s))
                        throw new ValidacionException("El símbolo '" + s + "' no está declarado.");
                    simbolos.add(s);
                }
            }
            Produccion nueva = new Produccion(izq, simbolos);
            List<Produccion> lista = producciones.computeIfAbsent(izq, k -> new ArrayList<>());
            if (lista.contains(nueva))
                throw new ValidacionException("La producción '" + nueva + "' ya existe.");
            lista.add(nueva);
        }
    }

    public List<Produccion> getProduccionesDe(String nt) {
        return producciones.getOrDefault(nt, Collections.emptyList());
    }

    private String limpiar(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El " + campo + " no puede estar vacío.");
        return valor.trim();
    }

    // ---- Getters ----
    public String getNombre() { return nombre; }
    public Set<String> getNoTerminales() { return Collections.unmodifiableSet(noTerminales); }
    public Set<String> getTerminales() { return Collections.unmodifiableSet(terminales); }
    public String getInicial() { return inicial; }
    public Map<String, List<Produccion>> getProducciones() {
        return Collections.unmodifiableMap(producciones);
    }
}
```

---

## 5. Servicios

### 5.1 Repositorio (Singleton)

**`servicio/RepositorioAutomatas.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.servicio;

import automatas.modelo.AFD;
import automatas.modelo.Gramatica;
import automatas.validacion.ValidacionException;
import java.util.*;

/** Registro único de AFDs y gramáticas por nombre (Singleton). */
public class RepositorioAutomatas {

    private static final RepositorioAutomatas INSTANCIA = new RepositorioAutomatas();

    private final Map<String, AFD> afds = new LinkedHashMap<>();
    private final Map<String, Gramatica> gramaticas = new LinkedHashMap<>();

    private RepositorioAutomatas() { }

    public static RepositorioAutomatas getInstancia() { return INSTANCIA; }

    /** El nombre es único entre AFDs y gramáticas. */
    public boolean existe(String nombre) {
        return afds.containsKey(nombre) || gramaticas.containsKey(nombre);
    }

    public void registrar(AFD afd) throws ValidacionException {
        if (existe(afd.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + afd.getNombre() + "'.");
        afds.put(afd.getNombre(), afd);
    }

    public void registrar(Gramatica g) throws ValidacionException {
        if (existe(g.getNombre()))
            throw new ValidacionException("Ya existe algo llamado '" + g.getNombre() + "'.");
        gramaticas.put(g.getNombre(), g);
    }

    public AFD obtenerAFD(String nombre) { return afds.get(nombre); }
    public Gramatica obtenerGramatica(String nombre) { return gramaticas.get(nombre); }
}
```

### 5.2 Interfaz y resultado (Strategy)

**`servicio/Evaluador.java`**
```java
package automatas.servicio;

/** Estrategia de evaluación de cadenas (AFD o gramática). */
public interface Evaluador {
    ResultadoEvaluacion evaluar(String cadena);
}
```

**`servicio/ResultadoEvaluacion.java`**
```java
package automatas.servicio;

/** Resultado de evaluar una cadena: validez + ruta/expansión como texto. */
public class ResultadoEvaluacion {

    private final boolean valida;
    private final String detalle;   // "Ruta en AFD: ..." o "Expansión Gramática: ..."

    public ResultadoEvaluacion(boolean valida, String detalle) {
        this.valida = valida;
        this.detalle = detalle;
    }

    public boolean esValida() { return valida; }
    public String getDetalle() { return detalle; }

    @Override
    public String toString() {
        return detalle + "\nResultado: " + (valida ? "cadena válida" : "cadena inválida");
    }
}
```

### 5.3 Evaluador de AFD

**`servicio/EvaluadorAFD.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
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
```

---

## 6. Vista

### 6.1 Datos de Ayuda

**`vista/DatosCurso.java`**
```java
package automatas.vista;

/** Datos mostrados en la portada y en la opción Ayuda. Completar con los reales. */
public final class DatosCurso {
    public static final String CURSO = "Autómatas y Lenguajes Formales";
    public static final String SECCION = "<sección>";
    public static final String CARNE = "<carné>";
    public static final String CATEDRATICO = "<nombre del catedrático>";

    private DatosCurso() { }

    public static String textoAyuda() {
        String ultimo = CARNE.isEmpty() ? "?" : CARNE.substring(CARNE.length() - 1);
        return "Curso: " + CURSO
             + "\nCatedrático: " + CATEDRATICO
             + "\nÚltimo dígito del carné: " + ultimo;
    }
}
```

### 6.2 Ventana principal (CardLayout)

**`vista/VentanaPrincipal.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.vista;

import java.awt.CardLayout;
import javax.swing.*;

/** Ventana única; cambia de pantalla con CardLayout. */
public class VentanaPrincipal extends JFrame {

    public static final String PORTADA = "portada";
    public static final String MENU = "menu";

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);

    public VentanaPrincipal() {
        super("Proyecto Autómatas – " + DatosCurso.CURSO);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        contenedor.add(new PanelPortada(this), PORTADA);
        contenedor.add(new PanelMenu(this), MENU);
        // Agregar aquí los paneles de Crear AFD, Crear Gramática, Evaluar, etc.

        add(contenedor);
        mostrar(PORTADA);
    }

    /** Muestra la pantalla con el nombre indicado. */
    public void mostrar(String nombre) {
        cartas.show(contenedor, nombre);
    }

    /** Registra una pantalla nueva para poder navegar hacia ella. */
    public void agregarPantalla(String nombre, JPanel panel) {
        contenedor.add(panel, nombre);
    }
}
```

### 6.3 Portada (ENTER para continuar)

**`vista/PanelPortada.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/** Carátula con datos del curso; ENTER lleva al menú principal. */
public class PanelPortada extends JPanel {

    public PanelPortada(VentanaPrincipal ventana) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 41, 59));

        JPanel caja = new JPanel();
        caja.setOpaque(false);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));

        caja.add(etiqueta("Universidad Mariano Gálvez", 28, true));
        caja.add(etiqueta(DatosCurso.CURSO, 22, false));
        caja.add(etiqueta("Sección: " + DatosCurso.SECCION, 18, false));
        caja.add(etiqueta("Carné: " + DatosCurso.CARNE, 18, false));
        caja.add(Box.createVerticalStrut(40));
        caja.add(etiqueta("Presione ENTER para continuar", 16, false));
        add(caja);

        // ENTER funciona sin importar qué componente tenga el foco
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "entrar");
        getActionMap().put("entrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.mostrar(VentanaPrincipal.MENU);
            }
        });
    }

    private JLabel etiqueta(String texto, int tam, boolean negrita) {
        JLabel l = new JLabel(texto);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", negrita ? Font.BOLD : Font.PLAIN, tam));
        l.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        return l;
    }
}
```

> **Ojo:** `WHEN_IN_FOCUSED_WINDOW` en `PanelPortada` capta ENTER aunque la portada esté oculta. Si pasa, usar `setEnabled` o remover el binding al salir de la portada.

### 6.4 Menú principal

**`vista/PanelMenu.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas.vista;

import java.awt.*;
import javax.swing.*;

/** Menú principal con las opciones del enunciado. */
public class PanelMenu extends JPanel {

    public PanelMenu(VentanaPrincipal ventana) {
        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        agregarBoton("Crear AFD", () -> pendiente("Crear AFD"));
        agregarBoton("Crear Gramática", () -> pendiente("Crear Gramática"));
        agregarBoton("Evaluar Cadenas", () -> pendiente("Evaluar Cadenas"));
        agregarBoton("Reportes", () -> pendiente("Reportes"));
        agregarBoton("Cargar archivo de entrada", () -> pendiente("Cargar archivo"));
        agregarBoton("Guardar", () -> pendiente("Guardar"));
        agregarBoton("Ayuda", () -> JOptionPane.showMessageDialog(
                this, DatosCurso.textoAyuda(), "Ayuda", JOptionPane.INFORMATION_MESSAGE));
        agregarBoton("Salir", () -> System.exit(0));
    }

    private void agregarBoton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.PLAIN, 16));
        b.addActionListener(e -> accion.run());
        add(b);
    }

    /** Marcador temporal hasta implementar cada pantalla. */
    private void pendiente(String nombre) {
        JOptionPane.showMessageDialog(this, "Pantalla '" + nombre + "' pendiente de implementar.");
    }
}
```

### 6.5 Main

**`Main.java`**
```java
/*
 * Autor: <Nombre> - Carné: <carné>
 */
package automatas;

import automatas.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
```

---

## 7. Prueba rápida: el ejemplo del enunciado (`aababb`)

Agregar temporalmente en `Main` (o como test JUnit) para verificar el modelo y el evaluador **sin UI**:

```java
AFD afd = new AFD("ejemplo");
for (String s : new String[]{"A","B","C","D"}) afd.agregarEstado(s);
afd.agregarSimbolo("a");
afd.agregarSimbolo("b");
afd.setEstadoInicial("A");
afd.agregarEstadoAceptacion("D");
afd.agregarTransicion("A","A","a");  afd.agregarTransicion("A","C","b");
afd.agregarTransicion("B","A","a");  afd.agregarTransicion("B","C","b");
afd.agregarTransicion("C","B","a");  afd.agregarTransicion("C","D","b");

System.out.println(new EvaluadorAFD(afd).evaluar("aababb"));
```

Salida esperada:
```
Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b
Resultado: cadena válida
```

> El método `main` de prueba debe declarar `throws Exception` o usar `try/catch` por `ValidacionException`.

---

## 8. Checklist del día 1

- [ ] Proyecto creado, paquetes en su lugar, Git inicializado
- [ ] Todos los archivos copiados y **compilando sin errores**
- [ ] Portada se abre, ENTER lleva al menú, **Ayuda** muestra los datos
- [ ] Datos reales en `DatosCurso` (sección, carné, catedrático)
- [ ] Prueba del `aababb` imprime la ruta exacta del enunciado
- [ ] Cada integrante clona el repo y lo ejecuta en su equipo
- [ ] Encabezado con nombre y carné en cada archivo

---

## 9. Qué sigue (en orden)

1. `EvaluadorGramatica` (expansión `A>0B>00B>...`) y `GeneradorCadenas`.
2. Parser de transiciones **modo 1** y **modo 2** hacia `AFD`.
3. Conversiones gramática ↔ AFD.
4. Lectores/escritores `.afd` y `.gtk`.
5. Pantallas Swing de cada menú, reemplazando los `pendiente(...)`.
6. `GeneradorDot` + Graphviz + PDF.
