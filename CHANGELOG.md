# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Tool image identity: `ToolImage` now carries an immutable `digest` (moved from `SecurityMetadata`), `repository`, optional `tag`, `platforms`, `parentDigest` and multiple `registries`; `OCIRegistry` gains `proxyOf`.
- `Tool` gains `version`; `JobExecution` gains `resolvedImageDigest` to record the image actually run.
- `Compliance` gains signature, signer, SBOM and provenance fields; `Vulnerability` gains `severity` and `scannedAt`.
