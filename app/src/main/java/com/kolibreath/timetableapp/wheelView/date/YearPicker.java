package com.kolibreath.timetableapp.wheelView.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.kolibreath.timetableapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YearPicker extends WheelPicker<Integer> {

    private int mStartYear, mEndYear;
    private int mSelectedYear;
    private OnYearSelectedListener mOnYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 通过构造函数构建当前Picker，主要分成以下几个步骤：
     * 1. initAttrs 读取xml文件中的设置
     * 2. setItemMaximumWidthText 设置当前显示的每一个Item的宽度，通过字符串设置，如2020可以表示为四个字符的字符串长
     * 3. updateYear 更新数据，并且调用父类WheelPicker的方法将内容填充进去
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public YearPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setItemMaximumWidthText("0000");
        updateYear();
        setSelectedYear(mSelectedYear, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                mSelectedYear = item;
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearSelected(item);
                }
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker);
        mStartYear = a.getInteger(R.styleable.YearPicker_startYear, 1900);
        mEndYear = a.getInteger(R.styleable.YearPicker_endYear, 2100);
        a.recycle();

    }

    private void updateYear() {
        List<Integer> list = new ArrayList<>();
        for (int i = mStartYear; i <= mEndYear; i++) list.add(i);
        setDataList(list);
    }

    public void setStartYear(int startYear) {
        mStartYear = startYear;
        updateYear();
        setSelectedYear(Math.max(mStartYear, mSelectedYear), false);
    }

    public void setEndYear(int endYear) {
        mEndYear = endYear;
        updateYear();
        setSelectedYear(Math.max(mEndYear, mSelectedYear), false);
    }

    public void setYear(int startYear, int endYear) {
        setStartYear(startYear);
        setEndYear(endYear);
    }

    public void setSelectedYear(int selectedYear) {
        setSelectedYear(selectedYear, true);
    }

    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        setCurrentPosition(selectedYear - mStartYear, smoothScroll);
    }

    public int getSelectedYear() {
        return mSelectedYear;
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }

}
