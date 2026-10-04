# Plan de trabajo – Proyecto Autómatas y Lenguajes Formales 2026

**Entrega:** jueves 05 de noviembre, 23:59 (sin prórroga) · **Hoy:** 03 de octubre → ~33 días
**Stack:** Java + NetBeans (Swing) · Graphviz (`dot`) · librería para PDF (OpenPDF / iText / PDFBox)
**Paradigma exigido:** Programación Orientada a Objetos (se revisa el código y hay que saber explicarlo)

---

## 1. Análisis del enunciado

### 1.1 ¿Qué hay que construir?
Una aplicación de escritorio que permita:

1. **Crear AFDs** (estados, alfabeto, inicial, aceptación, transiciones).
2. **Crear gramáticas regulares** (NT, terminales, NT inicial, producciones).
3. **Convertir** entre ambos (gramática → AFD y AFD → gramática), según el diagrama de flujo.
4. **Evaluar cadenas** y mostrar la **ruta** (AFD) o la **expansión** (gramática).
5. **Cargar** archivos `.afd` y `.gtk` y **guardarlos**.
6. **Reportes**: ver detalle en pantalla y generar **PDF** con grafo (Graphviz), cadenas válidas/inválidas y cadenas evaluadas.

### 1.2 Pantallas / menús

| # | Menú | Funciones clave |
|---|------|-----------------|
| 1 | Portada | Curso, sección, carné; **ENTER** pasa al menú |
| 2 | Menú principal | Crear AFD, Crear Gramática, Evaluar Cadenas, Reportes, Cargar archivo, Salir |
| 3 | Crear AFD | Pide nombre → Estados, Alfabeto, Estado inicial, Estados de aceptación, Transiciones (modo 1 / modo 2), Ayuda |
| 4 | Crear Gramática | Pide nombre → NT, Terminales, NT inicial, Producciones, Ayuda |
| 5 | Evaluar Cadenas | Pide nombre → Solo validar, Ayuda |
| 6 | Cargar archivo | `.afd` o `.gtk` |
| 7 | Guardar | Nombre del AFD/gramática + nombre de archivo destino |
| 8 | Reportes | Pide nombre → Ver detalle, Generar reporte (PDF), Ayuda |

> **Ayuda** (en todos los menús): curso, nombre del catedrático y último dígito del carné.

### 1.3 Reglas de validación (puntos fáciles de perder nota)

**AFD**
- Estados sin repetir y **ningún estado igual a un símbolo del alfabeto** (y viceversa).
- Estado inicial debe existir; si se reingresa, **reemplaza** al anterior.
- Estados de aceptación deben existir; si no, mostrar error.
- Transición **modo 1**: `A,B,0;false,true` (origen, destino, símbolo; aceptación del origen, aceptación del destino). Una a la vez.
- Dos transiciones con el mismo símbolo desde el mismo estado → **error** ("solo posible en AFN"). Lo mismo con **epsilon**.
- Transición **modo 2** (matriz):
  ```
  [a, b]                       <- terminales (columnas)
  [A, B, C, D; B, D]           <- estados (filas); después del ; los de aceptación
  [A, C; A, C; B, D; -, -]     <- destinos; columnas con coma, filas con ;  y "-" = sin transición
  ```

**Gramática**
- NT sin repetir, terminales sin repetir, y **ningún NT igual a un terminal**.
- NT inicial debe existir; si se reingresa, reemplaza.
- Producciones en notación `<NT> > <símbolos separados por espacio>`; vacío = `epsilon`.
- Sin producciones repetidas.
- Disyunción de **dos maneras**: con `|` en una línea, o con dos producciones del mismo NT.

**Archivos**
- `.afd`: una línea por transición `A,B,0;false,true`; **estado inicial = primer estado del archivo**; si un estado aparece con aceptación distinta, **gana la última**; el nombre del AFD = nombre del archivo.
- `.gtk`: `A>Ab`, `A>bC`…; **mayúsculas = NT, minúsculas = terminales**; **inicial = NT de la primera línea**; nombre de la gramática = nombre del archivo; el `|` se representa repitiendo el NT en otra línea.
- No se necesita analizador léxico/sintáctico (los archivos vienen sin errores), pero **no se pueden modificar** los archivos de prueba.

