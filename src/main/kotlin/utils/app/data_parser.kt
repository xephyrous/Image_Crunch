package utils.app

import ui.AlertBox
import utils.storage.ConfigData
import utils.storage.DecoratedError
import utils.storage.InvalidTSFFile
import utils.storage.ThemeData
import utils.tsf.TSFParseLevel
import utils.tsf.TSFParser
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths

val TSF_PARSER_ERROR_CALLBACK: (String) -> Unit = { errStr -> AlertBox.DisplayAlert("Non-Matching Variable in TSF file! [$errStr], aborting!") }
val TSF_PARSER_WARNING_CALLBACK: (String) -> Unit = { errStr -> AlertBox.DisplayAlert("Non-Matching Variable in TSF file! [$errStr], the file will not be loaded!") }
val TSF_PARSER_LENIENT_CALLBACK: (String) -> Unit = { errStr -> AlertBox.DisplayAlert("Non-Matching Variable in TSF file! [$errStr], the default class will be used!") }


/**
 * Collects themes from the default (or specified) folder path.
 */
fun getThemes(startPath: String = "\\config\\themes\\"): ArrayList<ThemeData> {
    val themes: ArrayList<ThemeData> = arrayListOf()
    val dir = File(Paths.get("").toAbsolutePath().toString() + startPath)

    if(!dir.exists() || !dir.isDirectory()) {
        throw InvalidPathException(startPath, "Invalid Theme Directory Path!")
    }

    val files = dir.listFiles()

    files?.forEach {
        try {
            val parser = TSFParser(ThemeData(""), it).apply {
                setParseCallbackFun(TSF_PARSER_ERROR_CALLBACK, TSFParseLevel.STRICT)
                setParseCallbackFun(TSF_PARSER_WARNING_CALLBACK, TSFParseLevel.WARNING)
                setParseCallbackFun(TSF_PARSER_LENIENT_CALLBACK, TSFParseLevel.LENIENT)
            }

            parser.parse().onSuccess {  themeData ->
                themes.add(themeData)
            }
        } catch(e: InvalidTSFFile) {
            throw DecoratedError("Theme Loader", "Error loading theme file '${it.name}'")
        }
    }

    return themes
}

/**
 * Loads the last saved configuration
 *  - Later on I'll add support for loading multiple to select from for more customization,
 *  but this will be simpler for the time being.
 */
fun getConfigData(startPath: String = "\\config\\"): ConfigData {
    var config: ConfigData = ConfigData()
    val dir = File(Paths.get("").toAbsolutePath().toString() + startPath)

    if(!dir.exists() || !dir.isDirectory()) {
        throw InvalidPathException(startPath, "Invalid Config Directory Path!")
    }

    val files = dir.listFiles()

    files?.forEach {
        if(it.extension != "tsf") { return@forEach }

        try {
            val parser = TSFParser(ConfigData(), it).apply {
                setParseCallbackFun(TSF_PARSER_ERROR_CALLBACK, TSFParseLevel.STRICT)
                setParseCallbackFun(TSF_PARSER_WARNING_CALLBACK, TSFParseLevel.WARNING)
                setParseCallbackFun(TSF_PARSER_LENIENT_CALLBACK, TSFParseLevel.LENIENT)
            }

            parser.parse().onSuccess {  configData ->
                config = configData
            }
        } catch(e: InvalidTSFFile) {
            throw DecoratedError("Config Loader", "Error loading config file '${it.name}'")
        }
    }

    return config
}