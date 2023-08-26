package cn.xiaowine.xkt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.res.Configuration
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import cn.xiaowine.xkt.AcTool.checkInstalled
import kotlin.system.exitProcess


@SuppressLint("StaticFieldLeak")
object AcTool {

    lateinit var context: Context

    val activity: Activity by lazy { context as Activity }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * Init
     *
     * @param context [Context]
     */
    fun init(context: Context) {
        this.context = context
    }


    /**
     * 判断当前是否为横屏
     * @return [Boolean] true为横屏，false为竖屏
     */
    fun isLandscape(): Boolean = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    /**
     * 跳转浏览器打开网页
     * @receiver [String] 网址
     */
    fun String.openURL() = context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    /**
     * 显示Toast，可任意线程调用
     * @receiver [T] 显示内容
     * @param duration [Int] 显示时长，与Toast相同，默认Toast.LENGTH_SHORT
     * @param predicate [Boolean] 显示条件，为false时不显示，默认null
     * @return [T] 返回自身
     */
    fun <T> T?.showToast(duration: Int = Toast.LENGTH_SHORT, predicate: (() -> Boolean)?): T? {
        if (predicate?.invoke() == false) return this
        handler.post {
            Toast.makeText(context, this.toString(), duration).show()
        }
        return this
    }

    /**
     * 重启App
     *
     */
    fun restartApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName).apply {
            this?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
        exitProcess(0)
    }


    /**
     * 判断是否安装某个应用
     * Android11+ 注意权限
     * @receiver [String] 获取软件包名
     * @return [Boolean] true为已安装，false为未安装
     */
    fun String.checkInstalled(): Boolean {
        return try {
            this.getApplicationInfo(PackageManager.GET_META_DATA)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * 获取应用信息
     * @receiver [String] 获取软件包名
     * @param flags [PackageInfoFlags] 默认0
     * @return [ApplicationInfo]
     */
    fun String.getApplicationInfo(flags: Int = 0): ApplicationInfo {
        return context.packageManager.getApplicationInfo(this, flags)
    }


    /**
     * 获取所有应用信息
     * @param flags [PackageInfoFlags] 默认0
     * @return [PackageInfo] PackageInfo列表
     */
    fun getAllApplicationInfoInfo(flags: Int = 0): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(flags)
    }
}