package cn.xiaowine.xkt

import android.os.Handler
import android.os.Looper
import android.util.Base64
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Tool {

    /**
     * 处理空字符
     * @receiver [String] 原字符串
     * @return [String] 处理后的字符串
     */
    fun String.handleNullCharacters(): String = this.regexReplace("\n| ", "")

    /**
     * 正则表达式替换字符串
     * @receiver [String] 原字符串
     * @param pattern [String] 正则表达式
     * @param newString [String] 替换的字符串
     * @return [String] 替换后的字符串
     */
    fun String.regexReplace(pattern: String, newString: String): String {
        val m = Pattern.compile("(?i)$pattern").matcher(this)
        return m.replaceAll(newString)
    }

    /**
     * 转到主线程
     *
     * @param delayed [Long] 延迟时间
     * @param callback [Unit] 回调函数
     * @return [Boolean] 是否成功
     */
    fun goMainThread(delayed: Long = 0, callback: () -> Unit): Boolean {
        return Handler(Looper.getMainLooper()).postDelayed({
            callback()
        }, delayed * 1000)
    }

    /**
     * 侦测数据变化
     *
     * @param initialValue [T] 初始值
     * @param onChange [Unit] 回调函数
     * @return [ReadWriteProperty] 侦测数据变化
     */
    inline fun <T> observableChange(initialValue: T, crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit): ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (oldValue != newValue) {
                onChange(property, oldValue, newValue)
            }
        }
    }


    /**
     * Base64 解码
     * @receiver [String] 原字符串
     * @return [String] 解码后的字符串
     */
    fun String.base64Decode(): String = Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)

    /**
     * Base64 编码
     * @receiver [String] 原字符串
     * @return [String] 编码后的字符串
     */
    fun String.base64Encode() = Base64.encode(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT).toString(Charsets.UTF_8)

    /**
     * MD5 编码
     * @receiver [String] 原字符串
     * @return [String] 编码后的字符串
     */
    fun String.MD5Encode(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(this.toByteArray())).toString(16)

    /**
     * Sha1 编码
     * @receiver [String] 原字符串
     * @return [String] 编码后的字符串
     */
    fun String.SHA1Encode(): String = BigInteger(1, MessageDigest.getInstance("SHA-1").digest(this.toByteArray())).toString(16)

    /**
     * Sha256 编码
     * @receiver [String] 原字符串
     * @return [String] 编码后的字符串
     */
    fun String.SHA256Encode(): String = BigInteger(1, MessageDigest.getInstance("SHA-256").digest(this.toByteArray())).toString(16)

    /**
     * 大写第一个字母
     * @receiver [String] 原字符串
     * @return [String] 处理后的字符串
     */
    fun String.toUpperFirstCase(): String {
        if (isNotEmpty()) {
            val firstChar = this[0]
            if (firstChar.isLowerCase()) {
                return firstChar.uppercaseChar() + substring(1)
            }
        }
        return this
    }

    /**
     * 大写第一个字母，其余小写
     * @receiver [String] 原字符串
     * @return [String] 处理后的字符串
     */
    fun String.toUpperFirstCaseAndLowerOthers(): String {
        if (isNotEmpty()) {
            val firstChar = this[0]
            val restOfString = substring(1).lowercase()
            return firstChar.uppercaseChar() + restOfString
        }
        return this
    }

    /**
     * 随机生成7位数字符串
     * @return [String] 随机生成的字符串
     */
    val randomString get() = randomString(7)

    /**
     * 随机生成数字符串
     *
     * @param length [Int] 长度
     * @return [String] 随机生成的字符串
     */
    fun randomString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length).map { str[java.util.Random().nextInt(str.length)] }.joinToString("")
    }


    /**
     * 字母转数字
     * @receiver [String] 原字符串
     * @return [Long] 转换后的数字
     */
    fun String.latinToLong(): Long {
        val abc = "abcdefghijklmnopqrstuvwxyz"
        return fold(0L) { acc, char ->
            val index = abc.indexOf(char.lowercase())
            if (index == -1) {
                acc * 10 + (char - '0')
            } else {
                acc * 10 + index
            }
        }
    }

    /**
     * 移位字符串
     * @receiver [String] 原字符串
     * @param amount [Int] 移位数
     * @return
     */
    fun String.shiftString2(amount: Int = -3): String {
        val shiftedChars = CharArray(this.length)
        for (i in this.indices) {
            val originalChar = this[i]
            val shiftedChar = (originalChar.code + amount).toChar()
            shiftedChars[i] = shiftedChar
        }
        return String(shiftedChars)
    }

    /**
     * 判断是否为Null，不为Null则执行回调
     * @receiver [Any] 判断对象
     * @param callback [Unit] 回调函数
     * @return [Boolean] 是否为Null
     */
    inline fun <T> T?.isNotNull(callback: (T) -> Unit): Boolean {
        if (this != null) {
            callback(this)
            return true
        }
        return false
    }

    /**
     * 判断是否为Null，为Null则执行回调
     * @receiver [Any] 判断对象
     * @param callback [Unit] 回调函数
     * @return [Boolean] 是否为Null
     */
    inline fun Any?.isNull(callback: () -> Unit): Boolean {
        if (this == null) {
            callback()
            return true
        }
        return false
    }

    /**
     * 判断是否为Null
     * @receiver [Any] 判断对象
     */
    fun Any?.isNull() = this == null

    /**
     *
     * 判断是否不为Null
     * @receiver [Any] 判断对象
     */
    fun Any?.isNotNull() = this != null


    /**
     * @receiver [Boolean] 是否为False，为False则执行回调
     * @param callback [Unit] 回调函数
     */
    inline fun Boolean.isNot(callback: () -> Unit) {
        if (!this) {
            callback()
        }
    }
}