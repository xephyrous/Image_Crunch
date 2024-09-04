package utils.tsf

import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * The level of error strictness during parsing
 *
 * @property STRICT Fatal error, program exits, callback function is called if set
 * @property WARNING Minor error, prints warning of error, callback function is called if set
 * @property LENIENT Insignificant error, callback function is called if set
 */
enum class TSFParseLevel {
    STRICT,
    WARNING,
    LENIENT,
}

/**
 * Houses the internal variables for the TSF file format
 * @see TSFParser
 * @see TSFFile
 */
object Internal {
    /**
     * Flags for keyword functionality
     */
    enum class TSFKeywordFlag(val value: Int) {
        NONE(0b00000000),
        SKIP(0b00000001),
        COLLECTION(0b00000010),
        TYPED(0b00000100)
    }

    /**
     * A map of kotlin types with differing tsf names
     */
    val keywordConversions: Map<String, String> = mapOf(
        "Map" to "NamedMap",
        "HashMap" to "NamedHMap"
    )

    /**
     * A map of .tsf keywords and their kotlin class equivalents
     */
    val keywords: Map<String, KClass<*>> = mapOf(
        "Object" to Object::class,
        "String" to String::class,
        "Bool" to Boolean::class,
        "Int" to Int::class,
        "Short" to Short::class,
        "Long" to Long::class,
        "Double" to Double::class,
        "Char" to Char::class,
        "NamedMap" to Map::class,
        "NamedHMap" to HashMap::class,
    )

    /**
     *  List of flags for each keyword, used for parsing
     */
    val keywordFlags: Map<String, Int> = mapOf(
        "Object" to (TSFKeywordFlag.SKIP.value),
        "String" to (TSFKeywordFlag.NONE.value),
        "Bool" to (TSFKeywordFlag.NONE.value),
        "Int" to (TSFKeywordFlag.NONE.value),
        "Short" to (TSFKeywordFlag.NONE.value),
        "Long" to (TSFKeywordFlag.NONE.value),
        "Double" to (TSFKeywordFlag.NONE.value),
        "Char" to (TSFKeywordFlag.NONE.value),
        "NamedMap" to (TSFKeywordFlag.COLLECTION.value or TSFKeywordFlag.TYPED.value),
        "NamedHMap" to (TSFKeywordFlag.COLLECTION.value or TSFKeywordFlag.TYPED.value),
        "Group" to (TSFKeywordFlag.COLLECTION.value or TSFKeywordFlag.TYPED.value)
    )

    /**
     * Internal Kotlin class members to avoid during parsing
     */
    val parseMask = arrayListOf(
        "equals",
        "hashCode",
        "toString"
    )

    /**
     * TODO : Document
     */
    val specialChars = arrayListOf(
        ":",
        "{",
        "}",
        ","
    )
}