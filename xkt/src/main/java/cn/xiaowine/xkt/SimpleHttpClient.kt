package cn.xiaowine.xkt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//HttpURLConnection太基础了，除了拿来简单的get，其他的都不太好用，所以这里就不写其他请求了，建议复杂的还是使用Okhttp
object SimpleHttpClient {

    /**
     * Get
     *
     * @param connectTimeout
     * @param instanceFollowRedirects
     * @param onSuccess
     * @param onError
     */
    fun String.get(connectTimeout: Int = 5000, instanceFollowRedirects: Boolean = true, onSuccess: ((String) -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        try {
            val connection = (URL(this).openConnection() as HttpURLConnection).apply {
                this.connectTimeout = connectTimeout
                requestMethod = "GET"
                this.instanceFollowRedirects = instanceFollowRedirects
            }
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            onSuccess?.invoke(reader.readText())
        } catch (e: Exception) {
            onError?.invoke(e)
        }
    }
}