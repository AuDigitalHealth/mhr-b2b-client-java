#!/usr/bin/env bash
# Install pcehr-compiled-wsdl-java (java-11-jakarta) before first build; see README.md.
# Optional: MVN_SETTINGS=/path/to/settings.xml
set -euo pipefail
cd "$(dirname "$0")"
extra=()
wsimport_mode=false
case "${1:-}" in
    '') ;;
    shaded|-shaded|--shaded) extra=(-Pfat-jar) ;;
    wsimport|-wsimport|--wsimport) wsimport_mode=true ;;
    *)
        echo "Unknown argument: $1" >&2
        echo "Optional: shaded | wsimport (validate WSDL codegen)" >&2
        exit 2
        ;;
esac
settings_args=()
if [[ -n "${MVN_SETTINGS:-}" ]]; then
    settings_args=(-s "$MVN_SETTINGS")
fi
if [ "$wsimport_mode" = true ]; then
    extra=(-Pwsimport "${extra[@]}")
fi
if [ "${#extra[@]}" -eq 0 ]; then
    echo 'Building mhr-b2b-client JAR'
elif [ "$wsimport_mode" = true ]; then
    echo 'Building mhr-b2b-client JAR (with in-repo wsimport validation)'
else
    echo 'Building mhr-b2b-client FAT/UBER JAR'
fi
exec mvn -B "${settings_args[@]}" "${extra[@]}" -Dgpg.skip=true clean verify
