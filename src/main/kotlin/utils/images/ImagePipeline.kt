package utils.images

import utils.storage.*
import java.awt.image.BufferedImage
import java.io.IOException

class ImagePipeline {
    /**
     * The current state of the pipeline's image
     */
    private var _image: LockType<BufferedImage?> = LockType(null)

    /**
     * The pure data of the image being stored
     */
    private var _data: LockType<ImageData?> = LockType(null)

    /**
     * A list of linkable image effectors to be enacted on the image
     */
    private var _effectorChain: ArrayList<ImageEffector<*, *>> = arrayListOf()

    /**
     * The position of the last error in the [_effectorChain]
     */
    var errorPos: Int? = null

    /**
     * Loads and image and its data into the pipeline
     */
    fun loadImage(img: BufferedImage?) {
        _image.value = img
        _data.value = _image.value?.let { ImageData(it) }
    }

    /**
     * Builds the current state of the image data into an image and returns it
     * @return On a successful read of the image data, the stored image as a [BufferedImage].
     * @throws IOException On a failed read of the image data.
     */
    fun getImage(): Result<BufferedImage> {
        _data.value?.getImage()?.onSuccess {
            _image.value = it
            return Result.success(it)
        }?.onFailure {
            return Result.failure(it)
        }

        return Result.failure(IOException("Failed to read image data!"));
    }

    /**
     * Adds any number of ImageEffectors to the pipeline
     *
     * @return A result object that contains the success state of the function
     * along with a message, used for UI integration/callbacks.
     */
    fun chain(vararg effectors: ImageEffector<*, *>): Result<Unit> {
        if (effectors.isEmpty()) { // No effectors? (https://i.imgflip.com/65939r.jpg?a478752)
            return Result.success(Unit)
        }

        if (_effectorChain.size == 0) { // Chain does not start with Unit inputType
            if (effectors[0].inputType != Unit::class) {
                errorPos = 0
                return Result.failure(
                    ImagePipelineError(
                        "Invalid Starting Effector!",
                        PipelineErrorType.CHAIN_ERROR
                    )
                )
            }

            if (_effectorChain[0].outputType != effectors[0]) { // Current chain cannot connect with added effectors
                errorPos = _effectorChain.size
                return Result.failure(
                    ImagePipelineError(
                        "Non-Connecting Effector!",
                        PipelineErrorType.CHAIN_ERROR
                    )
                )
            }
        }

        // Validate added effectors
        for (i in 1..effectors.size) {
            if (effectors[i - 1].outputType != effectors[i].inputType) {
                return Result.failure(
                    ImagePipelineError(
                        "Non-Connecting Effector!",
                        PipelineErrorType.CHAIN_ERROR
                    )
                )
            }
        }

        _effectorChain.addAll(effectors)
        return Result.success(Unit)
    }

    /**
     * Applies all effectors in the [_effectorChain] to the image loaded in the pipeline
     */
    fun run() {
        // TODO : Implement run()
    }
}