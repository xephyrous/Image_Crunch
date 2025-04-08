import org.jetbrains.compose.desktop.application.dsl.TargetFormat
// there is no god
plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.8.0-beta01"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "com.xephyrous"
version = "1.2.0a"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://dl.bintray.com/kotlin/dokka")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect")
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
}

dependencies {
    // Compose & Material
    implementation(compose.ui)
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.material3)

    // Jewel
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone-243:0.27.0")
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window-243:0.27.0")

    // KotlinX
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Fluent UI
    implementation("com.konyaco:fluent-desktop:0.0.1-dev.8")
    implementation("com.konyaco:fluent-icons-extended-desktop:0.0.1-dev.8")

    // Misc
    implementation(kotlin("reflect"))
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    val optIns= listOf<String>(
    ).map {
        "-Xopt-in=$it"
    }
    val contextReceivers="-Xcontext-receivers"
    kotlinOptions {
        freeCompilerArgs += optIns+contextReceivers
    }
}