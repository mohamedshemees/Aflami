import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.firebase.gms.service) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.performance) apply false
    alias(libs.plugins.ksp) apply false

    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}
subprojects {
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
        verbose.set(true)
        android.set(true)

        outputToConsole.set(true)

        outputColorName.set("RED")
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)

        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }

        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
        dependencies {
            ktlintRuleset("io.nlopez.compose.rules:ktlint:0.4.18")
        }
    }
}
// Register a task to copy git hooks
// Copy Git hooks
tasks.register<Copy>("copyGitHooks") {
    description = "Copies the git hooks from /git-hooks to the .git folder."
    group = "git hooks"
    from("$rootDir/.hooks/pre-commit")
    into("$rootDir/.git/hooks/")
}

// Install Git hooks
tasks.register<Exec>("installGitHooks") {
    description = "Installs the pre-commit git hooks from /git-hooks."
    group = "git hooks"
    workingDir = rootDir
    commandLine("chmod", "-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")

    doLast {
        logger.info("Git hook installed successfully.")
    }
}

// Ensure Git hooks run before app:preBuild (in the root build.gradle.kts)
gradle.projectsEvaluated {
    project(":app").tasks.named("preBuild").configure {
        dependsOn(":installGitHooks")
    }
}
