package uk.co.transferx.app.ui.mainscreen.adapters

import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * Created by sergey on 13/04/2018.
 */

abstract class SwipeHelper(private var recyclerView: RecyclerView?) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val textSize: Int
    private val buttonWidth: Int
    private var buttons: MutableList<SwipeHelper.UnderlayButton>? = null
    private lateinit var gestureDetector: GestureDetector
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private val buttonsBuffer: MutableMap<Int, MutableList<UnderlayButton>?>
    private lateinit var recoverQueue: Queue<Int>
    private val displayMetrics: DisplayMetrics? = recyclerView?.context?.resources?.displayMetrics
    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons!!) {
                if (button.onClick(e.x, e.y))
                    break
            }
            return true
        }
    }

    private val onTouchListener = View.OnTouchListener { view, e ->
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(e.rawX.toInt(), e.rawY.toInt())

        val swipedViewHolder = recyclerView?.findViewHolderForAdapterPosition(swipedPos)
        val swipedItem = swipedViewHolder?.itemView ?: return@OnTouchListener false

        val rect = Rect()
        swipedItem.getGlobalVisibleRect(rect)

        if (e.action == MotionEvent.ACTION_DOWN || e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y && rect.bottom > point.y)
                gestureDetector.onTouchEvent(e)
            else {
                recoverQueue.add(swipedPos)
                swipedPos = -1
                recoverSwipedItem()
            }
        }
        false
    }

    init {
        buttonWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, BUTTON_WITH_DP.toFloat(), displayMetrics
        ).toInt()
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            TEXT_SIZE_SP.toFloat(),
            displayMetrics
        ).toInt()
        this.buttons = ArrayList()
        this.gestureDetector = GestureDetector(recyclerView?.context, gestureListener)
        this.recyclerView?.setOnTouchListener(onTouchListener)
        buttonsBuffer = HashMap()
        recoverQueue = object : LinkedList<Int>() {
            override fun add(o: Int): Boolean {
                return if (contains(o))
                    false
                else
                    super.add(o)
            }
        }

        attachSwipe()
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition

        if (swipedPos != pos)
            recoverQueue.add(swipedPos)

        swipedPos = pos

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer[swipedPos]
        else
            buttons?.clear()

        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons!!.size.toFloat() * buttonWidth.toFloat()
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView

        //    if (pos < 0) {
        //       swipedPos = pos;
        //       return;
        //    }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<UnderlayButton>? = ArrayList()

                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer[pos] = buffer
                } else {
                    buffer = buttonsBuffer[pos]
                }

                translationX = dX * buffer?.size!!.toFloat() * buttonWidth.toFloat() /
                        itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val pos = recoverQueue.poll()
            if (pos > -1) {
                recyclerView!!.adapter.notifyItemChanged(pos)
            }
        }
    }

    private fun drawButtons(
        canvas: Canvas,
        itemView: View,
        buffer: MutableList<UnderlayButton>?,
        pos: Int,
        dX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * dX / buffer!!.size

        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                canvas,
                RectF(
                    left,
                    itemView.top.toFloat(),
                    right,
                    itemView.bottom.toFloat()
                ),
                pos
            )
            right = left
        }
    }

    fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder,
        underlayButtons: MutableList<UnderlayButton>?
    )

    inner class UnderlayButton(
        private val text: String,
        private val color: Int,
        private val clickListener: UnderlayButtonClickListener
    ) {
        private var pos: Int = 0
        private var clickRegion: RectF? = null
        private val rectText = Rect()
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun onClick(x: Float, y: Float): Boolean {
            if (clickRegion != null && clickRegion!!.contains(x, y)) {
                clickListener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(canvas: Canvas, rect: RectF, pos: Int) {
            // Draw background
            paint.color = color
            canvas.drawRect(rect, paint)
            // Draw Text
            paint.color = Color.WHITE
            paint.textSize = textSize.toFloat()
            val cWidth = rect.width()
            val cHeight = rect.height()
            paint.textAlign = Paint.Align.LEFT
            paint.getTextBounds(text, 0, text.length, rectText)
            val x = cWidth / 2f - rectText.width() / 2f - rectText.left.toFloat()
            val y = cHeight / 2f + rectText.height() / 2f - rectText.bottom
            canvas.drawText(text, rect.left + x, rect.top + y, paint)
            clickRegion = rect
            this.pos = pos
        }
    }

    interface UnderlayButtonClickListener {
        fun onClick(pos: Int)
    }

    companion object {
        private val BUTTON_WITH_DP = 46
        private val TEXT_SIZE_SP = 7
    }

}
