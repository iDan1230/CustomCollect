package com.jayce.art.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayce.art.R;

/*
                  _ooOoo_
                 o8888888o
                 88" . "88
                 (| -_- |)
                 O\  =  /O
              ____/`---'\____
            .'  \\|     |//  `.
           /  \\|||  :  |||//  \
          /  _||||| -:- |||||-  \
          |   | \\\  -  /// |   |
          | \_|  ''\---/''  |   |
          \  .-\__  `-`  ___/-. /
        ___`. .'  /--.--\  `. . __
     ."" '<  `.___\_<|>_/___.'  >'"".
    | | :  `- \`.;`\ _ /`;.`/ - ` : | |
    \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                  `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            佛祖保佑       永无BUG
开发：杨智丹 on 2017/10/23 18:20
邮箱：yangzhidan@tederen.com
备注：
*/
public class RefreshHeadLayout extends RefreshLayout {
    private Context mContext;
    private ImageView imageView;
    private TextView tvTitle;

    public RefreshHeadLayout(Context context) {
        this(context, null);
    }

    public RefreshHeadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init() {
        addView(LayoutInflater.from(mContext).inflate(R.layout.refresh_head, null));
        imageView = findViewById(R.id.image_view);
        tvTitle = findViewById(R.id.tv_title);


    }

    public void setImageSrc(int img) {
        imageView.setImageResource(img);
    }
}
