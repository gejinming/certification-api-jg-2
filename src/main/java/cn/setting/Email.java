package cn.setting;

import com.gnet.plugin.setting.settingEnum.ISettingEnum;

public interface Email {

	public static final String REGION = "email";
	
	public enum EMAIL_ENUM implements ISettingEnum {

		EMAIL_ADDRESS("email_address", "gwxuetang@gnetedu.com"),			// 邮件发送人地址
		EMAIL_USER("email_user", "gwxuetang@gnetedu.com"),					// 邮件用户
		EMAIL_PASSWD("email_passwd", "Yj123456"),							// 邮件密码
		ADMIN_EMAIL_ADDRESS("admin_email_address", "xuqiang@gnetedu.com");	// 管理者邮箱

		private String value;
		private Object defaultValue;
		
		private EMAIL_ENUM(String value, Object defaultValue) {
			this.value = value;
			this.defaultValue = defaultValue;
		}
		
		@Override
		public String getValue() {
			return value;
		}

		@Override
        public Object getDefaultValue() {
            return defaultValue;
        }
		
	}
}
