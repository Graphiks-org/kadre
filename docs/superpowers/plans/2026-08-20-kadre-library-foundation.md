# Kadre Library Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Convert the generic KMP starter into the `kadre` library foundation described in the approved design.

**Architecture:** Keep one public KMP module, `:kadre`, plus the existing `:docs` site module. Preserve the template’s JVM, Android, iOS arm64 and iOS simulator arm64 targets, remove application-oriented dependencies and sample layers, and align publication, Dokka, CI and repository metadata with `Graphiks-org/kadre`.

**Tech Stack:** Kotlin 2.4.10, Gradle 9.6.1, Kotlin Multiplatform, Android KMP library plugin, Dokka, Vanniktech Maven Publish, MkDocs Material, Java 25.

**Spec:** `docs/superpowers/specs/2026-08-20-kadre-library-foundation-design.md`

## Global Constraints

- Preserve the product name `kadre` and repository URL `https://github.com/Graphiks-org/kadre`.
- Use Maven group `org.graphiks`, artifact `kadre`, and default version `1.0.0-SNAPSHOT`.
- Keep only the current template target baseline: JVM, Android, iOS arm64 and iOS simulator arm64.
- Do not copy kffi FFI, JNI, cinterop, benchmark, POSIX or Wayland modules.
- Remove Compose, Koin, Ktor, Kotlin Serialization and Android activity dependencies from the library.
- Keep `ygdrasil.conventions` only as the internal `buildSrc` convention package.
- Every implementation task must run its failing verification before the change and its passing verification after the change.
- Use Conventional Commit subjects with scopes `kadre`, `buildSrc`, `docs` or `release`.

---

### Task 1: Rename the project and primary module

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `build.gradle.kts`
- Rename: `shared/` → `kadre/`
- Rename: `shared/build.gradle.kts` → `kadre/build.gradle.kts`
- Test: Gradle project identity commands

**Interfaces:**
- Produces project path `:kadre`, root name `kadre-root`, group `org.graphiks` and default version `1.0.0-SNAPSHOT` for later tasks.

- [ ] **Step 1: Run the failing identity check**

Run:

```bash
./gradlew :kadre:properties --no-daemon --console=plain -PreleaseVersion=
```

Expected: FAIL because the current project exposes `:shared`, not `:kadre`.

- [ ] **Step 2: Rename the module directory**

Run:

```bash
git mv shared kadre
```

- [ ] **Step 3: Update root identity and module inclusion**

Set the relevant blocks in `settings.gradle.kts` to:

```kotlin
rootProject.name = "kadre-root"
include(":kadre")
include(":docs")

group = "org.graphiks"
version = (project.findProperty("releaseVersion") as? String)
    ?.trim()
    ?.takeIf { it.isNotBlank() }
    ?: "1.0.0-SNAPSHOT"
```

Set `build.gradle.kts` to:

```kotlin
group = "org.graphiks"
```

- [ ] **Step 4: Run the passing identity check**

Run:

```bash
./gradlew :kadre:properties --no-daemon --console=plain -PreleaseVersion=
```

Expected: the output contains `path: :kadre`, `group: org.graphiks` and `version: 1.0.0-SNAPSHOT`.

- [ ] **Step 5: Commit the module rename**

```bash
git add settings.gradle.kts build.gradle.kts kadre
git commit -m "build(kadre): rename shared module to kadre"
```

### Task 2: Reduce the module to a dependency-free library baseline

**Files:**
- Modify: `kadre/build.gradle.kts`
- Modify: `gradle/libs.versions.toml`
- Delete: `kadre/src/commonMain/kotlin/io/ygdrasil/shared/`
- Delete: `kadre/src/androidMain/kotlin/io/ygdrasil/shared/`
- Delete: `kadre/src/iosMain/kotlin/io/ygdrasil/shared/`
- Delete: `kadre/src/jvmMain/kotlin/io/ygdrasil/shared/`
- Delete: `kadre/src/commonTest/kotlin/io/ygdrasil/shared/`
- Test: static dependency check and JVM compilation/test tasks

**Interfaces:**
- Consumes `:kadre` from Task 1.
- Produces a KMP library module with no application-layer sample API and only Kotlin test support.

- [ ] **Step 1: Run the failing dependency check**

Run:

```bash
if rg -n -i 'compose|koin|ktor|serialization|activity-compose' kadre/build.gradle.kts gradle/libs.versions.toml; then
  exit 1
fi
```

Expected: FAIL because the starter dependencies are still present.

- [ ] **Step 2: Replace the module build script**

Set `kadre/build.gradle.kts` to:

