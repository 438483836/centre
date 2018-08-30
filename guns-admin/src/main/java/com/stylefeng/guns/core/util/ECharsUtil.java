package com.stylefeng.guns.core.util;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.data.PointData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**

* @Description:    返回前台echars的option

* @Author:         sipengfei

* @CreateDate:     2018/8/20 12:11

* @UpdateUser:     sipengfei

* @UpdateDate:     2018/8/20 12:11

* @UpdateRemark:   修改内容

* @Version:        1.0

*/
public class ECharsUtil {

    private static final Logger logger = LoggerFactory.getLogger(ECharsUtil.class);
     /**
     * @author Pengfei Si
     * @date 2018/8/20 11:02
     * @param title 标题
     * @param barName 名字
     * @param xAxis     X轴值
     * @param yAxis Y轴值
     * @return 折线图和柱状图
     */
    public static String getLineAndBarOption(String title,String barName, List<Object> xAxis,List<Object> yAxis){
        Option option = new Option();
        option.title().text(title);
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar, Magic.pie).show(true), Tool.restore, Tool.saveAsImage,Tool.dataZoom);
        //设置提示框
        option.tooltip().trigger(Trigger.axis);
        //是否设置拖拽计算
        option.calculable(true);
        option.legend();
        //设置x
        option.xAxis(new CategoryAxis().data(xAxis.toArray()));
        option.yAxis(new ValueAxis());
        //设置Bar
        option.setCalculable(false);
        Bar bar = new Bar(barName);
        bar.barWidth(10);
        bar.getItemStyle();
        bar.data(yAxis.toArray());
        option.series(bar);
        bar.markPoint().data(new PointData().type(MarkType.max).name("最大值"), new PointData().type(MarkType.min).name("最小值"));
        //bar.markLine().data(new PointData().type(MarkType.average).name("平均值"));

        Gson gson = new Gson();
        String json = gson.toJson(option);
        return json;
    }


    /**
     * @author Pengfei Si
     * @date 2018/8/20 11:02
     * @param title 标题
     * @param legendNames 饼状图区块名字
     * @param legendValues 饼状图区块值
     * @return
     */
    public static String getPieOption(String title, List<Object> legendNames,List<Object> legendValues){
        Option option = new Option();
        //时间轴
        //timeline变态的地方在于多个Option
        if (legendNames.size()!=legendValues.size()){
            logger.error("legendNames legendValues mismatching .legendNames.size[{}],legendValues.size[{}]",
                    legendNames.size(),legendValues.size());
            return null;
        }
        option.title().text(title);
        option.legend().data("暴力分拣", "人工补码", "百世推送异常口","分拣超时","无条码").setX("left");
        option.legend().setOrient(Orient.vertical);
        option.legend().setY("45");
        option.toolbox().
                show(true).feature(Tool.saveAsImage);
        option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        Pie pie = new Pie();
        List<PieData> pieDataList = new ArrayList<>();
        for (int i=0;i<legendNames.size();i++){
            pieDataList.add(new PieData((String) legendNames.get(i), legendValues.get(i)));
        }
        int size=pieDataList.size();
        PieData[] pieArray = pieDataList.toArray(new PieData[size]);
        pie.data(pieArray);
        option.series(pie);
        Gson gson = new Gson();
        String json = gson.toJson(option);
        return json;
    }
}
