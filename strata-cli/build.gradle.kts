plugins {
    application
}

application {
    mainClass.set("tech.pujit.strata.cli.StrataCli")
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "jdk.incubator.vector",
        "--enable-native-access=ALL-UNNAMED"
    )
}

dependencies {
    implementation(project(":strata-core"))
    implementation(project(":strata-storage"))
    implementation(project(":strata-engine"))

    implementation(rootProject.libs.picocli)
    annotationProcessor(rootProject.libs.picocli.codegen)
}
