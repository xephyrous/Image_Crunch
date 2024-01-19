package utils

typealias KeyVal<A, B> = Pair<A, B>

// Used as custom function parameter lists
// Access variables using the syntax:
//      list.at("varName") as Type

class ParameterList(parameters: ArrayList<KeyVal<String, Any>>) {
    private var parameterMap = HashMap<String, Any>()

    init {
        for(obj: KeyVal<String, Any> in parameters) {
            parameterMap[obj.first] = obj.second
        }
    }

    fun add(name: String, value: Any) {
        parameterMap[name] = value
    }

    fun remove(name: String) {
        try {
            parameterMap.remove(name)
        } catch(_: Exception) {}
    }

    fun at(name: String) : Any? {
        return try {
            parameterMap[name]
        } catch(err: Exception) {
            Any()
        }
    }
}