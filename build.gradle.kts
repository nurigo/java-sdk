import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.0"
    `maven-publish`
    signing
}

group = "net.nurigo"
version = "4.0.5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.3.1")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))

    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.0")
    dokkaHtmlPlugin("org.jetbrains.dokka:javadoc-plugin:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

val ossusername: String by project
val osspassword: String by project

publishing {
    repositories {
        maven {
            name = "oss"
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = ossusername
                password = osspassword
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = group.toString()
            artifactId = "sdk"
            version = version

            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("Nurigo SDK")
                description.set("누리고 서비스에서 사용되는 문자 발송용 SDK")
                url.set("https://github.com/nurigo/java-sdk-v4")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("hosy")
                        name.set("Hosy Lee")
                        email.set("hosy@nurigo.net")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/nurigo/java-sdk-v4.git")
                    developerConnection.set("scm:git:ssh://github.com/nurigo/java-sdk-v4.git")
                    url.set("https://github.com/nurigo/java-sdk-v4")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
