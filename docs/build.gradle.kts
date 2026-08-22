import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Sync

plugins {
    id("dev.opensavvy.dokka-mkdocs") version "0.6.3"
}

evaluationDependsOn(":kadre")

val kadreProject = project(":kadre")
val kadreDokkaModule = kadreProject.tasks.named("dokkaGenerateModuleMkdocs")
val kadreDokkaModuleOutput = kadreProject.layout.buildDirectory.dir("dokka-module/mkdocs/module")

val clearKadreDokkaFromMkDocs = tasks.register<Delete>("clearKadreDokkaFromMkDocs") {
    delete(layout.projectDirectory.dir("docs/api/kadre"))
}

val copyKadreDokkaIntoMkDocs = tasks.register<Sync>("copyKadreDokkaIntoMkDocs") {
    dependsOn(kadreDokkaModule)
    dependsOn(clearKadreDokkaFromMkDocs)

    from(kadreDokkaModuleOutput)
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

        val generatedOutput = kadreDokkaModuleOutput.get().asFile
        check(generatedOutput.isDirectory) {
            "$generatedTaskPath must generate the module output directory"
        }

        val moduleDescriptor = generatedOutput.parentFile.resolve("module-descriptor.json")
        check(moduleDescriptor.isFile) {
            "$generatedTaskPath must generate module-descriptor.json"
        }

        val descriptor = moduleDescriptor.readText()
        check(Regex("\\\"modulePath\\\"\\s*:\\s*\\\"kadre\\\"").containsMatchIn(descriptor)) {
            "$generatedTaskPath must describe the kadre module"
        }
        check(Regex("\\\"moduleOutputDirName\\\"\\s*:\\s*\\\"module\\\"").containsMatchIn(descriptor)) {
            "$generatedTaskPath must describe the module output directory"
        }
    }
}

tasks.named("generateMkDocsNavigation") {
    dependsOn(validateKadreDokkaEmbedding)
}
