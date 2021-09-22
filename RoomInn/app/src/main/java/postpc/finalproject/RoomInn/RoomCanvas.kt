package postpc.finalproject.RoomInn

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

class RoomCanvas(
    context: Context, attrs: AttributeSet?,
    private var path: Path,
    private var hasPath: Boolean = true
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    constructor(context: Context) : this(context, null, Path()) {
        hasPath = false
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, Path()) {
        hasPath = false
    }

    private val paint = Paint()

    fun setPath(newPath: Path) {
        path = newPath
        hasPath = true
    }

    fun isInit(): Boolean {
        return hasPath
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 15f
        paint.strokeJoin = Paint.Join.MITER
        canvas?.drawPath(path, paint)
    }


}

