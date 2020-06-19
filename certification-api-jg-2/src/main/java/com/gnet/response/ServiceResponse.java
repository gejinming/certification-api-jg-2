package com.gnet.response;


/**
 * Service 请求返回结果体
 * @author xuq
 * @date 2014年11月27日
 */
public class ServiceResponse {

    /**
     * Service 请求返回结果类型
     * @author xuq
     * SUCCESS: 成功
     * ERROR: 失败
     */
    public static enum RESPONSE_TYPE {
        SUCCESS, ERROR
    }
    
    /**
     * 返回类型
     */
    private RESPONSE_TYPE responseType;
    /**
     * 返回内容
     */
    private Object content;
    
    /**
     * 构造方法
     * @param responseType
     *          返回类型
     * @param content
     *          返回内容
     */
    public ServiceResponse(RESPONSE_TYPE responseType, Object content) {
        this.responseType = responseType;
        this.content = content;
    }
    
    /**
     * 返回一个成功的消息体
     * @param content
     *          返回内容
     * @return
     *          消息体
     */
    public static ServiceResponse succ(Object content) {
        return new ServiceResponse(RESPONSE_TYPE.SUCCESS, content);
    }
    
    /**
     * 返回一个错误的消息体
     * @param content
     *          返回内容
     * @return
     *          消息体
     */
    public static ServiceResponse error(Object content) {
        return new ServiceResponse(RESPONSE_TYPE.ERROR, content);
    }
    
    /**
     * 获得消息类型
     * @return
     *          消息类型
     */
    public RESPONSE_TYPE getResponseType() {
        return responseType;
    }
    /**
     * 设置消息类型
     * @param responseType
     *          消息类型
     */
    public void setResponseType(RESPONSE_TYPE responseType) {
        this.responseType = responseType;
    }
    /**
     * 获得消息内容
     * @return
     *          消息内容
     */
    @SuppressWarnings("unchecked")
	public <T> T getContent() {
        return (T) content;
    }
    /**
     * 设置消息内容
     * @param content
     *          消息内容
     */
    public void setContent(Object content) {
        this.content = content;
    }
    
    /**
     * 是否为成功的消息
     * @return
     *          是否成功
     */
    public boolean isSucc() {
        return this.responseType.equals(RESPONSE_TYPE.SUCCESS);
    }
    
}
