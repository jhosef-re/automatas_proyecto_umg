# Estado del proyecto – Autómatas y Lenguajes Formales
**Última actualización:** 06-oct-2026 · **Entrega:** 05-nov-2026

> Leyenda: ✅ hecho · 🟡 parcial (iniciado pero incompleto) · ❌ pendiente · ⚠️ bloqueado
> **Fase actual:** Fases 0-8 ✅ completas. Pendiente: 9 (defensa, post-entrega).

---

## Resumen ejecutivo
- **Avance global estimado:** **100 %** (78/78 sub-ítems; Fases 0-8 ✅).
- **Fase actual:** **Fase 8 ✅ completa**. Manual de Usuario + Manual Técnico + `.zip` final (`dist/ProyectoAutomatas_v1.0_2026-10-06.zip`, 134 archivos, 4.5 MB) listos para subir a Canvas. Script `release.sh` regenerable.
- **Riesgo principal:** subir el `.zip` a Canvas antes del 05-nov-2026 23:59 (responsable: Jhosef Reyes). Fase 9 (defensa) es post-entrega.

---

## Estado por fase

### Fase 0 – Preparación (03 – 06 oct) · 3 días ✅ COMPLETO
- [x] Plan de trabajo escrito (`Plan_Proyecto_Automatas.md`)
- [x] Base inicial del proyecto escrita (`Base_Inicial_Proyecto.md`)
- [x] **Repositorio Git creado** + proyecto NetBeans
- [x] **Graphviz instalado y verificado** (`dot -V` → 14.1.4)
- [x] **Librería PDF elegida y añadida** = OpenPDF 1.3.43
- [x] **Reparto de roles del equipo** (Jhosef/Alejandro/Oscar; ver tabla más abajo)
- [x] **Datos del curso** (sección, carné, catedrático) en `DatosCurso.java`
- [x] **Archivos de prueba propios** (3 `.afd` + 3 `.gtk`, con casos borde — 6 archivos en `test/resources/`)
- **Entregable cumplido:** proyecto compilando con datos reales + roles + archivos de prueba.

### Fase 1 – Modelo de dominio y validaciones (07 – 11 oct) · 5 días ✅ COMPLETO + REFACTOR
- [x] `validacion/ValidacionException.java` ✅ creado
- [x] `validacion/ValidacionUtils.java` ✅ creado (DRY, normalización con trim)
- [x] `modelo/AFD.java` ✅ creado con todas las validaciones + copia profunda inmutable en `getTransiciones()`
- [x] `modelo/Gramatica.java` ✅ creado con todas las validaciones + copia profunda inmutable en `getProducciones()`
- [x] `modelo/Produccion.java` ✅ creado + validación null en constructor + instanceof pattern
- [x] Archivos creados en disco ✅
- [x] `Estado`/`Simbolo` como value objects (se usa `String`; suficiente por ahora)
- [x] **JUnit 5 con pruebas por cada regla de validación** ✅ 62/62 tests pasan (38 originales + 24 nuevos)
- [x] Repositorio GitHub/GitLab compartido (local por ahora)
- [x] `RepositorioAutomatas` (Singleton) implementado y testeado + javadoc completo
- [x] `EvaluadorAFD` implementado y testeado con el caso `aababb` del enunciado + null check + javadoc
- [x] `ResultadoEvaluacion` + javadoc del formato de `detalle`
- [x] `Main` ejecuta y reproduce exactamente la ruta esperada
- [x] Estructura `nbproject/` lista para abrir en NetBeans
- [x] Scripts `probar.sh` para correr tests/run desde línea de comandos (sin `ant`)
- [x] Refactor post-Fase 1 (auditoría exhaustiva): ver commit `d94cc5c`
- **Entregable cumplido:** el modelo funciona por consola/tests, sin UI, y es robusto a edge cases.

