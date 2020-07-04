package duodev.valerio.electric.Utils

import com.google.gson.Gson

fun <T> convertToPojo(
    data: MutableMap<String, Any>,
    pojo: Class<T>
): T {
    val gson = Gson()
    data.toMap()
    val jsonElement = gson.toJsonTree(data)
    return gson.fromJson(jsonElement, pojo)
}