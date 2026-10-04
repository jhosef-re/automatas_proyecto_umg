# 📘 Contexto del Proyecto – Autómatas y Lenguajes Formales

> **Documento vivo**: este es el "mapa completo" del estado del proyecto.
> Cada vez que avancemos una fase, lo actualizo con los nuevos commits,
> % de progreso y preguntas respondidas.
>
> **Última actualización:** 03-oct-2026 · **Próxima entrega:** 05-nov-2026

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
| ⏳ **Días restantes** | **33 días** (al 03-oct) |
| 📊 **Avance global** | **33 %** (26/78 sub-ítems completos) |
| 🚦 **Estado actual** | 🟡 Pausado – esperando respuestas del catedrático a **3 preguntas críticas** |
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

### 🎯 Global: **33 %** (26 / 78 sub-ítems)

```
███████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 33 %
                              ▲
                              │
                          AQUÍ ESTAMOS
```

### 📋 Desglose por fase

| Fase | Descripción                                  | % fase | Items | Barra |
|:----:|----------------------------------------------|:------:|:-----:|:-----:|
| **0** | Preparación (instalaciones + repo + dudas)   | **75 %** | 6/8 | `█████████████░░░` |
| **1** | Modelo + validaciones + tests                | **100 %** | 17/17 | `█████████████████` |
| **2** | `EvaluadorGramatica` + `GeneradorCadenas`    | **43 %** | 3/7 | `████████░░░░░░░░` |
| **3** | Conversiones AFD ↔ Gramática                 | **0 %** | 0/4 | `░░░░░░░░░░░░░░░░` |
| **4** | Lectores/Escritores `.afd` y `.gtk`          | **0 %** | 0/5 | `░░░░░░░░░░░░░░░░` |
| **5** | UI Swing: 7 paneles + menú                   | **38 %** | 5/13 | `███████░░░░░░░░░` |
| **6** | Reportes PDF + Graphviz                      | **0 %** | 0/6 | `░░░░░░░░░░░░░░░░` |
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

#### 🔍 Fase 2 – Evaluación (3/7)
- ✅ `Evaluador` (interfaz Strategy)
- ✅ `ResultadoEvaluacion` con formato de `detalle` documentado
- ✅ `EvaluadorAFD` con caso `aababb` listo
- ❌ `EvaluadorGramatica` (bloqueado por §1.1 y §1.5)
- ❌ `GeneradorCadenas` (bloqueado por §1.6)
- ❌ Pruebas del ejemplo `0011` del enunciado
- ❌ Manejo de recursión por izquierda

#### 🖼️ Fase 5 – UI Swing (5/8)
- ✅ `VentanaPrincipal` (CardLayout)
- ✅ `PanelPortada` (ENTER para continuar)
- ✅ `PanelMenu` (estructura con 8 botones)
- ✅ `DatosCurso` (placeholders listos para completar)
- ✅ `Main.java`
- ❌ `PanelCrearAFD`, `PanelCrearGramatica`, `PanelEvaluar`, `PanelCargar`, `PanelGuardar`, `PanelReportes`
- 🟡 Botón **Ayuda** existe solo en `PanelMenu`, falta propagar

### ❌ Lo que falta (52 sub-ítems)

- 🟡 **Fase 2:** 4 ítems (esperando dudas del catedrático)
- ❌ **Fase 3:** 4 ítems (conversiones)
- ❌ **Fase 4:** 5 ítems (lectores/escritores)
- ❌ **Fase 5:** 8 ítems (6 paneles + 2 extras)
- ❌ **Fase 6:** 6 ítems (PDF + Graphviz)
- ❌ **Fase 7:** 4 ítems (pruebas integrales)
- ❌ **Fase 8:** 4 ítems (manuales + entrega)
- ❌ **Fase 9:** 3 ítems (preparación defensa)

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

### 🟡 Fase 2 – Evaluación de cadenas (43 %) **PAUSADA**

```
████████░░  (3/7 items)
```

**Hecho:**
- Interfaz `Evaluador` (Strategy)
- `EvaluadorAFD` con formato de ruta exacto del enunciado

**Pendiente (esperando catedrático):**
- ❌ `EvaluadorGramatica` (bloqueado por §1.1, §1.5)
- ❌ `GeneradorCadenas` (bloqueado por §1.6)
- ❌ Pruebas del ejemplo `0011`
- ❌ Manejo de recursión por izquierda + ciclos con epsilon

