package utils.storage

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import ui.AlertBox
import utils.tsf.TSFFile
import utils.tsf.TSFParser
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation

/**
 * The time delay in between thread checks (polling rate)
 */
const val AUTO_SAVE_SLEEP_DURATION: Long = 15000

/**
 * A fallback UI function for if an internal error occurs with the auto-saving.
 */
val AUTO_SAVE_FAIL_UI_CALLBACK: (String, List<Int>) -> Unit = { name, codes ->
    AlertBox.displayAlert("Auto-save failed for class object '$name', disabling auto-save for class instance. <Error Code ${codes.joinToString(".")}>")
}

/**
 * Annotation for auto-saving classes to denote save path
 */
annotation class AutoSaveProperties(val path: String, val name: String, val useHash: Boolean = false)

/**
 * Custom observable property for auto-saving properties
 */
class AutoSave<T>(private var value: T, manager: AutoSaveManager) : ReadWriteProperty<AutoSaveManager, T>, AccessStatTracker() {
    /**
     * Whether the reads and writes to the variable should be tracked
     */
    private var _trackRW: Boolean = false

    /**
     * Initializes the variable in the AutoSaveManager instance
     */
    init { manager.updateValue(this@AutoSave::value.name, value) }

    /**
     * Sets the variable and alerts the AutoSaveManager of the change.
     * If RW tracking is enabled, it also alerts the AccessStatTracker of the write.
     */
    override fun getValue(thisRef: AutoSaveManager, property: KProperty<*>): T {
        if (_trackRW) {
            // Track read here (stamtistmics 👍)
        }

        return value
    }

    /**
     * Sets the variable and alerts the AutoSaveManager of the change.
     * If RW tracking is enabled, it also alerts the AccessStatTracker of the write.
     */
    override fun setValue(thisRef: AutoSaveManager, property: KProperty<*>, value: T) {
        if (_trackRW) {
            // Track write here (stamtistmics 👍)
        }

        thisRef.updateValue(property.name, value)
        this.value = value
    }

}

/**
 * Tracks the reads and writes of variables during runtime within auto-saving classes
 * in order to provide more in-depth analytics
 */
open class AccessStatTracker() {
    // TODO : IMPLEMENT
}

/**
 * A manager for auto-saving classes
 * **`super.init(this::class)` must be called _FIRST_ in a derived class's init block!**
 */
open class AutoSaveManager {
    /**
     * If the auto-saving global thread is running
     */
    private var _globalThread: Job = Job()

    /**
     * Alerts when [_globalThread] is safe to stop
     */
    private var _threadLock: CountDownLatch = CountDownLatch(1)

    /**
     * Used by [forceSave] to skip thread waits and save immediately
     */
    private var _skipSignal: CompletableDeferred<Unit> = CompletableDeferred()

    /**
     * The path to the auto-save directory
     */
    private lateinit var _path: Path

    /**
     * The name of the tsf file to save to or create
     */
    private lateinit var _saveName: String

    /**
     * Stores any changes in class data until they are saved
     */
    private val _dataMap = ConcurrentHashMap<String, Any?>()

    /**
     * Initializes save path via annotation verification along with hash (if required)
     * @param derivedClass The class object to be loaded/verified
     */
    fun <T: Any> init(derivedClass: T) {
        val _class = derivedClass::class

        if (!derivedClass::class.hasAnnotation<AutoSaveProperties>()) {
            internalError(AutoSaveError.MISSING_ANNOTATION.ordinal)
        }

        val annotation = _class.annotations.find { it is AutoSaveProperties } as AutoSaveProperties
        _path = Paths.get(annotation.path)

        if (annotation.useHash) {
            _saveName = if (annotation.name.isEmpty()) _class.simpleName.toString() else annotation.name
            _saveName += "_${getStableHash(derivedClass)}"
        }

        launchManagerThread(derivedClass)
    }

    /**
     * Updates a stored class value in [_dataMap].
     * @param name The name of the variable to update
     * @param value The value to set the variable to
     */
    fun <T> updateValue(name: String, value: T) {
        try {
            _dataMap[name] = value as Any
        } catch (e: Exception) {
            internalError()
        }
    }

    /**
     *  Displays an error to the UI using [AUTO_SAVE_FAIL_UI_CALLBACK] and disables auto-saving for the class instance
     *  @param errorCode The [AutoSaveError]s used for the error code(s)
     */
    private  fun internalError(vararg errorCode: Int) {
        AUTO_SAVE_FAIL_UI_CALLBACK(_saveName, errorCode.asList())
        stop()
    }

