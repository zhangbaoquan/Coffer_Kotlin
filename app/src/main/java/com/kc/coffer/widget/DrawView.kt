package com.kc.coffer.widget;

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kc.coffer.R
import com.kc.coffer.util.*

/**
 * author      : coffer
 * date        : 10/25/21
 * description :
 */
open class DrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 雷达的中心坐标
     */
    private var mRadarCenterX = 0
    private var mRadarCenterY = 0

    /**
     * 雷达网格最大半径
     */
    private var mRadarRadius = 0f

    /**
     * 雷达中间网格的层数
     */
    private var mRadarCount = 0

    /**
     * 雷达图里的绘制6边行的角度
     */
    private var mRadarAngle = 0f

    /**
     * 雷达画笔
     */
    private lateinit var mRadarPaint: Paint

    /**
     * 雷达数据画笔
     */
    private lateinit var mRadarDataPaint: Paint

    /**
     * 模拟雷达数据
     */
    private var mRadarData = arrayOf(2, 5, 1, 3, 6, 4)

    /**
     * 雷达数里的最大值
     */
    private val mRadarMaxValue = 6

    /**
     * 圆形图像的bitmap
     */
    private lateinit var mCircleBitmap: Bitmap

    /**
     * 圆形图像路径
     */
    private lateinit var mmCirclePath: Path

    /**
     * 圆形图像画笔
     */
    private lateinit var mCirclePaint: Paint

    init {
        initRadar()
        initCircleImg()
    }

    /**
     * 雷达图初始化参数
     */
    fun initRadar() {
        mRadarCenterX = getScreenWidth() / 2
        mRadarCenterY = getScreenHeight() / 2
        mRadarRadius = mRadarCenterX * 0.8f
        mRadarCount = 6
        mRadarAngle = (Math.PI * 2 / mRadarCount).toFloat()
        // 配置线条画笔
        mRadarPaint = Paint()
        mRadarPaint.color == Color.RED
        mRadarPaint.style = Paint.Style.STROKE
        mRadarPaint.strokeWidth = 5f
        mRadarPaint.isAntiAlias = true
        // 配置数据区域画笔
        mRadarDataPaint = Paint()
        mRadarDataPaint.color = Color.BLUE
    }

    /**
     * 初始化圆形头像的配置
     */
    fun initCircleImg() {
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_HARDWARE, null)
        mCircleBitmap = getBitmap()
        mCirclePaint = Paint()
        mCirclePaint.color = Color.BLACK
        mmCirclePath = Path()
        // 设置头像大小
        // 设置头像大小
        val w = mCircleBitmap.width.toFloat()
        val h = mCircleBitmap.height.toFloat()
        // 添加头像路径
        // 添加头像路径
        mmCirclePath.addCircle(
            w / 2 + 100,
            h / 2 + 100, w / 2, Path.Direction.CW
        )
    }

    private fun getBitmap(): Bitmap {
        return drawableToBitmap(context.resources.getDrawable(R.drawable.haruhi))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        drawCircle(canvas)
//        drawBg(canvas)
//        drawLineAndPoint(canvas)
//        drawRect(canvas)
//        drawRoundRect(canvas)
//        drawPath(canvas)
//        drawRadar(canvas)
//        drawText(canvas)
//        drawRegion(canvas)
        // 画布练习
//        drawCanvas(canvas)
        // 绘制圆形头像
        drawCircleImage(canvas)
    }

    /*****   这一部分主要是说明Paint   *****/

    /*****   这一部分主要是说明Paint    */
    /**
     * 绘制各种圆形
     *
     * @param canvas
     */
    private fun drawCircle(canvas: Canvas) {
        val paint = Paint()
        val radius = 50f
        // 普通的圆
        paint.color = Color.RED
        // 设置抗锯齿
        paint.isAntiAlias = true
        canvas.drawCircle(100f, 100f, radius, paint)

        // 描边
        paint.style = Paint.Style.STROKE
        // 设置画笔宽度，仅在设置STROKE 和 FILL_AND_STROKE 起作用
        paint.strokeWidth = 20f
        paint.color = Color.BLUE
        canvas.drawCircle(220f, 100f, radius, paint)

        // 填充
        paint.style = Paint.Style.FILL
        paint.color = Color.GREEN
        canvas.drawCircle(350f, 100f, radius, paint)

        // 描边且填充
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.BLACK
        canvas.drawCircle(500f, 100f, radius, paint)
    }

    /*****   这一部分是画布的练习     */
    /**
     * 绘制背景
     *
     * @param canvas
     */
    private fun drawBg(canvas: Canvas) {
        // 设置画布背景要在其他图形绘制前设置，否则设置好的背景色会覆盖原有的图形。
        canvas.drawColor(Color.DKGRAY)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        canvas.drawCircle(100f, 100f, 100f, paint)
    }

    /**
     * 绘制直线 & 点
     * 直线、点的宽度或者大小取决于 Paint 的 setStrokeWith(width) 中传入的宽度。
     * 绘制直线需要注意的是，只有当 Style 是 STROKE、FILL_AND_STROKE 时绘制才有效。
     *
     * @param canvas
     */
    private fun drawLineAndPoint(canvas: Canvas) {
        val paint = Paint()
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.RED
        canvas.drawLine(50f, 50f, 200f, 200f, paint)
        paint.strokeWidth = 20f
        canvas.drawPoint(300f, 300f, paint)
    }


    /**
     * 绘制矩形
     * Android 提供了 Rect 和 RectF 类用于存储矩形数据结构
     * Rect 和 RectF 区别就是前者存储的是int 类型，后者是float类型
     *
     *
     * 补充：
     * drawRect 和  drawPoint 都可以绘制矩形，他们的区别是：
     * 1、形状：
     * drawPoint() 只能指定矩形中心的坐标，只能画出正方形。
     * drawRect()  需要指定矩形左上和右下两个点的位置，可以是长方形或者正方形。
     *
     *
     * 2、样式：
     * drawPoint() 只能是填充样式。
     * drawRect()  可以自己选择样式，可以是描边也可以是填充。
     *
     *
     * 结论：绘制矩形首选drawRect。
     *
     * @param canvas
     */
    private fun drawRect(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.YELLOW
        paint.strokeWidth = 10f
        // 描边矩形
        paint.style = Paint.Style.STROKE
        val rect = Rect(100, 100, 200, 200)
        canvas.drawRect(rect, paint)

        // 填充矩形
        paint.style = Paint.Style.FILL
        val rectF = RectF(300.0f, 300.0f, 500.0f, 500.0f)
        canvas.drawRect(rectF, paint)
    }

    /**
     * 绘制圆角矩形
     * 圆角矩形不支持，Rect，仅支持RectF
     *
     * @param canvas
     */
    private fun drawRoundRect(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.DKGRAY
        paint.strokeWidth = 20f
        // 圆角半径
        val radius = 30.0f

        // 描边圆角矩形
        paint.style = Paint.Style.STROKE
        val rect = RectF(100f, 100f, 200f, 200f)
        canvas.drawRoundRect(rect, radius, radius, paint)

        // 填充圆角矩形
        paint.style = Paint.Style.FILL
        val rectF1 = RectF(300.0f, 300.0f, 500.0f, 500.0f)
        canvas.drawRoundRect(rectF1, radius, radius, paint)
    }


    /**
     * 绘制路径
     *
     * @param canvas
     */
    private fun drawPath(canvas: Canvas) {
        /**  绘制直线路径部分    */
//        drawLinePath(canvas);
        /**  绘制弧线路径部分    */
//       drawArcPath(canvas);
        /**  绘制复杂路径    */
//        drawAddPath(canvas);
        /**  填充路径   */
        drawFillPath(canvas)
    }

    /**
     * 绘制直线路径
     *
     * @param canvas
     */
    private fun drawLinePath(canvas: Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.RED

        // 创建路径
        val path = Path()
        // 第一条线的起点
        path.moveTo(100f, 100f)
        // 第一条线的终点，第二条线的起点
        path.lineTo(400f, 300f)
        // 第二条线的终点
        path.lineTo(100f, 300f)
        // 绘制第三条线，形成闭环。如果连续画了几条直线，但没有形成闭环，
        // 调用 close() 函数会把路径首尾连接起来形成闭环，相当于是帮我们画多一条直线，
        // 如果只画了一条直线，那 close() 方法是不会起作用的。
        path.close()
        // 绘制直线路径
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制弧线路径
     * Path 的 arcTo() 方法可用于绘制弧线，弧线在这里指的是椭圆上截取的一部分。
     * 在这里我们要注意的是，弧线默认是填充的，更准确的来说 drawArc() 方法是切出椭圆中的一块,
     * 如果我们只想要一条线的话，就要自己设置描边样式和描边宽度（例如圆形进度）
     * startAngle  是的起始角度，位置在圆形的右边。
     * sweepAngle  表示扫描角度，值为负数表示逆时针，值为正数表示顺时针。
     * forceMoveTo 重置起点，把绘制弧线的起点从 moveTo 的坐标重置到 startAngle 的位置。
     *
     * @param canvas
     */
    private fun drawArcPath(canvas: Canvas) {
        // 弧线画笔
        val paint = Paint()
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        // 背景画笔
        val ovalPaint = Paint()
        ovalPaint.color = Color.YELLOW
        val path = Path()
        val rectF = RectF(300f, 100f, 500f, 300f)
        // 指定角度
        path.arcTo(rectF, 0f, -90f)
        // 绘制背景
        canvas.drawOval(rectF, ovalPaint)
        // 绘制弧线
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制复杂路径
     * 这里使用Path里addXX 系列方法
     *
     * @param canvas
     */
    private fun drawAddPath(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        val path = Path()
        // 1、先绘制一条直线
        path.moveTo(100f, 100f)
        path.lineTo(200f, 200f)

        // 2、绘制一个弧度
        val rectF = RectF(110f, 90f, 190f, 120f)
        path.addArc(rectF, 0f, 180f)

        // 绘制路径
        canvas.drawPath(path, paint)
    }

    /**
     * 填充路径
     *
     * @param canvas
     */
    private fun drawFillPath(canvas: Canvas) {
        // 1、填充路径内部区域
//        drawFillPathImpl(canvas, Path.FillType.WINDING, 100, 100);
        // 2、填充路径外部区域
//        drawFillPathImpl(canvas, Path.FillType.INVERSE_WINDING, 100, 500);
        // 3、填充路径相交的区域
//        drawFillPathImpl(canvas, Path.FillType.EVEN_ODD, 450, 100);
        // 4、填充路径相交和外部区域
//        drawFillPathImpl(canvas, Path.FillType.INVERSE_EVEN_ODD, 450, 500);
    }


    /**
     * 填充路径的具体实现
     * 这里使用矩形 + 圆形
     * Direction.CW 表示顺时针
     *
     * @param canvas
     * @param fillType 填充类型
     * @param left     左边的间距
     * @param top      顶部的间距
     */
    private fun drawFillPathImpl(
        canvas: Canvas,
        fillType: Path.FillType,
        left: Int,
        top: Int
    ) {
        val paint = Paint()
        paint.color = Color.RED
        val path = Path()
        // 1、先绘制一个矩形，顺时针
        val rectF = RectF(
            left.toFloat(),
            top.toFloat(), (left + 150).toFloat(), (top + 150).toFloat()
        )
        path.addRect(rectF, Path.Direction.CW)

        // 2、绘制一个圆形，顺时针
        path.addCircle((left + 150).toFloat(), (top + 150).toFloat(), 100f, Path.Direction.CW)

        // 设置填充类型
        path.fillType = fillType
        // 绘制路径
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制雷达图
     *
     * @param canvas
     */
    private fun drawRadar(canvas: Canvas) {
        drawRadarPolygon(canvas)
        drawRadarLine(canvas)
        drawRadarRegion(canvas)
    }


    /**
     * 绘制雷达蜘蛛网格
     *
     * @param canvas
     */
    private fun drawRadarPolygon(canvas: Canvas) {
        val path = Path()
        // 每个蛛丝之间的间距
        val gap = mRadarRadius / (mRadarCount - 1)
        CofferLog.D(TAG, "gap : $gap")
        // 绘制5个六边形，即绘制5次，中心不用绘制
        for (i in 1 until mRadarCount) {
            // 当前半径
            val curR = gap * i
            // 重置路径
            path.reset()

            // 从最里层的开始绘制，绘制一个六边形需要绘制6次直线
            for (j in 0 until mRadarCount) {
                if (j == 0) {
                    // 这里是绘制的起点
                    path.moveTo(mRadarCenterX + curR, mRadarCenterY.toFloat())
                } else {
                    // 顺时针依次绘制剩下的6条线段，这里需要用到三角函数，计算剩下的5个点的坐标
                    val x =
                        (mRadarCenterX + curR * Math.cos((mRadarAngle * j).toDouble())).toFloat()
                    val y =
                        (mRadarCenterY + curR * Math.sin((mRadarAngle * j).toDouble())).toFloat()
                    path.lineTo(x, y)
                }
            }
            // 连接最后一条线，形成一个环状
            path.close()
            canvas.drawPath(path, mRadarPaint)
        }
    }

    /**
     * 绘制雷网格中线
     *
     * @param canvas
     */
    private fun drawRadarLine(canvas: Canvas) {
        val path = Path()
        for (i in 0 until mRadarCount) {
            path.reset()
            path.moveTo(mRadarCenterX.toFloat(), mRadarCenterY.toFloat())
            val x =
                (mRadarCenterX + mRadarRadius * Math.cos((mRadarAngle * i).toDouble())).toFloat()
            val y =
                (mRadarCenterY + mRadarRadius * Math.sin((mRadarAngle * i).toDouble())).toFloat()
            path.lineTo(x, y)
            canvas.drawPath(path, mRadarPaint)
        }
    }

    /**
     * 绘制雷达数据区域
     *
     * @param canvas
     */
    private fun drawRadarRegion(canvas: Canvas) {
        val path = Path()
        for (i in 0 until mRadarCount) {
            // 计算数据值的比重
            val percent = (mRadarData[i] / mRadarMaxValue).toDouble()
            // 计算每一个点的坐标, 这个和计算绘制蜘蛛网的点坐标差不多，只是需要加上占比值
            val x =
                (mRadarCenterX + mRadarRadius * Math.cos((mRadarAngle * i).toDouble()) * percent).toFloat()
            val y =
                (mRadarCenterY + mRadarRadius * Math.sin((mRadarAngle * i).toDouble()) * percent).toFloat()
            if (i == 0) {
                path.moveTo(x, mRadarCenterY.toFloat())
            } else {
                path.lineTo(x, y)
            }
            // 绘制数据所在位置的小圆点
            mRadarDataPaint.alpha = 255
            canvas.drawCircle(x, y, 10f, mRadarDataPaint)
        }
        // 绘制填充区域
        mRadarDataPaint.strokeWidth = 10f
        mRadarDataPaint.alpha = 127
        mRadarDataPaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(path, mRadarDataPaint)
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private fun drawText(canvas: Canvas) {
        // 默认填充
//        drawStyleText(canvas, null, null, 100, false);
        // 描边样式
//        drawStyleText(canvas, Paint.Style.STROKE, Paint.Align.CENTER, 200, true);
        // 描边且填充
//        drawStyleText(canvas, Paint.Style.FILL_AND_STROKE, Paint.Align.RIGHT, 300, false);
        // 根据路径绘制文字
//        drawPathText(canvas);
        // 在文字下绘制下划线，这里考察文字的测量
//        drawTextLine(canvas);
        // 在文字上绘制一个矩形，这里考察文字的测量
//        drawTextRect(canvas);
    }

    /**
     * 绘制文字样式、对齐、下划线
     *
     * @param canvas
     * @param style  文字样式
     * @param align  文字对齐方式
     * @param line   是否需要下划线
     * @param top
     */
    private fun drawStyleText(
        canvas: Canvas, style: Paint.Style?, align: Paint.Align?,
        top: Float, line: Boolean
    ) {
        val paint = Paint()
        paint.color = Color.DKGRAY
        paint.isAntiAlias = true
        paint.strokeWidth = 3f
        paint.textSize = spToPixel(context, 30).toFloat()
        // 设置文字样式
        if (style != null) {
            paint.style = style
        }
        if (align != null) {
            paint.textAlign = align
        }
        if (line) {
            // 设置下划线
            paint.isUnderlineText = true
        } else {
            // 设置删除线，很像价格
            paint.isStrikeThruText = true
        }
        // 绘制文字
        canvas.drawText("哈哈", 300f, top, paint)
    }


    /**
     * 根据路径绘制文字
     *
     * @param canvas
     */
    private fun drawPathText(canvas: Canvas) {
        // 无偏移
        drawCirclePathText(canvas, 100f, 200f, 0f, 0f)
        // 正向水平偏移
        drawCirclePathText(canvas, 350f, 200f, 20f, 0f)
        // 反向水平偏移
        drawCirclePathText(canvas, 600f, 200f, -20f, 0f)
        // 正向垂直偏移
        drawCirclePathText(canvas, 200f, 500f, 0f, 30f)
        // 反向垂直偏移
        drawCirclePathText(canvas, 500f, 500f, 0f, -20f)
    }

    /**
     * 在圆形上绘制文字
     *
     * @param canvas
     * @param centerX          圆心的横坐标
     * @param centerY          圆心的纵坐标
     * @param horizontalOffset 水平方向上的偏移值
     * @param verticalOffset   垂直方向上的偏移值
     */
    private fun drawCirclePathText(
        canvas: Canvas, centerX: Float, centerY: Float,
        horizontalOffset: Float, verticalOffset: Float
    ) {

        val paint = Paint()
        paint.textSize = spToPixel(getContext(), 12).toFloat()
        paint.color = Color.BLUE
        paint.alpha = 127
        paint.isAntiAlias = true

        val path = Path()
        path.addCircle(centerX, centerY, 80f, Path.Direction.CW)
        canvas.drawPath(path, paint)

        // 在路径上绘制文字
        canvas.drawTextOnPath("哈哈", path, horizontalOffset, verticalOffset, paint)
    }

    /**
     * 在文字下绘制下划线，这里考察文字的测量
     *
     * @param canvas
     */
    private fun drawTextLine(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        paint.textSize = spToPixel(context, 16).toFloat()
        canvas.drawText("哈哈哈", mRadarCenterX.toFloat(), mRadarCenterY.toFloat(), paint)
        val textWidth = paint.measureText("哈哈哈").toInt()
        CofferLog.D(TAG, "textWidth : $textWidth")
        // 绘制下划线
        val paintLine = Paint()
        paintLine.color = Color.BLUE
        paintLine.strokeWidth = 10f
        paintLine.style = Paint.Style.STROKE
        canvas.drawLine(
            mRadarCenterX.toFloat(),
            (mRadarCenterY + 40).toFloat(),
            (mRadarCenterX + textWidth).toFloat(),
            (
                    mRadarCenterY + 40).toFloat(),
            paintLine
        )
    }


    /**
     * 在文字上绘制一个矩形，这里考察文字的测量
     *
     * @param canvas
     */
    private fun drawTextRect(canvas: Canvas) {
        val content = "哈哈哈"
        val paint = Paint()
        paint.color = Color.RED
        paint.textSize = spToPixel(context, 16).toFloat()
        canvas.drawText(content, mRadarCenterX.toFloat(), mRadarCenterY.toFloat(), paint)

        // 文字区域
        val rect = Rect()
        // 使用getTextBounds 获取文字的大小，这里需要转换成对应的位置坐标
        paint.getTextBounds(content, 0, content.length, rect)
        rect.left = mRadarCenterX - rect.left
        rect.top = mRadarCenterY - rect.bottom - rect.top
        rect.right = mRadarCenterX + rect.right + 10
        rect.bottom = mRadarCenterY + rect.bottom + 10
        // 绘制矩形
        val paintRect = Paint()
        paintRect.color = Color.BLUE
        paintRect.strokeWidth = 5f
        paintRect.style = Paint.Style.STROKE
        canvas.drawRect(rect, paintRect)
    }

    /**
     * 绘制区域
     *
     * @param canvas
     */
    private fun drawRegion(canvas: Canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        // 创建一个正方形
//        Rect rect = new Rect(600,100,700,150);
//        // 创建一个区域
//        Region region = new Region(600,100,650,300);
//        // 合并区域
//        region.union(rect);
//        drawRegionImpl(canvas,region,paint);

        // 区域裁剪
//        drawRegionClip(canvas);
        // 区域的集合运算
        drawRegionOp(canvas)
    }

    private fun drawRegionClip(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED

        // 椭圆路径
        val ovalPath = Path()
        // 设定椭圆范围
        val rect = RectF()
        // 添加椭圆，这里使用逆时针
        ovalPath.addOval(rect, Path.Direction.CCW)
        // 原始区域
        val originRegion = Region()
        // 设置裁剪区域
        val clip = Region(400, 50, 550, 200)
        // 设置裁剪路径，setPath 方法是将两个区域相交的部分保留
        originRegion.setPath(ovalPath, clip)
        // 绘制区域
        drawRegionImpl(canvas, originRegion, paint)

    }

    /**
     * 区域集合运算，即两个区域的交集、并集、异或、替换等
     *
     * @param canvas
     */
    private fun drawRegionOp(canvas: Canvas) {
        // 补集
        drawRegionOpImpl(canvas, Region.Op.DIFFERENCE, 100, 300)
        // 反转补集
        drawRegionOpImpl(canvas, Region.Op.REVERSE_DIFFERENCE, 300, 300)
        // 交集
        drawRegionOpImpl(canvas, Region.Op.INTERSECT, 500, 300)
        // 并集
        drawRegionOpImpl(canvas, Region.Op.UNION, 100, 600)
        // 异或
        drawRegionOpImpl(canvas, Region.Op.XOR, 300, 600)
        // 替换原有区域
        drawRegionOpImpl(canvas, Region.Op.REPLACE, 500, 600)
    }

    /**
     * 区域集合运算实现
     *
     * @param canvas
     * @param op     运算模式
     * @param left
     * @param top
     */
    private fun drawRegionOpImpl(canvas: Canvas, op: Region.Op, left: Int, top: Int) {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        // 绘制黄色的矩形
        val yellowRect = Rect(left, top, left + 50, top + 150)
        paint.color = Color.BLUE
        canvas.drawRect(yellowRect, paint)

        // 绘制绿色的矩形
        val greenRect = Rect(left - 50, top + 50, left + 100, top + 100)
        paint.color = Color.GREEN
        canvas.drawRect(greenRect, paint)

        // 进行区域集合运算
        val yellowRegion = Region(yellowRect)
        val greenRegion = Region(greenRect)
        yellowRegion.op(greenRegion, op)

        // 绘制运算结果
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        drawRegionImpl(canvas, yellowRegion, paint)
    }

    /**
     * 绘制区域的实现
     */
    private fun drawRegionImpl(canvas: Canvas, region: Region, paint: Paint) {
        val iterator = RegionIterator(region)
        val r = Rect()
        while (iterator.next(r)) {
            canvas.drawRect(r, paint)
        }
    }

    private fun drawCanvas(canvas: Canvas) {
        // 画布平移
//        drawCanvasTranslate(canvas);
        // 画布的裁剪
//        drawCanvasClip(canvas);
        // 画布的保存和恢复
        drawCanvasSaveRestore(canvas)
    }

    /**
     * 画布平移
     * <p>
     * 注意：
     * 1、生成新的图层：
     * 每次调用绘制方法 drawXXX 时，都会产生一个新的 Canvas 透明图层。
     * 2、操作不可逆：
     * 调用了绘制方法前，平移和旋转等函数对 Canvas 进行了操作，那么这个操作是不可逆的，
     * 每次产生的画布的最新位置都是这些操作后的位置。
     * 3、超出不显示：
     * 在 Canvas 图层与屏幕合成时，超出屏幕范围的图像是不会显示出来的。
     *
     * @param canvas
     */
    private fun drawCanvasTranslate(canvas: Canvas) {
        // 绘制一个绿色的矩形
        drawRectImpl(canvas, Color.GREEN)
        // 平移画布。当我们绘制红色矩形时，会产生另一个新的 Canvas 透明图层，此时画布坐标改变了
        // 由于 Canvas 已经平移了 350 像素，所以画图时是以新原点来产生视图的，然后再合成到屏幕上。
        canvas.translate(350f, 350f)
        // 绘制一个红色的矩形
        drawRectImpl(canvas, Color.RED)
    }

    private fun drawRectImpl(canvas: Canvas, color: Int) {
        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
        val rect = Rect(50, 50, 300, 300)
        canvas.drawRect(rect, paint)
    }

    /**
     * 画布的裁剪
     *
     * @param canvas
     */
    private fun drawCanvasClip(canvas: Canvas) {
        canvas.drawColor(Color.RED)
        // 设置裁剪范围
        val rect = Rect(100, 100, 200, 200)
        // 裁剪
        canvas.clipRect(rect)
        // 裁剪后
        canvas.drawColor(Color.GREEN)
    }

    /**
     * 画布的保存和恢复
     *
     *
     * 由于画布的操作是不可逆的，画布状态改变后又影响了后面的绘制效果。
     * 因此，画布提供了保存和恢复功能，这两个功能对应的方法是 save() 和 restore()，每调用一次 save() 就会把当前画布状态保存到栈中。
     *
     * @param canvas
     */
    private fun drawCanvasSaveRestore(canvas: Canvas) {
        // 1、绘制红色全屏
        canvas.drawColor(Color.RED)
        // 记录当前图层的位置
        val canvasState1 = canvas.save()
        // 2、绘制绿色700
        canvas.clipRect(RectF(100f, 100f, 700f, 700f))
        canvas.drawColor(Color.GREEN)
        val canvasState2 = canvas.save()
        // 3、绘制蓝色400
        canvas.clipRect(RectF(200f, 200f, 600f, 600f))
        canvas.drawColor(Color.BLUE)
        val canvasState3 = canvas.save()
        // 4、绘制黑色200
        canvas.clipRect(RectF(300f, 300f, 500f, 500f))
        canvas.drawColor(Color.BLACK)
        val canvasState4 = canvas.save()
        // 5、绘制白色100
        canvas.clipRect(RectF(350f, 350f, 450f, 450f))
        canvas.drawColor(Color.WHITE)
        val canvasState5 = canvas.save()

        // 两次出栈，相当于白色、黑色失效,使用黄色覆盖到黑色失效区域
//        canvas.restore();
//        canvas.restore();
//        canvas.drawColor(Color.YELLOW);

        // 指定到蓝色出栈，相当于执行了3次canvas.restore
        canvas.restoreToCount(canvasState3)
        canvas.drawColor(Color.YELLOW)
    }


    fun drawCircleImage(canvas: Canvas) {
        // 裁剪, clipPath 是有锯齿的
//        canvas.clipPath(mmCirclePath);
//        canvas.drawBitmap(mCircleBitmap,100,100,mCirclePaint);

        // 可以用 PorterDuffXfermode 来实现无锯齿的圆形图片
        val bitmapWidth = mCircleBitmap.width
        // 1、设置源范围
        val src = Rect(0, 0, bitmapWidth, bitmapWidth)
        // 2、设置目标范围
        val dst = Rect(0, 0, bitmapWidth, bitmapWidth)
        // 3、绘制一个空白图片
        val outBitmap = Bitmap.createBitmap(bitmapWidth, bitmapWidth, Bitmap.Config.RGB_565)
        // 4、设置空画布
        val canvas1 = Canvas(outBitmap)
        val xferPaint = Paint()
        // 5、绘制圆形
        val radius = bitmapWidth / 2
        canvas1.drawRoundRect(RectF(dst), radius.toFloat(), radius.toFloat(), xferPaint)
        // 6、设置模式
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        // 7、进行混合
        canvas1.drawBitmap(mCircleBitmap, src, dst, xferPaint)
        // 8、绘制结果
        canvas.drawBitmap(outBitmap, 0f, 0f, mCirclePaint)
        // 9、清除模式
        xferPaint.xfermode = null

    }
}
