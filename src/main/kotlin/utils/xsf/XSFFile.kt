package utils.xsf

import utils.storage.extractClass
import utils.storage.extractClassParam
import utils.storage.getStableHash
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.isWritable
import kotlin.reflect.*

/**
 * TODO : Document & Finish
 *  + Add support for more than String keys in maps
 */
class XSFFile(
    var name: String = getStableHash(java.time.Instant.now()),
) {
    /**
     * All allowed variable types in a XSF file
     */
    private val _allowedClasses: ArrayList<KClass<*>> = arrayListOf(
        String::class,
        Boolean::class,
        Int::class,
        Short::class,
        Long::class,
        Double::class,
        Char::class,
        Map::class,
        HashMap::class,
    )

    /**
     * The save path of the file
     */
    private var _path: Path = Paths.get("").toAbsolutePath().normalize()

    /**
     * The file contents
     */
    private var _contents: String = ""

    /**
     * Loads in all members of a class [T]
     */
    fun <T> loadClass(classObj: T) {
        classObj!!::class.members.forEach {
            if (Internal.parseMask.contains(it.name)) { return@forEach }

            when (it.returnType.classifier as KClass<*>) {
                String::class   -> _contents += "String ${it.name}: \"${(it as KProperty<*>).getter.call(classObj).toString()}\"\n\n"
                Char::class     -> _contents += "Char ${it.name}: ${(it as KProperty<*>).getter.call(classObj)}\n\n"
                Boolean::class,
                Int::class,
                Short::class,
                Long::class,
                Double::class,  -> _contents += extractClass(it).simpleName!! +
                                                " ${it.name}: ${(it as KProperty<*>).getter.call(classObj)}\n\n"
                HashMap::class -> {
                    _contents += "NamedHMap ${it.name}: ${extractClassParam(it, 0).simpleName} {\n"
                    ((it as KProperty<*>).getter.call(classObj) as HashMap<*, *>).forEach { entry ->
                        _contents += "\t${entry.key}: ${entry.value},\n"
                    }
                    _contents += "}\n\n"
                }

                Map::class -> {
                    _contents += "NamedMap ${it.name}: ${extractClassParam(it, 0).simpleName} {\n"
                    ((it as KProperty<*>).getter.call(classObj) as HashMap<*, *>).forEach { entry ->
                        _contents += "\t${entry.key}: ${entry.value},\n"
                    }
                    _contents += "}\n\n"
                }
            }
        }
    }

    /**
     * Sets and verifies the file save path
     * @param path The desired file save path as a [Path]
     */
    fun setPath(path: Path) {
        if (!path.isDirectory() || !path.isWritable()) {
            throw IOException("Invalid or secure path [$path]!")
        }

        _path = path
    }

    /**
     * Sets and verifies the file save path, a helper function that accepts a [String]
     * @param path The desired file save path as a [String]
     */
    fun setPath(path: String) {
        setPath(Paths.get(path))
    }

    /**
     * Returns the file save path
     * @return The file save path as a [Path]
     */
    fun getPath(): Path {
        return _path
    }

    /**
     * Builds the file contents and saves it at the set path
     */
    fun save() {
        try {
            val file = FileWriter("$_path\\$name.xsf", false)
            file.write(_contents)
            file.close()
        } catch (err: IOException) {
            throw IOException("Error in creating and writing to  file [$_path\\$name.xsf]!")
        }
    }
}