import org.gradle.api.tasks.Copy
import org.gradle.internal.os.OperatingSystem

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
val copyGitHooks by tasks.registering(Copy::class) {
    description = "Copies pre-commit hook to the .git directory"
    group = "git hooks"

    from(layout.projectDirectory.dir(".hooks").file("pre-commit"))
    into(layout.projectDirectory.dir(".git/hooks"))
}

tasks.register("installGitHooks") {
    group = "git hooks"
    description = "Installs the pre-commit git hooks."

    doLast {
        val hookSrc = file("$rootDir/.hooks/pre-commit")
        val hookDst = file("$rootDir/.git/hooks/pre-commit")

        hookSrc.copyTo(hookDst, overwrite = true)

        if (!OperatingSystem.current().isWindows) {
            hookDst.setExecutable(true)
        }

        println("✅ Git pre-commit hook installed.")
    }
}
