package com.gnet.plugin.push.impl.jpush;

import java.util.Map;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.push.PushModel;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * JPush对象
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public class JPush extends PushModel {
	
	private static final long serialVersionUID = -6500870726354321366L;

	private static final String IOS_SOUND_DEFAULT = "default";
	private static final Boolean apnsProduction = ConfigUtils.getBoolean("global", "isDev") ? false : true;;
	
	private PushPayload pushPayload;
	
	/**
	 * 推送全平台
	 * 
	 * @param alert 推送消息
	 * @param badge 推送消息显示
	 */
	public JPush(String alert, Integer badge) {
		this(alert, badge, null);
	}
	
	/**
	 * 推送全平台
	 * 
	 * @param alert 推送消息
	 * @param badge 推送消息显示
	 * @parma extras 额外的信息
	 */
	public JPush(String alert, Integer badge, Map<String, String> extras) {
		this(PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(Notification.newBuilder()
						// IOS平台
						.addPlatformNotification(IosNotification.newBuilder()
								.setAlert(alert)
								.setBadge(badge)
								.setSound(IOS_SOUND_DEFAULT)
								.addExtras(extras)
								.build())
						// Android平台
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(alert)
								.addExtras(extras)
								.build())
						.build())
				.setOptions(Options.newBuilder()
						.setApnsProduction(apnsProduction)
						.build())
				.build());
	}
	
	/**
	 * 推送全平台(自定义消息)
	 * 
	 * @param alert 推送消息
	 * @param badge 推送消息显示
	 * @param msgContent 消息内容
	 * @parma extras 额外的信息
	 */
	public JPush(String alert, Integer badge, String msgContent, Map<String, String> extras) {
		this(PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(Notification.newBuilder()
						// IOS平台
						.addPlatformNotification(IosNotification.newBuilder()
								.setAlert(alert)
								.setBadge(badge)
								.setSound(IOS_SOUND_DEFAULT)
								.addExtras(extras)
								.build())
						// Android平台
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(alert)
								.addExtras(extras)
								.build())
						.build())
				.setMessage(Message.content(msgContent))
				.setOptions(Options.newBuilder()
						.setApnsProduction(apnsProduction)
						.build())
				.build());
	}
	
	/**
	 * 推送一个或多个设备
	 * 
	 * @param alert 推送消息
	 * @param badge 推送消息显示
	 * @param msgContent 消息内容
	 * @param extras 额外的信息
	 * @param registerIds 设备编号列表
	 */
	public JPush(String alert, Integer badge, String msgContent, Map<String, String> extras, String... registerIds) {
		this(PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.registrationId(registerIds))
				.setNotification(Notification.newBuilder()
						// IOS平台
						.addPlatformNotification(IosNotification.newBuilder()
								.setAlert(alert)
								.setBadge(badge)
								.setSound(IOS_SOUND_DEFAULT)
								.addExtras(extras)
								.build())
						// Android平台
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(alert)
								.addExtras(extras)
								.build())
						.build())
				.setMessage(Message.content(msgContent))
				.setOptions(Options.newBuilder()
						.setApnsProduction(apnsProduction)
						.build())
				.build());
	}
	
	/**
	 * 自行构建推送
	 * 
	 * @param pushPayload 推送规则
	 */
	public JPush(PushPayload pushPayload) {
		this.pushPayload = pushPayload;
	}

	/**
	 * 获得构建规则
	 * 
	 * @return 构建规则
	 */
	public PushPayload getPushPayload() {
		return pushPayload;
	}
	
	@Override
	public String toString() {
		return pushPayload.toString();
	}

}
