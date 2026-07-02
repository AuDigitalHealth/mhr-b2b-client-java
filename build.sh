#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
SHADED=0
for a in "$@"; do
  case "$a" in
    -Shaded|shaded|-shaded|--shaded) SHADED=1 ;;
    *) echo "Unknown argument: $a" >&2; echo "Optional: -Shaded | shaded | -shaded | --shaded" >&2; exit 2 ;;
  esac
done
MVN_ARGS=(-B)
if [[ -n "${MVN_SETTINGS:-}" ]]; then
  MVN_ARGS+=(-s "$MVN_SETTINGS")
fi
if [[ "$SHADED" -eq 1 ]]; then
  echo 'Building mhr-b2b-client FAT/UBER JAR'
  MVN_ARGS+=(-Pfat-jar)
else
  echo 'Building mhr-b2b-client JAR'
fi
MVN_ARGS+=(-Dgpg.skip=true clean verify)
exec mvn "${MVN_ARGS[@]}"
