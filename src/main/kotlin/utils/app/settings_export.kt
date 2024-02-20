package utils.app

import utils.storage.generatorType
import utils.storage.squareColumns
import utils.storage.squareRows

fun settingsToCSV() {
    // There's nothing to make...
}

fun settingsToString(): String {
    return "${generatorType.value()};${squareRows.value()}:${squareColumns.value()}"
}

fun CSVtoSettings() {
    // The opposite of settingsToCSV()
}

fun stringToSettings() {
    // The opposite of settingsToString()
}