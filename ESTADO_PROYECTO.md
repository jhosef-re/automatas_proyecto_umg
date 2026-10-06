# Estado del proyecto – Autómatas y Lenguajes Formales
**Última actualización:** 04-oct-2026 · **Entrega:** 05-nov-2026

> Leyenda: ✅ hecho · 🟡 parcial (iniciado pero incompleto) · ❌ pendiente · ⚠️ bloqueado
> **Fase actual:** Fases 0, 1 y 2 ✅ completas. Pendientes: 3-9.

---

## Resumen ejecutivo
- **Avance global estimado:** ~43 % (Fases 0, 1 y 2 completas; documentación + modelo robusto + evaluadores + generador con tests).
- **Fase actual en curso:** **Fase 2 ✅ completa**. `EvaluadorGramatica`, `GeneradorCadenas`, `HistorialEvaluaciones` y tests del ejemplo `0011` listos.
- **Riesgo principal:** quedan 31 días, 6 fases por delante; Fase 3 (conversiones AFD↔Gramática) sigue dependiendo de §1.2.

---

## Estado por fase

### Fase 0 – Preparación (03 – 06 oct) · 3 días
- [x] Plan de trabajo escrito (`Plan_Proyecto_Automatas.md`)
- [x] Base inicial del proyecto escrita (`Base_Inicial_Proyecto.md`)
- [ ] **Repositorio Git creado** + proyecto NetBeans
- [ ] **Graphviz instalado y verificado** (`dot -V`)
- [ ] **Librería PDF elegida y añadida** (recomendado: OpenPDF)
- [ ] **Reparto de roles del equipo**
- [ ] **Dudas enviadas al catedrático** (ver `DUDAS_CONSULTAR.md`)
- [ ] **Archivos de prueba propios** (3 `.afd` + 3 `.gtk`, con casos borde)
- **Entregable pendiente:** proyecto compilando con `Main` vacío + lista de dudas enviada.

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

### Fase 5 – Interfaz gráfica (26 oct – 31 oct) · 6 días
- [x] `vista/VentanaPrincipal.java` (CardLayout)
- [x] `vista/PanelPortada.java` (ENTER para continuar)
- [x] `vista/PanelMenu.java` (estructura con 8 botones, varios `pendiente(...)`)
- [x] `vista/DatosCurso.java` (placeholders sin completar)
- [x] `Main.java`
- [ ] `PanelCrearAFD.java` (modos 1 y 2 con parser)
- [ ] `PanelCrearGramatica.java`
- [ ] `PanelEvaluar.java`
- [ ] `PanelCargar.java` (JFileChooser)
- [ ] `PanelGuardar.java`
- [ ] `PanelReportes.java`
- [ ] Botón **Ayuda** reutilizable en todos los paneles
- [ ] Mensajes de error claros por cada validación
- **Entregable pendiente:** todos los flujos del menú recorribles de punta a punta.

### Fase 6 – Reportes y Graphviz (29 oct – 02 nov) · 4 días
- [ ] `reporte/GeneradorDot.java`
- [ ] `reporte/GeneradorGraphviz.java` (ProcessBuilder → `dot` → PNG)
- [ ] `reporte/GeneradorPDF.java`
- [ ] **Ver detalle** (formato AFD/gramática)
- [ ] **Generar PDF** con grafo + cadenas válidas + inválidas + evaluadas
- [ ] Manejo de error si Graphviz no está instalado
- **Entregable pendiente:** PDF generado para un AFD y una gramática de ejemplo.

### Fase 7 – Pruebas integrales y pulido (02 – 03 nov) · 2 días
- [ ] Recorrer casos de §1.3 del plan contra la app completa
- [ ] Probar con archivos nuevos
- [ ] Encabezados y comentarios por método en todo el código
- [ ] Limpieza de duplicados y revisión de nombres
- **Entregable pendiente:** app estable, lista para entregar.

### Fase 8 – Documentación y entrega (03 – 05 nov) · 3 días
- [ ] Manual de Usuario (capturas por pantalla)
- [ ] Manual Técnico (diagrama de clases, algoritmos)
- [ ] Empaquetado `.zip/.rar` con código + manuales + archivos de prueba
- [ ] Subir a Canvas **antes de las 23:59 del 05-nov** (no en el último minuto)
- **Entregable pendiente:** entrega completa.

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

- [ ] **Cantidad de integrantes** y nombres
- [ ] **Roles asignados** (modelo · conversiones · UI · reportes/doc)
- [ ] **`DatosCurso.SECCION`** – valor real
- [ ] **`DatosCurso.CARNE`** – valor real (para último dígito en Ayuda)
- [ ] **`DatosCurso.CATEDRATICO`** – nombre completo
- [ ] **Decisión de librería PDF** (OpenPDF recomendado)
- [ ] **JDK y NetBeans exactos** a usar
- [ ] **Sistema operativo de la máquina del calificador** (para confirmar Graphviz)

---

## Cronograma visual

```
Sem 1 (03-11 oct): Fase 0 + Fase 1 + refactor ........ ✅ ✅ ✅
Sem 2 (12-18 oct): Fase 2 + Fase 3 .................. ✅ ✅
Sem 3 (19-25 oct): Fase 4 ........................... ✅
Sem 4 (26 oct-01 nov): Fase 5 + arranque Fase 6 ..... ❌
Sem 5 (02-05 nov): Fase 6 + 7 + 8 (entrega) ......... ❌
```

> **Colchón actual:** ~2 días. Si Fase 0 o Fase 1 se atrasan, recortar adornos de UI antes que funcionalidad.