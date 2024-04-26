package utils.app

import utils.storage.DecoratedError
import utils.storage.convertT
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.system.exitProcess

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
 * A map of .tsf keywords and their kotlin equivalents
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
    "Group" to Object::class
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
    private val classMap: HashMap<String, Pair<KClass<*>, Any>> = hashMapOf()

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

        // Add class member details to classMap
        _class!!::class.members.forEach {
            if(parseMask.contains(it.name)) { return@forEach }
            classMap[it.name] = Pair(Nothing::class, it.returnType)
        }

        /* TSFParseLevel.entries.forEach {
            parseCallbackFunctions[it.ordinal] = {}
        } */
    }

    /**
     * Parses class members into a designated class
     *
     * @return An instance of the initialized class
     */
    fun parse() : T {
        // Parse in from file
        val reader = BufferedReader(FileReader(configFile))
        var line: String?
        val splitLine: MutableList<String> = MutableList(0) { "" }
        var storedVars: MutableMap<String, Pair<KClass<*>, String>> = mutableMapOf() // Name, Type, Value
        var storedGroups: MutableMap<String, ArrayList<Pair<String, String>>> = mutableMapOf() // All groups

        var currLine = 1
        var currPos = 1

        while(reader.readLine().also { line = it } != null) {
            // Trim input and separate tokens
            line = line!!.trim()
            specialChars.forEach { line = line!!.replace(it, " $it ") }
            line = line!!.replace("\\s+".toRegex(), " ")

            // Tokenize line
            splitLine.addAll(line!!.split(" "))
        }

        var fullSet = false
        var inGroup = false

        var currName: String? = null

        var outerType: String? = null
        var currType: KClass<*>? = null
        var subType: KClass<*>? = null

        var groupVars: ArrayList<Pair<String, String>> = ArrayList()

        var currVal: String? = null
        var token: String

        // Parse tokens
        var i: Int = 0
        while(i < splitLine.size) {
            token = splitLine[i]

            if(currName != null && currType != null && currVal != null) { fullSet = true }

            try {
                // Match type
                if(inGroup) { throw Exception() }
                if(currType != null && subType == null) {
                    subType = keywords[token]
                    if(subType == null) { throw Exception() }
                    i++
                    continue
                }
                currType = keywords[token]
                if(currType == null) { throw Exception() }
                outerType = token

                i++
                continue
            } catch(_: Exception) { }

            when(token) {
                ":" -> {
                    if (inGroup) {
                        try {
                            //Optimally this would attempt to convert, but currently the converting functions do not have enough information for type inferencing
                            token = splitLine[i + 1]
                            groupVars.add(Pair(groupVars.last().first, token))
                            ++i
                        } catch(RAHH: Exception) {
                            RAHH.printStackTrace()
                            throw DecoratedError("TSF", "Invalid data type for value '${splitLine[i + 1]}' in $currType! [$configFile] | Line $currLine, Character $currPos")
                        }
                    } else {
                        // Validate syntax (Undeclared type or name)
                        if (currType == null || currName == null) {
                            throw DecoratedError(
                                "TSF",
                                "Unexpected token '$token' following ':' in file [$configFile] | Line $currLine, Character $currPos"
                            )
                        }
                    }
                }
                "{" -> {
                    if(currType != null && currName != null && subType == null) {
                        throw DecoratedError(
                            "TSF",
                            "Missing subtype for type '$currType'! [$configFile] | Line $currLine, Character $currPos"
                        )
                    }
                    inGroup = true
                }
                "}" -> {
                    inGroup = false
                    storedGroups[currName!!] = groupVars

                    currName = null
                    currVal = null
                    currType = null
                    outerType = null
                    subType = null

                    groupVars = ArrayList()
                    currLine++
                }
                "," -> {
                    // Validate syntax ("," outside of group definition)
                    if(!inGroup) {
                        throw DecoratedError(
                            "TSF",
                            "Unexpected character ',' in file [$configFile] | Line $currLine, Character $currPos"
                        )
                    }
                }
                "\n", "\r", "" -> {
                    // Set variable in map & reset temp variables
                    if(fullSet) {
                        storedVars[currName!!] = Pair(currType!!, currVal!!)

                        currName = null
                        currVal = null
                        currType = null
                        outerType = null
                        subType = null

                        groupVars = ArrayList()

                        fullSet = false
                    }

                    currLine++
                }
                else -> {
                    if(inGroup) {
                        groupVars += Pair(token, "")
                        ++i
                        continue
                    }

                    // Set string value
                    if(token.isNotEmpty() && token[0] == '"') {
                        var tempToken: String = token

                        while(tempToken.last() != '"') {
                            ++i
                            tempToken += splitLine[i]
                        }

                        currVal = tempToken.substring(1, tempToken.length - 1)
                        continue
                    }

                    // Validate & set name
                    if (currType != null && currName == null) {
                        if(token[0].isDigit()) {
                            throw DecoratedError(
                                "TSF",
                                "Illegal starting character '${token[0]}' in variable name! [$configFile] | Line $currLine, Character $currPos"
                            )
                        }

                        var innerPos = 0
                        token.forEach {
                            if(
                                !it.isLetter() && !it.isDigit() &&
                                it.toString().toInt() != 32
                              ) {
                                throw DecoratedError(
                                    "TSF",
                                    "Unexpected character '$it' in variable name! [$configFile] | Line $currLine, Character ${currPos + innerPos}"
                                )
                            }
                            innerPos++
                        }

                        currName = token
                    } else if (currName != null && currType == null) {
                        // Set type
                        try {
                            currType = keywords[token]
                            if (currType == null) { throw Exception() }
                            outerType = token
                        } catch (_: Exception) {
                            throw DecoratedError("TSF", "Invalid type $token'! [$configFile] | Line $currLine, Character $currPos")
                        }
                    } else if(currName != null && currVal == null) {
                        // Set value
                        currVal = token
                    }
                }
            }

            if(subType == null && currType != null && (keywordFlags[outerType]?.and(TSFKeywordFlag.TYPED.value) != 0)) {
                // Set subtype
                try {
                    if (keywords[token] == null) { throw Exception() }
                    subType = keywords[token]
                } catch (_: Exception) { }
                // womp womp
            }

            currPos += token.length
            i++
        }

        var match = false
        try{
            classMap.forEach { (key, value) ->
                match = false
                print("[${key}] : ")

                _class!!::class.members.forEach {
                    if(it.name == key) {
                        // (it as KMutableProperty<*>).setter.call(_class, value.second.toString().convertT(value.first))
                        match = true
                    }
                }

                if(!match) {
                    throw DecoratedError(
                        "TSF", "HAHA YOU FUCKED UP DUMB BITCH"
                    )
                }

                println("")
            }
        } catch(_: Exception) {
            // Handles errors in parsing according to parse level, calls a callback set by user if defined
            when(parseLevel) {
                TSFParseLevel.STRICT -> {
                    parseCallbackFunctions[TSFParseLevel.STRICT.ordinal]()
                    throw DecoratedError("TSF", "Non-Matching Variable in TSF file! [$currName]")
                    exitProcess(-1)
                }

                TSFParseLevel.WARNING -> {
                    parseCallbackFunctions[TSFParseLevel.WARNING.ordinal]()
                    throw DecoratedError("TSF", "Non-Matching Variable in TSF file! [$currName]")
                }

                TSFParseLevel.LENIENT -> {
                    parseCallbackFunctions[TSFParseLevel.LENIENT.ordinal]()
                }
            }
        }

        return _class
    }
}