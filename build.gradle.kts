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

val kotlinPluginVersion by extra { "1.2.40" }

task<Wrapper>("wrapper") {
    gradleVersion = "4.7"
    distributionType = DistributionType.ALL
}