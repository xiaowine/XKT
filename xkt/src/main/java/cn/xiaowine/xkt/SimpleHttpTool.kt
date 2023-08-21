package cn.xiaowine.xkt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

//HttpURLConnection太基础了，除了拿来简单的get，其他的都不太好用，所以这里就不写其他请求了，建议复杂的还是使用Okhttp
object SimpleHttpTool {

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

    fun isUrl(urls: String): Boolean {
        val regex = ("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)")
        val pat = Pattern.compile(regex.trim { it <= ' ' })
        val mat = pat.matcher(urls.trim { it <= ' ' })
        return mat.matches()
    }

    fun isHttpUrl(urls: String): Boolean {
        val regex = ("(((http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)")
        val pat = Pattern.compile(regex.trim { it <= ' ' })
        val mat = pat.matcher(urls.trim { it <= ' ' })
        return mat.matches()
    }

    fun isHttpsUrl(urls: String): Boolean {
        val regex = ("(((https)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)")
        val pat = Pattern.compile(regex.trim { it <= ' ' })
        val mat = pat.matcher(urls.trim { it <= ' ' })
        return mat.matches()
    }
}