    /**
     * Launches the global auto-saving thread
     * @param classInstance The class instance to be auto-saved
     */
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private fun <T> launchManagerThread(classInstance: T) {
        _globalThread = GlobalScope.launch(Dispatchers.Default) {
            val filePath = File(Paths.get("").toAbsolutePath().normalize().toString() + "\\$_path\\$_saveName.tsf")

            // Create and populate the file if it does not exist
            if (!filePath.exists()) {
                runCatching {
                    withContext(Dispatchers.IO) {
                        filePath.createNewFile()
                    }
                }.onFailure { internalError(AutoSaveError.INVALID_FILE.ordinal) }

                TSFFile(_saveName).apply {
                    loadClass(classInstance)
                    save()
                }
            } else { // Load in saved class information if the file exists
                TSFParser(classInstance, filePath).parse().onFailure {
                    internalError(AutoSaveError.CLASS_ERROR.ordinal)
                }
            }

            val parser = TSFParser(
                classInstance,
                filePath
            )

            var parseResult: T? = null

            parser.parse().onSuccess{
                parseResult = it
            }.onFailure {
                internalError(AutoSaveError.CLASS_ERROR.ordinal)
            }

            // Main saving logic
            while (isActive) {

                if (_dataMap.isEmpty()) {
                    _threadLock.countDown()

                    select {
                        onTimeout(AUTO_SAVE_SLEEP_DURATION) { }
                        _skipSignal.onAwait { }
                    }

                    _threadLock = CountDownLatch(1)
                    continue
                }

                _dataMap.forEach { element ->
                    val property = parseResult!!::class.members
                        .filterIsInstance<KMutableProperty1<T, Any?>>()
                        .find { it.name == element.key }

                    try {
                        property!!.setter.call(parseResult, anyCast(element.value!!, extractClass(property)))

                        _dataMap.remove(element.key)
                    } catch (err: Exception) {
                        internalError(AutoSaveError.CLASS_ERROR.ordinal)
                    }
                }

                TSFFile(_saveName).apply {
                    setPath(Paths.get("").toAbsolutePath().normalize().toString() + "\\$_path")
                    loadClass(parseResult)
                    save()
                }

                _threadLock.countDown()

                select {
                    onTimeout(AUTO_SAVE_SLEEP_DURATION) { }
                    _skipSignal.onAwait { }
                }

                _threadLock = CountDownLatch(1)
            }
        }
    }

    /**
     * Forces a save to occur
     * @return A [Deferred] [Boolean] that returns true when the save finishes successfully, or returns exceptionally
     * if an error occurs (***Use runCatching!***)
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun forceSave() : Deferred<Boolean> {
        val result = CompletableDeferred<Boolean>()

        GlobalScope.launch(Dispatchers.Default) {
            try {
                if (_threadLock.count != 0L) {
                    withContext(Dispatchers.IO) { _threadLock.await() }
                }

                _skipSignal.complete(Unit)
                _skipSignal = CompletableDeferred()

                result.complete(true)
            } catch (e: Exception) {
                result.completeExceptionally(e)
            }
        }

        return result
    }

    /**
     * Stops the auto-saver via its main manager thread after forcing the file to save a final time.
     * @return A [Deferred] [Boolean] that returns true when the thread is successfully, or returns exceptionally
     * if an error occurs (***Use runCatching!***)
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun stop() {
        // stop() should never throw unless some insane bullshit happens in threadland,
        GlobalScope.launch(Dispatchers.Default) {
            if (!_globalThread.isActive) {
                internalError(AutoSaveError.THREAD_ERROR.ordinal)
            }

            forceSave().await()
            _globalThread.cancel()

            // Make sure we killed it
            if (_globalThread.isActive) { _globalThread.cancel() }
        }
    }
}

/**
 * Errors used in [AUTO_SAVE_FAIL_UI_CALLBACK] as error codes
 */
enum class AutoSaveError {
    /**
     * A class extending [AutoSaveManager] did not have an [AutoSaveProperties] annotation
     */
    MISSING_ANNOTATION,

    /**
     * There was an error accessing/loading the .tsf file
     */
    INVALID_FILE,

    /**
     * There was an error loading the class object, or saving the class memebers
     */
    CLASS_ERROR,

    /**
     * There was an error in thread handling
     */
    THREAD_ERROR
}