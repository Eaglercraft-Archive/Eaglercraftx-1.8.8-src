import java.util.*

plugins {
	id("java")
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
	named("main") {
		java.srcDirs(
			"../src/lwjgl/java"
		)
	}
}

dependencies {
	implementation(rootProject)
	implementation(libs.bundles.common)
	implementation(platform("org.lwjgl:lwjgl-bom:3.3.6"))
	implementation("org.lwjgl:lwjgl")
	implementation("org.lwjgl:lwjgl-egl")
	implementation("org.lwjgl:lwjgl-glfw")
	implementation("org.lwjgl:lwjgl-jemalloc")
	implementation("org.lwjgl:lwjgl-openal")
	implementation("org.lwjgl:lwjgl-opengles")
	implementation("org.java-websocket:Java-WebSocket:1.6.0")
	implementation("dev.onvoid.webrtc:webrtc-java:0.10.0")
	implementation(
		files(
			"../desktopRuntime/codecjorbis-20101023.jar",
			"../desktopRuntime/codecwav-20101023.jar",
			"../desktopRuntime/soundsystem-20120107.jar",
			"../desktopRuntime/UnsafeMemcpy.jar"
		)
	)
}

tasks.register<JavaExec>("eaglercraftDebugRuntime") {
	group = "desktopruntime"
	description = "Runs the desktop runtime"
	classpath += sourceSets["main"].runtimeClasspath

	val daArgs: MutableList<String> = mutableListOf(
		"-Xmx1G",
		"-Xms1G"
	)

	if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac")) {
		daArgs += "-XstartOnFirstThread"
	}

	jvmArgs = daArgs

	workingDir = file("../desktopRuntime")
	systemProperties = mapOf(
		"java.library.path" to workingDir.absolutePath
	)

	environment("LD_LIBRARY_PATH", workingDir.absolutePath)
	environment("__GL_THREADED_OPTIMIZATIONS", "0") // Hurts performance, fixes Nvidia crash on Linux

	mainClass = "net.lax1dude.eaglercraft.v1_8.internal.lwjgl.MainClass"
}
