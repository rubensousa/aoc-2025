plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
    test {
        kotlin.srcDir("test")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

dependencies {
    implementation("tools.aqua:z3-turnkey:4.14.1")
    testImplementation(kotlin("test"))
    testImplementation("com.google.truth:truth:1.4.4")
}
