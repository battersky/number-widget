package com.batter.simple.customizedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class NumberWidget extends RelativeLayout {

    private Button mNumberButton;
    private Button mDecreaseButton;
    private Button mIncreaseButton;
    private int mCount = 0;
    private List<NumberChangeListener> mListenerList = new ArrayList<NumberChangeListener>();

    public interface NumberChangeListener {
        public void onNumberChanged(int num);
    }

    public NumberWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.number_widget, this, true);

        TypedArray typedArray = context.getTheme().
                obtainStyledAttributes(attrs, R.styleable.NumberWidget, 0, 0);
        mCount = typedArray.getInteger(R.styleable.NumberWidget_defaultNumber, 0);
    }

    public void addNumberChangeListener(NumberChangeListener listener) {
        if (mListenerList != null) {
            mListenerList.add(listener);
        }
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int number) {
        mCount = number;
        expand(number);
    }

    public void expand(int number) {
        if (number > 0) {
            mDecreaseButton.setVisibility(View.VISIBLE);
            mNumberButton.setVisibility(View.VISIBLE);
            mNumberButton.setText(Integer.toString(mCount));
        }

    }

    public void collapse() {
        mNumberButton.setVisibility(View.INVISIBLE);
        mDecreaseButton.setVisibility(View.INVISIBLE);
    }

    protected void onFinishInflate () {
        super.onFinishInflate();
        mIncreaseButton = (Button)this.findViewById(R.id.BtnIncreaseButton);
        mDecreaseButton = (Button)this.findViewById(R.id.BtnDecreaseButton);
        mNumberButton = (Button)this.findViewById(R.id.BtnNumberButton);

        expand(this.mCount);

        if (mIncreaseButton != null) {
            mIncreaseButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    mCount ++;
                    expand(mCount);

                    if (mListenerList.size() != 0) {
                        for (int i = 0; i < mListenerList.size(); i++) {
                            mListenerList.get(i).onNumberChanged(1);
                        }
                    }
                }

            });
        }

        if (mDecreaseButton != null) {
            mDecreaseButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    mCount --;
                    if (mCount > 0) {
                        if (mNumberButton != null) {
                            mNumberButton.setText(Integer.toString(mCount));
                        }
                    } else {
                        collapse();
                    }

                    if (mListenerList.size() != 0) {
                        for (int i = 0; i < mListenerList.size(); i++) {
                            mListenerList.get(i).onNumberChanged(-1);
                        }
                    }
                }

            });
        }
    }
}