```kotlin
plugins {
    id("ygdrasil.conventions.kmp-library")
    id("ygdrasil.conventions.kmp-publish")
    id("ygdrasil.conventions.kmp-dokka")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
```

- [ ] **Step 3: Trim the version catalog**

Keep the tooling versions `kotlin`, `agp`, `dokka` and `maven-publish`. Remove the version entries and aliases for Compose, Koin, Ktor, Kotlin Serialization, AndroidX activity and unused application/cocoapods plugins. Keep only the plugin aliases used by the library build and conventions:

```toml
[versions]
kotlin = "2.4.10"
agp = "9.0.0"
dokka = "2.2.0"
maven-publish = "0.36.0"

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlin-dokka = { id = "org.jetbrains.dokka", version.ref = "dokka" }
maven-publish = { id = "com.vanniktech.maven.publish", version.ref = "maven-publish" }
```

- [ ] **Step 4: Remove the template source tree**

Delete the `Platform`, repository, model, use case, ViewModel, Koin module and `PlatformTest` files under the old package path. Do not add a placeholder public API; future `kadre` sources will define the public contract.

- [ ] **Step 5: Run the passing build check**

Run:

```bash
./gradlew :kadre:compileKotlinJvm :kadre:jvmTest --no-daemon --stacktrace
```

Expected: both tasks finish successfully without Compose, Koin, Ktor or Serialization resolution.

- [ ] **Step 6: Commit the library baseline**

```bash
git add kadre gradle/libs.versions.toml
git commit -m "build(kadre): remove starter application dependencies"
```

### Task 3: Align conventions, API documentation and version verification

**Files:**
- Modify: `buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-library.gradle.kts`
- Modify: `buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-publish.gradle.kts`
- Modify: `buildSrc/src/main/kotlin/ygdrasil/conventions/kmp-dokka.gradle.kts`
- Modify: `docs/build.gradle.kts`
- Modify: `scripts/test-publish-version.sh`
- Test: Dokka embedding and publication version script

**Interfaces:**
- Consumes the `:kadre` project from Tasks 1–2.
- Produces Android namespace `org.graphiks.kadre`, Maven coordinates `org.graphiks:kadre`, Dokka module `kadre`, and generated API path `docs/api/kadre`.

- [ ] **Step 1: Run the failing documentation/version checks**

Run:

```bash
./gradlew :docs:embedDokkaIntoMkDocs --no-daemon
scripts/test-publish-version.sh
```

Expected: at least one check fails because the current build files still reference `:shared`.

- [ ] **Step 2: Update the Android library convention**

Change only the published namespace in `kmp-library.gradle.kts`:

```kotlin
namespace = "org.graphiks.kadre"
```

Keep the current toolchain and target baseline (`jvmToolchain(25)`, Android, JVM, iOS arm64 and iOS simulator arm64).

- [ ] **Step 3: Update Maven publication metadata**

Use these values in `kmp-publish.gradle.kts`:

```kotlin
group = "org.graphiks"
version = (project.findProperty("releaseVersion") as? String)
    ?.trim()
    ?.takeIf { it.isNotBlank() }
    ?: "1.0.0-SNAPSHOT"

coordinates(group.toString(), project.name, version.toString())

pom {
    name.set("kadre")
    description.set("Kotlin Multiplatform library for kadre")
    url.set("https://github.com/Graphiks-org/kadre")
    licenses {
        license {
            name.set("MIT")
            url.set("https://opensource.org/license/MIT")
        }
    }
    developers {
        developer {
            id.set("graphiks-org")
            name.set("Graphiks.org")
        }
    }
    scm {
        connection.set("scm:git:https://github.com/Graphiks-org/kadre.git")
        developerConnection.set("scm:git:ssh://github.com/Graphiks-org/kadre.git")
        url.set("https://github.com/Graphiks-org/kadre")
    }
}
```

Preserve the existing conditional signing behavior and MIT license.

- [ ] **Step 4: Update Dokka and docs embedding**

Set the Dokka convention to:

```kotlin
moduleName.set("kadre")
remoteUrl.set(URI("https://github.com/Graphiks-org/kadre/blob/master/kadre/src/commonMain/kotlin"))
```

Use `kadre` in the docs build task and copy the generated module into `docs/api/kadre`:

```kotlin
val copyKadreDokkaIntoMkDocs by tasks.registering(Sync::class) {
    dependsOn(project(":kadre").tasks.named("dokkaGenerateModuleMkdocs"))
    dependsOn(tasks.named("dokkaCopyIntoMkDocs"))
    from(project(":kadre").layout.buildDirectory.dir("dokka-module/mkdocs/module"))
    into(layout.projectDirectory.dir("docs/api/kadre"))
}

tasks.named("generateMkDocsNavigation") {
    dependsOn(copyKadreDokkaIntoMkDocs)
}
```

