plugins {
    application
    alias(libs.plugins.protobuf)
}

application {
    mainClass.set("tech.pujit.strata.engine.Main")
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "jdk.incubator.vector",
        "--enable-native-access=ALL-UNNAMED"
    )
}

sourceSets {
    main {
        proto {
            srcDir("${rootDir}/proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.libs.versions.protobuf.get()}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.libs.versions.grpc.get()}"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}

dependencies {
    implementation(project(":strata-core"))
    implementation(project(":strata-storage"))
    implementation(project(":strata-query"))

    implementation(rootProject.libs.bundles.grpc)
    implementation(rootProject.libs.protobuf.java)
    implementation(rootProject.libs.protovalidate)
    implementation(rootProject.libs.jackson.databind)
    compileOnly(rootProject.libs.annotation.api)

    testImplementation(rootProject.libs.grpc.testing)
    testImplementation(rootProject.libs.grpc.inprocess)
}
