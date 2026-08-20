# kadre documentation

`kadre` is a Kotlin Multiplatform library for the Graphiks ecosystem.
This repository is being prepared as a reusable library foundation with a
published API, contributor guides, and generated reference documentation.

## Current status

The project is in foundation setup. The goal is to stabilize the module
layout, documentation, publication metadata, and contributor workflow before
expanding the runtime surface.

## Supported targets

- JVM (Java 25)
- Android (minSdk 24)
- iOS arm64
- iOS Simulator arm64

## Local development commands

Use these commands from the repository root:

```bash
./gradlew :kadre:jvmTest
./gradlew build
./gradlew :docs:embedDokkaIntoMkDocs
```

## API reference

The API reference is generated with Dokka and embedded into this MkDocs site.
After running `./gradlew :docs:embedDokkaIntoMkDocs`, browse the generated API
pages locally or through the published site at
<https://graphiks-org.github.io/kadre/>.

## Project links

- Repository: <https://github.com/Graphiks-org/kadre>
- Documentation site: <https://graphiks-org.github.io/kadre/>
- Contributing guide: [CONTRIBUTING.md](../../CONTRIBUTING.md)
- Support: [SUPPORT.md](../../SUPPORT.md)
