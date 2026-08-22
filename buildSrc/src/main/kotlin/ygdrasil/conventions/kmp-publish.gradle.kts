package ygdrasil.conventions

plugins {
    id("com.vanniktech.maven.publish")
}

group = "org.graphiks"
version = (project.findProperty("releaseVersion") as? String)
    ?.trim()
    ?.takeIf { it.isNotBlank() }
    ?: "1.0.0-SNAPSHOT"

val isPublishing = project.findProperty("signingInMemoryKey")?.toString()?.isNotBlank() == true
    || project.findProperty("signing.keyId")?.toString()?.isNotBlank() == true

mavenPublishing {
    if (isPublishing) {
        publishToMavenCentral()
        signAllPublications()
    }
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
}