**Salidas de evaluación**
```
Ruta en AFD: A, A, a; A, A, a; A, C, b; C, B, a; B, C, b; C, D, b
Resultado: cadena válida

Expansión Gramática: A>0B>00B>001A>0011A>0011(epsilon) > 0011
Resultado: cadena válida
```

### 1.4 Puntos ambiguos → **preguntar al catedrático lo antes posible**

1. **El ejemplo del `.gtk` mezcla formas**: `A>Ab` (recursiva por la izquierda) y `A>bC` (por la derecha). ¿Se espera soportar gramáticas lineales por izquierda y derecha, o solo por derecha? *Recomendación:* diseñar el evaluador de forma genérica (no asumir una sola forma) para no depender de la respuesta.
2. **Gramática → AFD**: una gramática con producciones como `B>c` (terminal sin NT) requiere un estado final extra. Confirmar el criterio de conversión.
3. **"¿Expresión regular?"** aparece en el diagrama de flujo sin explicación. Probablemente fuera de alcance; confirmar.
4. **Evaluar cadenas en AFD**: el menú dice "pide nombre de la gramática", pero las rutas se describen también para AFD. Asumir que el nombre puede ser de AFD **o** de gramática.
5. **Epsilon en `.gtk`**: el formato de archivo no muestra cómo se escribe el vacío. Confirmar.
6. **Cadenas válidas/inválidas del PDF**: ¿se generan automáticamente o salen de las evaluadas por el usuario? *Recomendación:* generarlas automáticamente (BFS) **y** listar las evaluadas.
7. **Conversión de AFD a gramática y de gramática a AFD**: ¿cuándo se dispara (automático al crear, o desde el menú)? El flujo sugiere que cada uno puede generar el otro.

---

## 2. Arquitectura propuesta (POO)

### 2.1 Paquetes
```
proyecto/
├── modelo/        -> Estado, Simbolo, Transicion, AFD, Gramatica, Produccion
├── servicio/      -> RepositorioAutomatas, ConversorGramaticaAFD, ConversorAFDGramatica,
│                     EvaluadorAFD, EvaluadorGramatica, GeneradorCadenas
├── archivo/       -> LectorAFD, LectorGTK, EscritorAFD, EscritorGTK
├── reporte/       -> GeneradorDot, GeneradorGraphviz, GeneradorPDF
├── validacion/    -> ValidadorAFD, ValidadorGramatica, ResultadoValidacion
├── vista/         -> VentanaPrincipal, PanelPortada, PanelMenu, PanelCrearAFD, PanelCrearGramatica,
│                     PanelEvaluar, PanelCargar, PanelGuardar, PanelReportes
└── Main.java
```

### 2.2 Clases y responsabilidades

| Clase | Responsabilidad |
|-------|-----------------|
| `AFD` | Estados (`LinkedHashSet`), alfabeto, inicial, aceptación, transiciones (`Map<Estado, Map<Simbolo, Estado>>`). Métodos `agregarEstado`, `agregarTransicion`, etc. con validaciones |
| `Gramatica` | NT, terminales, inicial, `Map<NT, List<Produccion>>` |
| `Produccion` | NT izquierdo + lista de símbolos derechos (o epsilon) |
| `RepositorioAutomatas` | **Singleton**: registro de AFDs y gramáticas por nombre (ambos en el mismo espacio de nombres para que Evaluar/Reportes/Guardar lo busquen por nombre) |
| `EvaluadorAFD` / `EvaluadorGramatica` | Implementan una interfaz común `Evaluador` (**Strategy**) y devuelven un `ResultadoEvaluacion` (válida/inválida + ruta o expansión) |
| `ConversorGramaticaAFD` / `ConversorAFDGramatica` | Conversiones entre ambos modelos |
| `GeneradorDot` + `GeneradorGraphviz` | Crea el `.dot` y llama a `dot` con `ProcessBuilder` para producir PNG |
| `GeneradorPDF` | Ensambla el reporte final |
| `GeneradorCadenas` | Produce ≥3 cadenas válidas y ≥3 inválidas (BFS por longitud) |

### 2.3 Paradigmas/patrones que conviene poder explicar
Encapsulamiento (atributos privados + validación en el modelo), **Interfaz/Polimorfismo** (`Evaluador`), **Singleton** (repositorio), **Strategy** (evaluadores), **Factory** (lectores de archivo según extensión), separación **MVC** (modelo / vista / controlador).

---

## 3. Fases del plan

> Cada fase tiene un **entregable verificable**. Hacer commit al final de cada una (Git).

