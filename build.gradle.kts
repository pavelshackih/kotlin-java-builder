import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

allprojects {
    group = "io.pavelshackih"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}

val kotlinPluginVersion by extra { "1.2.30" }

task<Wrapper>("wrapper") {
    gradleVersion = "4.6"
    distributionType = DistributionType.ALL
}