package utils.app

import androidx.compose.runtime.key
import utils.storage.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.sign
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

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
 * Flags for keyword functionality
 */
private enum class TSFKeywordFlag(val value: Int) {
    NONE(0b00000000),
    SKIP(0b00000001),
    COLLECTION(0b00000010),
    TYPED(0b00000100)
}

/**
 * A map of kotlin types with differing tsf names
 */
private val keywordConversions: Map<String, String> = mapOf(
    "Map" to "NamedMap",
    "HashMap" to "NamedHMap"
)

/**
 * A map of .tsf keywords and their kotlin class equivalents
 */
private val keywords: Map<String, KClass<*>> = mapOf(
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
private val keywordFlags: Map<String, Int> = mapOf(
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
private val parseMask = arrayListOf(
    "equals",
    "hashCode",
    "toString"
)

private val specialChars = arrayListOf(
    ":",
    "{",
    "}",
    ","
)

/**
 * A .tsf (Tirana Storage Format) file parser object
 *
 * @param _class The class to parse into
 * @param configFile The file to parse from
 * @param parseLevel The level of error strictness for parsing
 */
class TSFParser <T> (
    private val _class: T,
    private var configFile: File,
    private var parseLevel: TSFParseLevel = TSFParseLevel.STRICT
) {
    private val parseCallbackFunctions: ArrayList<() -> Unit> = ArrayList(TSFParseLevel.entries.count())
    private val classMap: HashMap<String, String> = hashMapOf()

    /**
     * Sets the callback function for a level of parsing error
     *
     * @param func The function to be set as the callback
     */
    fun setParseCallbackFun(func: () -> Unit, parseLevel: TSFParseLevel) {
        parseCallbackFunctions[parseLevel.ordinal] = func
    }

    /**
     * Checks file validity and loads class member names, types, and values
     */
    init {
        // Check file validity
        if(!configFile.exists() || !configFile.canRead()) {
            throw DecoratedError("TSF", "File $configFile does not exist or is unreadable!")
        }

        if(configFile.extension != "tsf") {
            throw DecoratedError("TSF", "Invalid file extension!\nExpected: .tsf | Received: .${configFile.extension}")
        }

        // Add class member names to classMap
        _class!!::class.members.forEach {
            if(parseMask.contains(it.name)) { return@forEach }

            // Coerce the member's return type to be a simple name
            // (Remove collection type specifications and kotlin class jargon)
            classMap[it.name] = "^[^<]*".toRegex().findAll(
                it.returnType.toString()
                .replace("\\bkotlin(?:\\.[^.A-Z<>]*)*\\.".toRegex(), "")
            ).first().value
        }

        TSFParseLevel.entries.forEach {
            parseCallbackFunctions.add(it.ordinal) {}
        }
    }

    private fun reservedKeywordCheck(token: String, line: Int) {
        if(keywords.contains(token)) {
            throw DecoratedError("TSF", "Reserved keyword '${token}' used on line $line [$configFile]")
        }
    }

    private fun keywordConversionCheck(keyword: String) : String {
        try {
            val converted = keywordConversions[keyword]
            if(converted != null) { return converted  }
        } catch(e: Exception) {
            return keyword
        }

        return keyword
    }

    /**
     * Parses class members into a designated class
     *
     * TODO : Add support for empty declaration and late assignment (optional)
     * TODO : Replace linePos with a line matching function
     * TODO : Create variable-input function for easy syntax checking (like reservedKeyWordCheck())
     *
     * @return An instance of the initialized class
     */
    fun parse() : T {
        // Parse in from file
        val reader = BufferedReader(FileReader(configFile))
        var line: String?
        val splitLine: MutableList<Array<String>> = MutableList(0) { arrayOf("") }
        var flattenedFile: String = ""

        var storedVars: MutableMap<String, Pair<String, Any>> = mutableMapOf() // Name, Type, Value
        var storedGroups: MutableMap<String, Pair<Pair<KClass<*>, String>, ArrayList<Pair<String, Any>>>> = mutableMapOf() // Name, (Group Type, Member Type), Group Values (Name, Value)
        var groupData: ArrayList<Pair<String, Any>> = arrayListOf()

        var linePos = 1

        while (reader.readLine().also { line = it } != null) {
            // Remove all inline comments
            line = line!!.replace("//.*".toRegex(), "");

            // Trim input and separate tokens
            line = line!!.trim()
            specialChars.forEach { line = line!!.replace(it, " $it ") }

            // Replace spaces in string literals with temporary token
            line = Regex("\".+\"").replace(line!!) { result ->
                result.value.replace(" ", "__SPACE__")
            }

            // Flatten whitespace
            line = line!!.replace("\\s+".toRegex(), " ")

            // Append line to flattened file
            flattenedFile += line + "__NEWLINE__"
        }

        // Remove all multi-line comments
        flattenedFile = flattenedFile.replace("\\/\\*(?:(?!\\*/|\\/\\*)[\\s\\S]*?\\*\\/)".toRegex(), "")

        // Replace temporary tokens with newlines, flatten newlines, and split into lines
        flattenedFile = flattenedFile.replace("__NEWLINE__", "\n")
        flattenedFile = flattenedFile.replace("(\\r?\\n){2,}".toRegex(), "\n")
        val tempSplit = flattenedFile.split("\n")

        // Replace temporary tokens with spaces and tokenize line
        for(currLine in tempSplit) {
            splitLine.add(currLine.split(" ").map { it.replace("__SPACE__", " ") }.toTypedArray())
        }
        var _type = ""
        var _mType = ""
        var _name = ""
        var inGroup = false

        // Parse file
        splitLine.forEach { currLine ->
            // Check for valid type
            if (keywords.keys.contains(currLine[0]) || inGroup) {
                // [Collection type]
                if (keywordFlags[currLine[0]]?.and(TSFKeywordFlag.COLLECTION.value) != 0 || inGroup) {
                    // Collection declaration checks
                    if(!inGroup) {
                        _type = currLine[0]

                        reservedKeywordCheck(currLine[1], linePos)
                        _name = currLine[1]

                        if (currLine[2] != ":") {
                            throw DecoratedError("TSF", "Illegal character '${currLine[2]}' on line $linePos [$configFile]")
                        }

                        if(!keywords.contains(currLine[3])) {
                            throw DecoratedError("TSF", "Invalid type in collection '$_name' on line $linePos [$configFile]")
                        }
                        _mType = currLine[3]

                        if(currLine[4] != "{") {
                            throw DecoratedError("TSF", "Missing collection definition on line $linePos [$configFile]")
                        }

                        inGroup = true
                        return@forEach
                    }

                    // Store group data and escape group
                    if(currLine.contains("}")) {
                        when(_mType) {
                            "String" -> storedGroups[_name] = Pair(Pair(String::class, _mType), ArrayList(groupData))
                            "Boolean" -> storedGroups[_name] = Pair(Pair(Boolean::class, _mType), ArrayList(groupData))
                            "Int" -> storedGroups[_name] = Pair(Pair(Int::class, _mType), ArrayList(groupData))
                            "Short" -> storedGroups[_name] = Pair(Pair(Short::class, _mType), ArrayList(groupData))
                            "Long" -> storedGroups[_name] = Pair(Pair(Long::class, _mType), ArrayList(groupData))
                            "Char" -> storedGroups[_name] = Pair(Pair(Char::class, _mType), ArrayList(groupData))
                            "Double" -> storedGroups[_name] = Pair(Pair(Double::class, _mType), ArrayList(groupData))
                        }
                        groupData.clear()
                        inGroup = false
                        return@forEach
                    }

                    // Collect next group member
                    reservedKeywordCheck(currLine[0], linePos)

                    if (currLine[1] != ":") {
                        throw DecoratedError("TSF", "Illegal character '${currLine[2]}' in collection '$_name' on line $linePos [$configFile]")
                    }

                    // Store value
                    try {
                        when(_mType) {
                            "String" -> groupData.add(Pair(currLine[0], currLine[2]))
                            "Boolean" -> groupData.add(Pair(currLine[0], currLine[2].toBoolean()))
                            "Int" -> groupData.add(Pair(currLine[0], currLine[2].toInt()))
                            "Short" -> groupData.add(Pair(currLine[0], currLine[2].toShort()))
                            "Long" -> groupData.add(Pair(currLine[0], currLine[2].toLong()))
                            "Double" -> groupData.add(Pair(currLine[0], currLine[2].toDouble()))
                            "Char" -> groupData.add(Pair(currLine[0], currLine[2][0]))
                        }
                    } catch (e: Exception) {
                        throw DecoratedError("TSF", "Failed to cast variable '$_name' to type '$_type' on line $linePos [$configFile]")
                    }

                    return@forEach
                }

                // [Primitive type]
                _type = currLine[0]

                reservedKeywordCheck(currLine[1], linePos)
                _name = currLine[1]

                if (currLine[2] != ":") {
                    throw DecoratedError("TSF", "Illegal character '${currLine[2]}' on line $linePos [$configFile]")
                }

                // Build type/value pair
                try {
                    when(_type) {
                        "String" -> storedVars[_name] = Pair("String", currLine[3])
                        "Boolean" -> storedVars[_name] = Pair("Boolean", currLine[3].toBoolean())
                        "Int" -> storedVars[_name] = Pair("Int", currLine[3].toInt())
                        "Short" -> storedVars[_name] = Pair("Short", currLine[3].toShort())
                        "Long" -> storedVars[_name] = Pair("Long", currLine[3].toLong())
                        "Double" -> storedVars[_name] = Pair("Double", currLine[3].toDouble())
                        "Char" -> storedVars[_name] = Pair("Char", currLine[3][0])
                    }
                } catch (e: Exception) {
                    throw DecoratedError("TSF", "Failed to cast variable '$_name' to type '$_type' on line $linePos [$configFile]")
                }

                linePos++
            }
        }

        var errStr = ""
        var _break = false

        // Set class instance data
        while (!_break) {
            try {
                classMap.forEach { (key, value) ->
                    try {
                        // Attempt to find and each variable
                        val member = _class!!::class.java.getDeclaredField(key)
                        val property = _class!!::class.members
                            .filterIsInstance<KMutableProperty1<T, Any?>>()
                            .find { it.name == key }

                        member.isAccessible = true

                        // [Collection type]
                        if(keywordFlags[value]?.and(TSFKeywordFlag.COLLECTION.ordinal) != 0) {
                            try {
                                val parameter = storedGroups[key]
                                member.set(_class,
                                    mapCast(
                                        parameter!!.second,
                                        property?.returnType?.classifier as KClass<*>,
                                        parameter.first.first
                                    ),
                                )
                            } catch(e: Exception) {
                                e.printStackTrace()
                                throw DecoratedError("TSF", "Group name mismatch in parsing! [$key]")
                            }

                            return@forEach
                        }

                        // [Primitive type]
                        member.set(_class, anyCast(storedVars[key]!!.second, property?.returnType?.classifier as KClass<*>))
                    } catch (e: Exception) {
                        errStr = key
                        e.printStackTrace()
                        throw Exception()
                    }
                }

                _break = true
            } catch (_: Exception) {
                when (parseLevel) {
                    // Handles errors in parsing according to parse level, calls a callback set by user if defined
                    TSFParseLevel.STRICT -> {
                        parseCallbackFunctions[TSFParseLevel.STRICT.ordinal]()
                        throw DecoratedError("TSF", "Non-Matching Variable in TSF file! [$errStr]")
                    }

                    TSFParseLevel.WARNING -> {
                        parseCallbackFunctions[TSFParseLevel.WARNING.ordinal]()
                        throw DecoratedWarning("TSF", "Non-Matching Variable in TSF file! [$errStr]")
                    }

                    TSFParseLevel.LENIENT -> {
                        parseCallbackFunctions[TSFParseLevel.LENIENT.ordinal]()
                    }
                }
            }
        }

        return _class
    }
}