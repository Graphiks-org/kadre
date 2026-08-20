# Getting started

This page is for contributors preparing a local `kadre` workspace.

## 1. Verify the baseline

Run the JVM test task first to confirm the workspace is healthy:

```bash
./gradlew :kadre:jvmTest
```

## 2. Build the library

Run the full build after the JVM checks pass:

```bash
./gradlew build
```

## 3. Generate the documentation site content

Embed the Dokka output into MkDocs:

```bash
./gradlew :docs:embedDokkaIntoMkDocs
```

## 4. Review what changed

- Read the [README.md](https://github.com/Graphiks-org/kadre/blob/master/README.md) for the current library summary.
- Read [SUPPORT.md](https://github.com/Graphiks-org/kadre/blob/master/SUPPORT.md) for user-facing help channels.
- Read [CHANGELOG.md](https://github.com/Graphiks-org/kadre/blob/master/CHANGELOG.md) for release history and pending work.

## 5. Before opening a contribution

- Confirm `./gradlew :kadre:jvmTest` still passes after your changes.
- Confirm `./gradlew build` still passes after your changes.
- Regenerate docs with `./gradlew :docs:embedDokkaIntoMkDocs` when API docs
  or user-facing pages change.
