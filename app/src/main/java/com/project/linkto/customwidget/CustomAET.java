package com.project.linkto.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.project.linkto.R;


public class CustomAET extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    //The image we are going to use for the Clear button
    private Drawable imgCloseButton = getResources().getDrawable(R.drawable.close_icon);
    private boolean touched = false;

    //   public CustomET(Context context) {
    //     super(context);
    //   init();;
    //}

    public CustomAET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomAET(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
        // Set bounds of the Clear button so it will look ok
        imgCloseButton.setBounds(-4, 0, imgCloseButton.getIntrinsicWidth(), imgCloseButton.getIntrinsicHeight());

        // There may be initial text in the field, so we may need to display the  button

        if (touched) {
            handleClearButton();
        }

        //if the Close image is displayed and the user remove his finger from the button, clear it. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CustomAET et = CustomAET.this;
                touched = true;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgCloseButton.getIntrinsicWidth()) {
                    et.setText("");
                    if (touched) {
                    CustomAET.this.handleClearButton();
                    }
                }
                return false;
            }
        });

        //if text changes, take care of the button
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (touched) {
                CustomAET.this.handleClearButton();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        // this


    }

    //intercept Typeface change and set it with our custom font
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            //  super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-B.ttf"));
            //super.setTypeface(tf);
        } else {
            //   super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-R.ttf"));
            //super.setTypeface(tf);
        }
    }

    void handleClearButton() {
        if (this.getText().toString().equals("") ) {
            // add the clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        } else {
            //remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], imgCloseButton, this.getCompoundDrawables()[3]);
        }
    }
}