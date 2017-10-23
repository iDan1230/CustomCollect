# CustomCollect
一、LineCharts : 左右惯性滑动</br></br>
1.贝塞尔曲线 + 渐变实心</br></br>
![image](https://github.com/yangzhidan/CustomCollect/blob/master/art/bezierFill.png)</br></br>
2.贝塞尔曲线 + 网格背景</br></br>
![image](https://github.com/yangzhidan/CustomCollect/blob/master/art/bezier.png)</br></br>
3.贝塞尔曲线 + 无背景 </br></br>
![image](https://github.com/yangzhidan/CustomCollect/blob/master/art/noGridLine.png)</br></br>
4.折线图 </br></br>
![image](https://github.com/yangzhidan/CustomCollect/blob/master/art/brokenLine.png)</br></br>
lineChart.setFill(false)//默认true,false不绘制实心</br>
                .setBezier(true)//默认true（贝塞尔曲线）,false绘制普通折线</br>
                .setChartLine(false)//绘制折线默认true，false:只有原点</br>
                .setGridLine(false)//默认true，false不会只网格背景</br>
                .setHorizontalLine(true)//默认true,false不会只横向坐标线</br>
                .setVerticalLine(true)//默认true,false不会只纵向坐标线</br>
                .setDatas(lineDatas);//数据源</br>
