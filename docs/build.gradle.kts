import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Sync

plugins {
    id("dev.opensavvy.dokka-mkdocs") version "0.6.3"
}

evaluationDependsOn(":kadre")

val kadreProject = project(":kadre")
val kadreDokkaModule = kadreProject.tasks.named("dokkaGenerateModuleMkdocs")

val clearKadreDokkaFromMkDocs = tasks.register<Delete>("clearKadreDokkaFromMkDocs") {
    delete(layout.projectDirectory.dir("docs/api/kadre"))
}

val copyKadreDokkaIntoMkDocs = tasks.register<Sync>("copyKadreDokkaIntoMkDocs") {
    dependsOn(kadreDokkaModule)
    dependsOn(clearKadreDokkaFromMkDocs)

    from(kadreProject.layout.buildDirectory.dir("dokka/mkdocs"))
    into(layout.projectDirectory.dir("docs/api/kadre"))
}

val validateKadreDokkaEmbedding = tasks.register("validateKadreDokkaEmbedding") {
    dependsOn(copyKadreDokkaIntoMkDocs)

    doLast {
        val copyTask = copyKadreDokkaIntoMkDocs.get()
        val generatedTaskPath = kadreDokkaModule.get().path
        check(copyTask.taskDependencies.getDependencies(copyTask).any { it.path == generatedTaskPath }) {
            "${copyTask.path} must depend on $generatedTaskPath"
        }
    }
}

tasks.named("generateMkDocsNavigation") {
    dependsOn(validateKadreDokkaEmbedding)
}
