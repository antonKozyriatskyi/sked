import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths

val versionName = readString("--name", isOptional = false)!!
var versionCode = readInt("--code", isOptional = true)
val workspase = readString("--workspace", isOptional = true)

val projectRoot = workspase?.let { File(it) } ?: getProjectDir()
val appBuildGradleFile = getAppBuildGradleFile(projectRoot)

if (versionCode == null) {
    log("Version code was not specified. Calculating version code manually.")

    val oldCode = readOldVersionCode(appBuildGradleFile).toInt()
    versionCode = oldCode + 1
}

setNewVersionNameAndCode(appBuildGradleFile, versionName, versionCode!!)

//commitAndPushFile(projectRoot, appBuildGradleFile, versionName, versionCode!!)


// Functions
fun setNewVersionNameAndCode(file: File, name: String, code: Int) {
    val newText = file.bufferedReader()
        .use(BufferedReader::readText)
        .replace(Regex("versionCode .+"), "versionCode $code")
        .replace(Regex("versionName .+"), "versionName \"$name\"")

    log("Updating build.gardle with new version name ($name) and code ($code)")

    file.bufferedWriter().use {
        it.write(newText)
        it.flush()
    }
}

fun readOldVersionCode(file: File): String {
    val versionCode = file.useLines { sequence ->
        sequence.map { line -> line.trimStart() }
            .first { line -> line.startsWith("versionCode") }
            .replace("versionCode", "")
            .trimStart()
    }

    return versionCode
}

fun getAppBuildGradleFile(root: File): File = File(root, "app/build.gradle")

fun readString(name: String, isOptional: Boolean): String? {
    return if (args.contains(name)) {
        args[args.indexOf(name) + 1]
    } else if (isOptional) {
        null
    } else {
        throw IllegalArgumentException("argument: $name not provided")
    }
}

fun readInt(name: String, isOptional: Boolean): Int? = readString(name, isOptional)?.toInt()

fun log(msg: String) {
    println(msg)
}

fun commitAndPushFile(root: File, file: File, name: String, code: Int) {
    listOf("git", "config", "user.name", "GitHub Actions Bot").execute(root)?.also { log(it) }
    listOf("git", "config", "user.email", "<>").execute(root)?.also { log(it) }
    listOf("git", "status").execute(root)?.also { log(it) }
    listOf("git", "add", file.relativeTo(root).path).execute(root)?.also { log(it) }
    listOf("git", "commit", "-m", createCommitMessage(name, code)).execute(root)?.also { log(it) }
    listOf("git", "status").execute(root)?.also { log(it) }
    listOf("git", "push").execute(root)?.also { log(it) }
    listOf("git", "status").execute(root)?.also { log(it) }
}

fun createCommitMessage(name: String, code: Int): String = "Release $name ($code) [skip-ci]"

fun List<String>.execute(workingDir: File): String? {
    try {
        val proc = ProcessBuilder(this)
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()

        proc.waitFor()
        return proc.inputStream.bufferedReader().use(BufferedReader::readText)
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }
}

fun getProjectDir(): File = File(Paths.get("").toAbsolutePath().toString()).parentFile.parentFile