# Release Notes v1.0 — 06-oct-2026

> Snapshot de la entrega final (Fase 8) listo para subir a Canvas.
>
> **Archivo:** `dist/ProyectoAutomatas_v1.0_2026-10-06.zip` (~4.5 MB, 134 archivos)
>
> **Para regenerar:** `./release.sh` (corre los 203 tests y falla si alguno no es verde).

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

## ✅ Estado al cierre

| Métrica | Valor |
|---------|-------|
| **Avance global** | **100 %** (Fases 0-7 completas, Fase 8 release) |
| **Tests JUnit** | 203/203 verde (182 originales + 21 nuevos de Fase 7) |
| **Warnings de compilación** | 0 |
| **Archivos de prueba** | 11 (5 enunciado + 6 propios) |
| **Capturas** | 8 PNG |
| **Manuales** | 2 (Usuario + Técnico) |
| **Integrantes** | 3 (Jhosef, Alejandro, Oscar) |
| **Catedrático** | Inge. Alan G. Ucelo Morán |
| **Stack** | JDK 17 + Swing + OpenPDF 1.3.43 + JUnit 5.10 + Graphviz 14.1.4 |

---

## 🚀 Cómo ejecutar la app desde el zip

```bash
unzip ProyectoAutomatas_v1.0_2026-10-06.zip -d ProyectoAutomatas
cd ProyectoAutomatas
./probar.sh test      # debería mostrar "203 tests successful"
./probar.sh run       # abre la GUI Swing
```

### Requisitos del calificador
- **JDK 17+** (`java -version`).
- **Graphviz** opcional para reportes con grafo (`dot -V`). Sin Graphviz el PDF se genera con el DOT como texto monoespaciado.

---

## 📤 Subida a Canvas

**Responsable:** Jhosef Reyes (9390-24-4816).
**Plazo:** antes del **05-nov-2026 23:59** (sin prórroga).
**Archivo a subir:** `dist/ProyectoAutomatas_v1.0_2026-10-06.zip`.

> Subir con holgura, no en el último minuto.

---

## 📋 Próximos pasos (Fase 9 — post-entrega)

- [ ] Cada integrante ensaya la defensa (15 min máximo).
- [ ] Jhosef explica modelo + conversores + parsers + archivo/.
- [ ] Alejandro explica la UI Swing completa (8 paneles).
- [ ] Oscar explica reportes PDF + manuales + entrega.
- [ ] Ensayo cruzado: cada uno responde sobre cualquier parte del código.