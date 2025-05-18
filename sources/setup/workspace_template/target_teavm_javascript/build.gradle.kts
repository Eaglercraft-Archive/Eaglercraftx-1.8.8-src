import com.resentclient.oss.eaglercraft.build.impl.js
import org.teavm.gradle.api.OptimizationLevel
import org.teavm.gradle.tasks.GenerateJavaScriptTask

buildscript {
	dependencies {
		classpath(files("../src/teavmc-classpath/resources"))
	}
}

plugins {
	id("java")
	id("org.teavm") version "0.9.2"

	id("com.resentclient.oss.eaglercraft.build") version "0.0.0"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
	named("main") {
		java.srcDirs(
			"../src/teavm/java",
			"../src/teavm-boot-menu/java"
		)
		resources.srcDirs(
			"../src/teavm/resources"
		)
	}
}

dependencies {
	teavm(teavm.libs.jso)
	teavm(teavm.libs.jsoApis)
	compileOnly("org.teavm:teavm-core:0.9.2") // workaround for a few hacks
	implementation(rootProject)
	implementation(libs.jorbis)
	implementation(libs.bundles.common)
}

val jsFolder = "javascript"
val jsFileName = "classes.js"

teavm.js {
	obfuscated = true
	sourceMap = true
	targetFileName = "../$jsFileName"
	optimization = OptimizationLevel.BALANCED // Change to "AGGRESSIVE" for release
	outOfProcess = false
	fastGlobalAnalysis = false
	processMemory = 512
	entryPointName.set("main")
	mainClass = "net.lax1dude.eaglercraft.v1_8.internal.teavm.MainClass"
	outputDir = file(jsFolder)
	properties = mapOf("java.util.TimeZone.autodetect" to "true")
	debugInformation = false
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

tasks.named<GenerateJavaScriptTask>("generateJavaScript") {
	doLast {
		try {
			// NOTE: This step may break at any time, and is not required for 99% of browsers

			var phile = file("$jsFolder/$jsFileName")
			var dest = phile.readText()
			var i = dest.substring(0, dest.indexOf("=\$rt_globals.Symbol('jsoClass');")).lastIndexOf("let ")
			dest = dest.substring(0, i) + "var" + dest.substring(i + 3)
			var j = dest.indexOf("function(\$rt_globals,\$rt_exports){")
			dest = dest.substring(
					0,
					j + 34
			) + "\n" + file("$jsFolder/ES6ShimScript.txt").readText() + "\n" + dest.substring(j + 34)
			phile.writeText(dest)
		} catch (ex: Exception) {
			if (teavm.js.obfuscated.get()) {
				logger.info("Error occured while adding support for old browsers failed!", ex)
				logger.info("This was probably caused by building with non-obfuscated javascript, " +
						"you can probably safely ignore this!")
				logger.info("If this error persists with obfuscated javascript, report to ayunami2000!")
			} else {
				logger.info("Adding support for old browsers failed!", ex)
				logger.info("Please contact ayunami2000 and report this!")
			}
		}
	}
}

eaglercraftBuild {
	suites {
		js("main") {
			sourceGeneratorOutput = file("$jsFolder/$jsFileName")
			offlineDownloadTemplate = file("javascript/OfflineDownloadTemplate.txt")
			mainOutput = file("$jsFolder/EaglercraftX_1.8_Offline_en_US.html")
			internationalOutput = file("$jsFolder/EaglercraftX_1.8_Offline_International.html")
		}.apply {
			epkSources = file("../desktopRuntime/resources")
			epkOutput = file("$jsFolder/assets.epk")

			languageMetadataInput = file("$jsFolder/lang")
			languageEpkOutput = file("$jsFolder/lang.tmp.epk")

			sourceGeneratorTaskName = "generateJavaScript"
		}
	}
}