### ❌ Fase 3 – Conversiones (0 %)

```
░░░░░░░░░░  (0/4 items)
```

Bloqueada por §1.2 (estado final extra en Gramática → AFD).

### ❌ Fase 4 – Archivos I/O (0 %)

```
░░░░░░░░░░  (0/5 items)
```

Bloqueada parcialmente por §1.5 (formato de epsilon en `.gtk`).

### 🟡 Fase 5 – UI Swing (38 %)

```
███████░░  (5/13 items)
```

**Hecho:**
- `VentanaPrincipal` con `CardLayout` para navegación
- `PanelPortada` con ENTER funcional
- `PanelMenu` con 8 botones (3 placeholders operativos, 5 `pendiente(...)`)
- `DatosCurso` con placeholders
- `Main` lanza la app

**Pendiente:**
- ❌ 6 paneles funcionales: Crear AFD, Crear Gramática, Evaluar, Cargar, Guardar, Reportes
- 🟡 Propagar botón **Ayuda** a todos los paneles
- ❌ Mensajes de error claros por validación
- ❌ Parser del modo 1 y modo 2 de transiciones

### ❌ Fase 6 – Reportes y Graphviz (0 %)

```
░░░░░░░░░░  (0/6 items)
```

Depende de `GeneradorCadenas` (Fase 2) y archivos `.afd/.gtk` (Fase 4).

### ❌ Fases 7-9 (0 %)

- **Fase 7** – Pruebas integrales (2 días, depende de todo lo anterior)
- **Fase 8** – Documentación final + entrega (3 días, depende de Fase 5-7)
- **Fase 9** – Preparación de defensa (post-entrega)

---

## 🏆 Logros recientes (últimos commits)

```
760c020 docs: agregar README.md completo + LICENSE (MIT)         ← más reciente
041746a docs: agregar PROMPT_RESUMIR.md para retomar sesiones futuras
2f2a740 docs: actualizar ESTADO/CHECKLIST/DUDAS post-refactor Fase 1
d94cc5c Refactor Fase 1: inmutabilidad profunda + edge cases + javadoc
5cb227b Fix: remove broken platform reference (platform.active=JDK_17)
8ab9454 Fase 1: modelo AFD/Gramática + validaciones + JUnit 5 (38/38 tests)
```

### 🎉 Hitos alcanzados

- 🏅 **Modelo robusto** con 62/62 tests pasando (Fase 1)
- 🏅 **Refactor completo** con inmutabilidad profunda y Javadoc
- 🏅 **Documentación completa**: 7 documentos (README + LICENSE + 5 .md)
- 🏅 **Repo en GitHub** público con SSH configurado persistentemente
- 🏅 **Evaluador AFD** reproduce exactamente la ruta del enunciado (`aababb`)
- 🏅 **Stack validado**: JDK 17 + NetBeans + Graphviz + OpenPDF funcionando

---

## ❓ Preguntas pendientes al catedrático

> 🔴 = **CRÍTICA** (bloquea trabajo inmediato)
> 🟡 = IMPORTANTE (afecta fases siguientes)
> 🟢 = DE IMPLEMENTACIÓN (técnica, se puede asumir si no responde)

### 🔴 Críticas (3 – bloquean Fase 2)

#### 1.1 Forma de las gramáticas (lineal izq/der/ambas) 🔴
> *"El ejemplo del `.gtk` muestra `A>Ab` (recursiva por la izquierda) y `A>bC` (por la derecha). ¿Se debe soportar **cualquier gramática regular** (lineal por izquierda o por derecha) o solo las lineales por la derecha?"*

- **Por qué importa:** el algoritmo de `EvaluadorGramatica` y la conversión gramática→AFD son diferentes para cada caso.
- **Recomendación:** implementar de forma genérica (no asumir una sola forma).
- **Impacto si no se responde:** diseño rígido que obligue a reescribir `EvaluadorGramatica` o el conversor.

#### 1.5 Epsilon en archivos `.gtk` 🔴
> *"El formato `.gtk` mostrado no incluye cómo se representa el vacío. ¿Se usa la palabra `epsilon`, un símbolo especial, o línea en blanco?"*

- **Por qué importa:** `LectorGTK` y `EscritorGTK` deben acordar la misma convención.
- **Recomendación:** usar la palabra `epsilon` (consistente con la UI).
- **Impacto si no se responde:** archivos generados no se pueden recargar.

