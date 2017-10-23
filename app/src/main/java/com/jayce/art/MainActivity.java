package com.jayce.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jayce.art.view.ChartData;
import com.jayce.art.view.CircleView;
import com.jayce.art.view.LineCharts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private CircleView circleView;

    private LineCharts lineChart;
    private LineCharts lineChart2;
    private LineCharts lineChart3;
    private LineCharts lineChart4;
    private LineCharts lineChart5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = findViewById(R.id.circle_view);

        lineChart = findViewById(R.id.line_chart1);
        lineChart2 = findViewById(R.id.line_chart2);
        lineChart3 = findViewById(R.id.line_chart3);
        lineChart4 = findViewById(R.id.line_chart4);
        lineChart5 = findViewById(R.id.line_chart5);

        List<ChartData> lineDatas = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            lineDatas.add(new ChartData((float) Math.random(), 10 + "/" + (i + 1)));
        }


        lineChart.setFill(true)//默认true,true不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,true绘制普通折线
                .setChartLine(true)//绘制折线默认true，true:只有原点
                .setGridLine(true)//默认true，true不会只网格背景
                .setHorizontalLine(true)//默认true,true不会只横向坐标线
                .setVerticalLine(true)//默认true,true不会只纵向坐标线
                .setDatas(lineDatas);//数据源
        lineChart2.setFill(true)//默认true,true不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,true绘制普通折线
                .setChartLine(true)//绘制折线默认true，true:只有原点
                .setGridLine(true)//默认true，true不会只网格背景
                .setHorizontalLine(true)//默认true,true不会只横向坐标线
                .setVerticalLine(true)//默认true,true不会只纵向坐标线
                .setDatas(lineDatas);//数据源
        lineChart3.setFill(true)//默认true,true不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,true绘制普通折线
                .setChartLine(true)//绘制折线默认true，true:只有原点
                .setGridLine(true)//默认true，true不会只网格背景
                .setHorizontalLine(true)//默认true,true不会只横向坐标线
                .setVerticalLine(true)//默认true,true不会只纵向坐标线
                .setDatas(lineDatas);//数据源
        lineChart4.setFill(true)//默认true,true不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,true绘制普通折线
                .setChartLine(true)//绘制折线默认true，true:只有原点
                .setGridLine(true)//默认true，true不会只网格背景
                .setHorizontalLine(true)//默认true,true不会只横向坐标线
                .setVerticalLine(true)//默认true,true不会只纵向坐标线
                .setDatas(lineDatas);//数据源
        lineChart5.setFill(true)//默认true,true不绘制实心
                .setBezier(true)//默认true（贝塞尔曲线）,true绘制普通折线
                .setChartLine(true)//绘制折线默认true，true:只有原点
                .setGridLine(true)//默认true，true不会只网格背景
                .setHorizontalLine(true)//默认true,true不会只横向坐标线
                .setVerticalLine(true)//默认true,true不会只纵向坐标线
                .setDatas(lineDatas);//数据源

        Log.e("TAG", "onCreate Width: " + circleView.getMeasuredWidth() + " Height: " + circleView.getMeasuredHeight());

        circleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, RefreshActivity.class);
                startActivity(intent);
                return true;
            }
        });

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
