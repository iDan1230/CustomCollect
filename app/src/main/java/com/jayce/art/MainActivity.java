package com.jayce.art;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jayce.art.view.CircleView;
import com.jayce.art.view.LineChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private CircleView circleView;

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = findViewById(R.id.circle_view);

        lineChart = findViewById(R.id.line_chart);

        List<Float> lineDatas = new ArrayList<>();
        //测试数据
        lineDatas = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            lineDatas.add(1f);
        }
        for (int i = 0; i < 20; i++) {
            lineDatas.add((float) Math.random());
        }


        lineChart.setFill(true)
                .setBezier(true)
                .setDatas(lineDatas);


        Log.e("TAG", "onCreate Width: " + circleView.getMeasuredWidth() + " Height: " + circleView.getMeasuredHeight());


    }


    @Override
    protected void onStart() {
        super.onStart();


        Log.e("TAG", "onStart Width: " + circleView.getMeasuredWidth() + " Height: " + circleView.getMeasuredHeight());

    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.e("TAG", "onResume Width: " + circleView.getMeasuredWidth() + " Height: " + circleView.getMeasuredHeight());

    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        circleView.getMeasuredWidth();
//
//        Log.e("TAG", "onWindow Width: " + circleView.getMeasuredWidth() + " Height: " + circleView.getMeasuredHeight());
//    }
}
