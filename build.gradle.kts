import org.jetbrains.compose.desktop.application.dsl.TargetFormat
// there is no god
plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.11"
    id("org.jetbrains.dokka") version "1.9.10"
    id("org.pkl-lang") version("0.25.1")
}

group = "com.tirana"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://dl.bintray.com/kotlin/dokka")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.material3)
    implementation("io.github.succlz123:compose-imageloader-desktop:0.0.2")
    implementation("org.pkl-lang:pkl-core:0.25.2")
    implementation("org.pkl-lang:pkl-config-kotlin:0.25.2")
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
