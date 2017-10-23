package com.jayce.art;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jayce.art.view.RefreshLayout;

/**
 * 刷新控件测试活动
 */
public class RefreshActivity extends Activity {

    private RefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        refresh = findViewById(R.id.refresh);


        refresh.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.moveHideEnd();
                        Log.e("msg", "activity");
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {

            }
        });
    }
}
