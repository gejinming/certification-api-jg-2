package com.gnet.api.sign;

import java.util.List;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 返回值
 * @author: Gjm
 * @create: 2020-10-14 15:37
 **/

public class Result {
    private boolean flag;
    private Integer code;
    private String message;
    private List<Long> data;
    private Long count;
    private Map<Object,Object> dataMap;


    public Map<Object, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Object, Object> dataMap) {
        this.dataMap = dataMap;
    }

    private Result(boolean flag, Integer code, String message) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    private Result(boolean flag, Integer code, String message, List<Long> data) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    private Result(boolean flag, Integer code,String message, Map<Object,Object> dataMap) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.dataMap = dataMap;
    }

    private Result(boolean flag, Integer code, String message, List<Long> data, Long count) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
        this.count = count;
    }



    public boolean getFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<Long> getData() {
        return data;
    }
    public void setData(List<Long> data) {
        this.data = data;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }


    /**
     * 返回成功消息
     * @return Result
     */
    public static Result ok() {
        return new Result(true, 200, "成功");
    }



    /**
     * 返回成功消息
     * @return Result
     */
    public static Result ok(List<Long> data) {
        return new Result(true, 200, "成功", data);
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static Result ok(Map<Object,Object> dataMap) {
        return new Result(true, 200, "成功", dataMap);
    }
    /**
     * 返回成功消息
     * @return Result
     */
    public static Result ok(String message, List<Long> data) {
        return new Result(true, 200, "成功", data);
    }

    /**
     * 返回成功消息
     * @return Result
     */
    public static Result ok(List<Long> data, Long count) {
        return new Result(true, 200, "成功", data, count);
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static Result error() {
        return new Result(false, 500, "失败");
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static Result error(String message) {
        return new Result(false, 500, message);
    }

    /**
     * 返回失败消息
     * @return Result
     */
    public static Result error(Integer code, String message) {
        return new Result(false, code, message);
    }


}
