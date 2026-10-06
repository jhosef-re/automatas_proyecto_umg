#!/bin/bash
# Empaqueta el proyecto en ProyectoAutomatas_Final.zip para entrega.
# No requiere dependencias externas (usa zip del sistema).
# Uso: ./release.sh
set -e

cd "$(dirname "$0")"

PROJECT="automatas"
SRC="src"
TEST="test"
LIB="lib"
BUILD="build"
OPENPDF="$LIB/openpdf-1.3.43.jar"
JUNIT="$LIB/junit-platform-console-standalone-1.10.2.jar"

VERSION="1.0.1"
DATE=$(date +%Y-%m-%d)
OUT="dist/ProyectoAutomatas_v${VERSION}_${DATE}.zip"

echo "==================================================="
echo " Empaquetando release v${VERSION} (${DATE})"
echo "==================================================="

# 1) Limpiar build/ y compilar fuentes y tests (para confirmar que el proyecto compila limpio).
echo "→ Compilando fuentes..."
rm -rf "$BUILD"
mkdir -p "$BUILD/classes" "$BUILD/test-classes"
find "$SRC" -name "*.java" > /tmp/.sources.txt
javac -d "$BUILD/classes" -cp "$OPENPDF" @/tmp/.sources.txt
echo "  Fuentes compiladas."

echo "→ Compilando tests..."
find "$TEST" -name "*.java" > /tmp/.test-sources.txt
javac -d "$BUILD/test-classes" -cp "$BUILD/classes:$JUNIT:$OPENPDF" @/tmp/.test-sources.txt
echo "  Tests compilados."

echo "→ Corriendo 203 tests..."
java -jar "$JUNIT" execute \
    --class-path "$BUILD/classes:$BUILD/test-classes:$OPENPDF" \
    --scan-class-path --details=none 2>&1 | tail -8

# 2) Construir el zip con árbol portable (sin build/, sin .git/, sin dist/).
echo "→ Construyendo $OUT..."
mkdir -p dist
rm -f "$OUT"

# Lista explícita de archivos/dirs a incluir (orden estable, sin metadata de git).
INCLUDE=(
    "src"
    "test"
    "lib"
    "nbproject"
    "build.xml"
    "probar.sh"
    "release.sh"
    "manifest.mf"
    "README.md"
    "LICENSE"
    "Plan_Proyecto_Automatas.md"
    "Base_Inicial_Proyecto.md"
    "ESTADO_PROYECTO.md"
    "CONTEXTO_PROYECTO.md"
    "DUDAS_CONSULTAR.md"
    "CHECKLIST_INSTALACION.md"
    "PROMPT_RESUMIR.md"
    "docs"
)

# zip recursivo.
zip -r "$OUT" "${INCLUDE[@]}" -x "*.class" "*/build/*" "*/dist/*" "*.git*" > /dev/null

SIZE=$(du -h "$OUT" | cut -f1)
echo "  Listo: $OUT ($SIZE)"

# 3) Verificar contenido.
echo "→ Verificando contenido del zip..."
ENTRIES=$(unzip -l "$OUT" | tail -1 | awk '{print $2}')
echo "  $ENTRIES archivos."

# Mostrar los archivos .md incluidos.
echo "→ Manuales incluidos:"
unzip -l "$OUT" | grep -E "MANUAL_(USUARIO|TECNICO)\.md" || echo "  (no encontrados)"

# Mostrar capturas incluidas.
echo "→ Capturas incluidas:"
unzip -l "$OUT" | grep "capturas/" | head -10

echo "==================================================="
echo " Empaquetado completo."
echo " Subir a Canvas antes del 05-nov-2026 23:59."
echo "==================================================="