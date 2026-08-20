package ygdrasil.conventions

import java.net.URI

plugins {
    id("org.jetbrains.dokka")
}

dokka {
    moduleName.set("kadre")
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(project.file("src/commonMain/kotlin"))
            remoteUrl.set(URI("https://github.com/Graphiks-org/kadre/blob/master/kadre/src/commonMain/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}
