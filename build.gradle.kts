plugins {
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.jmh) apply false
}

allprojects {
    group = "tech.pujit.strata"
    version = "3.0.0"
}

subprojects {
    apply(plugin = "java-library")

    val javaTarget = providers.gradleProperty("strata.java.version").getOrElse("25").toInt()

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaTarget))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        // Configure Java Vector API (incubator)
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:all",
                "-Xlint:-processing",
                "--add-modules", "jdk.incubator.vector"
            )
        )
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        // JVM execution flags: incubator Vector API and native access
        jvmArgs(
            "--add-modules", "jdk.incubator.vector",
            "--enable-native-access=ALL-UNNAMED"
        )
    }

    dependencies {
        add("implementation", rootProject.libs.slf4j.api)
        add("testImplementation", rootProject.libs.bundles.testing)
        add("testRuntimeOnly", rootProject.libs.junit.jupiter.engine)
        add("testRuntimeOnly", rootProject.libs.logback.classic)
    }
}
