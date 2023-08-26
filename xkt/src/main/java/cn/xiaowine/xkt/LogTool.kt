package cn.xiaowine.xkt

import android.health.connect.datatypes.units.Length
import android.util.Log

object LogTool {
    private var maxLength = 4000
    private lateinit var tag: String
    private const val XP_TAG = "LSPosed-Bridge"
    private var predicate = true
    private var printXp = true


    /**
     * 初始化LogTool
     *
     * @param tag [String] Log的tag
     * @param predicate [Unit] 显示条件
     * @param printXp [Boolean] 是否打印到Xposed日志
     */
    fun init(tag: String, predicate: (() -> Boolean)?= null, printXp: Boolean = false, maxLength: Int = 4000) {
        this.tag = tag
        this.printXp = printXp
        this.predicate = predicate?.invoke() ?: true
        this.maxLength = maxLength
    }


    /**
     * 打印日志
     * @receiver [T] 打印的内容
     * @param level [LogLevel] 日志等级
     * @return [T] 返回自身
     */
    fun <T> T?.log(level: LogLevel = LogLevel.DEBUG): T? {
        if (!predicate) return this
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
                print(tag, value, level)
                if (printXp) print(XP_TAG, "$tag:$value", level)
            }

        } else {
            print(tag, content, level)
            if (printXp) print(XP_TAG, "$tag:$content", level)
        }
        return this
    }

    private fun print(tag: String, content: String, level: LogLevel) {
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, content)
            LogLevel.DEBUG -> Log.d(tag, content)
            LogLevel.INFO -> Log.i(tag, content)
            LogLevel.WARN -> Log.w(tag, content)
            LogLevel.ERROR -> Log.e(tag, content)
            LogLevel.ASSERT -> Log.wtf(tag, content)
        }
    }
}

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT
}