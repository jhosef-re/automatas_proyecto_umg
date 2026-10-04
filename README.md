<div align="center">

# 🤖 Proyecto Autómatas y Lenguajes Formales

### Aplicación de escritorio en Java para crear, convertir y evaluar AFDs y Gramáticas Regulares

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![NetBeans](https://img.shields.io/badge/Apache_NetBeans-17+-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-GUI-007396?style=for-the-badge&logo=java&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5.10-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Graphviz](https://img.shields.io/badge/Graphviz-14-FF6C00?style=for-the-badge&logo=graphviz&logoColor=white)
![OpenPDF](https://img.shields.io/badge/OpenPDF-1.3.43-DC143C?style=for-the-badge)

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)
![Estado: En desarrollo](https://img.shields.io/badge/Estado-En%20desarrollo-orange?style=for-the-badge)

![Último commit](https://img.shields.io/github/last-commit/jhosef-re/automatas_proyecto_umg?style=flat-square&logo=git&logoColor=white)
![Lenguaje top](https://img.shields.io/github/languages/top/jhosef-re/automatas_proyecto_umg?style=flat-square&color=ED8B00)

---

[📋 Descripción](#-descripción) · [✨ Características](#-características) · [🛠️ Stack](#-stack-tecnológico) ·
[📦 Requisitos](#-requisitos) · [🚀 Instalación](#-instalación-y-uso) · [🧪 Tests](#-tests) · [🗺️ Roadmap](#-roadmap)

</div>

---

## 📋 Descripción

Aplicación de escritorio en **Java + Swing** que implementa la creación, conversión, evaluación y reporte
de **Autómatas Finitos Deterministas (AFD)** y **Gramáticas Regulares**, siguiendo las especificaciones del
curso de **Autómatas y Lenguajes Formales** de la **Universidad Mariano Gálvez**.

La aplicación permite:

- Definir AFDs y Gramáticas Regulares con validación completa (estados, símbolos, producciones).
- Evaluar cadenas mostrando la **ruta** (en AFD) o la **expansión** (en gramática).
- Convertir entre ambos modelos.
- Generar **reportes PDF** con el grafo (Graphviz), detalle y ejemplos de cadenas válidas/inválidas.

> 📌 **Estado actual:** Fase 1 completa (modelo de dominio + 62 tests JUnit).
> Fases 2–9 pendientes según el [roadmap](#-roadmap).

---

## ✨ Características

| Estado | Funcionalidad |
|:------:|---------------|
| ✅ | Crear AFD con validación (duplicados, determinismo, sin epsilon) |
| ✅ | Crear Gramática Regular con validación (lineal izquierda/derecha) |
| ✅ | Evaluar cadenas en AFD con ruta textual (`A, A, a; ...`) |
| ✅ | Repositorio único de AFDs y gramáticas (Singleton) |
| ✅ | 62 tests JUnit pasando (reglas de validación + casos borde) |
| 🟡 | Evaluar cadenas en Gramática con expansión (`A>0B>00B>...`) |
| 🟡 | Generador de cadenas (≥3 válidas + ≥3 inválidas) |
| ❌ | Conversión AFD ↔ Gramática |
| ❌ | Cargar/guardar archivos `.afd` y `.gtk` |
| ❌ | Paneles Swing: Crear AFD/Gramática, Evaluar, Cargar, Guardar, Reportes |
| ❌ | Generación de PDF con grafo (Graphviz) + cadenas |
| ❌ | Manual de Usuario y Manual Técnico |

---

## 🛠️ Stack Tecnológico

| Capa              | Herramienta                                          |
|-------------------|------------------------------------------------------|
| **Lenguaje**      | Java 17 (Temurin 17.0.20.1)                          |
| **IDE**           | Apache NetBeans 17+ (probado en 31)                  |
| **GUI**           | Java Swing + `CardLayout`                            |
| **Build**         | Apache Ant (incluido en NetBeans)                    |
| **Testing**       | JUnit 5.10 (standalone en `lib/`)                    |
| **PDF**           | OpenPDF 1.3.43 (`lib/openpdf-1.3.43.jar`)            |
| **Grafos**        | Graphviz 14.1.4 (`dot` CLI)                          |
| **Paradigma**     | POO (Singleton · Strategy · MVC · Encapsulamiento)   |

---

## 📦 Requisitos

### 🪟 Software base (Windows)

| Software                | Versión mín.   | Link oficial                                                       | Instalador |
|-------------------------|----------------|--------------------------------------------------------------------|------------|
| ☕ **JDK 17** (Temurin) | 17.0.20+       | https://adoptium.net/temurin/releases/?version=17                  | `.msi`     |
| 🟦 **Apache NetBeans**  | 17+            | https://netbeans.apache.org/download/index.html                    | `.exe`     |
| 🔧 **Git**              | 2.40+          | https://git-scm.com/download/win                                   | `.exe`     |
| 🕸️ **Graphviz**        | 14+            | https://graphviz.org/download/#windows                             | `.msi`     |

> ✅ **Las librerías de Java (`OpenPDF`, `JUnit`) ya vienen incluidas** en la carpeta `lib/` de este repo.

### 🪟 Setup específico en Windows

1. **Instalar JDK 17** con el `.msi` → deja `java` y `javac` automáticamente en el PATH.
2. **Instalar NetBeans** → abrir y registrar el JDK en `Tools > Java Platforms > Add Platform`.
3. **Instalar Graphviz** → ⚠️ **importante**: agregar `C:\Program Files\Graphviz\bin` al `PATH` del sistema
   (Panel de control → Sistema → Configuración avanzada → Variables de entorno → `Path`).
4. **(Opcional)** Definir `JAVA_HOME` apuntando a `C:\Program Files\Eclipse Adoptium\jdk-17.x.x`.

### 🐧 Setup específico en Linux (Fedora/Ubuntu)

```bash
sudo dnf install java-17-openjdk-devel graphviz       # Fedora
sudo apt install openjdk-17-jdk graphviz              # Ubuntu/Debian
```

> Ver `CHECKLIST_INSTALACION.md` para más detalles y troubleshooting del entorno.

---

## 🚀 Instalación y Uso

### 1. Clonar el repositorio

```bash
git clone https://github.com/jhosef-re/automatas_proyecto_umg.git
cd automatas_proyecto_umg
```

### 2. Abrir en NetBeans

```
File > Open Project > seleccionar la carpeta "automatas_proyecto_umg"
```

### 3. Compilar y ejecutar

```
Click derecho sobre el proyecto > Run (F6)
```

> 🪟 **En Windows**, si NetBeans (instalado vía Flatpak o portable) no detecta el JDK del sistema,
> copiá el JDK 17 a una ruta accesible y registralo manualmente desde `Tools > Java Platforms`.

### 4. Probar los tests

**Desde NetBeans:** click derecho en el proyecto → `Test` (`Alt+F6`).

**Desde terminal (Linux/macOS):**

```bash
./probar.sh test
```

---

## 🧪 Tests

El proyecto usa **JUnit 5.10** (standalone) con cobertura de:

- ✅ Reglas de validación del `AFD` (estados, alfabeto, transiciones, determinismo, sin epsilon).
- ✅ Reglas de validación de la `Gramatica` (NT, terminales, producciones, NT ≠ terminal).
- ✅ `RepositorioAutomatas` (Singleton, no colisión de nombres entre AFD y gramática).
- ✅ `EvaluadorAFD` con el ejemplo `aababb` del enunciado (formato de ruta exacto).

```bash
$ ./probar.sh test
...
Tests run: 62, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

> 📊 **Estado actual:** 62/62 tests pasando.

---

## 🎓 Paradigmas y Patrones de Diseño

El proyecto fue diseñado para aplicar y poder **explicar en la defensa** los siguientes paradigmas POO:

| Patrón            | Implementación                                              |
|-------------------|-------------------------------------------------------------|
| **Singleton**     | `servicio.RepositorioAutomatas` — registro único global     |
| **Strategy**      | `servicio.Evaluador` (interfaz) + `EvaluadorAFD` / `EvaluadorGramatica` |
| **Factory**       | `archivo.LectorAFD` / `LectorGTK` (por extensión, planificado) |
| **MVC**           | `modelo/` · `vista/` (Swing) · `servicio/` (controlador)    |
| **Encapsulamiento** | Validaciones en setters/métodos del modelo (atributos `private final`) |
| **Inmutabilidad** | Colecciones expuestas vía `Collections.unmodifiableXxx()`  |

---

## 📚 Documentación adicional

| Documento | Descripción | Estado |
|-----------|-------------|:------:|
| [`Plan_Proyecto_Automatas.md`](./Plan_Proyecto_Automatas.md) | Plan completo en 9 fases con cronograma y dudas al catedrático | ✅ |
| [`Base_Inicial_Proyecto.md`](./Base_Inicial_Proyecto.md) | Esqueleto de código del modelo + vista (Fase 0/1) | ✅ |
| [`ESTADO_PROYECTO.md`](./ESTADO_PROYECTO.md) | Checklist actualizado por fase | ✅ |
| [`DUDAS_CONSULTAR.md`](./DUDAS_CONSULTAR.md) | Preguntas pendientes al catedrático | ✅ |
| [`CHECKLIST_INSTALACION.md`](./CHECKLIST_INSTALACION.md) | Setup del entorno (Linux Fedora + NetBeans Flatpak) | ✅ |
| 📘 Manual de Usuario | Capturas por pantalla, flujo paso a paso | ❌ Fase 8 |
| 📗 Manual Técnico | Diagrama de clases, algoritmos | ❌ Fase 8 |

---

## 🗺️ Roadmap

| Fase | Descripción                                              | Estado |
|:----:|--------------------------------------------------------|:------:|
| **0** | Preparación (instalaciones + repo + dudas)              | ✅ |
| **1** | Modelo de dominio + validaciones + tests JUnit (62/62)  | ✅ |
| **2** | `EvaluadorGramatica` + `GeneradorCadenas`              | 🟡 *esperando dudas del catedrático* |
| **3** | Conversiones AFD ↔ Gramática                            | ❌ |
| **4** | Lectores/Escritores `.afd` y `.gtk`                     | ❌ |
| **5** | UI Swing: 7 paneles + menú                             | 🟡 *parcial* |
| **6** | Reportes PDF + Graphviz                                | ❌ |
| **7** | Pruebas integrales y pulido                            | ❌ |
| **8** | Documentación final + entrega (.zip)                   | ❌ |
| **9** | Preparación de defensa                                 | ❌ |

> 📅 **Entrega:** jueves 05 de noviembre de 2026, 23:59.

---

## 🤝 Contribuciones

Por ser un **proyecto académico de equipo**, las contribuciones externas no se aceptan.
Sin embargo, el código se publica bajo [MIT](./LICENSE) para que pueda ser reutilizado con crédito.

### 👥 Equipo

- **Jhosef Reyes** · Carné `9390-24-4816` — *modelo, conversiones, UI*

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT** — ver el archivo [`LICENSE`](./LICENSE) para el texto completo.

```
MIT License — Copyright (c) 2026 Jhosef Reyes
Se permite el uso, copia, modificación y distribución con fines académicos
o personales, conservando el aviso de copyright original.
```

---

## ✍️ Autor

<div align="center">

**Jhosef Reyes** · Carné `9390-24-4816`

🎓 *Universidad Mariano Gálvez*
📚 *Autómatas y Lenguajes Formales*

</div>

---

## 🙏 Agradecimientos

- 🎓 **Universidad Mariano Gálvez** — Facultad de Ingeniería
- 👨‍🏫 **Catedrático del curso** — por la guía y el enunciado del proyecto
- 🛠️ **Comunidad Apache NetBeans** — IDE robusto y portable
- 📄 **OpenPDF** — fork libre de iText 4 (compatible con JDK 17)
- 🕸️ **Graphviz** — herramienta estándar para grafos

---

<div align="center">

⭐ Si este proyecto te fue útil, dejá una estrella en el repo.

</div>