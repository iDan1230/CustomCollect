package com.jayce.art.view;

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
开发：杨智丹 on 2017/10/23 13:57
邮箱：yangzhidan@tederen.com
备注：
*/
public class ChartData {

    private float size;
    private String labelBottom;

    public ChartData(float size, String labelBottom) {
        this.size = size;
        this.labelBottom = labelBottom;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getLabelBottom() {
        return labelBottom;
    }

    public void setLabelBottom(String labelBottom) {
        this.labelBottom = labelBottom;
    }
}
