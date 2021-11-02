package com.kolibreath.timetableapp.schedule.addschedule

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.kolibreath.timetableapp.dp2px
import com.kolibreath.timetableapp.getScreenWidth

/**
 * 选择自定义课程上课的周数，View布局逻辑类似于WeekSelectView
 */
class WeekPicker(
    private val contxt: Context,
    private val attrSet: AttributeSet
): LinearLayout(contxt, attrSet) {

    private val weekTextViews: Array<TextView> = Array(21){TextView(contxt)}
    // WeekSelectView 左右边距
    private val horMargin = contxt.dp2px(16)
    // WeekSelectView中间的每一个View的边距
    private val innerViewMargin = contxt.dp2px(12).toInt()
    private val innerViewHeight = (contxt.getScreenWidth() - horMargin * 2) / 7 - innerViewMargin * 2
    private val innerViewWidth = innerViewHeight

    private var onCancelClickListener: OnClickListener = OnClickListener { }
    private var onConfirmClickListener: OnClickListener =  OnClickListener { }

    // 当前设置中被选中的周 默认选中所有周数，点击则取消改周被选中
    var selectedWeeks = ArrayList((1..21).toList())

    init {
        orientation = VERTICAL
        initTitleTextView(this)
        initGridLayout(this)

        val weekListeners = arrayOf(
            OnClickListener { setSelectedWeeks(ArrayList((1..21).toList())) } ,
            OnClickListener { setSelectOddWeeks() },
            OnClickListener { setSelectEvenWeeks() }
        )

        // 全选 单周 双周
        initButtons(this, arrayOf("全选", "单周", "双周"), Color.BLUE, weekListeners)

        val actionListeners = arrayOf(onCancelClickListener, onConfirmClickListener)

        // 确定或取消
        initButtons(this, arrayOf("取消", "确定"), Color.BLACK, actionListeners)
    }


    internal fun setOnCancelClickListener(listener: OnClickListener) {
        val linearLayout = getChildAt(3) as ViewGroup
        val cancelButton = linearLayout.getChildAt(0)
        cancelButton.setOnClickListener(listener)
    }

    internal fun setOnConfirmClickListener(listener: OnClickListener) {
        val linearLayout = getChildAt(3) as ViewGroup
        val confirmButton = linearLayout.getChildAt(1)
        confirmButton.setOnClickListener(listener)
    }

    // 初始化Title
    private fun initTitleTextView(rootView: LinearLayout){
        val title = TextView(contxt)
        title.text = "选择周数"
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.BLACK)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER_HORIZONTAL
        rootView.addView(title, params)
    }

    // 初始化GridLayout 其中包含所有可以选择的周数
    private fun initGridLayout(rootView: LinearLayout){
        val gridLayout = GridLayout(contxt).apply {
            columnCount = 7
            rowCount = 3
            // 设置GridLayout的左右padding
            setPadding(contxt.dp2px(16).toInt(), 0, contxt.dp2px(16).toInt(), 0)
            // TODO 统一颜色规范
            setBackgroundColor(Color.WHITE)
        }

        // 初始化每一个View的位置 颜色 文字
        for(i in weekTextViews.indices) {
            val textView = weekTextViews[i]
            textView.text = "${i + 1}"
            textView.setTextColor(Color.WHITE)
            textView.gravity = Gravity.CENTER

            gridLayout.addView(textView, innerViewHeight.toInt(), innerViewHeight.toInt())

            val gridLayoutParams = textView.layoutParams as GridLayout.LayoutParams
            gridLayoutParams.leftMargin = innerViewMargin
            gridLayoutParams.rightMargin = innerViewMargin
            gridLayoutParams.topMargin = innerViewMargin
            gridLayoutParams.bottomMargin = innerViewMargin

            // 如果当前选中的周数没有当前的数字，就加入，并且取消选中的的背景色
            textView.setOnClickListener {
                val curTextView = it as TextView
                // Case#1 如果已经被选中
                if (selectedWeeks.contains(i+1)) {
                    unselectView(curTextView)
                    selectedWeeks.remove(i+1)
                }else {
                    // Case#2 如果没有选中
                    // TODO a background drawable is needed!
                    selectView(curTextView)
                    selectedWeeks.add(i+1)
                }
            }
        }

        // 初始选中所有的周
        setSelectedWeeks(selectedWeeks)

        rootView.addView(gridLayout)
    }

    private fun initButtons(
        rootView: LinearLayout,
        texts: Array<String>, color: Int,
        listeners: Array<OnClickListener>
    ) {
        val linearLayout = LinearLayout(contxt).apply {
            orientation = HORIZONTAL
        }
        val linearLayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        fun setUpTextViews(): ArrayList<TextView> {
            val arrayList = ArrayList<TextView>()
            for((i, text) in texts.withIndex()) {
                arrayList.add(TextView(contxt).apply {
                    this.text = text
                    this.setTextColor(color)
                    gravity = Gravity.CENTER
                    this.setOnClickListener(listeners[i])
                })
            }
            return arrayList
        }


        // 共用的params
        val textParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
        // 添加三个按钮
        setUpTextViews().forEach { linearLayout.addView(it, textParams)}

        rootView.addView(linearLayout, linearLayoutParams)
    }


    // 当前的View被选中的字体和颜色切换
    private fun selectView(view: TextView) {
        view.setBackgroundColor(Color.BLUE)
        view.setTextColor(Color.WHITE)
    }

    // 当前View取消选中的字体和颜色切换
    private fun unselectView(view: TextView) {
        view.setBackgroundColor(Color.WHITE)
        view.setTextColor(Color.BLACK)
    }


    /**
     * 将当前选中的周的背景View设置颜色
     * @param weeks 当前选中的周
     */
    internal fun setSelectedWeeks(weeks: ArrayList<Int>) {
        // 重置所有的View
        weekTextViews.forEach { unselectView(it) }
        this.selectedWeeks = weeks
        weeks.forEach {
            selectView(weekTextViews[it-1])
        }
    }

    // 设置单周
    internal fun setSelectOddWeeks(){
        val oddWeeks = ArrayList<Int>()
        for (i in 1.. 21 step 2) oddWeeks.add(i)
        this.selectedWeeks = oddWeeks
        setSelectedWeeks(this.selectedWeeks)
    }

    // 设置双周
    internal fun setSelectEvenWeeks(){
        val evenWeeks = ArrayList<Int>()
        for (i in 2.. 21 step 2) evenWeeks.add(i)
        this.selectedWeeks = evenWeeks
        setSelectedWeeks(this.selectedWeeks)
    }



}