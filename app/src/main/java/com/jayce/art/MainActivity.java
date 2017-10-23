package com.jayce.art;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jayce.art.view.ChartData;
import com.jayce.art.view.CircleView;
import com.jayce.art.view.LineCharts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private CircleView circleView;

    private LineCharts lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = findViewById(R.id.circle_view);

        lineChart = findViewById(R.id.line_chart);

        List<ChartData> lineDatas = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            lineDatas.add(new ChartData((float) Math.random(), 10 + "/" + (i + 1)));
        }



        lineChart.setFill(false)//默认true,false不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,false绘制普通折线
                .setChartLine(false)//绘制折线默认true，false:只有原点
                .setGridLine(false)//默认true，false不会只网格背景
                .setHorizontalLine(true)//默认true,false不会只横向坐标线
                .setVerticalLine(true)//默认true,false不会只纵向坐标线
                .setDatas(lineDatas);//数据源

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
