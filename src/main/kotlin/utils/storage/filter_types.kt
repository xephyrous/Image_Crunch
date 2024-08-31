package utils.storage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/*
* Good Morning Dev Guys
*
* To make a new filter object, just tell me what kind of variables are required for it to run
* e.g. number of rows + columns
*
* You can then input this into the extended class and I will make a corresponding ui element for it.
* Everything else in terms of filters displays should be handled elsewhere.
*
* You wanted custom filters so you have to deal with my terms now
*/

abstract class Filter() {
    @Composable abstract fun display()
}

// Example of a slice filter and how it would be setup or smth idk
class Slice(
    var rows: Int,
    var cols: Int
) : Filter() {
    fun updateRows(rows: Int) {
        this.rows = rows
    }

    fun updateCols(cols: Int) {
        this.cols = cols
    }

    @Composable
    override fun display() {
        Text("Rows: $rows, Cols: $cols")
    }
}