- [ ] **Step 5: Update the version script**

Replace `:shared:properties` with `:kadre:properties` in `scripts/test-publish-version.sh`; keep the expected version `1.0.0-SNAPSHOT` and the existing `sed` extraction.

- [ ] **Step 6: Run the passing checks**

Run:

```bash
./gradlew :docs:embedDokkaIntoMkDocs --no-daemon --stacktrace
scripts/test-publish-version.sh
```

Expected: both commands exit with status 0 and generated API files appear under `docs/api/kadre`.

- [ ] **Step 7: Commit convention and documentation wiring**

```bash
git add buildSrc docs/build.gradle.kts scripts/test-publish-version.sh
git commit -m "build(buildSrc): align kadre publication and Dokka metadata"
```

### Task 4: Rewrite project documentation and support links

**Files:**
- Modify: `README.md`
- Modify: `docs/mkdocs.yml`
- Modify: `docs/docs/index.md`
- Modify: `docs/docs/index.fr.md`
- Modify: `docs/docs/getting-started.md`
- Modify: `SUPPORT.md`
- Modify: `SUPPORT.fr.md`
- Modify: `CHANGELOG.md`
- Test: repository identity search and Markdown diff check

**Interfaces:**
- Produces user-facing documentation that describes `kadre` as a KMP library in preparation, not as a generic starter pack.

- [ ] **Step 1: Run the failing documentation identity check**

Run:

```bash
if rg -n -i 'KMP Starter Pack|Starter Pack|project-template|:shared|io\\.ygdrasil\\.shared|ygdrasil-io\\.github\\.io/project-template' README.md docs/mkdocs.yml docs/docs SUPPORT.md SUPPORT.fr.md CHANGELOG.md; then
  exit 1
fi
```

Expected: FAIL on the template descriptions and links.

- [ ] **Step 2: Rewrite the README**

Use a concise library README with this required structure and commands:

````markdown
# kadre

Kotlin Multiplatform library for the Graphiks ecosystem.

## Status

The repository is being prepared as a reusable library foundation.

## Supported targets

- JVM (Java 25)
- Android (minSdk 24)
- iOS arm64
- iOS Simulator arm64

## Development

```bash
./gradlew :kadre:jvmTest
./gradlew build
./gradlew :docs:embedDokkaIntoMkDocs
```

## Publication

The Maven coordinates are `org.graphiks:kadre` and the default development
version is `1.0.0-SNAPSHOT`.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).
````

Keep the existing badges only when their links point to the `kadre` repository.

- [ ] **Step 3: Update MkDocs metadata and pages**

Set `docs/mkdocs.yml` to use `kadre`, `https://graphiks-org.github.io/kadre/`, and `https://github.com/Graphiks-org/kadre`. Keep the existing Material theme and bilingual navigation. Rewrite the English and French index pages around the library purpose, supported targets, local commands and generated API reference. Replace `getting-started.md` with contributor setup steps that start at `./gradlew :kadre:jvmTest` and do not mention renaming a template.

- [ ] **Step 4: Update support and changelog content**

Point `SUPPORT.md` and `SUPPORT.fr.md` documentation, issues and discussions links to `Graphiks-org/kadre` and `graphiks-org.github.io/kadre`. Rewrite the unreleased changelog entries to describe the `kadre` library foundation and remove the starter’s Compose/Koin/Ktor feature list. Keep historical versioning and license references intact.

- [ ] **Step 5: Run the passing documentation checks**

Run:

```bash
if rg -n -i 'KMP Starter Pack|Starter Pack|project-template|:shared|io\\.ygdrasil\\.shared|ygdrasil-io\\.github\\.io/project-template' README.md docs/mkdocs.yml docs/docs SUPPORT.md SUPPORT.fr.md CHANGELOG.md; then
  exit 1
fi
git diff --check
```

Expected: the search produces no output and `git diff --check` exits 0.

- [ ] **Step 6: Commit the documentation rewrite**

```bash
git add README.md docs SUPPORT.md SUPPORT.fr.md CHANGELOG.md
git commit -m "docs(kadre): replace starter documentation with library guidance"
```

### Task 5: Update contribution policy, CI and release workflows

