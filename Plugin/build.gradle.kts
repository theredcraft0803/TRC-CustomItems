plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")
    implementation("fr.skytasul:glowingentities:2.0.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.1.2")
        jvmArgs(
            "-Xms2G",
            "-Xmx2G",
            "-Dcom.mojang.eula.agree=true"
        )
    }

    processResources {
        val props = mapOf(
            "version" to version,
            "description" to project.description
        )

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
