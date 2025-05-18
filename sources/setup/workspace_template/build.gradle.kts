plugins {
	id("java")
}

allprojects {
	apply(plugin = "eclipse")

	repositories {
		mavenCentral()
	}

	plugins.withId("java") {
		java {
			toolchain {
				languageVersion = JavaLanguageVersion.of(17)
			}
		}
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
	named("main") {
		java.srcDirs(
			"src/main/java",
			"src/game/java",
			"src/protocol-game/java",
			"src/protocol-relay/java",
			"src/platform-api/java"
		)
	}
}

dependencies {
	implementation(libs.bundles.common)
}

tasks.withType<Jar> {
	// TeaVM will fail if anything from platform-api is in the JAR
	fileTree("src/platform-api/java").visit {
		if (!isDirectory) {
			if (path.endsWith(".java")) {
				exclude(path.substring(0, path.length - 5) + ".class")
			}
		}
	}
}
