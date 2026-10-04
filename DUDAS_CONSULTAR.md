# Dudas a consultar al catedrático
**Fecha de armado:** 03-oct-2026 · **Ventana para enviarlas:** cuanto antes (Fase 0)

---

## 1. Dudas del enunciado (sección 1.4 del plan)

### 1.1 Forma de las gramáticas (lineal por izquierda vs. por derecha)
- **Pregunta:** El ejemplo del `.gtk` muestra `A>Ab` (recursiva por la izquierda) y `A>bC` (por la derecha). ¿Se debe soportar **cualquier gramática regular** (lineal por izquierda o por derecha) o solo las lineales por la derecha?
- **Por qué importa:** El algoritmo de evaluación y la conversión gramática→AFD son diferentes para cada caso. Si solo se permiten por la derecha, el algoritmo es directo; si se permiten ambas, hay que cubrir más casos.
- **Recomendación:** Implementar de forma genérica (no asumir una sola forma) para no depender de la respuesta.
- **Impacto si no se responde a tiempo:** Diseño rígido que obligue a reescribir `EvaluadorGramatica` o el conversor.

### 1.2 Estado final extra en Gramática → AFD
- **Pregunta:** Para producciones como `B>c` (terminal sin NT en el lado derecho), ¿se requiere agregar un **estado final extra**? ¿Cuál es el criterio exacto?
- **Por qué importa:** El enunciado menciona un "estado final extra" en la conversión, pero no especifica cuándo se crea ni cómo se nombra.
- **Recomendación:** Asumir convención propia (p. ej., `F` como estado final genérico) y documentarla.
- **Impacto si no se responde:** Inconsistencias entre conversiones de AFD→Gramática y Gramática→AFD.

### 1.3 "¿Expresión regular?" en el diagrama de flujo
- **Pregunta:** En el diagrama de conversión aparece el nodo "Expresión regular" sin más explicación. ¿Está fuera del alcance del proyecto?
- **Por qué importa:** Si entra, hay un módulo adicional que no aparece en el plan.
- **Recomendación:** Asumir que **no** entra en alcance, salvo confirmación.
- **Impacto si no se responde:** Pérdida de puntos si se ignoró un requisito o tiempo desperdiciado en algo no pedido.

### 1.4 Evaluar Cadenas: ¿AFD o Gramática?
- **Pregunta:** El menú "Evaluar Cadenas" dice "pide nombre de la gramática", pero las rutas se describen también para AFD. ¿El nombre puede ser tanto de AFD **como** de gramática?
- **Por qué importa:** La pantalla debe buscar el nombre en el `RepositorioAutomatas` (que ya unifica ambos espacios de nombres).
- **Recomendación:** Implementar búsqueda unificada; detectar el tipo por el registro.
- **Impacto si no se responde:** Comportamiento confuso si solo acepta un tipo.

### 1.5 Epsilon en archivos `.gtk`
- **Pregunta:** El formato `.gtk` mostrado no incluye cómo se representa el vacío. ¿Se usa la palabra `epsilon`, un símbolo especial, o línea en blanco?
- **Por qué importa:** El `LectorGTK` y el `EscritorGTK` deben acordar la misma convención.
- **Recomendación:** Usar la palabra `epsilon` (consistente con la UI de la Fase 1).
- **Impacto si no se responde:** Archivos generados no se pueden recargar.

### 1.6 Cadenas válidas/inválidas en el PDF
- **Pregunta:** ¿Las cadenas válidas/inválidas del reporte se generan **automáticamente** (BFS por longitud) o deben salir de las que el usuario evaluó durante la sesión?
- **Por qué importa:** El generador de cadenas automático es trabajo extra; el alternativo requiere llevar un historial de evaluaciones.
- **Recomendación:** Generar **ambas**: ≥3 automáticas + las evaluadas por el usuario.
- **Impacto si no se responde:** Reporte incompleto o, peor, sin ejemplos en alguna categoría.

