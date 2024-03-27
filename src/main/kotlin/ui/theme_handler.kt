package ui

import java.io.File

/*
THEME HANDLER TODO:

Parse thru a folder list of themes
Convert Theme to a new object
Confirmation box

 */

/**
 * Grabs themes from the dedicated themes folder within config.
 * Returns a list of themes
 */
fun grabThemes(
    dirPath: String
): List<File>? {
    return File(dirPath).listFiles()?.filter { it.isFile } // i think this works?
}