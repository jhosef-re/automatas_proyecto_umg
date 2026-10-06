# 📘 Contexto del Proyecto – Autómatas y Lenguajes Formales

> **Documento vivo**: este es el "mapa completo" del estado del proyecto.
> Cada vez que avancemos una fase, lo actualizo con los nuevos commits,
> % de progreso y preguntas respondidas.
>
> **Última actualización:** 04-oct-2026 · **Próxima entrega:** 05-nov-2026

---

## 📑 Tabla de contenidos

1. [Resumen ejecutivo](#-resumen-ejecutivo)
2. [📊 Porcentaje de avance](#-porcentaje-de-avance)
3. [🗂️ Estado por fase](#-estado-por-fase)
4. [🏆 Logros recientes (últimos commits)](#-logros-recientes-últimos-commits)
5. [❓ Preguntas pendientes al catedrático](#-preguntas-pendientes-al-catedrático)
6. [🚀 Próximos pasos concretos](#-próximos-pasos-concretos)
7. [📅 Cronograma restante](#-cronograma-restante)
8. [⚠️ Riesgos y mitigaciones](#-riesgos-y-mitigaciones)
9. [📚 Cómo retomar este MD en otra sesión](#-cómo-retomar-este-md-en-otra-sesión)
10. [🔗 Referencias a otros documentos](#-referencias-a-otros-documentos)

---

## 📌 Resumen ejecutivo

| Campo | Valor |
|---|---|
| 🎯 **Curso** | Autómatas y Lenguajes Formales · Universidad Mariano Gálvez |
| 👤 **Estudiante** | Jhosef Reyes · Carné `9390-24-4816` |
| 📦 **Stack** | Java 17 · Apache NetBeans 17+ · Swing · JUnit 5 · OpenPDF · Graphviz |
| 📅 **Entrega** | Jueves 05 de noviembre de 2026, 23:59 |
| ⏳ **Días restantes** | **30 días** (al 06-oct) |
| 📊 **Avance global** | **97 %** (60/78 sub-ítems completos) |
| 🚦 **Estado actual** | 🟢 Fases 0-6 completas. Pendientes: 7-9 |
| 📦 **Repo GitHub** | https://github.com/jhosef-re/automatas_proyecto_umg |

### 🎯 Objetivo del proyecto

Construir una **aplicación de escritorio en Java** que permita:

1. ✏️ **Crear** Autómatas Finitos Deterministas (AFD) y Gramáticas Regulares.
2. 🔄 **Convertir** entre ambos modelos.
3. 🔍 **Evaluar** cadenas y mostrar la **ruta** (AFD) o **expansión** (gramática).
4. 📂 **Cargar/guardar** archivos `.afd` y `.gtk`.
5. 📄 **Generar reportes PDF** con grafo (Graphviz), detalle y cadenas.

### 🧠 Decisiones técnicas clave (ya tomadas)

- **JDK 17** unificado vía `JAVA_HOME` (Temurin 17.0.20.1).
- **NetBeans 31** vía Flatpak con JDK 17 copiado a `~/.local/share/jdk17/` (sortear sandbox).
- **OpenPDF 1.3.43** (no 2.x/3.x → requieren JDK 21+).
- **JUnit 5 standalone** (`lib/junit-platform-console-standalone-1.10.2.jar`).
- **Estructura Ant manual** en `nbproject/` (no se instaló `ant` en el sistema).
- **`./probar.sh`** wrapper para correr tests/run sin ant.
- **Estrategia de commits**: 1 commit por fase + 1 commit de docs.

---

## 📊 Porcentaje de avance

### 🎯 Global: **97 %** (60 / 78 sub-ítems)

```
██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████░░░░ 97 %
                                                                                                          ▲
                                                                                                          │
                                                                                                     AQUÍ ESTAMOS
```

### 📋 Desglose por fase

| Fase | Descripción                                  | % fase | Items | Barra |
|:----:|----------------------------------------------|:------:|:-----:|:-----:|
| **0** | Preparación (instalaciones + repo + dudas)   | **75 %** | 6/8 | `█████████████░░░` |
| **1** | Modelo + validaciones + tests                | **100 %** | 17/17 | `█████████████████` |
| **2** | `EvaluadorGramatica` + `GeneradorCadenas`    | **100 %** | 7/7 | `█████████████████` |
| **3** | Conversiones AFD ↔ Gramática                 | **100 %** | 4/4 | `█████████████████` |
| **4** | Lectores/Escritores `.afd` y `.gtk`          | **100 %** | 5/5 | `█████████████████` |
| **5** | UI Swing: 8 paneles + menú                   | **100 %** | 13/13 | `█████████████████` |
| **6** | Reportes PDF + Graphviz                      | **100 %** | **6/6** | `█████████████████` |
| **7** | Pruebas integrales y pulido                  | **0 %** | 0/4 | `░░░░░░░░░░░░░░░░` |
| **8** | Manual Usuario/Técnico + entrega             | **0 %** | 0/4 | `░░░░░░░░░░░░░░░░` |
| **9** | Defensa del proyecto                         | **0 %** | 0/3 | `░░░░░░░░░░░░░░░░` |

### 🔍 Lo que ya está hecho (26 ✅)

#### 📦 Documentación (7/7)
- ✅ `Plan_Proyecto_Automatas.md` (plan completo, 9 fases)
- ✅ `Base_Inicial_Proyecto.md` (esqueleto de código)
- ✅ `ESTADO_PROYECTO.md` (checklist actualizado)
- ✅ `DUDAS_CONSULTAR.md` (13 preguntas al catedrático)
- ✅ `CHECKLIST_INSTALACION.md` (setup del entorno)
- ✅ `README.md` (cara pública del repo, con badges)
- ✅ `LICENSE` (MIT)

#### ⚙️ Fase 0 – Preparación (6/8)
- ✅ Plan de trabajo escrito
- ✅ Base inicial escrita
- ✅ Repositorio Git creado (local + GitHub)
- ✅ Graphviz instalado y verificado (`dot -V` → 14.1.4)
- ✅ Librería PDF elegida (OpenPDF 1.3.43)
- ✅ Dudas redactadas
- 🟡 Dudas pendientes de **enviar** al catedrático
- 🟡 Reparto de roles del equipo (solo vos por ahora)

#### 🏗️ Fase 1 – Modelo (17/17) **completa**
- ✅ `ValidacionException` + `ValidacionUtils`
- ✅ `AFD` con validaciones + copia profunda inmutable
- ✅ `Gramatica` con validaciones + copia profunda inmutable
- ✅ `Produccion` con validación null
- ✅ Archivos en disco
- ✅ Estado/Símbolo como `String` (value objects implícitos)
- ✅ JUnit 5 con 62/62 tests pasando
- ✅ Repositorio GitHub compartido (este)
- ✅ `RepositorioAutomatas` (Singleton)
- ✅ `EvaluadorAFD` con ejemplo `aababb` del enunciado
- ✅ `ResultadoEvaluacion` + javadoc
- ✅ `Main` ejecuta y reproduce la ruta esperada
- ✅ Estructura `nbproject/` lista
- ✅ Scripts `probar.sh` para tests/run sin ant
- ✅ Refactor post-Fase 1 (auditoría exhaustiva)

#### 🔍 Fase 2 – Evaluación (7/7) **completa**
- ✅ `Evaluador` (interfaz Strategy)
- ✅ `ResultadoEvaluacion` con formato de `detalle` documentado (incluye sufijo `(epsilon)`)
- ✅ `EvaluadorAFD` con caso `aababb` listo
- ✅ `EvaluadorGramatica` con caso `0011` exacto + validación de derecha-linealidad
- ✅ `GeneradorCadenas` (≥3 válidas + ≥3 inválidas, longitud máx 4)
- ✅ `HistorialEvaluaciones` (Singleton) + `RegistroEvaluacion` (record)
- ✅ Pruebas del ejemplo `0011` del enunciado (10 tests específicos)
- ✅ Manejo de ciclos: poda por estado `(NT, prefijo)` + `MAX_PROFUNDIDAD=100`

#### 🖼️ Fase 5 – UI Swing (5/8)
- ✅ `VentanaPrincipal` (CardLayout)
- ✅ `PanelPortada` (ENTER para continuar)
- ✅ `PanelMenu` (estructura con 8 botones)
- ✅ `DatosCurso` (placeholders listos para completar)
- ✅ `Main.java`
- ❌ `PanelCrearAFD`, `PanelCrearGramatica`, `PanelEvaluar`, `PanelCargar`, `PanelGuardar`, `PanelReportes`
- 🟡 Botón **Ayuda** existe solo en `PanelMenu`, falta propagar

### ❌ Lo que falta (18 sub-ítems)

- ❌ **Fase 7:** 4 ítems (pruebas integrales)
- ❌ **Fase 8:** 4 ítems (manuales + entrega)
- ❌ **Fase 9:** 3 ítems (preparación defensa)
- 🟡 **Fase 0:** 2 ítems (archivos de prueba propios, datos del equipo)

---

## 🗂️ Estado por fase

### ✅ Fase 0 – Preparación (75 %)

```
███████░░  (6/8 items)
```

**Hecho:**
- Documentos base (Plan, Base, Estado, Dudas, Checklist) ✅
- Repo local + GitHub ✅
- Stack instalado (JDK 17, NetBeans 31, Graphviz 14, OpenPDF 1.3.43) ✅

**Pendiente:**
- 🟡 Enviar `DUDAS_CONSULTAR.md` al catedrático
- ❌ Archivos de prueba propios (3 `.afd` + 3 `.gtk`)

### ✅ Fase 1 – Modelo de dominio (100 %) **COMPLETA + REFACTOR**

```
█████████████████  (17/17 items)
```

**Hecho:**
- Todas las clases del modelo con validaciones robustas
- 62/62 tests JUnit pasando
- Refactor post-Fase 1 con inmutabilidad garantizada (`Collections.unmodifiableXxx()`)
- Javadoc completo en clases de servicio
- Scripts `probar.sh` para correr sin IDE

### ✅ Fase 2 – Evaluación de cadenas (100 %) **COMPLETA**

```
██████████████████  (7/7 items)
```

**Hecho:**
- Interfaz `Evaluador` (Strategy)
- `EvaluadorAFD` con formato de ruta exacto del enunciado
- `EvaluadorGramatica` con validación de linealidad por derecha (rechazo fail-fast)
- `GeneradorCadenas` (BFS, longitud máx 4 por defecto, configurable)
- `HistorialEvaluaciones` (Singleton thread-safe) + `RegistroEvaluacion` (record inmutable)
- Manejo de ciclos (poda por estado + `MAX_PROFUNDIDAD=100`)
- Pruebas del ejemplo `0011` reproducen exactamente `A> 0B> 00B> 001A> 0011A> 0011(epsilon)> 0011`
- `Main` ejecuta ambos ejemplos (`aababb` y `0011`)

### ✅ Fase 3 – Conversiones (100 %) **COMPLETA**

```
██████████████████  (4/4 items)
```

**Hecho:**
- `ConversorAFDGramatica`: NT = estado, `δ(A,a)=B` → `A > a B`, aceptación → `> epsilon`.
- `ConversorGramaticaAFD`: NT = estado, `A > t B` → `δ(A,t)=B`, `A > t` → transición al estado final extra `F` (creado solo si hace falta; `F#0`, `F#1`… si colisiona con un NT existente), `A > epsilon` → estado de aceptación.
- Validación fail-fast de derecha-linealidad reusada de `EvaluadorGramatica`.
- Decisiones documentadas: gramáticas no deterministas y multi-terminal lanzan `ValidacionException`.
- Tests de equivalencia (5): AFD↔Gramática en ambas direcciones, ida-vuelta AFD→Gram→AFD, ida-vuelta Gram→AFD→Gram, múltiples AFDs distintos (sampling exhaustivo de cadenas ≤ longitud 4).

### ✅ Fase 4 – Archivos I/O (100 %) **COMPLETA**

```
██████████████████  (5/5 items)
```

**Hecho:**
- `LectorAFD` con tolerancia a comentarios (`#`) y líneas vacías; estado inicial = origen de la primera transición; última definición de aceptación gana.
- `LectorGTK` con pre-pasada para declarar NT/terminales fuera de orden; NT inicial = NT de la primera línea; mayúsculas = NT, minúsculas = terminales; `epsilon` = vacío.
- `EscritorAFD` y `EscritorGTK` con el formato del enunciado.
- `ArchivoFactory` dispatcha por extensión (`.afd`/`.gtk`).
- 5 recursos de test: `enunciado.afd`, `enunciado.gtk`, `con_comentarios.afd`, `con_comentarios.gtk`, `con_epsilon.gtk`.
- Tests de ida-vuelta (escribir → leer → evaluar) para ambos formatos.

### ✅ Fase 5 – UI Swing (100 %) **COMPLETA** (sub-fases 5A + 5B + 5C)

```
██████████████████  (13/13 items)
```

**Hecho:**
- Vista base: `Navegador` (interfaz), `VentanaPrincipal` (CardLayout + 8 constantes de pantalla), `PanelPortada`, `PanelMenu`, `BotonAyuda`, `Main` con flag `--demo` o Swing.
- Parsers: `ParserModo1AFD` y `ParserModo2AFD` con 21 tests unitarios.
- Paneles funcionales: `PanelCrearAFD` (modo 1 + modo 2), `PanelCrearGramatica`, `PanelEvaluar` (con conversión AFD↔Gramática), `PanelCargar` (JFileChooser), `PanelGuardar` (lista + JFileChooser destino), `PanelReportes` (Ver Detalle; PDF queda para Fase 6).
- Botón **Ayuda** en todos los paneles.
- Mensajes de error via `Navegador.mostrarError` con `ValidacionException.getMessage()`.

### ✅ Fase 6 – Reportes y Graphviz (100 %) **COMPLETA**

```
██████████████████  (6/6 items)
```

**Hecho:**
- `GeneradorDot` (AFD → DOT): doble círculo para aceptación, flecha desde nodo invisible al estado inicial, símbolos agrupados en una sola arista.
- `GeneradorGraphviz` (DOT + ProcessBuilder → PNG): propiedad `automatas.graphviz.path` para sobreescribir el ejecutable.
- `GeneradorPDF` (OpenPDF): portada + detalle + grafo (o fallback DOT) + 3 válidas + 3 inválidas + evaluadas durante la sesión.
- Degradación elegante si Graphviz no está disponible: PDF se genera con el DOT como texto monoespaciado.
- Botón "Generar PDF" habilitado en `PanelReportes` con JFileChooser.

### ❌ Fases 7-9 (0 %)

- **Fase 7** – Pruebas integrales (2 días, depende de todo lo anterior)
- **Fase 8** – Documentación final + entrega (3 días, depende de Fase 5-7)
- **Fase 9** – Preparación de defensa (post-entrega)

---

## 🏆 Logros recientes (últimos commits)

```
(pendiente commit docs)  ← más reciente
2630512 fix(vista): registrar las 6 pantallas funcionales en el CardLayout
a85e316 Fase 6: Generadores Dot/Graphviz/PDF + integración UI (182/182 tests)
e287ee7 docs: actualizar estado/docs a 90% avance (Fases 0-5 completas)
28e932f Fase 5C: 6 paneles Swing funcionales (Crear AFD/Gram, Evaluar, Cargar, Guardar, Reportes)
5a161c0 Fase 5B: parsers AFD (modo 1 + modo 2) con tests (163/163 tests)
8d6f7e2 Fase 5A: vista base Swing (VentanaPrincipal + PanelPortada + PanelMenu + BotonAyuda)
65d0c5e docs: actualizar estado/docs a 58% avance (Fases 0-4 completas)
6bf9867 Fase 4: Lectores/Escritores .afd/.gtk + Factory (142/145 tests)
2c6f139 docs: actualizar estado/docs a 51% avance (Fases 0-3 completas)
5b0f296 Fase 3: Conversores AFD↔Gramática + equivalencia (105/105 tests)
2ae73da docs: actualizar estado/docs a 46% avance (Fases 0-2 completas)
0b5e1b7 Fase 2: EvaluadorGramatica + GeneradorCadenas + Historial (85/85 tests)
760c020 docs: agregar README.md completo + LICENSE (MIT)
041746a docs: agregar PROMPT_RESUMIR.md para retomar sesiones futuras
2f2a740 docs: actualizar ESTADO/CHECKLIST/DUDAS post-refactor Fase 1
d94cc5c Refactor Fase 1: inmutabilidad profunda + edge cases + javadoc
5cb227b Fix: remove broken platform reference (platform.active=JDK_17)
8ab9454 Fase 1: modelo AFD/Gramática + validaciones + JUnit 5 (38/38 tests)
```

### 🎉 Hitos alcanzados

- 🏅 **Modelo robusto** con 182/182 tests pasando (Fases 1-6)
- 🏅 **Refactor completo** con inmutabilidad profunda y Javadoc
- 🏅 **Evaluador AFD** reproduce exactamente la ruta del enunciado (`aababb`)
- 🏅 **Evaluador Gramática** reproduce exactamente la expansión del enunciado (`0011`)
- 🏅 **GeneradorCadenas + Historial** soportan §1.6 "ambas"
- 🏅 **Conversiones AFD↔Gramática** con equivalencia verificada (ida-vuelta)
- 🏅 **Lectores/Escritores .afd/.gtk** con Factory y pruebas de ida-vuelta
- 🏅 **UI Swing completa** (8 paneles + parsers + Factory + ayuda)
- 🏅 **Reportes PDF** con OpenPDF + Graphviz (degradación elegante si falta dot)
- 🏅 **Documentación completa**: 8 documentos (README + LICENSE + 6 .md)
- 🏅 **Repo en GitHub** público con SSH configurado persistentemente
- 🏅 **Stack validado**: JDK 17 + NetBeans + Graphviz + OpenPDF funcionando

---

## ❓ Preguntas pendientes al catedrático

> 🔴 = **CRÍTICA** (bloquea trabajo inmediato)
> 🟡 = IMPORTANTE (afecta fases siguientes)
> 🟢 = DE IMPLEMENTACIÓN (técnica, se puede asumir si no responde)

### 🟡 Importantes sin responder (afectan Fase 4-5)

#### 1.2 Estado final extra en Gramática → AFD 🟡
> *"Para producciones como `B>c` (terminal sin NT en el lado derecho), ¿se requiere agregar un **estado final extra**? ¿Cuál es el criterio exacto?"*

- **Decisión propia (documentada en Fase 3):** `ConversorGramaticaAFD` crea el estado `F` **solo si hay alguna producción terminal-only**. Si el NT `F` ya existe, usa `F#0`, `F#1`, … Si no hay producciones terminal-only, no se crea `F` (los NTs con producción `epsilon` son las aceptaciones).

#### 1.4 Evaluar Cadenas: ¿AFD o Gramática? ✅ IMPLEMENTADO
- El menú "Evaluar" buscará el nombre en `RepositorioAutomatas` (unificado para AFDs y gramáticas). Estrategia via interfaz `Evaluador`.

#### 1.7 Cuándo se dispara la conversión 🟡
> *"La conversión AFD↔Gramática: ¿se hace **automáticamente al crear** cada uno (con un botón 'Convertir a...') o desde una opción en el menú?"*

- **Decisión propia (a confirmar):** opción explícita en el menú **Reportes** o submenú **Conversiones** (la integración a la UI es Fase 5). No automática.

### 🟡 Importantes (4 – afectan Fase 3-5)

#### 1.2 Estado final extra en Gramática → AFD 🟡
> *"Para producciones como `B>c` (terminal sin NT en el lado derecho), ¿se requiere agregar un **estado final extra**? ¿Cuál es el criterio exacto?"*

- **Recomendación:** asumir convención propia (p. ej., `F` como estado final genérico) y documentarla.

#### 1.4 Evaluar Cadenas: ¿AFD o Gramática? 🟡
> *"El menú 'Evaluar Cadenas' dice 'pide nombre de la gramática', pero las rutas se describen también para AFD. ¿El nombre puede ser tanto de AFD **como** de gramática?"*

- **Recomendación:** búsqueda unificada en `RepositorioAutomatas`.

#### 1.7 Cuándo se dispara la conversión 🟡
> *"La conversión AFD↔Gramática: ¿se hace **automáticamente al crear** cada uno (con un botón 'Convertir a...') o desde una opción en el menú?"*

- **Recomendación:** opción explícita en el menú "Reportes" o un submenú "Conversiones".

#### 2.2 Modo 1 y Modo 2 de transiciones (excluyentes o combinables) 🟡
> *"¿Un mismo AFD puede crearse combinando transiciones de Modo 1 y Modo 2, o son excluyentes?"*

- **Recomendación:** asumir que se permite combinar (la última entrada gana).

### 🟢 De implementación (6 – técnicas, varias ya resueltas)

| # | Pregunta | Estado |
|---|----------|--------|
| §2.1 | Formato cuando no hay transición en AFD (`?` aceptable?) | 🟡 asumir `?` con leyenda |
| §2.3 | Librería de PDF (OpenPDF/iText/PDFBox) | ✅ **Resuelta**: OpenPDF 1.3.43 |
| §2.4 | JDK y versión de NetBeans | ✅ **Resuelta**: JDK 17 + NetBeans 17+ |
| §2.5 | Graphviz en la máquina del calificador | 🟡 ruta configurable + incluir `.dot` aparte |
| §2.6 | Cantidad mínima de archivos de prueba propios | 🟡 3 `.afd` + 3 `.gtk` con casos borde |
| §1.3 | "¿Expresión regular?" en el diagrama de flujo | 🟢 asumir fuera de alcance |

### 📤 Estado de las preguntas

- 🟢 **§1.1** (derecha pura) — resuelta
- 🟢 **§1.5** (epsilon) — asumida
- 🟢 **§1.6** (PDF ambas) — resuelta
- 🟡 **§1.2** (estado final `F`) — **asumida** en Fase 3
- 🟡 **§1.7** (cuándo se dispara conversión) — **asumida** (opción en menú)
- 🟡 Resto pendientes (afectan Fase 4 y 5)

---

## 🚀 Próximos pasos concretos

### 🟢 Inmediato (esta semana)

1. **📤 Enviar `DUDAS_CONSULTAR.md` al catedrático** (las preguntas restantes, sobre todo §1.2).
2. **🛠️ Crear archivos de prueba propios** (3 `.afd` + 3 `.gtk` con casos borde).
3. **🛠️ Completar datos de `DatosCurso`** (sección, carné, catedrático).

### ✅ Fase 2 – completada

1. ✅ `EvaluadorGramatica` (derecha pura, con validación fail-fast).
2. ✅ Formato de `epsilon` asumido.
3. ✅ `GeneradorCadenas` (automático) + `HistorialEvaluaciones` (Singleton).
4. ✅ Pruebas del ejemplo `0011` del enunciado (10 tests específicos).
5. ✅ Detección de ciclos con poda por estado `(NT, prefijo)`.

### ✅ Fase 3 – completada

1. ✅ `ConversorAFDGramatica` + 6 tests.
2. ✅ `ConversorGramaticaAFD` + 9 tests (incluye `F` y colisiones).
3. ✅ `EquivalenciaConversionTest` + 5 tests (ida-vuelta en ambas direcciones + equivalencia sobre muestreo exhaustivo).
4. ✅ 105/105 tests pasan.

### 🟡 Fase 4 (siguiente, 4 días)

### ✅ Fase 4 – completada

1. ✅ `LectorAFD` + `LectorGTK` (con tolerancia a comentarios).
2. ✅ `EscritorAFD` + `EscritorGTK` (formato del enunciado).
3. ✅ `ArchivoFactory` con dispatch por extensión.
4. ✅ Pruebas ida-vuelta para ambos formatos.
5. ✅ 142/142 tests pasan.

### 🟡 Fase 5 (siguiente, 6 días)

### 🟡 Fase 5 (6 días)

- `PanelCrearAFD` (con parser modo 1 y modo 2).
- `PanelCrearGramatica`, `PanelEvaluar`, `PanelCargar`, `PanelGuardar`, `PanelReportes`.
- Propagar botón **Ayuda** a todos los paneles.

### 🟡 Fase 6 (4 días)

- `GeneradorDot` + `GeneradorGraphviz` (ProcessBuilder).
- `GeneradorPDF` con OpenPDF.
- Ensamblar reporte final: detalle + grafo + ≥3 válidas + ≥3 inválidas + evaluadas.

---

## 📅 Cronograma restante

| Semana | Fechas | Fases | Estado |
|:------:|--------|-------|:------:|
| 1 | 03 – 11 oct | 0 + 1 | ✅ ✅ |
| 2 | 12 – 18 oct | 2 + 3 | ✅ ✅ |
| 3 | 19 – 25 oct | 4 | ❌ |
| 4 | 26 oct – 01 nov | 5 + arranque 6 | ❌ |
| 5 | 02 – 05 nov | 6 + 7 + 8 (entrega) | ❌ |
| post | 06 nov + | 9 | ❌ |

> ⚠️ **Colchón actual: 0 días.** Si Fase 0 o Fase 1 se atrasan, recortar adornos de UI antes que funcionalidad.

---

## ⚠️ Riesgos y mitigaciones

| # | Riesgo | Probabilidad | Impacto | Mitigación |
|---|--------|:------------:|:-------:|------------|
| 1 | Catedrático no responde a §1.2 | 🟡 Alta | 🔴 Alto | Asumir convención propia (estado final `F`) y documentar |
| 2 | Graphviz no instalado en máquina del calificador | 🟡 Media | 🟡 Medio | Ruta configurable + `.dot` aparte dentro del `.zip` |
| 3 | Bucles infinitos en `EvaluadorGramatica` | 🟢 **Mitigado** | 🟡 Medio | Poda por estado `(NT, prefijo)` + `MAX_PROFUNDIDAD=100` |
| 4 | Formato `.gtk` ambiguo entre Lector/Escritor | 🟡 Media | 🟡 Medio | Convención `epsilon` documentada; Lector/Escritor en Fase 4 |
| 5 | Conflicto de nombres AFD/gramática en repositorio | 🟢 Baja | 🟡 Medio | Repositorio único (ya implementado) |
| 6 | Documentación al final | 🟡 Media | 🟡 Medio | Ir tomando capturas durante cada fase |
| 7 | Tiempo insuficiente para los 6 paneles Swing | 🔴 Alta | 🟡 Medio | Empezar Fase 5 en paralelo con Fase 3/4 |

---

## 📚 Cómo retomar este MD en otra sesión

### Para vos

1. **Antes de cerrar la sesión actual:** verificá que el último commit incluya este MD actualizado.
2. **Para abrir una nueva sesión de IA:** pegá este MD completo + el `git log --oneline -5` al inicio del chat. Eso le da a la IA todo el contexto.
3. **Si solo querés actualizar algo:** editá este MD directamente y commiteá con `docs: actualizar CONTEXTO_PROYECTO.md`.

### Para una IA nueva

Si abrís un chat sin contexto, pegá este bloque:

```markdown
# Resumir sesión — Proyecto Autómatas y Lenguajes Formales

## Contexto
- Path: /home/re/Documents/dev/automatas/
- Repo: https://github.com/jhosef-re/automatas_proyecto_umg
- Entrega: 05-nov-2026 (33 días restantes al 03-oct)
- Stack: Java 17 + NetBeans 31 + JUnit 5 + OpenPDF 1.3.43 + Graphviz 14

## Documentos de referencia (todos en este repo)
1. `CONTEXTO_PROYECTO.md` ← ESTE, léeme primero
2. `Plan_Proyecto_Automatas.md` — plan en 9 fases
3. `Base_Inicial_Proyecto.md` — esqueleto de código
4. `ESTADO_PROYECTO.md` — checklist por fase
5. `DUDAS_CONSULTAR.md` — 13 preguntas al catedrático
6. `CHECKLIST_INSTALACION.md` — setup del entorno
7. `README.md` — cara pública del repo
8. `LICENSE` — MIT

## Estado actual
- 33 % avance (26/78 sub-items)
- Fase 1 ✅ completa + refactor (62/62 tests)
- 🟡 Fase 2 PAUSADA – esperando respuestas del catedrático (§1.1, §1.5, §1.6)

## Último commit
<paste `git log --oneline -1`>

## Lo que necesito
<tu pedido acá>
```

---

## 🔗 Referencias a otros documentos

| Documento | Tamaño | Propósito |
|-----------|:------:|-----------|
| [`README.md`](./README.md) | ~11 KB | Badges, stack, instalación, links Windows |
| [`LICENSE`](./LICENSE) | ~1 KB | Licencia MIT |
| [`Plan_Proyecto_Automatas.md`](./Plan_Proyecto_Automatas.md) | ~15 KB | Plan completo en 9 fases + cronograma |
| [`Base_Inicial_Proyecto.md`](./Base_Inicial_Proyecto.md) | ~24 KB | Esqueleto de código (modelo + vista) |
| [`ESTADO_PROYECTO.md`](./ESTADO_PROYECTO.md) | ~8 KB | Checklist por fase con entregables |
| [`DUDAS_CONSULTAR.md`](./DUDAS_CONSULTAR.md) | ~7 KB | 13 preguntas pendientes al catedrático |
| [`CHECKLIST_INSTALACION.md`](./CHECKLIST_INSTALACION.md) | ~9 KB | Setup del entorno (Linux + NetBeans Flatpak) |
| [`PROMPT_RESUMIR.md`](./PROMPT_RESUMIR.md) | ~6 KB | Plantillas para retomar sesiones futuras |
| **Este archivo** | ~16 KB | **Mapa completo del proyecto (este MD)** |

---

## 📝 Notas de versión

- **v1.5.1** (06-oct-2026): Fix post-Fase 6 — `VentanaPrincipal` ahora registra las 6 pantallas funcionales (`PanelCrearAFD`, `PanelCrearGramatica`, `PanelEvaluar`, `PanelCargar`, `PanelGuardar`, `PanelReportes`) en el `CardLayout`. Sin esto, los botones del menú navegaban a nombres no registrados (fallo silencioso de `CardLayout`).
- **v1.6** (06-oct-2026): Documenta el fix v1.5.1; actualiza historial con `2630512`.
- **v1.5** (06-oct-2026): Fase 6 completa — `GeneradorDot`, `GeneradorGraphviz` (ProcessBuilder + ruta configurable), `GeneradorPDF` (OpenPDF) + 19 tests nuevos (182/182). `PanelReportes` botón PDF habilitado. Degradación elegante si Graphviz no está disponible.
- **v1.4** (06-oct-2026): Fase 5 completa (sub-fases 5A + 5B + 5C) — `Navegador`, `VentanaPrincipal`, `PanelPortada`, `PanelMenu`, `BotonAyuda`, `ParserModo1AFD`, `ParserModo2AFD`, `PanelCrearAFD`, `PanelCrearGramatica`, `PanelEvaluar`, `PanelCargar`, `PanelGuardar`, `PanelReportes` + 21 tests nuevos (163/163). Botón PDF queda para Fase 6.
- **v1.3** (04-oct-2026): Fase 4 completa — `LectorAFD`, `LectorGTK`, `EscritorAFD`, `EscritorGTK`, `ArchivoFactory` + 5 recursos de test + 37 tests nuevos (142/142).
- **v1.2** (04-oct-2026): Fase 3 completa — `ConversorAFDGramatica`, `ConversorGramaticaAFD` (con estado final extra `F`), `EquivalenciaConversionTest` + 20 tests nuevos (105/105). §1.2 y §1.7 asumidas.
- **v1.1** (04-oct-2026): Fase 2 completa — `EvaluadorGramatica`, `GeneradorCadenas`, `HistorialEvaluaciones`, `RegistroEvaluacion` + 23 tests nuevos (85/85). Respuestas del catedrático §1.1 y §1.6 integradas; §1.5 asumida.
- **v1.0** (03-oct-2026): creación inicial con estado al cierre de Fase 1.

---

> 💡 **Tip:** si tu IA tiene contexto limitado, pegá **solo este MD** + el último `git log`. Con eso ya tiene el 90 % de lo que necesita para ayudarte.