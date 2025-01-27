package com.example.yiuhet.ktreader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yiuhet.ktreader.R;

/**
 * Created by yiuhet on 2017/6/3.
 */

public class MoreTextView extends LinearLayout {
    protected TextView contentView; //文本正文
    protected ImageView expandView; //展开按钮

    //对应styleable中的属性
    protected int textColor;
    protected float textSize;
    protected int maxLine;
    protected String text;
    protected float lineSpacingMultiplier;
    protected int lineSpacingExtra;
    //默认属性值
    public int defaultTextColor = Color.BLACK;
    public int defaultTextSize = 12;
    public int defaultLine = 3;



    public MoreTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initalize(); //初始化并添加View
        initWithAttrs(context, attrs);//取值并设置
        bindListener();//绑定点击事件

    }

    private void initalize() {
        setOrientation(VERTICAL); //设置垂直布局
        setGravity(Gravity.RIGHT); //右对齐
        //初始化textView并添加
        contentView = new TextView(getContext());
        addView(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //初始化ImageView并添加
        expandView = new ImageView(getContext());
        expandView.setImageResource(R.drawable.ic_down_arrow);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(expandView, linearParams);
    }

    private void initWithAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MoreTextStyle);
        textColor = a.getColor(R.styleable.MoreTextStyle_textColor,
                defaultTextColor); //取颜色值，默认defaultTextColor
        textSize = a.getDimensionPixelSize(R.styleable.MoreTextStyle_textSize, defaultTextSize);//取颜字体大小，默认defaultTextSize
        maxLine = a.getInt(R.styleable.MoreTextStyle_maxLine, defaultLine);//取颜显示行数，默认defaultLine
        text = a.getString(R.styleable.MoreTextStyle_text);//取文本内容
        lineSpacingExtra = a.getDimensionPixelSize(R.styleable.MoreTextStyle_lineSpacingExtra, 1);
        lineSpacingMultiplier = a.getFloat(R.styleable.MoreTextStyle_lineSpacingMultiplier,1f);
        //绑定到textView
        bindTextView(textColor,textSize,maxLine,text,lineSpacingExtra,lineSpacingMultiplier);

        a.recycle();//回收释放
    }
    //绑定到textView
    protected void bindTextView(int color,float size,final int line,String text,float lineSpacingExtra,float lineSpacingMultiplier){
        contentView.setTextColor(color);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size); //为sp
        contentView.setText(text);
        contentView.setHeight(contentView.getLineHeight() * line);
        contentView.setLineSpacing(lineSpacingExtra,lineSpacingMultiplier);

        post(new Runnable() {
            @Override
            public void run() {
                expandView.setVisibility(contentView.getLineCount() > line ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void bindListener() {
        setOnClickListener(new View.OnClickListener() {
            boolean isExpand;
            @Override
            public void onClick(View v) {
                if (contentView.getLineCount() > maxLine) {
                    isExpand = !isExpand;
                    contentView.clearAnimation();
                    final int deltaValue;
                    final int startValue = contentView.getHeight();
                    int durationMillis = 350;
                    if (isExpand) {
                        deltaValue = contentView.getLineHeight() * contentView.getLineCount() - startValue;
                        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(durationMillis);
                        animation.setFillAfter(true);
                        expandView.startAnimation(animation);
                    } else {
                        deltaValue = contentView.getLineHeight() * maxLine - startValue;
                        RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(durationMillis);
                        animation.setFillAfter(true);
                        expandView.startAnimation(animation);
                    }
                    Animation animation = new Animation() {
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            contentView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                        }

                    };
                    animation.setDuration(durationMillis);
                    contentView.startAnimation(animation);
                }
            }
        });
    }

    public void setText(String string){

        bindTextView(textColor,textSize,maxLine,string,lineSpacingExtra,lineSpacingMultiplier);
    }
}
