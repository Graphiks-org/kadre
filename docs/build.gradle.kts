import org.gradle.api.tasks.Sync

plugins {
    id("dev.opensavvy.dokka-mkdocs") version "0.6.3"
}

val copyKadreDokkaIntoMkDocs by tasks.registering(Sync::class) {
    dependsOn(tasks.named("dokkaGenerateModuleMkdocs"))
    dependsOn(tasks.named("dokkaCopyIntoMkDocs"))

    from(layout.buildDirectory.dir("dokka/mkdocs"))
    into(layout.projectDirectory.dir("docs/api/kadre"))
}

tasks.named("generateMkDocsNavigation") {
    dependsOn(copyKadreDokkaIntoMkDocs)
}
