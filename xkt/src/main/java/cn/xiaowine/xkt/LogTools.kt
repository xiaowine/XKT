package cn.xiaowine.xkt

import android.util.Log

object LogTools {
    private const val maxLength = 4000
    private lateinit var tag: String
    private const val XP_TAG = "LSPosed-Bridge"
    private var isDebug = true

    fun init(tag: String, isDebug: Boolean) {
        this.tag = tag
        this.isDebug = isDebug
    }

    fun <T> T?.log(): T? {
        if (!isDebug) return this
        val content = if (this is Throwable) Log.getStackTraceString(this) else this.toString()
        if (content.length > maxLength) {
            val chunkCount = content.length / maxLength
            for (i in 0..chunkCount) {
                val max = 4000 * (i + 1)
                val value = if (max >= content.length) {
                    content.substring(maxLength * i)
                } else {
                    content.substring(maxLength * i, max)
                }
                Log.d(tag, value)
                Log.d(XP_TAG, "$tag:$value")
            }

        } else {
            Log.d(tag, content)
            Log.d(XP_TAG, "$tag:$content")
        }
        return this
    }
}