#### 1.6 Cadenas válidas/inválidas en el PDF 🔴
> *"¿Las cadenas válidas/inválidas del reporte se generan **automáticamente** (BFS por longitud) o deben salir de las que el usuario evaluó durante la sesión?"*

- **Por qué importa:** el generador de cadenas automático es trabajo extra; el alternativo requiere llevar un historial.
- **Recomendación:** generar **ambas**: ≥3 automáticas + las evaluadas por el usuario.
- **Impacto si no se responde:** reporte incompleto o sin ejemplos en alguna categoría.

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

- 🟡 **Dudas redactadas** en `DUDAS_CONSULTAR.md` (13 preguntas)
- ❌ **NO enviadas todavía** al catedrático – **acción prioritaria esta semana**
- ⏳ **Sin respuestas** → Fase 2 pausada hasta recibir §1.1, §1.5, §1.6

---

## 🚀 Próximos pasos concretos

### 🟢 Inmediato (esta semana)

1. **📤 Enviar `DUDAS_CONSULTAR.md` al catedrático** (las 13 preguntas o, al menos, las 3 críticas).
2. **🛠️ Crear archivos de prueba propios** (3 `.afd` + 3 `.gtk` con casos borde).
3. **🛠️ Completar datos de `DatosCurso`** (sección, carné, catedrático).

### 🟡 Cuando lleguen respuestas del catedrático (Fase 2)

1. Implementar `EvaluadorGramatica` según respuesta §1.1.
2. Implementar formato de epsilon según §1.5.
3. Implementar `GeneradorCadenas` (automático + historial) según §1.6.
4. Agregar pruebas del ejemplo `0011` del enunciado.
5. Manejar recursión por izquierda con detección de ciclos.

### 🟡 Fase 3 (siguiente, 5 días)

- `ConversorGramaticaAFD` (con estado final extra según §1.2).
- `ConversorAFDGramatica`.
- Verificación de equivalencia.

### 🟡 Fase 4 (4 días)

- `LectorAFD` + `LectorGTK` con epsilon configurable.
- `EscritorAFD` + `EscritorGTK`.
- Prueba de ida-vuelta: cargar → guardar → cargar.

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
| 2 | 12 – 18 oct | 2 + mitad 3 | 🟡 esperando dudas |
| 3 | 19 – 25 oct | 3 + 4 | ❌ |
| 4 | 26 oct – 01 nov | 5 + arranque 6 | ❌ |
| 5 | 02 – 05 nov | 6 + 7 + 8 | ❌ |
| post | 06 nov + | 9 | ❌ |

> ⚠️ **Colchón actual: 0 días.** Si Fase 0 o Fase 1 se atrasan, recortar adornos de UI antes que funcionalidad.

---

## ⚠️ Riesgos y mitigaciones

| # | Riesgo | Probabilidad | Impacto | Mitigación |
|---|--------|:------------:|:-------:|------------|
| 1 | Catedrático no responde a tiempo | 🟡 Alta | 🔴 Alto | Asumir recomendaciones propias (genérico, `epsilon`, ambas) y documentar |
| 2 | Graphviz no instalado en máquina del calificador | 🟡 Media | 🟡 Medio | Ruta configurable + `.dot` aparte dentro del `.zip` |
| 3 | Bucles infinitos en `EvaluadorGramatica` (recursión izq + epsilon) | 🔴 Alta | 🔴 Alto | Límite de longitud + conjunto de estados visitados |
| 4 | Formato `.gtk` ambiguo entre Lector/Escritor | 🟡 Media | 🟡 Medio | Convención única + prueba con archivos propios |
| 5 | Conflicto de nombres AFD/gramática en repositorio | 🟢 Baja | 🟡 Medio | Repositorio único (ya implementado) |
| 6 | Documentación al final | 🟡 Media | 🟡 Medio | Ir tomando capturas durante cada fase |
| 7 | Tiempo insuficiente para los 6 paneles Swing | 🔴 Alta | 🟡 Medio | Empezar Fase 5 en paralelo con Fase 2 |

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

- **v1.0** (03-oct-2026): creación inicial con estado al cierre de Fase 1.
- Próxima versión: tras implementar Fase 2 (EvaluadorGramatica + GeneradorCadenas).

---

> 💡 **Tip:** si tu IA tiene contexto limitado, pegá **solo este MD** + el último `git log`. Con eso ya tiene el 90 % de lo que necesita para ayudarte.