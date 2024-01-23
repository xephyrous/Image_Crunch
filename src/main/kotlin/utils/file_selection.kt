package utils

import java.io.File
import java.io.FileFilter
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

// i hope this works
/**
 * ImageFileSelection
 *
 * Opens a window for user to specify an image file
 *
 */
fun ImageFileSelection(): File? {
    val fileFilter = FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg")
    val fileChooser = JFileChooser()
    fileChooser.fileFilter = fileFilter

    val sourceReturn = fileChooser.showOpenDialog(null)

    if (sourceReturn == JFileChooser.APPROVE_OPTION) {
        return fileChooser.selectedFile
    }
    return null
}

// idk why this still exists
private fun getExtension(input: File): String? {
    var extension: String? = null
    val fileName = input.name
    val x = fileName.lastIndexOf('.')

    if (x > 0 && x < (fileName.length - 1)) {
        extension = fileName.substring(x+1).lowercase()
    }
    return extension
}