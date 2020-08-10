package com.gnet.plugin.echart;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 数据载体
 * @author cwledit
 *
 */
public class Series implements Serializable {
	
	private static final long serialVersionUID = 3905115550538893290L;

	/**
     * 系列名称，如启用legend，该值将被legend.data索引相关 
     */
    private String name; //与legend对应
    
    /**
     * 图表类型，必要参数！如为空或不支持类型，则该系列数据不被显示。可选为：
     * 'line'（折线图） | 'bar'（柱状图） | 'scatter'（散点图） | 'k'（K线图）
     * 'pie'（饼图） | 'radar'（雷达图） | 'chord'（和弦图） | 'force'（力导向布局图） | 'map'（地图） 
     */
    private String type;
    
    /**
     * 系列中的数据内容数组，折线图以及柱状图时数组长度等于所使用类目轴文本标签数组axis.data的长度，并且他们间是一一对应的。数组项通常为数值，如:
     * [12, 34, 56, ..., 10, 23]
     * 当某类目对应数据不存在时（ps：'不存在' 不代表值为 0），可用'-'表示，无数据在折线图中表现为折线在该点断开，在柱状图中表现为该点无柱形，如：
     * [12, '-', 56, ..., 10, 23](废弃该注释)
     */
    private List<BigDecimal> data;//这里要用数字类型 不能用String 不然前台显示不正常（特别是在做数学运算的时候）
    
    public Series() {}
    
    public Series(String type, List<BigDecimal> data) {
    	this.type = type;
        this.data = data;
    }
    
    public Series(String name, String type, List<BigDecimal> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<BigDecimal> getData() {
		return data;
	}

	public void setData(List<BigDecimal> data) {
		this.data = data;
	}

}