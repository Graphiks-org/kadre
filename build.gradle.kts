group = "org.graphiks"

allprojects {
    group = "org.graphiks"
    version = (rootProject.findProperty("releaseVersion") as? String)
        ?.trim()
        ?.takeIf { it.isNotBlank() }
        ?: "1.0.0-SNAPSHOT"
}
