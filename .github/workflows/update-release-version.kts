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

fun getProjectDir(): File = File(Paths.get("").toAbsolutePath().toString()).parentFile.parentFile