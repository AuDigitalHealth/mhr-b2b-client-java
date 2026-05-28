# Security

## If you integrate this library

Do **not** embed mutual-TLS private keys, keystore passwords, or production PCEHR B2B URLs in source control in **your** application. Load secrets from your platform's vault, environment, or secure configuration.

## Reporting issues

For **security vulnerabilities in this library** (not routine support):

1. Prefer **GitHub private vulnerability reporting** for this repository if it is enabled (**Security** tab → **Report a vulnerability**).
2. Otherwise use your organisation's usual channel for **ADHA / AuDigitalHealth** repositories. Do **not** post exploit details, live credentials, or production URLs in a **public issue** before triage.

## This repository

- **Do not commit secrets to git.** That includes passwords, API tokens, private keys, real mutual-TLS keystores, production or staging PCEHR endpoint URLs, and vendor registration material — even in comments, test fixtures, sample code, or tracked documentation.
- **PCEHR WSDL/XSD** under **`wsdls/src/main/resources/`** are vendored for build convenience. Confirm your organisation's licence before redistributing modified WSDL in public forks.
- **`local.properties.example`** is a template for future test configuration. Copy to **`local.properties`** (gitignored), fill in values, and never commit the populated file.
- Historical integration tests may reference cert-environment hostnames in **`src/test/java`**. Treat those as examples only; rotate credentials if they were ever real.