### Fase 0 – Preparación (03 – 06 oct) · ~3 días
- [ ] Crear repositorio Git y proyecto NetBeans (Java + Swing).
- [ ] Instalar **Graphviz** y verificar que `dot -V` funciona; decidir cómo configurar la ruta del ejecutable.
- [ ] Elegir librería de PDF y añadirla (Maven/lib).
- [ ] Repartir roles del equipo (modelo · conversiones · UI · reportes/documentación).
- [ ] Mandar al catedrático las **dudas de la sección 1.4**.
- [ ] Crear los **archivos de prueba** propios: 3 `.afd` y 3 `.gtk`, incluyendo casos borde.
- **Entregable:** proyecto compilando con `Main` vacío + lista de dudas enviada.

### Fase 1 – Modelo de dominio y validaciones (07 – 11 oct) · ~5 días
- [ ] Clases `AFD`, `Gramatica`, `Produccion`, `Estado/Simbolo`.
- [ ] Validaciones: duplicados, estado ≠ símbolo, NT ≠ terminal, inicial/aceptación existentes, determinismo, no epsilon en AFD.
- [ ] `RepositorioAutomatas` (Singleton).
- [ ] **Pruebas unitarias** (JUnit) de cada regla de validación.
- **Entregable:** el modelo funciona por consola/tests, sin UI.

### Fase 2 – Evaluación de cadenas (12 – 16 oct) · ~5 días
- [ ] `EvaluadorAFD`: recorrido con ruta `A, A, a; A, C, b; ...` y resultado.
- [ ] `EvaluadorGramatica`: búsqueda de derivación (backtracking con poda por longitud/ciclos) que entregue la **expansión** `A>0B>00B>...>0011`.
  - Cuidar: recursión por la izquierda (`A>Ab`) y ciclos con `epsilon` para evitar bucles infinitos.
- [ ] Interfaz `Evaluador` + clase `ResultadoEvaluacion`.
- [ ] `GeneradorCadenas` (≥3 válidas y ≥3 inválidas).
- **Entregable:** pruebas reproduciendo exactamente los ejemplos del enunciado (`aababb` y `0011`).

### Fase 3 – Conversiones Gramática ↔ AFD (17 – 21 oct) · ~5 días
- [ ] Gramática → AFD: NT → estados; `A>aB` → δ(A,a)=B; `A>epsilon` → A es de aceptación; `A>a` → estado final extra.
- [ ] AFD → Gramática: δ(A,a)=B → `A>aB`; cada estado de aceptación → `A>epsilon`.
- [ ] Verificar equivalencia: las mismas cadenas válidas en ambos modelos.
- [ ] Manejar el caso de gramáticas **no determinísticas** (decidir con el catedrático: error o conversión por subconjuntos).
- **Entregable:** conversiones probadas en ambas direcciones con casos del enunciado.

### Fase 4 – Archivos de entrada y salida (22 – 25 oct) · ~4 días
- [ ] `LectorAFD` (`A,B,0;false,true`: inicial = primera línea; última definición de aceptación gana; nombre = archivo).
- [ ] `LectorGTK` (mayúsculas = NT, minúsculas = terminales; inicial = primera línea; nombre = archivo).
- [ ] `EscritorAFD` y `EscritorGTK` (menú **Guardar**).
- [ ] Prueba de ida y vuelta: cargar → guardar → cargar y comparar.
- **Entregable:** carga/guardado funcionando con los archivos de prueba, **sin modificarlos**.

### Fase 5 – Interfaz gráfica (26 oct – 31 oct) · ~6 días *(se puede arrancar en paralelo desde la fase 2)*
- [ ] `VentanaPrincipal` con `CardLayout` para cambiar de panel.
- [ ] Portada (curso, sección, carné) con **ENTER** → menú principal.
- [ ] Paneles: Crear AFD (incluye modos 1 y 2), Crear Gramática, Evaluar, Cargar (`JFileChooser`), Guardar, Reportes.
- [ ] Mensajes de error claros para cada validación del enunciado.
- [ ] Botón/menú **Ayuda** reutilizable en todas las pantallas.
- [ ] Parser del **modo 2** (matriz) y del modo 1.
- **Entregable:** todos los flujos del menú recorribles de punta a punta.

