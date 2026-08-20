import org.gradle.api.tasks.Sync

plugins {
    id("dev.opensavvy.dokka-mkdocs") version "0.6.3"
}

val copySharedDokkaIntoMkDocs by tasks.registering(Sync::class) {
    dependsOn(project(":kadre").tasks.named("dokkaGenerateModuleMkdocs"))
    dependsOn(tasks.named("dokkaCopyIntoMkDocs"))

    from(project(":kadre").layout.buildDirectory.dir("dokka-module/mkdocs/module"))
    into(layout.projectDirectory.dir("docs/api/shared"))
}

tasks.named("generateMkDocsNavigation") {
    dependsOn(copySharedDokkaIntoMkDocs)
}