### 1.7 Cuándo se dispara la conversión
- **Pregunta:** La conversión AFD↔Gramática: ¿se hace **automáticamente al crear** cada uno (con un botón "Convertir a...") o desde una opción en el menú?
- **Por qué importa:** Cambia la arquitectura del menú y la responsabilidad del usuario.
- **Recomendación:** Opción explícita en el menú "Reportes" o un submenú "Conversiones" para que el usuario decida.
- **Impacto si no se responde:** Sobrecosto de UI o conversiones silenciosas que confunden.

---

## 2. Dudas de implementación detectadas en la base inicial

### 2.1 Formato cuando no hay transición en el AFD
- **Pregunta:** En `EvaluadorAFD` (línea ~480 de la base), cuando no existe transición para un símbolo, se agrega `actual, ?, simbolo` a la ruta. ¿Ese formato es aceptable o se espera otra convención (p. ej., cortar la ruta sin agregar paso)?
- **Por qué importa:** El reporte PDF muestra la ruta textual; el `?` puede verse como un símbolo válido.
- **Recomendación:** Documentar como "transición bloqueada" con el `?` y agregar leyenda en el PDF.
- **Impacto si no se responde:** PDF poco claro en cadenas inválidas.

### 2.2 Modo 1 y Modo 2 de transiciones
- **Pregunta:** ¿Un mismo AFD puede crearse combinando transiciones de Modo 1 y Modo 2, o son excluyentes?
- **Por qué importa:** El parser debe rechazar combinaciones inválidas si son excluyentes.
- **Recomendación:** Asumir que **se permite combinar** (la última entrada gana, como en el archivo).
- **Impacto si no se responde:** AFDs creados con ambos modos podrían no ser consistentes.

### 2.3 Librería de PDF
- **Pregunta:** ¿Hay preferencia del catedrático entre OpenPDF, iText o PDFBox? ¿O cualquiera sirve?
- **Por qué importa:** Cada una tiene licencias y APIs distintas; iText tiene restricciones comerciales recientes.
- **Recomendación:** **OpenPDF** (fork libre de iText 4, API conocida).
- **Impacto si no se responde:** Cambio de librería a mitad de fase 6.

### 2.4 JDK y versión de NetBeans
- **Pregunta:** ¿JDK 17+ y NetBeans 17+ son aceptables, o hay que usar una versión específica del curso?
- **Por qué importa:** Compatibilidad de `var`, `sealed`, `records` que se quieran usar.
- **Recomendación:** JDK 17 LTS + NetBeans 17; evita el riesgo de una versión vieja.
- **Impacto si no se responde:** Proyecto no compila en la máquina del calificador.

### 2.5 Graphviz en la máquina del calificador
- **Pregunta:** ¿La máquina donde se calificará tiene Graphviz preinstalado, o es necesario entregar un `.bat/.sh` con la ruta configurable?
- **Por qué importa:** Si `dot` no está en el PATH, el PDF queda sin imagen.
- **Recomendación:** Ruta configurable vía propiedad del sistema + incluir el `.dot` aparte dentro del `.zip` como respaldo.
- **Impacto si no se responde:** PDF sin grafo = pérdida de puntos en la sección visual.

### 2.6 Cantidad mínima de archivos de prueba propios
- **Pregunta:** ¿Cuántos archivos `.afd` y `.gtk` se deben entregar como pruebas propias? (El plan sugiere 3 de cada.)
- **Por qué importa:** Define el alcance de la Fase 0 y los casos borde a cubrir.
- **Recomendación:** 3 de cada, incluyendo casos borde (AFD con 1 estado, gramática con epsilon, gramática recursiva por izquierda, AFD sin cadenas válidas, etc.).
- **Impacto si no se responde:** Cobertura insuficiente en pruebas integrales.

---

## 3. Datos del curso faltantes (TODO)

- `DatosCurso.SECCION`
- `DatosCurso.CARNE` (para calcular último dígito en Ayuda)
- `DatosCurso.CATEDRATICO`

> Confirmar con el equipo antes de la primera entrega; están como `<placeholder>` en `DatosCurso.java`.

---

## 4. Datos del equipo (TODO en `ESTADO_PROYECTO.md`)

- ¿Cuántos integrantes? (El plan asume roles: modelo · conversiones · UI · reportes/doc.)
- ¿Reparto de roles ya hecho o pendiente?