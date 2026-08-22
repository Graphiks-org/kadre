# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Changed
- Kotlin 2.4.0 -> 2.4.10
- Gradle 9.5.0 -> 9.6.1
- Added blocking pull request policy checks aligned with `CONTRIBUTING.md`.
- Replaced starter-oriented project documentation with `kadre` library guidance.
- Reframed the published site and support content around the `:kadre` module and
  `org.graphiks:kadre` coordinates.

### Added
- `kadre` Kotlin Multiplatform library foundation targeting JVM, Android, iOS
  arm64, and iOS Simulator arm64
- Contributor-facing MkDocs and Dokka documentation for the library foundation
- Support pages pointing to the `Graphiks-org/kadre` repository documentation,
  issues, and discussions
- Code of Conduct, CONTRIBUTING, SECURITY, SUPPORT, and CHANGELOG documents

### Fixed
- Default snapshot publication version when no workflow version is provided.

### Built with
- Kotlin 2.4.10, Gradle 9.6.1, AGP 9.0.0
