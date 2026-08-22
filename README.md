# kadre - Kotlin Multiplatform library foundation (JVM / Android / iOS)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-purple?logo=kotlin)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-9.6.1-blue?logo=gradle)](https://gradle.org)
[![AGP](https://img.shields.io/badge/AGP-9.0.0-green?logo=android)](https://developer.android.com/build/releases/gradle-plugin)
[![JDK](https://img.shields.io/badge/JDK-25-red?logo=openjdk)](https://openjdk.org)
[![CI](https://github.com/Graphiks-org/kadre/actions/workflows/ci.yml/badge.svg)](https://github.com/Graphiks-org/kadre/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue?style=plastic)](LICENSE)
[![Contributing](https://img.shields.io/badge/Contributing-guide-purple)](CONTRIBUTING.md)
[![Project: Incubating](https://img.shields.io/badge/Status-Incubating-orange)](https://github.com/Graphiks-org/kadre)

`kadre` is a Kotlin Multiplatform library foundation for the Graphiks
ecosystem. The repository currently provides the standalone Gradle module,
publication metadata, API documentation pipeline, and contributor workflow
that future library sources will build on.

This is an **incubating foundation**: no provisional public runtime API has
been introduced yet. The initial work focuses on keeping the build, targets,
publication coordinates, documentation, and CI coherent before the library
contract is defined.

- [Supported targets](#supported-targets)
- [Consuming kadre](#consuming-kadre)
- [Current library contract](#current-library-contract)
- [Versioning and publication](#versioning-and-publication)
- [Contributing](#contributing)
- [Project architecture](#project-architecture)
- [CI/CD workflow](#cicd-workflow)
- [Useful development commands](#useful-development-commands)
- [License](#license)

## Supported targets

| Target | Configuration | Notes |
|--------|---------------|-------|
| **JVM** | Kotlin/JVM | JDK 25 toolchain |
| **Android** | Kotlin Multiplatform Android library | `minSdk 24`, `compileSdk 37` |
| **iOS** | `iosArm64()` | Physical iOS devices |
| **iOS Simulator** | `iosSimulatorArm64()` | Apple Silicon simulators |

The supported target set is intentionally limited to the current foundation.
Additional Kotlin/Native targets can be introduced when the library contract
requires them.

## Consuming kadre

Publication is configured for Maven Central under the `org.graphiks` group.
The root Kotlin Multiplatform coordinate is:

```kotlin
implementation("org.graphiks:kadre:1.0.0-SNAPSHOT")
```

For a Kotlin Multiplatform consumer, declare the dependency in `commonMain`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("org.graphiks:kadre:1.0.0-SNAPSHOT")
        }
    }
}
```

The default development version is `1.0.0-SNAPSHOT`. Replace it with an
explicit release version when consuming a published release. Platform
variants are resolved by Gradle from the Kotlin Multiplatform metadata.

The runtime surface is intentionally empty in this foundation release, so
there is no application-level API to demonstrate yet.

## Current library contract

The repository is prepared to receive future sources under the
`org.graphiks.kadre` package. The public source sets will follow the standard
Kotlin Multiplatform layout:

```text
kadre/src/commonMain/kotlin/org/graphiks/kadre/
kadre/src/androidMain/kotlin/org/graphiks/kadre/
kadre/src/jvmMain/kotlin/org/graphiks/kadre/
kadre/src/iosMain/kotlin/org/graphiks/kadre/
```

No placeholder `Kadre` class, sample Clean Architecture layer, or provisional
domain contract is published. This keeps the initial artifact focused on
project integration until the real library API is agreed.

## Versioning and publication

`kadre` follows Semantic Versioning once its public contract is established.
During foundation work, the default version remains `1.0.0-SNAPSHOT`.

Gradle version selection uses the `releaseVersion` property:

```bash
./gradlew build -PreleaseVersion=1.0.0-SNAPSHOT
```

The release workflow publishes the `:kadre` module to Maven Central from
`master`, release tags, or an explicit workflow dispatch version. Publication
coordinates are:

| Context | Coordinate |
|---------|------------|
| Kotlin Multiplatform root | `org.graphiks:kadre` |
| JVM variant | `org.graphiks:kadre-jvm` |
| Android variant | `org.graphiks:kadre-android` |
| iOS variants | Generated Kotlin Multiplatform target artifacts |

## Contributing

Contributions are welcome. Please see:

- [Contributing Guide](CONTRIBUTING.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)
- [Security Policy](SECURITY.md)
- [Support](SUPPORT.md)
- [Changelog](CHANGELOG.md)

Before opening a pull request, run the local JVM test task and the full build.
Pull requests targeting `master` also run the deep multiplatform CI checks.

## Project architecture

This repository is a standalone Kotlin Multiplatform build with one library
module and one documentation module:

| Module | Purpose |
|--------|---------|
| `:kadre` | The future multiplatform runtime library and platform targets |
| `:docs` | Dokka API output, MkDocs content, and contributor documentation |
| `buildSrc` | Internal Kotlin and publication conventions |

The project deliberately does not copy the native FFI, cinterop, JNI, or
benchmark modules from `kffi`. The relationship to `kffi` is structural:
`kadre` uses the same kind of standalone KMP library, publication, Dokka, and
CI foundations while leaving its own runtime scope open.

## Documentation

API documentation is generated with Dokka and embedded into the MkDocs site.
Generate it locally with:

```bash
./gradlew :docs:embedDokkaIntoMkDocs
```

The documentation site is published at
<https://graphiks-org.github.io/kadre/>. The source repository is
<https://github.com/Graphiks-org/kadre>.

## CI/CD workflow

The GitHub Actions workflows provide separate checks for library builds,
documentation, policy validation, and publication:

- **Fast-track checks** run `./gradlew :kadre:jvmTest` on non-release branch
  events that do not target `master`.
- **Deep checks** run `./gradlew allTests` for pushes to `master` and pull
  requests targeting `master`, covering JVM, Android, and iOS Simulator paths.
- **Documentation deployment** generates Dokka output, embeds it into MkDocs,
  builds the Material site, and deploys it to GitHub Pages.
- **Publication** runs `:kadre:publishToMavenCentral` from the release
  workflow with the configured Maven Central and signing credentials.
- **PR policy** validates the branch prefix, Conventional Commit title and
  subjects, required PR headings, changelog decision, documentation decision,
  and linear history.

## Useful development commands

### Inspect the project model

```bash
./gradlew projects
```

### Run JVM tests

```bash
./gradlew :kadre:jvmTest
```

### Run all target tests

```bash
./gradlew allTests
```

### Build all modules

```bash
./gradlew build
```

### Generate and embed API documentation

```bash
./gradlew :docs:embedDokkaIntoMkDocs
```

### Inspect effective project identity

```bash
./gradlew :kadre:properties --no-daemon --console=plain -PreleaseVersion=
```

## License

MIT — see [LICENSE](LICENSE).
