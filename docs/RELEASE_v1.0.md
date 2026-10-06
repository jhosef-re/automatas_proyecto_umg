# Release Notes v1.0.1 — 06-oct-2026

> Patch sobre v1.0: la portada y el PDF ahora muestran los **3 integrantes**
> del equipo con sus respectivos carnés (antes solo aparecía el carné principal).
>
> **Archivo:** `dist/ProyectoAutomatas_v1.0.1_2026-10-06.zip` (~4.5 MB, 137 archivos)
>
> **Para regenerar:** `./release.sh` (corre los 208 tests y falla si alguno no es verde).

## 📋 Cambios respecto a v1.0

| # | Cambio |
|---|--------|
| 1 | `DatosCurso.nombresCompletos()` — nuevo método estático que devuelve los 3 nombres con carné, uno por línea. |
| 2 | `PanelPortada` — la línea "Carné: 9390-24-4816" se reemplazó por un bloque "Integrantes:" + 3 sub-líneas. |
| 3 | `GeneradorPDF` — el párrafo "Carné: ..." se reemplazó por bloque "Integrantes:" + 3 sub-párrafos (centrados, fuente bold para el título). |
| 4 | `DatosCursoTest` — 5 tests nuevos (208/208 verde total). |
| 5 | `docs/capturas/01_portada.png` — re-capturada con la nueva portada. |
| 6 | `docs/MANUAL_USUARIO.md` — bloque de Portada actualizado a v1.1. |

---

# Release Notes v1.0 — 06-oct-2026 (histórico)

> Snapshot original de la entrega final (Fase 8).
>
> **Archivo:** `dist/ProyectoAutomatas_v1.0_2026-10-06.zip` (~4.5 MB, 134 archivos)
>
> **Para regenerar:** checkout al commit `8e73377` y correr `./release.sh`.

---

## 📦 Contenido (134 archivos)

### Código fuente (42 .java)
```
src/automatas/
├── Main.java
├── modelo/        AFD.java, Gramatica.java, Produccion.java
├── validacion/    ValidacionException.java, ValidacionUtils.java
├── servicio/      Evaluador(.java), EvaluadorAFD, EvaluadorGramatica,
│                  ResultadoEvaluacion, RegistroEvaluacion,
│                  HistorialEvaluaciones, RepositorioAutomatas,
│                  GeneradorCadenas, ConversorAFDGramatica,
│                  ConversorGramaticaAFD
├── archivo/       Lector, Escritor, LectorAFD, LectorGTK,
│                  EscritorAFD, EscritorGTK, ArchivoFactory
├── reporte/       ExcepcionReporte, GeneradorDot, GeneradorGraphviz,
│                  GeneradorPDF
├── vista/         VentanaPrincipal, Navegador, DatosCurso,
│                  PanelPortada, PanelMenu, PanelCrearAFD,
│                  PanelCrearGramatica, PanelEvaluar, PanelCargar,
│                  PanelGuardar, PanelReportes, BotonAyuda
├── util/          ParserModo1AFD, ParserModo2AFD
└── tools/         CapturadorPantallas  (utilidad de mantenimiento)
```

### Tests (22 .java, 11 recursos)
- 203 tests JUnit verde
- 11 archivos de prueba en `test/resources/`:
  - 5 archivos del enunciado (`enunciado.afd`, `enunciado.gtk`, `con_comentarios.afd`, `con_comentarios.gtk`, `con_epsilon.gtk`)
  - 6 archivos propios con casos borde (`un_estado.afd`, `sin_validas.afd`, `lenguaje_ab.afd`, `solo_terminales.gtk`, `disyuncion.gtk`, `multi_terminal.gtk`)

### Librerías (`lib/`, ~4.6 MB)
- `openpdf-1.3.43.jar` — Generación de PDFs (sin dependencia de iText).
- `junit-platform-console-standalone-1.10.2.jar` — JUnit 5 standalone.