**Files:**
- Modify: `CONTRIBUTING.md`
- Modify: `.github/PULL_REQUEST_TEMPLATE.md`
- Modify: `.github/contributing-policy.toml`
- Modify: `.github/scripts/test_validate_pr_policy.py`
- Modify: `.github/workflows/ci.yml`
- Modify: `.github/workflows/docs.yml`
- Modify: `.github/workflows/publish.yml`
- Modify: `.github/ISSUE_TEMPLATE/config.yml`
- Test: policy fixture tests and stale-reference search

**Interfaces:**
- Produces CI tasks and policy fixtures that use `kadre` consistently and keep the existing branch, PR and changelog rules.

- [ ] **Step 1: Run the failing workflow/policy search**

Run:

```bash
if rg -n -i 'KMP Starter Pack|project-template|:shared|allowed_scopes.*shared|feat\\(shared\\)|docs\\(shared\\)' CONTRIBUTING.md .github scripts; then
  exit 1
fi
```

Expected: FAIL on the existing module scope, workflow tasks, fixture titles and issue-config links.

- [ ] **Step 2: Update the policy and test fixtures**

Set `.github/contributing-policy.toml` to:

```toml
allowed_scopes = ["kadre", "buildSrc", "docs", "release"]
```

Update every expected policy fixture in `.github/scripts/test_validate_pr_policy.py` from `shared` to `kadre`, preserving each test’s rule and expected pass/fail outcome. Update `CONTRIBUTING.md` and `.github/PULL_REQUEST_TEMPLATE.md` commands, scopes and examples to `:kadre:jvmTest` and `feat(kadre): ...`.

- [ ] **Step 3: Update CI, docs deployment and publishing**

Change `.github/workflows/ci.yml` to `name: Kadre CI` and replace `:shared:jvmTest` with `:kadre:jvmTest`. Change `.github/workflows/docs.yml` path filters from `shared/src/commonMain/**` to `kadre/src/commonMain/**`. Replace both `:shared:publishToMavenCentral` invocations in `.github/workflows/publish.yml` with `:kadre:publishToMavenCentral`. Update `.github/ISSUE_TEMPLATE/config.yml` links to the `Graphiks-org/kadre` discussions page and `graphiks-org.github.io/kadre` site.

- [ ] **Step 4: Run the passing policy checks**

Run:

```bash
python3 .github/scripts/test_validate_pr_policy.py
if rg -n -i 'KMP Starter Pack|project-template|:shared|allowed_scopes.*shared|feat\\(shared\\)|docs\\(shared\\)' CONTRIBUTING.md .github scripts; then
  exit 1
fi
```

Expected: the policy test exits 0 and the stale-reference search produces no output.

- [ ] **Step 5: Commit repository automation updates**

```bash
git add CONTRIBUTING.md .github
git commit -m "ci(kadre): update policy and workflows for library module"
```

### Task 6: Run the complete acceptance verification

**Files:**
- Test: Gradle build, JVM tests, Dokka embedding, publication version script, stale-reference checks and Git diff

**Interfaces:**
- Verifies every acceptance criterion in the approved design before the work is reported as complete.

- [ ] **Step 1: Verify the project model**

Run:

```bash
./gradlew projects --no-daemon --console=plain
./gradlew :kadre:properties --no-daemon --console=plain -PreleaseVersion=
```

Expected: the project list includes `:kadre` and `:docs`; the properties output contains `org.graphiks`, `:kadre` and `1.0.0-SNAPSHOT`.

- [ ] **Step 2: Run JVM tests and the complete build**

Run:

```bash
./gradlew :kadre:jvmTest --no-daemon --stacktrace
./gradlew build --no-daemon --stacktrace
```

Expected: both commands exit 0.

- [ ] **Step 3: Generate API documentation and verify publication version**

Run:

```bash
./gradlew :docs:embedDokkaIntoMkDocs --no-daemon --stacktrace
scripts/test-publish-version.sh
```

Expected: both commands exit 0 and `docs/api/kadre` contains the generated module documentation.

- [ ] **Step 4: Verify stale references and whitespace**

Run:

```bash
if rg -n -i 'KMP Starter Pack|Starter Pack|project-template|:shared|io\\.ygdrasil\\.shared|ygdrasil-io\\.github\\.io/project-template|feat\\(shared\\)|docs\\(shared\\)' README.md CONTRIBUTING.md CHANGELOG.md SUPPORT.md SUPPORT.fr.md docs/mkdocs.yml docs/docs docs/build.gradle.kts .github scripts kadre gradle build.gradle.kts settings.gradle.kts; then
  exit 1
fi
git diff --check
git status --short
```

Expected: no stale references, no whitespace errors, and only the intended implementation files are present.

- [ ] **Step 5: Record the final verification result**

Capture the exit-0 results and the final file list in the handoff. Do not claim completion if any command above fails.
