import org.gradle.api.Action
import org.gradle.api.Project

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kadre-root"
include(":kadre")
include(":docs")

val releaseVersion = providers.gradleProperty("releaseVersion")
    .getOrElse("")
    .trim()
    .takeIf { it.isNotBlank() }
    ?: "1.0.0-SNAPSHOT"

class ProjectIdentityAction(private val releaseVersion: String) : Action<Project> {
    override fun execute(project: Project) {
        project.group = "org.graphiks"
        project.version = releaseVersion
    }
}

gradle.afterProject(ProjectIdentityAction(releaseVersion))
