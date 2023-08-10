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
     * @param context
     */
    fun init(context: Context) {
        this.context = context
    }

    fun isLandscape() = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    /**
     * Open url
     *
     */
    fun String.openURL() {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
    }

    /**
     * Show toast
     *
     * @param isShort
     */
    fun Any?.showToast(isShort: Boolean = false) {
        handler.post {
            Toast.makeText(context, this.toString(), if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Restart app
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
     * Check installed
     * Android11+ 注意权限
     *
     * @return [Boolean]
     */
    fun String.checkInstalled(): Boolean {
        return try {
            this.getAppInfo(PackageManager.GET_META_DATA)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Get app info
     *
     * @return [ApplicationInfo?]
     */
    fun String.getAppInfo(flags: Int = 0): ApplicationInfo {
        return context.packageManager.getApplicationInfo(this, flags)
    }


    /**
     * Get app info
     *
     * @return [List<PackageInfo]
     */
    fun getAppInfo(flags: Int = 0): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(flags)
    }


}