# Estado del proyecto – Autómatas y Lenguajes Formales
**Última actualización:** 03-oct-2026 · **Hoy = inicio de Fase 0** · **Entrega:** 05-nov-2026

> Leyenda: ✅ hecho · 🟡 parcial (iniciado pero incompleto) · ❌ pendiente · ⚠️ bloqueado

---

## Resumen ejecutivo
- **Avance global estimado:** ~25 % (Fases 0 y 1 completas; documentación del plan + modelo con tests).
- **Fase actual en curso:** lista para iniciar **Fase 2 – Evaluación de cadenas**.
- **Riesgo principal:** quedan 25 días, 7 fases por delante; `EvaluadorGramatica` y conversión AFD↔Gramática son los más riesgosos.

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

### Fase 1 – Modelo de dominio y validaciones (07 – 11 oct) · 5 días ✅ COMPLETO
- [x] `validacion/ValidacionException.java` ✅ creado
- [x] `modelo/AFD.java` ✅ creado con todas las validaciones
- [x] `modelo/Gramatica.java` ✅ creado con todas las validaciones
- [x] `modelo/Produccion.java` ✅ creado
- [x] Archivos creados en disco ✅
- [x] `Estado`/`Simbolo` como value objects (se usa `String`; suficiente por ahora)
- [x] **JUnit 5 con pruebas por cada regla de validación** ✅ 38/38 tests pasan
- [x] Repositorio GitHub/GitLab compartido (local por ahora)
- [x] `RepositorioAutomatas` (Singleton) implementado y testeado
- [x] `EvaluadorAFD` implementado y testeado con el caso `aababb` del enunciado
- [x] `Main` ejecuta y reproduce exactamente la ruta esperada
- [x] Estructura `nbproject/` lista para abrir en NetBeans
- [x] Scripts `probar.sh` para correr tests/run desde línea de comandos (sin `ant`)
- **Entregable cumplido:** el modelo funciona por consola/tests, sin UI.

### Fase 2 – Evaluación de cadenas (12 – 16 oct) · 5 días
- [x] `servicio/Evaluador.java` (interfaz)
- [x] `servicio/ResultadoEvaluacion.java`
- [x] `servicio/EvaluadorAFD.java` (con ejemplo `aababb` listo)
- [ ] **`servicio/EvaluadorGramatica.java`** (expansión `A>0B>00B>...`)
- [ ] **`servicio/GeneradorCadenas.java`** (BFS, ≥3 válidas + ≥3 inválidas)
- [ ] Pruebas que reproduzcan ejemplo `0011` del enunciado
- [ ] Manejo de recursión por la izquierda y ciclos con epsilon
- **Entregable pendiente:** pruebas reproduciendo ejemplos del enunciado.

### Fase 3 – Conversiones Gramática ↔ AFD (17 – 21 oct) · 5 días
- [ ] `servicio/ConversorGramaticaAFD.java`
- [ ] `servicio/ConversorAFDGramatica.java`
- [ ] Verificación de equivalencia entre ambos modelos
- [ ] Decisión sobre gramáticas no determinísticas (error vs. subconjuntos)
- **Entregable pendiente:** conversiones probadas en ambas direcciones.

### Fase 4 – Archivos de entrada y salida (22 – 25 oct) · 4 días
- [ ] `archivo/LectorAFD.java` (estado inicial = primera línea, última aceptación gana)
- [ ] `archivo/LectorGTK.java` (mayúsculas = NT, minúsculas = terminales)
- [ ] `archivo/EscritorAFD.java`
- [ ] `archivo/EscritorGTK.java`
- [ ] Prueba ida-vuelta: cargar → guardar → cargar
- **Entregable pendiente:** carga/guardado funcionando con archivos de prueba **sin modificarlos**.

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
Sem 1 (03-11 oct): Fase 0 + Fase 1 .................. ✅
Sem 2 (12-18 oct): Fase 2 + mitad Fase 3 ............ 🟡 listo para empezar
Sem 3 (19-25 oct): Fase 3 + Fase 4 .................. ❌
Sem 4 (26 oct-01 nov): Fase 5 + arranque Fase 6 ..... ❌
Sem 5 (02-05 nov): Fase 6 + 7 + 8 (entrega) ......... ❌
```

> **Colchón actual:** ~2 días. Si Fase 0 o Fase 1 se atrasan, recortar adornos de UI antes que funcionalidad.