### Proyecto NetBeans (`nbproject/`)
- `build-impl.xml`, `project.properties`, `project.xml`, `private/private.xml`, `private/private.properties`.
- `build.xml` (raíz) — script Ant compatible con NetBeans.

### Documentación (9 .md en raíz + 2 manuales en `docs/`)
1. `README.md` — cara pública del repo (badges + instalación).
2. `LICENSE` — MIT.
3. `Plan_Proyecto_Automatas.md` — plan completo en 9 fases.
4. `Base_Inicial_Proyecto.md` — esqueleto de código del modelo + vista.
5. `ESTADO_PROYECTO.md` — checklist actualizado por fase.
6. `CONTEXTO_PROYECTO.md` — mapa completo del proyecto.
7. `DUDAS_CONSULTAR.md` — preguntas al catedrático.
8. `CHECKLIST_INSTALACION.md` — setup del entorno.
9. `PROMPT_RESUMIR.md` — plantilla para retomar sesiones con IA.
10. `docs/MANUAL_USUARIO.md` — capturas + flujos paso a paso.
11. `docs/MANUAL_TECNICO.md` — arquitectura + patrones + algoritmos + diagrama Mermaid.

### Capturas (`docs/capturas/`, 9 archivos)
8 PNG de las pantallas (960×640) + `README.md` que las indexa.

### Scripts
- `probar.sh` — compilar/correr tests/run sin NetBeans.
- `release.sh` — generar el `.zip` de entrega.
- `manifest.mf` — manifiesto del JAR.

---

## ✅ Estado al cierre (v1.0.1)

| Métrica | Valor |
|---------|-------|
| **Avance global** | **100 %** (Fases 0-8 completas) |
| **Tests JUnit** | 208/208 verde (203 originales + 5 nuevos de `DatosCursoTest`) |
| **Warnings de compilación** | 0 |
| **Archivos de prueba** | 11 (5 enunciado + 6 propios) |
| **Capturas** | 8 PNG (01_portada regenerada con 3 integrantes) |
| **Manuales** | 2 (Usuario v1.1 + Técnico) |
| **Integrantes** | **3 visibles** en Portada, PDF y manuales (Jhosef, Alejandro, Oscar) |
| **Catedrático** | Inge. Alan G. Ucelo Morán |
| **Stack** | JDK 17 + Swing + OpenPDF 1.3.43 + JUnit 5.10 + Graphviz 14.1.4 |

---

## 🚀 Cómo ejecutar la app desde el zip

```bash
unzip ProyectoAutomatas_v1.0.1_2026-10-06.zip -d ProyectoAutomatas
cd ProyectoAutomatas
./probar.sh test      # debería mostrar "208 tests successful"
./probar.sh run       # abre la GUI Swing
```

### Requisitos del calificador
- **JDK 17+** (`java -version`).
- **Graphviz** opcional para reportes con grafo (`dot -V`). Sin Graphviz el PDF se genera con el DOT como texto monoespaciado.

---

## 📤 Subida a Canvas

**Responsable:** Jhosef Reyes (9390-24-4816).
**Plazo:** antes del **05-nov-2026 23:59** (sin prórroga).
**Archivo a subir:** `dist/ProyectoAutomatas_v1.0.1_2026-10-06.zip`.

> Subir con holgura, no en el último minuto.

---

## 📋 Próximos pasos (Fase 9 — post-entrega)

- [ ] Cada integrante ensaya la defensa (15 min máximo).
- [ ] Jhosef explica modelo + conversores + parsers + archivo/.
- [ ] Alejandro explica la UI Swing completa (8 paneles).
- [ ] Oscar explica reportes PDF + manuales + entrega.
- [ ] Ensayo cruzado: cada uno responde sobre cualquier parte del código.
- [ ] Revisar respuesta del catedrático a `docs/EMAIL_CATEDRATICO.md` (7 preguntas asumidas).