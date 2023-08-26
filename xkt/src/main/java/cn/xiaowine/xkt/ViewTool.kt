package cn.xiaowine.xkt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF

object ViewTool {


    /**
     * 获取圆角位图
     * @receiver [Bitmap] 原图
     * @param cornerRadius [Float] 圆角半径
     * @return [Bitmap] 圆角位图
     */
    fun Bitmap.getRoundedCornerBitmap(cornerRadius: Float): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            Canvas(this).apply {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
                paint.color = Color.BLACK
                drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                drawBitmap(this@getRoundedCornerBitmap, 0f, 0f, paint)
            }
        }
    }


    /**
     * 创建纯色位图
     *
     * @param width [Int] 宽度
     * @param height [Int] 高度
     * @param color [Int] 颜色
     * @return [Bitmap] 纯色位图
     */
    fun createSolidColorBitmap(width: Int, height: Int, color: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            Canvas(this).apply {
                drawColor(color)
            }
        }
    }
}