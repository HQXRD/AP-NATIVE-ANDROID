package com.xtree.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.xtree.base.R

class AllRoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var mPath: Path? = null
    private var mRectF: RectF? = null

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private var rids: FloatArray? = null
    private var imageCorn: Float = 5f
    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        imageCorn = typedArray.getDimension(R.styleable.CircleImageView_imageCorn, 0f)
        imageCorn = if (imageCorn == 0f) dp2px(5f) else imageCorn
        typedArray.recycle()

        //rids = floatArrayOf(imageCorn, imageCorn, imageCorn,imageCorn, dp2px(imageCorn), imageCorn,imageCorn, imageCorn);
        rids = floatArrayOf(
            imageCorn,
            imageCorn,
            imageCorn,
            imageCorn,
            imageCorn,
            imageCorn,
            imageCorn,
            imageCorn
        )
        mPath = Path()
        mRectF = RectF()
    }

    private fun dp2px(dpValue: Float): Float {
        val scale = this.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    /**
     * 画图
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        val w = this.width
        val h = this.height
        mRectF!![0f, 0f, w.toFloat()] = h.toFloat()
        mPath!!.addRoundRect(mRectF!!, rids!!, Path.Direction.CW)
        canvas.clipPath(mPath!!)
        super.onDraw(canvas)
    }

    init {
        init(context, attrs)
    }
}