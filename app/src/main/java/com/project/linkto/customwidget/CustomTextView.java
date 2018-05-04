package com.project.linkto.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.project.linkto.R;


/**
 * Created by Amal off 14/04/2015.
 */


public class CustomTextView extends TextView{

    public String ctTextFont = null;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context) {
        super(context);

    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);

        ctTextFont = a.getString(R.styleable.CustomTextView_ctTextFont);
        if (ctTextFont != null) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    ("fonts/" + ctTextFont + ".ttf").replace(" ",""));
            setTypeface(tf);
        }

        // Don't forget this
        a.recycle();
    }

    public void setCustomFont(String ctTextFont_) {
        ctTextFont = ctTextFont_;
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/" + ctTextFont + ".ttf");
        setTypeface(tf);
    }


}
