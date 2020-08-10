package com.gnet.plugin.push;

import java.io.Serializable;

import com.gnet.plugin.push.impl.email.Email;
import com.gnet.plugin.push.impl.jpush.JPush;

/**
 * 推送对象抽象Model<br/>
 * <p>
 * 邮件实现Model {@link Email }<br/>
 * JPush实现Model {@link JPush }<br/>
 * </p>
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public abstract class PushModel implements Serializable {

	private static final long serialVersionUID = -9222840186050908939L;

}
