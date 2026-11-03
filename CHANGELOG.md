# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Tool image identity: `ToolImage` now carries an immutable `digest` (moved from `SecurityMetadata`), `repository`, optional `tag`, `platforms`, `parentDigest` and multiple `registries`; `OCIRegistry` gains `proxyOf`.
- `Tool` gains `version`; `JobExecution` gains `resolvedImageDigest` to record the image actually run.
- `Compliance` gains signature, signer, SBOM and provenance fields; `Vulnerability` gains `severity` and `scannedAt`.
- `Tool.image` is now a composition; `ToolImage.parentImage` removed in favour of `parentDigest`.
- Attribute renames avoiding Java/SQL keyword and JSON-schema pluralisation clashes: `Tool.interface`→`toolInterface`, `Discovery.keywords`→`keyword`, `Discovery.tools`→`software`, `ParameterValue.value`→`content`, `EnvVar.value`→`setting`, `ParameterOrGroup.group`→`subGroup`.
- `BaseExample` expanded to exercise locations, nodes, data resources, registries, tool images, parameter definitions/groups and several job executions.
