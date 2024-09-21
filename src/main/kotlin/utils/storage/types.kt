package utils.storage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import utils.storage.GeneratorType.*
import java.awt.Dimension
import java.security.MessageDigest
import kotlin.IllegalArgumentException
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * A single position on an image
 */
typealias PositionNode = Pair<Int, Int>

/**
 * List of node generation functions
 *
 * @property NONE No set generator type, similar to a null, should be used as a fallback value
 * @property SQUARE Square grid of pieces
 * @property SQUARE_NOISE Square grid of pieces with noisy edges
 */
enum class GeneratorType {
    NONE,
    SQUARE,
    SQUARE_NOISE
}

/**
 * A masked region of an image with its top-left coordinate
 *
 * @param size The size of the mask
 *
 * @property bits The array of bits in the mask
 * @property position The position of the top-left corner of the mask
 */
class Mask(val size: Dimension) {
    var bits: ImageMask = Array(size.height) {
        Array(size.width) { 0 }
    }
    var position: PositionNode = PositionNode(-1, -1)

    /**
     * Used for debugging, prints the mask array showing only 1s
     */
    fun print() {
        for(y in bits) {
            for(x in y) {
                print(1)
            }
            println()
        }
    }
}

/**
 * Protects a variable from race conditions by "locking" the value
 * It will also hold any values attempted to be set while locked and update after it is unlocked
 *
 * @param T The type of the data being stored
 * @param lockVal The data to be stored, of type [T]
 *
 * @property value The current value of the variable
 * @property locked If the variable is locked
 * @property holdVal Any queued value
 */
class LockType<T>(lockVal: T) {
    var value: T = lockVal
        get() {
            if(holdVal != null && !locked) {
                value = holdVal!!
                holdVal = null
            }

            return field
        }
        set(newVal) {
            if (locked) {
                holdVal = newVal
                return
            }

            field = newVal
        }

    private var locked: Boolean = false
    private var holdVal: T? = null

    /**
     * Returns if the value is locked
     */
    fun locked(): Boolean {
        return locked
    }

    /**
     * Locks the value, it cannot be modified
     */
    fun lock() {
        locked = true
    }

    /**
     * Unlocks the property, it can be modified
     */
    fun unlock() {
        locked = false
    }
}

/**
 * Casts from Any to designated type
 *
 * @param value The value to be cast
 * @param targetType The target type to cast to
 *
 * @return The inputted value cast to the target type
 */
fun anyCast(value: Any?, targetType: KClass<*>): Any {
    return when(targetType) {
        String::class -> value as String
        Boolean::class -> value as Boolean
        Int::class -> value as Int
        Short::class -> value as Short
        Long::class -> value as Long
        Double::class -> value as Double
        Char::class -> value as Char
        else -> throw IllegalArgumentException("Unsupported anyCast type conversion : ${targetType.simpleName}")
    }
}

/**
 * Casts an ArrayList of name / value pairs to a designated map collection type
 */
fun mapCast(list: ArrayList<Pair<String, Any>>, mapType: KClass<*>, targetType: KClass<*>): Any {
    return when (mapType) {
        Map::class -> {
            val buildMap: MutableMap<String, Any> = mutableMapOf()
            list.forEach { buildMap[it.first] = anyCast(it.second, targetType) }
            buildMap
        }

        HashMap::class -> {
            val buildMap: HashMap<String, Any> = hashMapOf()
            list.forEach { buildMap[it.first] = anyCast(it.second, targetType) }
            buildMap
        }

        else -> throw IllegalArgumentException("Unsupported mapCast type conversion : ${targetType.simpleName}")
    }
}

/**
 * Empty class for theme data to be parsed into
 *
 * @property name The name of the theme
 * @property icon The icon color
 * @property header The header color
 * @property button The button color
 * @property fab The Floating Action Button Color (Currently Unused)
 * @property card Map of colors for Card Gradient
 * @property textColors Map of colors for all text variations
 * @property backgroundColors Map of colors for Background Gradient
 * @property textFields Map of colors for all textField segments
 */
class ThemeData(var name: String) {
    var icon: Long = 0
    var header: Long = 0
    var button: Long = 0
    var border: Long = 0
    var fab: Long = 0
    var card: Map<String, Long> = mapOf()
    var textColors: Map<String, Long> = mapOf()
    var backgroundColors: Map<String, Long> = mapOf()
    var textFields: Map<String, Long> = mapOf()
}

