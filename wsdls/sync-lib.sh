#!/usr/bin/env bash
# Download EE4J jaxws-tools/jaxws-rt + ant-contrib into lib/provided (no Metro webservices-*).
set -euo pipefail
cd "$(dirname "$0")"
exec mvn -B -f ee4j-jaxws-lib-pom.xml package "$@"
