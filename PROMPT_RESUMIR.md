# Prompt para resumir la sesión

**Propósito:** cuando vuelvas a abrir el chat con la IA, pegá una de las
plantillas de abajo para que tome el contexto completo sin tener que
re-descubrir el proyecto.

> El proyecto vive en `/home/re/Documents/dev/automatas/`. La IA no tiene
> memoria entre sesiones; cada conversación nueva empieza de cero.

---

## 1. Prompt base (copiá y pegá al inicio de la sesión)

```markdown
# Resumir sesión — Proyecto Autómatas y Lenguajes Formales

## Contexto del proyecto
- **Path del proyecto:** `/home/re/Documents/dev/automatas/`
- **Entrega:** 05-nov-2026 · faltan ~N días (calcular según hoy)
- **Stack:** Java + NetBeans 31 (Flatpak) + JDK 17 + JUnit 5 + OpenPDF 1.3.43 + Graphviz 14.1.4

## Documentos de referencia
- `Plan_Proyecto_Automatas.md` — plan completo, 9 fases
- `Base_Inicial_Proyecto.md` — código base propuesto (modelo + vista)
- `ESTADO_PROYECTO.md` — qué está hecho y qué falta
- `DUDAS_CONSULTAR.md` — 13 preguntas pendientes al catedrático
- `CHECKLIST_INSTALACION.md` — setup + caveats (NetBeans sandbox, OpenPDF 1.x)

## Estado actual (al momento de pausar)
- ✅ Fase 0 – Preparación (instalaciones)
- ✅ Fase 1 – Modelo + validaciones + refactor (62/62 tests)
- 🟡 **Esperando respuesta del catedrático** a DUDAS_CONSULTAR.md
- ❌ Fase 2 – Evaluación (incluye EvaluadorGramatica y GeneradorCadenas)
- ❌ Fases 3-9

## Decisiones técnicas ya tomadas
- JDK 17 copiado a `~/.local/share/jdk17/` (NetBeans Flatpak sandbox no ve /usr/lib/jvm)
- OpenPDF 1.3.43 (no 2.x/3.x — esas requieren JDK 21+)
- JUnit 5 standalone (`lib/junit-platform-console-standalone-1.10.2.jar`)
- Estructura NetBeans Ant manual en `nbproject/` (no `ant` instalado en sistema)
- `./probar.sh` para tests/run sin ant
- Commit único por fase + commit único de docs

## Decisiones de Fase 2 pendientes del catedrático
- §1.1 de DUDAS: ¿gramáticas lineales por izquierda, derecha, o ambas?
- §1.6 de DUDAS: ¿cadenas en PDF automáticas o evaluadas por usuario?
- §1.5 de DUDAS: ¿formato de epsilon en `.gtk`?

## Datos del equipo (placeholders pendientes)
- `DatosCurso.SECCION`, `DatosCurso.CARNE`, `DatosCurso.CATEDRATICO` (en `src/automatas/vista/DatosCurso.java`)
- Nombre/carné ya está: Jhosef Reyes · 9390-24-4816

## Último commit
`git log --oneline -1` y pegame el output. Si tenés respuestas nuevas del catedrático, pegá también.

## ¿Qué necesito?
<tu pedido acá, p.ej. "arrancar Fase 2", "agregar más tests", "refactor X", etc.>
```

---

## 2. Variantes según el momento

### Variante A — Continuar Fase 2 (llegaron respuestas del catedrático)

```markdown
Tengo respuestas del catedrático a las dudas de DUDAS_CONSULTAR.md.
Resumen:
- §1.1 (forma de gramáticas): <pegar respuesta>
- §1.6 (cadenas en PDF): <pegar respuesta>
- §1.5 (epsilon en .gtk): <pegar respuesta>

Proyecto: /home/re/Documents/dev/automatas/
Último commit: <pegar `git log --oneline -1`>

Procedemos con Fase 2 — EvaluadorGramatica + GeneradorCadenas.
```

### Variante B — Seguir esperando

```markdown
El catedrático aún no respondió. Mientras, quiero <agregar tests / refactor / crear archivos .afd/.gtk / etc.>.
Proyecto: /home/re/Documents/dev/automatas/
Estado: Fase 1 ✅ (62/62 tests), esperando dudas.
Último commit: <pegar `git log --oneline -1`>
```

### Variante C — Sesión completamente nueva (no hay contexto previo)

```markdown
Soy Jhosef Reyes, estoy trabajando en un proyecto de Autómatas y Lenguajes
Formales. El proyecto está en /home/re/Documents/dev/automatas/.
Por favor:
1. Lee los 4 archivos markdown (Plan, Base, Estado, Dudas, Checklist).
2. Corre `git log --oneline` y `ls src test lib`.
3. Decime el estado actual y qué proponés como siguiente paso.
```

---

## 3. Tips para la próxima sesión

1. **Si tenés contexto nuevo** (respuestas del catedrático, cambios en la base inicial, etc.), pegá arriba del todo del prompt.
2. **Si el proyecto cambia de ruta** (otro equipo, otra carpeta), avisá el nuevo path antes.
3. **Si pasaron varios días**, corré `./probar.sh test` vos mismo primero y pegá el resultado — así la IA sabe que tu ambiente sigue bien.
4. **Pegá siempre la salida de `git log --oneline -1`** para que sepa en qué commit quedaste.
5. **El prompt base** (primer bloque) puede quedarse guardado en este archivo. Solo editás ligeramente y lo pegás al chat.

---

## 4. Mapa rápido: commit → fase

| Commit (prefijo) | Contenido |
|------------------|-----------|
| `8ab9454` | Fase 1 inicial — modelo + validaciones + 38 tests |
| `5cb227b` | Fix: broken platform reference |
| `d94cc5c` | Refactor Fase 1: inmutabilidad + edge cases + javadoc (62 tests) |
| `2f2a740` | Docs post-refactor (ESTADO/CHECKLIST/DUDAS) |
| (siguiente) | Fase 2: EvaluadorGramatica + GeneradorCadenas |
| (siguiente) | Fase 3: Conversiones AFD↔Gramática |
| (siguiente) | Fase 4: Lectores/Escritores .afd y .gtk |
| (siguiente) | Fase 5: UI Swing (VentanaPrincipal + 7 paneles) |
| (siguiente) | Fase 6: Reportes PDF + Graphviz |
| (siguiente) | Fase 7: Pruebas integrales |
| (siguiente) | Fase 8: Documentación y entrega |

> Si el `git log --oneline` muestra commits distintos a estos, la IA va a
> poder ubicarse con solo ver la lista completa.

---

## 5. Estado actual del entorno (chequeo rápido)

Antes de empezar una sesión, validá que todo siga funcionando:

```bash
cd /home/re/Documents/dev/automatas
./probar.sh test          # debe decir 62/62
git status                # debe estar limpio
git log --oneline -1      # pegar en el prompt
```

Si algo falla, revisar `CHECKLIST_INSTALACION.md` sección correspondiente.