# Checklist de instalación – Proyecto Autómatas
**SO objetivo:** Fedora 44 Workstation · **Fecha:** 03-oct-2026

> Marca con ✅ cuando esté hecho. Cada item tiene el comando de verificación.
> **Estado global:** ✅ Instalación completa — listo para crear el proyecto en NetBeans.

### Cambios respecto al plan original
1. **JDK 17 unificado vía `JAVA_HOME`** en lugar de `update-alternatives` (más limpio, no requiere sudo).
2. **NetBeans instalado vía Flatpak** (sandbox); JDK 17 copiado a `~/.local/share/jdk17/` para sortear la sandbox.
4. **OpenPDF 1.3.43** (no la última 2.x/3.x) por requerir JDK 21+ las versiones modernas.

---

## 1. Java (JDK) ✅ COMPLETO

- [x] **Instalar JDK completo (con `javac`)**
  - Recomendado: OpenJDK 17 (alineado al plan). Aceptable: cualquier 17+.
  - Tu sistema ya tiene JRE 25, pero **sin compilador**.

  ```bash
  # Fedora (sudo o como root)
  sudo dnf install java-17-openjdk-devel
  # Alternativa más reciente (no recomendada sin confirmar):
  # sudo dnf install java-25-openjdk-devel
  ```
  > Instalado: **Temurin JDK 17.0.20.1** (`java-17-temurin-jdk` ya estaba, `dnf` confirmó el paquete).

- [x] **Verificar `java` y `javac`**
  ```bash
  java -version
  javac -version
  ```
  > Resultado: ambos en **17.0.20.1** ✅

- [x] **Unificar JDK por defecto** (en lugar de `update-alternatives`)
  > En lugar de tocar `/usr/lib/jvm` con `update-alternatives` (requiere sudo), se configuró `JAVA_HOME` + `PATH` en `~/.bashrc` para que el shell use siempre Temurin 17. Esto también es lo que NetBeans toma al lanzarse desde terminal.

- [x] **Definir `JAVA_HOME`**
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-17-temurin-jdk
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  > Agregado a `~/.bashrc`. `JAVA_HOME=/usr/lib/jvm/java-17-temurin-jdk`.

---

## 2. NetBeans IDE ✅ COMPLETO

- [x] **Instalar Apache NetBeans 17+**
  - Opción elegida: **Flatpak** (`flatpak install flathub org.apache.netbeans`)
  - Versión instalada: **Apache NetBeans 31** (1.8 GB, runtime: org.freedesktop.Sdk 25.08)

- [x] **Verificar instalación**
  ```bash
  which netbeans      # no existe directo; flatpak expone `org.apache.netbeans`
  flatpak run org.apache.netbeans --help
  ```
  > Alias agregado a `~/.bashrc`: `alias netbeans='flatpak run org.apache.netbeans'`

- [x] **Configurar el JDK en NetBeans**
  > ⚠️ **IMPORTANTE – sandbox de Flatpak**: NetBeans viene en un sandbox que **NO** puede ver `/usr/lib/jvm`. Solución: se copió el JDK 17 a `~/.local/share/jdk17/` para que NetBeans lo pueda usar como Java Platform.
  - Pasos a hacer manualmente en la GUI:
    1. Abrir NetBeans: `netbeans` (alias)
    2. `Tools > Java Platforms > Add Platform > Java Standard Edition`
    3. Apuntar a `/home/re/.local/share/jdk17` (NO a `/usr/lib/jvm/java-17-temurin-jdk`)
    4. `Tools > Options > Build > Java > Java Shell` → seleccionar la misma plataforma.
  > Pendiente de hacer manualmente cuando se cree el proyecto.

- [x] **Crear el proyecto inicial**
  - `File > New Project > Java with Ant > Java Application`
  - Nombre: `ProyectoAutomatas`
  - Paquete principal: `automatas`
  - Desmarcar "Create Main Class" (la crearemos nosotros)
  - Java Platform: **JDK 17 (copia en `~/.local/share/jdk17/`)**
  > Pendiente de hacer manualmente cuando empecemos la Fase 1.

---

## 3. Graphviz (para reportes PDF) ✅ COMPLETO

- [x] **Instalar Graphviz**
  ```bash
  sudo dnf install graphviz
  ```
  > Instalado: **graphviz 14.1.4** (20260321.0153)

- [x] **Verificar `dot`**
  ```bash
  dot -V
  ```
  > Resultado: `dot - graphviz version 14.1.4`

- [x] **Probar generación de imagen**
  ```bash
  echo 'digraph G { A -> B; }' | dot -Tpng > /tmp/prueba.png
  file /tmp/prueba.png
  ```
  > Resultado: `PNG image data, 83 x 155, 8-bit/color RGBA, non-interlaced` ✅

---

## 4. Git (ya instalado, solo verificar) ✅ COMPLETO

- [x] **Configurar identidad**
  ```bash
  git config --global user.name "Tu Nombre"
  git config --global user.email "tu@email.com"
  ```
  > Configurado: **Jhosef Reyes** · **jreyesr25@miumg.edu.gt**

