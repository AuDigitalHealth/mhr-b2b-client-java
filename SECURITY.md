# Security

## Reporting issues

For **security vulnerabilities in this library**:

1. Prefer **GitHub private vulnerability reporting** if enabled (**Security** tab).
2. Otherwise use your organisation's channel for **ADHA / AuDigitalHealth** repositories. Do **not** post exploit details or live credentials in public issues.

## This repository

- **Do not commit secrets to git:** passwords, API tokens, private keys, real mutual-TLS keystores, or production endpoint URLs — including in tests, samples, or documentation.
- **`local.properties`** is gitignored. Never commit real PCEHR credentials or keystores.
- **`src/test/resources/**/*.jks`** and **`src/sample/resources/**/*.jks`** are gitignored. Provide test keystores locally for **`-Pintegration`** only.
- **`wsdls/lib/provided/`** contains only public Maven Central build tooling (EE4J JAX-WS); no credentials or licensed HI-style contract bundles.
