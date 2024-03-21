package utils.app

import org.pkl.config.java.Config
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.core.ModuleSource
import java.io.File

fun parseThemeData() {
    val evaluator = ConfigEvaluator.preconfigured().forKotlin()
    var file: Config

    File("src/main/config/themes").walk().forEach {
        file = evaluator.evaluate(ModuleSource.file(it))
    }
}

fun parseConfigData() {
    val evaluator = ConfigEvaluator.preconfigured().forKotlin()
    var file: Config


}