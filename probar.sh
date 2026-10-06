#!/bin/bash
# Compila y corre los tests JUnit 5 + ejecuta Main.
# No requiere Ant; usa los .jar en lib/.
# Uso: ./probar.sh [test|run|clean]
set -e

cd "$(dirname "$0")"

PROJECT="automatas"
SRC="src"
TEST="test"
BUILD="build"
LIB="lib"
OPENPDF="$LIB/openpdf-1.3.43.jar"
JUNIT="$LIB/junit-platform-console-standalone-1.10.2.jar"

action="${1:-test}"

case "$action" in
    clean)
        rm -rf "$BUILD"
        echo "Build limpio."
        ;;
    compile)
        mkdir -p "$BUILD/classes"
        find "$SRC" -name "*.java" > /tmp/.sources.txt
        javac -d "$BUILD/classes" -cp "$OPENPDF" @/tmp/.sources.txt
        echo "Compilación OK."
        ;;
    test)
        "$0" compile
        mkdir -p "$BUILD/test-classes"
        find "$TEST" -name "*.java" > /tmp/.test-sources.txt
        javac -d "$BUILD/test-classes" -cp "$BUILD/classes:$JUNIT:$OPENPDF" @/tmp/.test-sources.txt
        echo "Tests compilados. Ejecutando…"
        java -jar "$JUNIT" execute --class-path "$BUILD/classes:$BUILD/test-classes:$OPENPDF" --scan-class-path --details=tree
        ;;
    run)
        "$0" compile
        java -cp "$BUILD/classes:$OPENPDF" "$PROJECT.Main"
        ;;
    *)
        echo "Uso: $0 [clean|compile|test|run]"
        exit 1
        ;;
esac