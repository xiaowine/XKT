package cn.xiaowine.xkt

import android.os.Handler
import android.os.Looper
import android.util.Base64
import java.math.BigInteger
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
    inline fun <T> observable(initialValue: T, crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit): ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
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