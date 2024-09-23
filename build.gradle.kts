import org.jetbrains.compose.desktop.application.dsl.TargetFormat
// there is no god
plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.11"
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "com.xephyrous"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://dl.bintray.com/kotlin/dokka")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
    implementation("com.mohamedrejeb.dnd:compose-dnd:0.2.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Image_Crunch"
            packageVersion = "1.0.0"
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("docs/html"))
}