/**
 * Mutable class for theme data to be read from and inputted into display | Copies Theme Data into a mutable Color rather than a number
 *
 * @param tD Initial Theme Data to copy
 *
 * @property name The name of the theme
 * @property icon The icon color
 * @property header The header color
 * @property button The button color
 * @property fab The Floating Action Button Color (Currently Unused)
 * @property card Map of colors for Card Gradient
 * @property textColors Map of colors for all text variations
 * @property backgroundColors Map of colors for Background Gradient
 * @property textFields Map of colors for all textField segments
 */
class ThemeStorage(tD: ThemeData) {
    var name by mutableStateOf(tD.name)
    var icon by mutableStateOf(Color(tD.icon))
    var header by mutableStateOf(Color(tD.header))
    var button by mutableStateOf(Color(tD.button))
    var border by mutableStateOf(Color(tD.border))
    var fab by mutableStateOf(Color(tD.fab))
    var card: MutableMap<String, Color> = tD.card.mapValues {
        Color(it.value)
    }.toMutableMap()
    var textColors: MutableMap<String, Color> = tD.textColors.mapValues {
        Color(it.value)
    }.toMutableMap()
    var backgroundColors: MutableMap<String, Color> = tD.backgroundColors.mapValues {
        Color(it.value)
    }.toMutableMap()
    var textFields: MutableMap<String, Color> = tD.textFields.mapValues {
        Color(it.value)
    }.toMutableMap()
}

/*
 _._     _,-'""`-._
(,-.`._,'(       |\`-/|
    `-.-' \ )-`( ,  o o)
          `-    \`_`"'-
 */

/**
 * Theme Switching Button Data Holder - Converted to a Composable during runtime
 *
 * @param tD The passed theme Data
 *
 * @property name The name of the theme - Doubles as button text
 * @property themeData The button's stored theme
 * @property height Controller for buttons height
 */
class ThemeButton(tD: ThemeData) {
    var name = tD.name
    var themeData = tD
    var height by mutableStateOf(50.dp)

    fun setButtonHeight(height: Dp) {
        this.height = height
    }

    fun exportData(): ThemeStorage {
        return ThemeStorage(themeData)
    }
}

class ConfigData() {

}

/**
 * TODO : Document DecoratedError
 */
open class DecoratedError(type: String, message: String, code: Int? = null) : Throwable(message) {
    init {
        print(
            "\n\u001b[31m\u001b[1m[\u001b[0m\u001b[31m ${
                type.uppercase().replace(" ", "-")
            }-ERROR : $message \u001b[1m]"
        )

        if (code != null) {
            print(" <Code $code>")
        }

        print("\u001b[0m\n\n")
    }
}

/**
 * TODO : Document DecoratedWarning
 */
open class DecoratedWarning(type: String, message: String) : Exception(message) {
    init {
        print("\u001b[31m")
        println("\u001b[1m[\u001B[0m\u001b[31m ${type.uppercase()}-WARNING : $message \u001B[1m]\u001B[0m")
        print("\u001b[0m")
    }
}

/**
 * Custom error class for XSF files
 * @param message The error message to display
 */
class InvalidXSFFile(message: String) : DecoratedError("XSF", message)

/**
 * Generates a stable/static hash for a given object
 * @param obj The object to be hashed
 * @return The hash as a [String]
 */
fun getStableHash(obj: Any): String {
    val messageDigest = MessageDigest.getInstance("SHA-1")
    val bytes = obj.toString().toByteArray()
    val hashBytes = messageDigest.digest(bytes)
    return hashBytes.joinToString("") { String.format("%02x", it) }
}

/**
 * TODO : Document
 */
fun extractClass(obj: KCallable<*>): KClass<*> {
    return obj.returnType.classifier as KClass<*>
}

/**
 * TODO : Document
 */
fun extractClassParam(obj: KCallable<*>, index: Int): KClass<*> {
    if (index >= obj.returnType.arguments.size) {
        throw IndexOutOfBoundsException("Invalid type parameter index of $index!")
    }

    return obj.returnType.arguments[index].type?.classifier as KClass<*>
}

/**
 * TODO : Document
 */
fun isFieldDelegate(instance: Any, name: String): Boolean {
    val _class = instance::class.java

    return try {
        val field = _class.getDeclaredField("$name\$delegate")
        field.isAccessible = true

        val fieldType = field.type
        ReadWriteProperty::class.java.isAssignableFrom(fieldType) ||
                ReadOnlyProperty::class.java.isAssignableFrom(fieldType)
    } catch (e: NoSuchFieldException) {
        false
    }
}
