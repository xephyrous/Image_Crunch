package utils.app

import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.core.ModuleSource

var tempruns = 0

fun evalSettings() {
    try {
        val config = ConfigEvaluator.preconfigured().forKotlin().use {
            it.evaluate(ModuleSource.modulePath("config/config.pkl"))
        }

        println(config["Generator Type"])
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// For testing, please make this read from config.pkl
fun isFirstLaunch(): Boolean {
    if (tempruns == 0) {
        tempruns++
        return true
    }
    tempruns++
    return false
}