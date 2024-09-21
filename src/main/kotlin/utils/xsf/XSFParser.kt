package utils.xsf

import utils.storage.*
import utils.xsf.Internal.keywordFlags
import utils.xsf.Internal.keywords
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * A .xsf (Xephyr Storage Format) file parser object
 *
 * @param _class The class to parse into
 * @param inFile The file to parse from
 * @param parseLevel The level of error strictness for parsing
 */
class XSFParser <T> (
    private val _class: T,
    private var inFile: File,
    private var parseLevel: XSFParseLevel = XSFParseLevel.STRICT
) {
    private val parseCallbackFunctions: ArrayList<(String) -> Unit> = ArrayList(XSFParseLevel.entries.count())
    private val classMap: HashMap<String, String> = hashMapOf()

    /**
     * Sets the callback function for a level of parsing error
     *
     * @param func The function to be set as the callback
     */
    fun setParseCallbackFun(func: (String) -> Unit, parseLevel: XSFParseLevel) {
        parseCallbackFunctions[parseLevel.ordinal] = func
    }

    /**
     * Checks file validity and loads class member names, types, and values
     */
    init {
        // Check file validity
        if(!inFile.exists() || !inFile.canRead()) {
            throw InvalidXSFFile("File $inFile does not exist or is unreadable!")
        }

        if(inFile.extension != "xsf") {
            throw InvalidXSFFile("Invalid file extension!\nExpected: .xsf | Received: .${inFile.extension}")
        }

        // Add class member names to classMap
        _class!!::class.memberProperties.forEach {
            if(Internal.parseMask.contains(it.name)) { return@forEach }

            // Coerce the member's return type to be a simple name
            // (Remove collection type specifications and kotlin class jargon)
            classMap[it.name] = "^[^<]*".toRegex().findAll(
                it.returnType.toString()
                    .replace("\\bkotlin(?:\\.[^.A-Z<>]*)*\\.".toRegex(), "")
            ).first().value
        }

        XSFParseLevel.entries.forEach {
            parseCallbackFunctions.add(it.ordinal) {}
        }
    }

    private fun reservedKeywordCheck(token: String, line: Int) {
        if(keywords.contains(token)) {
            throw InvalidXSFFile("Reserved keyword '${token}' used on line $line [$inFile]")
        }
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
    fun parse() : Result<T> {
        // Parse in from file
        val reader = BufferedReader(FileReader(inFile))
        var line: String?
        val splitLine: MutableList<Array<String>> = MutableList(0) { arrayOf("") }
        var flattenedFile = ""

        val storedVars: MutableMap<String, Pair<String, Any?>> = mutableMapOf() // Name, Type, Value
        val storedGroups: MutableMap<String, Pair<Pair<KClass<*>, String>, ArrayList<Pair<String, Any>>>> = mutableMapOf() // Name, (Group Type, Member Type), Group Values (Name, Value)
        val groupData: ArrayList<Pair<String, Any>> = arrayListOf()

        var linePos = 1

        while (reader.readLine().also { line = it } != null) {
            // Remove all inline comments
            line = line!!.replace("//.*".toRegex(), "")

            // Trim input and separate tokens
            line = line!!.trim()
            Internal.specialChars.forEach { line = line!!.replace(it, " $it ") }

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
                if (keywordFlags[currLine[0]]?.and(Internal.XSFKeywordFlag.COLLECTION.value) != 0 || inGroup) {
                    // Collection declaration checks
                    if(!inGroup) {
                        _type = currLine[0]

                        reservedKeywordCheck(currLine[1], linePos)
                        _name = currLine[1]

                        if (currLine[2] != ":") {
                            throw InvalidXSFFile("Illegal character '${currLine[2]}' on line $linePos [$inFile]")
                        }

                        if(!keywords.contains(currLine[3])) {
                            throw InvalidXSFFile("Invalid type in collection '$_name' on line $linePos [$inFile]")
                        }
                        _mType = currLine[3]

                        if(currLine[4] != "{") {
                            throw InvalidXSFFile("Missing collection definition on line $linePos [$inFile]")
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
                        throw InvalidXSFFile("Illegal character '${currLine[2]}' in collection '$_name' on line $linePos [$inFile]")
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
                        throw InvalidXSFFile("Failed to cast variable '$_name' to type '$_type' on line $linePos [$inFile]")
                    }

                    return@forEach
                }

                // [Primitive type]
                _type = currLine[0]

                reservedKeywordCheck(currLine[1], linePos)
                _name = currLine[1]

                if (currLine[2] != ":") {
                    throw InvalidXSFFile("Illegal character '${currLine[2]}' on line $linePos [$inFile]")
                }

                // Build type/value pair
                try {
                    when(_type) {
                        "String" -> storedVars[_name] = Pair("String", currLine[3]
                            .removePrefix("\"")
                            .removeSuffix("\""))
                        "Boolean" -> storedVars[_name] = Pair("Boolean", currLine[3].toBoolean())
                        "Int" -> storedVars[_name] = Pair("Int", currLine[3].toInt())
                        "Short" -> storedVars[_name] = Pair("Short", currLine[3].toShort())
                        "Long" -> storedVars[_name] = Pair("Long", currLine[3].toLong())
                        "Double" -> storedVars[_name] = Pair("Double", currLine[3].toDouble())
                        "Char" -> storedVars[_name] = Pair("Char", currLine[3][0])
                    }
                } catch (e: Exception) {
                    throw InvalidXSFFile("Failed to cast variable '$_name' to type '$_type' on line $linePos [$inFile]")
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
                        // Attempt to find and each variable, accounting for delegates
                        var _memberName = key
                        if (isFieldDelegate(_class as Any, key)) {
                            _memberName += "\$delegate"
                        }

                        val member = _class!!::class.java.getDeclaredField(_memberName)
                        val property = _class!!::class.members
                            .filterIsInstance<KProperty1<T, Any?>>()
                            .find { it.name == key }

                        member.isAccessible = true

                        // [Collection type]
                        if(
                            keywordFlags[value]?.and(Internal.XSFKeywordFlag.COLLECTION.ordinal) != 0
                             && property is KMutableProperty1<T, Any?>
                             && storedGroups[key] != null
                          ) {
                            try {
                                val parameter = storedGroups[key]
                                property.setter.call(_class,
                                    mapCast(
                                        parameter!!.second,
                                        property.returnType.classifier as KClass<*>,
                                        parameter.first.first
                                    ),
                                )
                            } catch(e: Exception) {
                                e.printStackTrace()
                                throw InvalidXSFFile("Group name mismatch in parsing! [$key]")
                            }

                            return@forEach
                        }

                        // [Primitive type]
                        if (
                            storedVars[key]?.second == null
                             && property is KMutableProperty1<T, Any?>
                             && property.returnType.isMarkedNullable
                        ) {
                            property.setter.call(_class, null)
                        } else if (
                            property is KMutableProperty1<T, Any?>
                             && !property.returnType.isMarkedNullable
                             && storedVars[key]?.second != null
                        ) {
                            property.setter.call(_class, anyCast(storedVars[key]!!.second, property.returnType.classifier as KClass<*>))
                        }
                    } catch (e: Exception) {
                        errStr = key
                        e.printStackTrace()
                        throw Exception()
                    }
                }

                _break = true
            } catch (_: Exception) {
                // Handles errors in parsing according to parse level, calls a callback set by user if defined
                when (parseLevel) {
                    XSFParseLevel.STRICT -> {
                        parseCallbackFunctions[XSFParseLevel.STRICT.ordinal](errStr)
                        throw InvalidXSFFile("Non-Matching Variable in XSF file! [$errStr]")
                    }

                    XSFParseLevel.WARNING -> {
                        parseCallbackFunctions[XSFParseLevel.WARNING.ordinal](errStr)
                        return Result.failure(DecoratedWarning("XSF", "Non-Matching Variable in XSF file! [$errStr]"))
                    }

                    XSFParseLevel.LENIENT -> {
                        parseCallbackFunctions[XSFParseLevel.LENIENT.ordinal](errStr)
                        return _class!!::class.constructors.firstOrNull { it.parameters.isEmpty() }?.call()
                            ?.let { Result.success(it) }
                            ?: Result.failure(DecoratedWarning("XSF", "Non-Matching Variable in XSF file! [$errStr]"))
                    }
                }
            }
        }

        return Result.success(_class)
    }
}