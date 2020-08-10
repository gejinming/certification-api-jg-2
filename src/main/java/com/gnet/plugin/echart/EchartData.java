package com.gnet.plugin.echart;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 详见官方文档：
 * http://echarts.baidu.com/doc/doc.html
 * @author cwledit
 *
 */
public class EchartData implements Serializable {

	private static final long serialVersionUID = 1226804253357098630L;
	
	private List<String> legend;// 数据分组
	private List<String> category;// 横坐标
	private List<Series> series;// 数据
    
	public EchartData() {
		legend = new ArrayList<String>();
		category = new ArrayList<String>();
		series = new ArrayList<Series>();
	}
	
	public EchartData(List<String> categoryList, List<Series> seriesList) {
		this();
		this.category = categoryList;
        this.series = seriesList;
	}
    
    public EchartData(List<String> legendList, List<String> categoryList, List<Series> seriesList) {
        this();
        this.legend = legendList;
        this.category = categoryList;
        this.series = seriesList;
    }

	public List<String> getLegend() {
		return legend;
	}

	public void setLegend(List<String> legend) {
		this.legend = legend;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
    
}