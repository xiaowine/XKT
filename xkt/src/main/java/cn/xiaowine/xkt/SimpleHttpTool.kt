package cn.xiaowine.xkt

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

// HttpURLConnection太基础了，除了拿来简单的get，其他的都不太好用，所以这里就不写其他请求了，建议复杂的还是使用Okhttp
object SimpleHttpTool {

    /**
     * 发起Get请求
     *
     * @param connectTimeout [Int] 连接超时时间，默认5000，单位毫秒
     * @param instanceFollowRedirects [Boolean] 是否自动重定向，默认true
     * @param onSuccess [String] 请求成功回调，返回请求结果
     * @param onError [Exception] 请求失败回调，返回异常
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


    /**
     * 判断是否为HttpURL
     *
     * @param urlType [URLTYPE] 判断类型，默认为[URLTYPE.ALL]，即http和https都判断
     * @return [Boolean] true为是，false为否
     */
    fun String.isUrl(urlType: URLTYPE): Boolean {
        val regex = ("(((${if (urlType == URLTYPE.HTTP) "http" else if (urlType == URLTYPE.HTTPS) "https" else "https|http"})?://)?([a-z0-9]+[.])|(www.))\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)")
        val pat = Pattern.compile(regex.trim { it <= ' ' })
        val mat = pat.matcher(this.trim { it <= ' ' })
        return mat.matches()
    }


    enum class URLTYPE {
        HTTP,
        HTTPS,
        ALL
    }
}