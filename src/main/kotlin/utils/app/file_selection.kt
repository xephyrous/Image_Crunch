package utils.app

import java.io.File
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