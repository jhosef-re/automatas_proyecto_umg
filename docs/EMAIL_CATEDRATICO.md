# Mensaje al catedrático — cierre de dudas

> **Para enviar por el canal que use el curso** (Classroom, WhatsApp institucional
> o email). Copiá el bloque de abajo y enviá tal cual — ya está formateado
> para copiar/pegar.
>
> **Cuándo:** antes del 05-nov-2026 (idealmente cuanto antes, para tener
> tiempo de ajustar el código si hay observación).

---

## 📧 Mensaje listo para enviar

**Asunto:** Consultas sobre decisiones de diseño — Proyecto Autómatas (cierre previo a la entrega)

**Cuerpo:**

```
Estimado Inge. Alan G. Ucelo Morán:

Antes de la entrega del 05-nov, queríamos confirmar con usted algunas
decisiones de diseño que tomamos en el proyecto. Las marcamos como
asumidas en el código (con fallback documentado en CONTEXTO_PROYECTO.md
y en cada clase), pero queríamos darle la oportunidad de corregirnos
si alguna no coincide con lo esperado por el curso.

Las preguntas concretas:

 §1.2 — Estado final extra en Gramática → AFD.
    Cuando la gramática tiene producciones terminal-only (ej. "B > c"),
    creamos un estado final extra llamado "F" para que la transición
    apunte allí. Si "F" ya está declarado como NT, usamos "F#0",
    "F#1", …, hasta "F#9999". Si la gramática no tiene producciones
    terminal-only, NO creamos F. ¿El procedimiento es correcto?

  §1.3 — "Expresión regular" en el diagrama de flujo.
    Asumimos que está fuera del alcance del proyecto (el plan solo
    describe conversiones AFD↔Gramática). ¿Correcto?

  §1.7 — Cuándo se dispara la conversión AFD↔Gramática.
    Implementamos un botón explícito "Convertir a…" en la pantalla de
    Evaluar Cadenas (no automática al crear). ¿OK?

  §2.1 — Formato cuando no hay transición para un símbolo en AFD.
    Cuando el AFD se bloquea, mostramos el paso como "actual, ?, simbolo"
    (el ? indica transición inexistente) y marcamos la cadena como
    inválida. ¿Aceptable o prefiere otro formato?

  §2.2 — Modo 1 y Modo 2 de transiciones combinables.
    El parser permite combinar transiciones de ambos modos en el mismo
    AFD (la última entrada gana). ¿Es lo esperado o son excluyentes?

  §2.5 — Graphviz en la máquina del calificador.
    Implementamos dos mitigaciones:
      (a) Degradación elegante: si "dot" no está en el PATH, el PDF se
          genera con el DOT como texto monoespaciado.
      (b) Ruta configurable vía propiedad del sistema
          -Dautomatas.graphviz.path=/ruta/a/dot.
    ¿Es suficiente o prefiere otro mecanismo?

  §2.6 — Cantidad mínima de archivos de prueba propios.
    Entregamos 11 archivos: 5 del enunciado + 6 propios con casos borde
    (un_estado.afd, sin_validas.afd, lenguaje_ab.afd, solo_terminales.gtk,
    disyuncion.gtk, multi_terminal.gtk). ¿Suficiente?

Quedamos atentos a su respuesta. Si no hay observaciones, entendemos
que las decisiones asumidas son aceptables y procedemos con la entrega.

Saludos cordiales,
Jhosef Reyes · 9390-24-4816 (en representación del equipo)
```

---

## 📋 Referencia rápida

- **§1.2** — Ver `src/automatas/servicio/ConversorGramaticaAFD.java:48`
  (constante `NOMBRE_ESTADO_FINAL = "F"`).
- **§1.3** — Ver `Plan_Proyecto_Automatas.md` §1.4 pregunta 3.
- **§1.7** — Ver `src/automatas/vista/PanelEvaluar.java` (botón "Convertir a…").
- **§2.1** — Ver `src/automatas/servicio/EvaluadorAFD.java:53` (línea "actual, ?, simbolo").
- **§2.2** — Ver `src/automatas/util/ParserModo1AFD.java` y `ParserModo2AFD.java`.
- **§2.5** — Ver `src/automatas/reporte/GeneradorGraphviz.java:29` (`PROPIEDAD_PATH`).
- **§2.6** — Ver `test/resources/` (11 archivos).

## ⏭️ Después de enviar

- Si hay respuesta: crear commit `docs: anotar respuestas del catedrático` con el feedback.
- Si no hay respuesta antes del 30-oct: hacer commit `docs: cierre de dudas sin respuesta del catedrático` indicando que procedemos con las asumidas.