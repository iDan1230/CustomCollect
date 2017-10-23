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



        lineChart.setFill(false)
                .setBezier(true)
                .setChartLine(true)
                .setGridLine(false)
                .setHorizontalLine(true)
                .setVerticalLine(true)
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
