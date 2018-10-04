package manager.spring

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtils {

    fun <T> fromStructureToJson(structure: T): String {
        val listType = object : TypeToken<T>() {}.type
        return Gson().toJson(structure, listType)
    }
}