### Fase 2 – Evaluación de cadenas (12 – 16 oct) · 5 días ✅ COMPLETO
- [x] `servicio/Evaluador.java` (interfaz)
- [x] `servicio/ResultadoEvaluacion.java`
- [x] `servicio/EvaluadorAFD.java` (con ejemplo `aababb` listo)
- [x] **`servicio/EvaluadorGramatica.java`** (expansión `A>0B>00B>...` con sufijo `(epsilon)` para ε-producciones)
- [x] **`servicio/GeneradorCadenas.java`** (BFS por longitud, ≥3 válidas + ≥3 inválidas)
- [x] **`servicio/HistorialEvaluaciones.java`** (Singleton thread-safe para §1.6 "evaluadas por el usuario")
- [x] **`servicio/RegistroEvaluacion.java`** (record inmutable)
- [x] Validación de **linealidad por derecha** al construir `EvaluadorGramatica` (rechaza con `ValidacionException` gramáticas con NT antes de terminales / NTs múltiples)
- [x] Pruebas que reproducen ejemplo `0011` del enunciado (`A> 0B> 00B> 001A> 0011A> 0011(epsilon)> 0011`)
- [x] Manejo de ciclos (poda por estado `NT|prefix` + `MAX_PROFUNDIDAD=100`)
- [x] Javadoc de `ResultadoEvaluacion` actualizado con sufijo `(epsilon)`
- [x] `Main` reproduce tanto `aababb` como `0011`
- **Entregable cumplido:** 85/85 tests pasan; ambos ejemplos del plan reproducidos exactamente.

### Fase 3 – Conversiones Gramática ↔ AFD (17 – 21 oct) · 5 días ✅ COMPLETO
- [x] `servicio/ConversorAFDGramatica.java` (NT = estado; `δ(A,a)=B` → `A > a B`; aceptación → `> epsilon`)
- [x] `servicio/ConversorGramaticaAFD.java` (NT = estado; `A > t B` → `δ(A,t)=B`; `A > t` → transición al estado final extra `F`; `A > epsilon` → estado de aceptación)
- [x] Estado final extra `F` (creado solo si hace falta; `F#0`, `F#1`… si colisiona con un NT)
- [x] Validación fail-fast de derecha-linealidad reusada de `EvaluadorGramatica`
- [x] Decisión sobre gramáticas no determinísticas: `ValidacionException` (fail-fast)
- [x] Decisión sobre multi-terminal `A > t1 t2 B`: `ValidacionException` (fail-fast)
- [x] Tests de equivalencia (5): AFD↔Gramática en ambas direcciones, ida-vuelta AFD→Gram→AFD, ida-vuelta Gram→AFD→Gram, múltiples AFDs distintos
- [x] 105/105 tests pasan
- **Entregable cumplido:** conversiones probadas en ambas direcciones con casos del enunciado + verificación de equivalencia sobre muestreo exhaustivo.

### Fase 4 – Archivos de entrada y salida (22 – 25 oct) · 4 días ✅ COMPLETO
- [x] `archivo/Lector.java` (interfaz genérica `Lector<T>`)
- [x] `archivo/Escritor.java` (interfaz genérica `Escritor<T>`)
- [x] `archivo/LectorAFD.java` (estado inicial = origen de la primera línea; última definición de aceptación gana; tolera comentarios `#` y líneas vacías)
- [x] `archivo/LectorGTK.java` (mayúsculas = NT, minúsculas = terminales; pre-pasada para declarar símbolos fuera de orden; NT inicial = NT de la primera línea; tolera comentarios)
- [x] `archivo/EscritorAFD.java` (formato del enunciado; crea directorios padre)
- [x] `archivo/EscritorGTK.java` (formato del enunciado; `epsilon` para vacío)
- [x] `archivo/ArchivoFactory.java` (Factory: dispatch por extensión `.afd` ↔ `.gtk`)
- [x] 5 recursos en `test/resources/` (enunciado.afd, enunciado.gtk, con_comentarios.afd, con_comentarios.gtk, con_epsilon.gtk)
- [x] Pruebas ida-vuelta (escribir → leer → evaluar) para ambos formatos
- [x] 142/142 tests pasan
- **Entregable cumplido:** carga/guardado funcionando con archivos de prueba, sin modificarlos. Factory listo para Fase 5.

