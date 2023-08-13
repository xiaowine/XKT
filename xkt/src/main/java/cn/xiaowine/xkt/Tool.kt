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
     * Handle null characters
     *
     */
    fun String.handleNullCharacters() = this.regexReplace("\n| ", "")

    /**
     * Regex replace
     *
     * @param pattern
     * @param newString
     * @return
     */
    fun String.regexReplace(pattern: String, newString: String): String {
        val m = Pattern.compile("(?i)$pattern").matcher(this)
        return m.replaceAll(newString)
    }

    /**
     * Go main thread
     *
     * @param delayed
     * @param callback
     * @return
     */
    fun goMainThread(delayed: Long = 0, callback: () -> Unit): Boolean {
        return Handler(Looper.getMainLooper()).postDelayed({
            callback()
        }, delayed * 1000)
    }

    /**
     * Observable
     *
     * @param T
     * @param initialValue
     * @param onChange
     * @return
     */
    inline fun <T> observableChange(initialValue: T, crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit): ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (oldValue != newValue) {
                onChange(property, oldValue, newValue)
            }
        }
    }


    /**
     * Base64decode
     *
     */
    fun String.base64Decode() = Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)

    /**
     * Base64encode
     *
     */
    fun String.base64Encode() = Base64.encode(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT).toString(Charsets.UTF_8)

    /**
     * Md5 encode
     *
     * @return
     */
    fun String.MD5Encode(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(this.toByteArray())).toString(16)

    /**
     * Sha1 encode
     *
     * @return
     */
    fun String.SHA1Encode(): String = BigInteger(1, MessageDigest.getInstance("SHA-1").digest(this.toByteArray())).toString(16)

    /**
     * To upper first case
     *
     * @return
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
     * To upper first case and lower others
     *
     * @return
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
     * Random string
     */
    val randomString get() = randomString(7)

    /**
     * Random string
     *
     * @param length
     * @return
     */
    fun randomString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length).map { str[java.util.Random().nextInt(str.length)] }.joinToString("")
    }


    /**
     * Latin to long
     *
     * @return
     */
    fun String.latinToLong(): Long {
        val abc = "abcdefghijklmnopqrstuvwxyz"
        if (all { it in abc }) {
            return fold(0L) { acc, char ->
                val index = abc.indexOf(char.lowercase())
                if (index == -1) {
                    acc * 10 + (char - '0')
                } else {
                    acc * 10 + index
                }
            }
        }
        return 0L
    }


    /**
     * Shift string
     *
     * @param content
     * @param amount
     * @return
     */
    fun shiftString(content: String, amount: Int): String {
        val shiftedChars = CharArray(content.length)
        for (i in content.indices) {
            val originalChar = content[i]
            val shiftedChar = (originalChar.toInt() + amount).toChar()
            shiftedChars[i] = shiftedChar
        }
        return String(shiftedChars)
    }


    /**
     * Is not null
     *
     * @param T
     * @param callback
     * @return
     */
    inline fun <T> T?.isNotNull(callback: (T) -> Unit): Boolean {
        if (this != null) {
            callback(this)
            return true
        }
        return false
    }

    /**
     * Is not
     *
     * @param callback
     * @receiver
     */
    inline fun Boolean.isNot(callback: () -> Unit) {
        if (!this) {
            callback()
        }
    }

    /**
     * Is null
     *
     * @param callback
     * @receiver
     * @return
     */
    inline fun Any?.isNull(callback: () -> Unit): Boolean {
        if (this == null) {
            callback()
            return true
        }
        return false
    }

    /**
     * Is null
     *
     */
    fun Any?.isNull() = this == null

    /**
     * Is not null
     *
     */
    fun Any?.isNotNull() = this != null
}