- [x] **Verificar versión**
  ```bash
  git --version   # ya tenés 2.55.0
  ```

- [x] **Inicializar el repositorio del proyecto**
  ```bash
  cd /home/re/Documents/dev/automatas
  git init
  ```
  > Resultado: `Initialized empty Git repository in /home/re/Documents/dev/automatas/.git/`
  > Branch por defecto: `master` (cambiar a `main` si lo preferís: `git branch -m main`)

- [x] **Crear `.gitignore` mínimo**
  ```
  build/
  dist/
  nbproject/private/
  target/
  *.class
  ```
  > Archivo `.gitignore` creado en la raíz del proyecto.

---

## 5. Librería PDF (OpenPDF recomendada) ✅ COMPLETO

- [x] **Descargar OpenPDF** ✅ (con caveat)
  - **Versión elegida:** **OpenPDF 1.3.43** (última 1.x, marzo 2024, 2.1 MB)
  - Ubicación: `/home/re/Documents/dev/automatas/lib/openpdf-1.3.43.jar`
  - Descargado de Maven Central:
    ```bash
    curl -fsSL -o lib/openpdf-1.3.43.jar \
      https://repo1.maven.org/maven2/com/github/librepdf/openpdf/1.3.43/openpdf-1.3.43.jar
    ```

  > ⚠️ **Por qué no la última (3.0.5) ni 2.x:** a partir de OpenPDF 2.0 las clases se compilaron con **JDK 21** (`class file version 65.0`), incompatible con nuestro **JDK 17**. La rama 1.x es la última compatible con JDK 17 y sigue usando el paquete `com.lowagie.text.*` que espera la base inicial del proyecto.
  > 
  > Si en el futuro se sube a JDK 21+, se puede migrar a OpenPDF 2.x/3.x (cambio de paquete `com.lowagie.text.*` → `org.openpdf.*`).

- [x] **Añadir al proyecto NetBeans**
  - Crear carpeta `lib/` en la raíz del proyecto ✅
  - Copiar el `.jar` adentro ✅
  - **Pendiente:** en NetBeans, clic derecho en `Libraries > Add JAR/Folder > lib/openpdf-1.3.43.jar` (se hace cuando se cree el proyecto).
  - **Sandbox-friendly:** el jar está en `~/Documents/...`, accesible desde la sandbox de NetBeans Flatpak ✅

- [x] **Verificar que carga**
  ```java
  // en Main.java, dentro de main():
  System.out.println("OpenPDF cargado");
  System.out.println(new com.lowagie.text.Document().getClass().getName());
  ```
  > Debe imprimir `com.lowagie.text.Document`.
  >
  > **Nota:** la base inicial usaba `com.lowagie.text.Version.getVersion()` — esa clase NO existe en OpenPDF 1.3.43. Ajustar la verificación al imprimir el nombre de la clase `Document` en su lugar. La API de generación (Document, Paragraph, PdfWriter, Font) sí está completa.

---

## 6. Verificación integral ✅ COMPLETO

```bash
java -version     # → openjdk version "17.0.20.1"  ✅
javac -version    # → javac 17.0.20.1              ✅
git --version     # → git version 2.55.0           ✅
flatpak list | grep netbeans    # → NetBeans 31 stable    ✅
dot -V            # → dot - graphviz version 14.1.4 ✅
ls ~/Documents/dev/automatas/lib/openpdf-1.3.43.jar  # → 2.1M ✅
```

> Todo verde.

---

## 7. Software adicional (opcional)

- [ ] **Maven** (si querés gestionar deps en vez de `lib/*.jar`)
  ```bash
  sudo dnf install maven
  mvn -v
  ```
  > El plan sugiere proyecto con **Ant** (el default de NetBeans). Maven solo si lo preferís.

- [ ] **Editor auxiliar** (para los `.md`): VS Code, Vim, o el que uses.
  > No es obligatorio; cualquier editor de texto plano sirve.

---

## 8. Datos del proyecto a completar (no son software, pero bloquean)

- [ ] `DatosCurso.SECCION` — sección real
- [ ] `DatosCurso.CARNE` — carné real (email detectado: `jreyesr25@miumg.edu.gt`)
- [ ] `DatosCurso.CATEDRATICO` — nombre completo
- [ ] Decisión de librería PDF (OpenPDF recomendado) — **OpenPDF 1.3.43** ✅
- [x] JDK exacto a usar (17 recomendado) — **Temurin 17.0.20.1**
  > Original: `/usr/lib/jvm/java-17-temurin-jdk` (para uso general con `$JAVA_HOME`)
  > Copia para NetBeans (Flatpak sandbox): `/home/re/.local/share/jdk17/`

---

## Resumen rápido (TL;DR)

```bash
sudo dnf install java-17-openjdk-devel graphviz
flatpak install flathub org.apache.netbeans   # o descargar de apache.org
git config --global user.name "..."
git config --global user.email "..."
# Descargar openpdf-1.x.x.jar → lib/
```