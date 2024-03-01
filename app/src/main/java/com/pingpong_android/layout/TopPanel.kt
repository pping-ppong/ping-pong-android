package com.pingpong_android.layout

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.pingpong_android.R

open class TopPanel(context: Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {

    private var title : TextView
    private var leftBtn : ImageView
    private var rightBtn : ImageView

    init {
        val v = View.inflate(context, R.layout.layout_top_panel, this)
        title = v.findViewById(R.id.title)
        leftBtn = v.findViewById(R.id.left_btn)
        rightBtn = v.findViewById(R.id.right_btn)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TopPanel,
            0,0).apply {
                try {
                    val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TopPanel)
                    setTitle(getString(R.styleable.TopPanel_topPanelTitle))
                    setLeftBtn(getBoolean(R.styleable.TopPanel_topPanelBack, true), getBoolean(R.styleable.TopPanel_topPanelClose, false))
                    setRightBtn(typeArray.getDrawable(R.styleable.TopPanel_topPanelRightBtnImage))
                } finally {
                    recycle()
                }
        }
    }

    open fun setTitle(text: String?) {
        title.text = text
    }

    open fun setLeftBtn(isBack: Boolean, isClose: Boolean) {
        if (isBack)
            leftBtn.setImageResource(R.drawable.ic_back)
        else if (isClose)
            leftBtn.setImageResource(R.drawable.ic_close)
        else
            leftBtn.setImageResource(R.drawable.ic_back)
    }

    open fun setRightBtn(icon: Drawable?) {
        if(icon != null)
            rightBtn.setImageDrawable(icon)
    }

    open fun setLeftClickListener(listener: OnClickListener) {
        leftBtn.setOnClickListener(listener)
    }

    open fun setRightClickListener(listener: OnClickListener) {
        rightBtn.setOnClickListener(listener)
    }
}