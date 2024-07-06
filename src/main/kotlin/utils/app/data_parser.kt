package utils.app

import utils.storage.DecoratedError
import utils.storage.InvalidTSFFile
import utils.storage.ThemeData
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths

/**
 * Collects themes from the default (or specified) folder path.
 */
fun getThemes(startPath: String = "\\src\\main\\config\\themes\\") : ArrayList<ThemeData> {
    val themes: ArrayList<ThemeData> = arrayListOf()
    val dir = File(Paths.get("").toAbsolutePath().toString() + startPath)
    println(dir.path)

    if(!dir.exists() || !dir.isDirectory()) {
        throw InvalidPathException(startPath, "Invalid Theme Directory Path!")
    }

    val files = dir.listFiles()

    files?.forEach {
        try {
            val parser = TSFParser(ThemeData(""), it)
            val data = parser.parse()
            themes.add(data)
        } catch(e: InvalidTSFFile) {
            throw DecoratedError("Theme Loader", "Error loading theme file '${it.name}'")
        }
    }

    return themes
}