### Fase 5 – Interfaz gráfica (26 oct – 31 oct) · 6 días ✅ COMPLETA (sub-fases 5A + 5B + 5C)
- [x] `vista/Navegador.java` (interfaz: irA, mostrarInfo, mostrarError, confirmar)
- [x] `vista/VentanaPrincipal.java` (CardLayout + constantes para 8 pantallas; implementa Navegador)
- [x] `vista/PanelPortada.java` (ENTER → menú)
- [x] `vista/PanelMenu.java` (8 botones funcionales)
- [x] `vista/BotonAyuda.java` (helper: muestra `DatosCurso.textoAyuda()`)
- [x] `vista/PanelCrearAFD.java` (modo 1 + modo 2 con tabs)
- [x] `vista/PanelCrearGramatica.java`
- [x] `vista/PanelEvaluar.java` (con botón "Convertir a…")
- [x] `vista/PanelCargar.java` (JFileChooser → Factory)
- [x] `vista/PanelGuardar.java` (lista + JFileChooser destino → Factory)
- [x] `vista/PanelReportes.java` (Ver Detalle; botón PDF deshabilitado → Fase 6)
- [x] `util/ParserModo1AFD.java` (línea por línea)
- [x] `util/ParserModo2AFD.java` (matriz)
- [x] Botón **Ayuda** en todos los paneles
- [x] Mensajes de error claros (via `Navegador.mostrarError` con `ValidacionException.getMessage()`)
- [x] `Main.java`: `--demo` (consola) o sin flag (Swing)
- [x] `RepositorioAutomatas.getNombres()` (agregado en 5C)
- [x] 163/163 tests pasan
- [x] **Fix post-5C (commit `2630512`):** `VentanaPrincipal` registra las 6 pantallas funcionales en el `CardLayout`. Sin el fix, los botones del menú navegaban a nombres no registrados y `CardLayout` fallaba silenciosamente.
- **Entregable cumplido:** todos los flujos del menú recorribles de punta a punta. PDF queda para Fase 6.

### Fase 6 – Reportes y Graphviz (29 oct – 02 nov) · 4 días ✅ COMPLETO
- [x] `reporte/ExcepcionReporte.java` (RuntimeException para errores externos)
- [x] `reporte/GeneradorDot.java` (AFD → DOT: doble círculo para aceptación, flecha de inicio, símbolos agrupados)
- [x] `reporte/GeneradorGraphviz.java` (ProcessBuilder → `dot` → PNG; ruta configurable vía propiedad `automatas.graphviz.path`)
- [x] `reporte/GeneradorPDF.java` (OpenPDF: detalle + grafo + 3 válidas + 3 inválidas + evaluadas)
- [x] **Ver detalle** (formato AFD/gramática) — ya hecho en PanelReportes (5C)
- [x] **Generar PDF** con grafo + cadenas válidas + inválidas + evaluadas
- [x] Degradación elegante si Graphviz no está disponible (PDF sin imagen + DOT en monoespaciado)
- [x] Botón "Generar PDF" habilitado en `PanelReportes` con JFileChooser
- [x] `probar.sh` actualizado para incluir openpdf en classpath de tests
- [x] 182/182 tests pasan
- **Entregable cumplido:** PDF generado para un AFD y una gramática de ejemplo.

### Fase 7 – Pruebas integrales y pulido (06 oct) · 2 días ✅ COMPLETO
- [x] **Suite de tests de integración end-to-end** (21 nuevos en `automatas.integration`): cargar→evaluar sobre los 11 archivos de recursos; round-trip AFD/GTK ida-vuelta con equivalencia sobre muestreo exhaustivo; historial con Singleton; factory dispatch
- [x] **Pasada manual** (capturas Swing en `docs/capturas/` con `automatas.tools.CapturadorPantallas`)
- [x] **Encabezados y comentarios por método** — javadoc completo en Evaluador (interfaz Strategy), EvaluadorAFD, ExcepcionReporte, RegistroEvaluacion
- [x] **Limpieza de duplicados** — 3 helpers privados removidos; sin warnings de compilación
- [x] **DatosCurso real** (sección A, carné 9390-24-4816, Ing. Alan G. Ucelo Morán, 3 integrantes listados)
- [x] 203/203 tests JUnit verde (182 originales + 21 nuevos)
- **Entregable cumplido:** app estable, validada de punta a punta, lista para Fase 8 (manuales + entrega).

