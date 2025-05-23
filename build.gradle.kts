import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.compose.gradle.kotlinKarmaConfig
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets

plugins {
    kotlin("multiplatform") apply false
}

val COMPOSE_WEB_VERSION: String = extra["compose.version"] as String
val COMPOSE_REPO_USERNAME: String? by project
val COMPOSE_REPO_KEY: String? by project
val COMPOSE_WEB_BUILD_WITH_SAMPLES = project.property("compose.web.buildSamples")!!.toString().toBoolean()

kotlinKarmaConfig.rootDir = rootProject.rootDir.toString()

apply<jetbrains.compose.web.gradle.SeleniumDriverPlugin>()

fun Project.isSampleProject() = projectDir.parentFile.name == "examples"

tasks.register("printBundleSize") {
    dependsOn(
        subprojects.filter { it.isSampleProject() }.map { ":examples:${it.name}:printBundleSize" }
    )
}

subprojects {
    apply(plugin = "maven-publish")

    val projectName = name
    group = "net.perfectdreams.compose.htmldreams"
    version = COMPOSE_WEB_VERSION

    if ((project.name != "html-widgets") && (project.name != "html-integration-widgets")) {
        afterEvaluate {
            if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                project.kotlinExtension.targets.forEach { target ->
                    target.compilations.forEach { compilation ->
                        compilation.kotlinOptions {
                            allWarningsAsErrors = false
                            // see https://kotlinlang.org/docs/opt-in-requirements.html
                            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
                        }
                    }
                }
            }
        }
    }



    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }

    pluginManager.withPlugin("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "internal"
                    url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
                    credentials {
                        username = COMPOSE_REPO_USERNAME ?: ""
                        password = COMPOSE_REPO_KEY ?: ""
                    }
                }

                maven {
                    name = "PerfectDreams"
                    url = uri("https://repo.perfectdreams.net/")
                    credentials(PasswordCredentials::class)
                }
            }
            publications.all {
                this as MavenPublication
                pom {
                    name.set("JetBrains Compose Multiplatform HTML library")
                    description.set("JetBrains Compose Multiplatform HTML library")
                    url.set("https://www.jetbrains.com/lp/compose-mpp/")
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("JetBrains")
                            name.set("JetBrains Compose Team")
                            organization.set("JetBrains")
                            organizationUrl.set("https://www.jetbrains.com")
                        }
                    }
                    scm {
                        connection.set("scm:git://github.com/JetBrains/compose-multiplatform.git")
                        developerConnection.set("scm:git://github.com/JetBrains/compose-multiplatform.git")
                        url.set("https://github.com/jetbrains/compose-multiplatform")
                    }
                }
            }
            publications {
                val oldArtifactId = when (projectName) {
                    "html-core" -> "web-core"
                    "html-svg" -> "web-svg"
                    "html-test-utils" -> "test-utils"
                    "html-benchmark-core" -> "web-benchmark-core"
                    "internal-html-core-runtime" -> "internal-web-core-runtime"
                    "html-integration-core" -> "web-integration-core"
                    "compose-compiler-integration" -> "compose-compiler-integration"
                    "compose-compiler-integration-lib" -> "compose-compiler-integration-lib"
                    else -> null
                }

                // TODO Remove this publishing in Compose 1.7. The package was migrated in 1.4.
                if (oldArtifactId != null) {
                    create<MavenPublication>("relocation") {
                        pom {
                            // Old artifact coordinates
                            groupId = "org.jetbrains.compose.web"
                            artifactId = oldArtifactId
                            distributionManagement {
                                relocation {
                                    // New artifact coordinates
                                    groupId.set("org.jetbrains.compose.html")
                                    artifactId.set(projectName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    pluginManager.withPlugin("kotlin-multiplatform") {
        val printTestBundleSize by tasks.registering {
            dependsOn(tasks.named("jsTest"))
            doLast {
                val bundlePath = buildDir.resolve(
                    "compileSync/test/testDevelopmentExecutable/kotlin/${rootProject.name}-${project.name}-test.js"
                )
                if (bundlePath.exists()) {
                    val size = bundlePath.length()
                    println("##teamcity[buildStatisticValue key='testBundleSize::${project.name}' value='$size']")
                }
            }
        }

        afterEvaluate {
            tasks.named("jsTest") { finalizedBy(printTestBundleSize) }
        }
    }


    if (isSampleProject()) {
        val printBundleSize by tasks.registering {
            dependsOn(tasks.named("jsBrowserDistribution"))
            doLast {
                val jsFile = buildDir.resolve("distributions/${project.name}.js")
                val size = jsFile.length()
                println("##teamcity[buildStatisticValue key='bundleSize::${project.name}' value='$size']")
            }
        }

        afterEvaluate {
            tasks.named("build") { finalizedBy(printBundleSize) }
        }
    }

    if (COMPOSE_WEB_BUILD_WITH_SAMPLES) {
        println("substituting published artifacts with projects ones in project $name")
        configurations.all {
            resolutionStrategy.dependencySubstitution {
                substitute(module("org.jetbrains.compose.html:html-core")).apply {
                    using(project(":html-core"))
                }
            }
        }
    }

    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        }
        maven {
            url = uri("https://packages.jetbrains.team/maven/p/ui/dev")
        }
        google()
    }

    tasks.withType<AbstractTestTask> {
        testLogging {
            events("FAILED")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
            showStackTraces = true
        }
    }
}
