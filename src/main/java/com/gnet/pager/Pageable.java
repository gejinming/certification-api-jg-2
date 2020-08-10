package com.gnet.pager;

import java.io.Serializable;
import java.util.List;

import com.gnet.object.OrderType;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;

/**
 * 分页信息
 * 
 */
public class Pageable implements Serializable {

    private static final long serialVersionUID = -2036054238691644952L;

  	/** 默认页码 */
	private static final int DEFAULT_PAGE_NUMBER = 1;

	/** 默认每页记录数 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 最大每页记录数 */
	private static final int MAX_PAGE_SIZE = 1000;

	/** 页码 */
	private int pageNumber;

	/** 每页记录数 */
	private int pageSize;
  
	/** 搜索属性 */
	private String searchProperty;

	/** 搜索值 */
	private String searchValue;

	/** 排序属性 */
	private String orderProperty;

	/** 排序方向 */
	private String orderDirection;
	
	/** 是否分页 */
	private Boolean isPaging;
	
	/**
	 * 多个查询条件
	 */
	private List<Filter> filters = Lists.newArrayList();


	/**
	 * 初始化一个新创建的Pageable对象
	 */
	public Pageable(Controller controller) {
		this(
			controller.getParaToInt("pageNumber"), 
			controller.getParaToInt("pageSize"), 
			controller.getPara("searchProperty"), 
			controller.getPara("searchValue"),
			controller.getPara("orderProperty"),
			controller.getPara("orderDirection")
		);
	}
	
	public Pageable() {
		this(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
	}
	
	public Pageable(Integer pageNumber, Integer pageSize) {
		this(pageNumber, pageSize, null, null);
	}
	
	public Pageable(Integer pageNumber, Integer pageSize, String searchProperty, String searchValue) {
		this(pageNumber, pageSize, searchProperty, searchValue, null, null);
	}
	
	/**
	 * @editor SY 
	 * @version 编辑时间：2016年10月26日 上午11:13:55 
	 */
	public Pageable(Integer pageNumber, Integer pageSize, String searchProperty, String searchValue, String orderProperty, String orderDirection) {
		if(pageNumber == null && pageSize == null) {
			this.isPaging = Boolean.FALSE;
		} else {
			this.isPaging = Boolean.TRUE;
		}
		this.pageNumber = (pageNumber != null && pageNumber >= 1) ? pageNumber : DEFAULT_PAGE_NUMBER;
		this.pageSize = (pageSize != null && pageSize >= 1 && pageSize <= MAX_PAGE_SIZE) ? pageSize : DEFAULT_PAGE_SIZE;
		this.searchProperty = searchProperty;
		this.searchValue = searchValue;
		this.orderProperty = orderProperty;
		this.orderDirection = orderDirection;
	}
	
	/**
	 * 获取页码
	 * 
	 * @return 页码
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置页码
	 * 
	 * @param pageNumber
	 *            页码
	 */
	public void setPageNumber(int pageNumber) {
		if (pageNumber < 1) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		this.pageNumber = pageNumber;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 */
	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	/**
	 * 获取搜索属性
	 * 
	 * @return 搜索属性
	 */
	public String getSearchProperty() {
		return searchProperty;
	}

	/**
	 * 设置搜索属性
	 * 
	 * @param searchProperty
	 *            搜索属性
	 */
	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}

	/**
	 * 获取搜索值
	 * 
	 * @return 搜索值
	 */
	public String getSearchValue() {
		return searchValue;
	}

	/**
	 * 设置搜索值
	 * 
	 * @param searchValue
	 *            搜索值
	 */
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	/**
	 * 获取排序属性
	 * 
	 * @return 排序属性
	 */
	public String getOrderProperty() {
		return orderProperty;
	}

	/**
	 * 设置排序属性
	 * 
	 * @param orderProperty
	 *            排序属性
	 */
	public void setOrderProperty(String orderProperty) {
//		this.orderProperty = orderProperty;
		this.orderProperty = StrKit.isBlank(orderProperty) ? orderProperty : orderProperty.startsWith(OrderType.NUMBER_ORDER) ? StringUtils.substringAfter(orderProperty, OrderType.NUMBER_ORDER) : String.format("convert(%s using gbk )", orderProperty);
	}

	/**
	 * 获取排序方向
	 * 
	 * @return 排序方向
	 */
	public String getOrderDirection() {
		return orderDirection;
	}

	/**
	 * 设置排序方向
	 * 
	 * @param orderDirection
	 *            排序方向
	 */
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
	
	/**
	 * 获取是否已分页
	 * 
	 * @return 是否分页
	 */
	public Boolean isPaging() {
		return isPaging;
	}
	
	/**
	 * 设置是否分页
	 * 
	 * @param isPaging
	 *            是否分页
	 */
	public void setPaging(Boolean isPaging) {
		this.isPaging = isPaging;
	}
	
	/**
	 * 获得所有查询条件
	 * @return
	 *         查询条件list
	 */
	public List<Filter> getFilters() {
        return filters;
    }

    /**
     * 设置查询条件
     * @param filters
     *          查询条件list
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Pageable other = (Pageable) obj;
		return new EqualsBuilder().append(getSearchProperty(), other.getSearchProperty()).append(getSearchValue(), other.getSearchValue()).append(getOrderProperty(), other.getOrderProperty()).append(getOrderDirection(), other.getOrderDirection()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getSearchProperty()).append(getSearchValue()).append(getOrderProperty()).append(getOrderDirection()).toHashCode();
	}

}