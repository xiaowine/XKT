package cn.xiaowine.xkt

import android.os.Handler
import android.os.Looper
import java.util.regex.Pattern
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Tool {

    /**
     * Handle null characters
     *
     */
    fun String.handleNullCharacters() = this.regexReplace(" ", "").regexReplace("\n", "")

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