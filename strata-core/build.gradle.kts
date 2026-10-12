plugins {
    alias(libs.plugins.jmh)
}

dependencies {
    jmh(rootProject.libs.jmh.core)
    jmhAnnotationProcessor(rootProject.libs.jmh.generator.annprocess)
}