### Fase 6 – Reportes y Graphviz (29 oct – 02 nov) · ~4 días
- [ ] `GeneradorDot`: nodos, doble círculo para aceptación, flecha de inicio, etiquetas en aristas (agrupar símbolos que comparten origen y destino).
- [ ] `GeneradorGraphviz`: llamar a `dot` y obtener la imagen; manejar error si no está instalado.
- [ ] **Ver detalle** (AFD: alfabeto, estados, inicial, aceptación, transiciones · Gramática: NT, terminales, inicio, producciones).
- [ ] **Generar reporte PDF**: detalle + grafo + ≥3 cadenas válidas + ≥3 inválidas + cadenas evaluadas con su resultado.
- **Entregable:** PDF generado para un AFD y una gramática de ejemplo.

### Fase 7 – Pruebas integrales y pulido (02 – 03 nov) · ~2 días
- [ ] Recorrer todos los casos de la sección 1.3 contra la aplicación completa.
- [ ] Probar con archivos de prueba nuevos (como los usará el catedrático).
- [ ] Revisar que **todo el código tenga encabezado** (nombre y carné) y comentarios por método.
- [ ] Limpiar código, quitar duplicados, revisar nombres.

### Fase 8 – Documentación y entrega (03 – 05 nov) · ~3 días
- [ ] **Manual de Usuario** (capturas por pantalla, flujo paso a paso).
- [ ] **Manual Técnico**: diagrama de clases, explicación de AFD/gramáticas regulares, algoritmos de conversión, evaluación y generación del grafo.
- [ ] Empaquetar `.zip/.rar` con código, proyecto NetBeans, compilados, configuración, manuales y archivos de prueba.
- [ ] **Subir a Canvas antes de las 23:59 del 05 nov** (no subir en el último minuto).
- **Entregable:** entrega completa.

### Fase 9 – Preparación de la calificación (06 nov en adelante)
- [ ] Cada integrante debe poder **explicar cualquier parte del código** (15 min máximo; si falta alguien, solo se califica a los presentes).
- [ ] Ensayar la demo: crear AFD → evaluar → generar PDF → cargar `.gtk`.
- [ ] Preparar respuestas sobre paradigmas usados (sección 2.3).

---

## 4. Cronograma resumido

| Semana | Fechas | Fases |
|--------|--------|-------|
| 1 | 03 – 11 oct | 0 y 1 |
| 2 | 12 – 18 oct | 2 y primera mitad de 3 |
| 3 | 19 – 25 oct | 3 y 4 |
| 4 | 26 oct – 01 nov | 5 y arranque de 6 |
| 5 | 02 – 05 nov | 6, 7 y 8 (entrega) |

> **Colchón:** el plan ya reserva solo ~2 días de margen. Si una fase se atrasa, recortar adornos de UI antes que funcionalidad o documentación.

---

## 5. Riesgos y mitigaciones

| Riesgo | Mitigación |
|--------|-----------|
| Graphviz no instalado/ruta distinta en la máquina del calificador | Ruta configurable + mensaje claro de error + incluir el `.dot` aparte |
| Bucles infinitos al evaluar gramáticas con `A>Ab` o epsilon | Límite de longitud y conjunto de estados visitados |
| Formato `.gtk` ambiguo | Resolver con el catedrático (sección 1.4) y evaluador genérico |
| Conflicto de nombres entre un AFD y una gramática | Un único registro de nombres, rechazar duplicados |
| Documentación al final | Ir tomando capturas y notas durante cada fase |
| Un integrante no sabe explicar el código | Revisiones de código cruzadas por fase |

---

## 6. Checklist final de entrega

- [ ] Portada con curso, sección, carné y ENTER
- [ ] Menú principal con las 6 opciones (+ Guardar)
- [ ] Crear AFD: estados, alfabeto, inicial, aceptación, modo 1 y modo 2
- [ ] Crear gramática: NT, terminales, inicial, producciones (`|` y epsilon)
- [ ] Conversiones Gramática ↔ AFD
- [ ] Evaluar cadenas con ruta / expansión y mensaje válida/inválida
- [ ] Cargar `.afd` y `.gtk` · Guardar `.afd` y `.gtk`
- [ ] Ver detalle y PDF con grafo, 3 válidas, 3 inválidas y cadenas evaluadas
- [ ] Ayuda en todos los menús
- [ ] Encabezados y comentarios en todo el código (nombre + carné)
- [ ] Manual de usuario · Manual técnico · Código fuente en `.zip/.rar`