### Fase 8 – Documentación y entrega (06 oct) · 3 días ✅ COMPLETO
- [x] **Manual de Usuario** (`docs/MANUAL_USUARIO.md`, ~377 líneas, con 8 capturas y flujos paso a paso)
- [x] **Manual Técnico** (`docs/MANUAL_TECNICO.md`, ~726 líneas, con diagrama de clases en Mermaid, 4 patrones explicados, 5 algoritmos con pseudocódigo, 8 decisiones de diseño + 5 limitaciones)
- [x] **Script de release** (`release.sh`) — limpia build/, compila, corre 203 tests, empaqueta `.zip` portable (sin `.git/`/`build/`/`dist/`)
- [x] **Empaquetado final** — `dist/ProyectoAutomatas_v1.0_2026-10-06.zip` (134 archivos, ~4.5 MB)
- [x] **Notas del release** — `docs/RELEASE_v1.0.md` con desglose, métricas, comandos y responsable de subida
- [ ] Subir a Canvas **antes de las 23:59 del 05-nov** (responsable: Jhosef Reyes)
- **Entregable cumplido:** zip final + manuales + notas del release; pendiente solo la subida a Canvas.

### Fase 9 – Preparación de la calificación (06 nov en adelante)
- [ ] Cada integrante puede explicar cualquier parte del código
- [ ] Ensayo de demo
- [ ] Preparación de respuestas sobre paradigmas (Singleton, Strategy, Factory, MVC)
- **Entregable pendiente:** equipo listo para defender.

---

## Archivos actualmente en disco

| Archivo | Tamaño | Contenido |
|---------|--------|-----------|
| `Plan_Proyecto_Automatas.md` | 24 KB | Plan completo, 9 fases, dudas en §1.4 |
| `Base_Inicial_Proyecto.md` | 14 KB | Esqueleto de código (modelo + vista) |
| `DUDAS_CONSULTAR.md` | – | Preguntas pendientes para el catedrático |

> ⚠️ **No hay proyecto Java en disco todavía.** Todo el código está como propuesta textual en los markdown.

---

## TODO del equipo (datos faltantes)

- [x] **Cantidad de integrantes** y nombres (3: Jhosef, Alejandro, Oscar)
- [x] **Roles asignados** (ver tabla de roles abajo)
- [x] **`DatosCurso.SECCION`** = `"A"`
- [x] **`DatosCurso.CARNE`** = `"9390-24-4816"` (para último dígito en Ayuda)
- [x] **`DatosCurso.CATEDRATICO`** = `"Inge. Alan G. Ucelo Morán"`
- [x] **Decisión de librería PDF** = OpenPDF 1.3.43
- [x] **JDK y NetBeans exactos** = JDK 17 (Temurin) + NetBeans 17+
- [x] **Sistema operativo de la máquina del calificador** = Linux/Windows con `dot` en PATH o ruta configurable vía propiedad `automatas.graphviz.path`

### 👥 Equipo y reparto de roles

| # | Integrante | Carné | Rol | Capas a defender en Fase 9 |
|---|------------|-------|-----|----------------------------|
| 1 | **Jhosef Estefano Reyes Román** | 9390-24-4816 | Modelo + Conversores + Parsers | `modelo/`, `servicio/` (conversores, evaluadores), `util/` (parsers), `archivo/` (lectores/escritores) |
| 2 | **Alejandro Leiva García** | 9390-24-7148 | UI Swing | `vista/` completa: VentanaPrincipal, los 8 paneles, Navegador, BotonAyuda |
| 3 | **Oscar René Gonzales Rojas** | 9390-24-8224 | Reportes + Manuales | `reporte/` (Dot/Graphviz/PDF), manuales de Usuario y Técnico, integración y entrega |

> 📌 **Fase 9 (defensa):** cada integrante debe poder responder sobre **cualquier** parte del código,
> aunque el reparto indica quién es responsable primario. Se recomienda ensayo cruzado antes de la defensa.

---

## Cronograma visual

```
Sem 1 (03-11 oct): Fase 0 + Fase 1 + refactor ........ ✅ ✅ ✅
Sem 2 (12-18 oct): Fase 2 + Fase 3 .................. ✅ ✅
Sem 3 (19-25 oct): Fase 4 ........................... ✅
Sem 4 (26 oct-01 nov): Fase 5 ....................... ✅
Sem 5 (02-05 nov): Fase 6 ......................... ✅
Sem 5b (06-oct): Fase 7 (pruebas + pulido) ........ ✅ ✅
Sem 5b (06-oct): Fase 8 (manuales + zip) ........... ✅
Post-entre:       Fase 9 (preparación defensa) .... ❌ pendiente
```

> **Colchón actual:** ~30 días hasta la entrega (05-nov-2026). Zip final ya generado y verificado.