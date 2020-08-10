package com.gnet;

/**
 * 公共参数
 * 
 * cwledit
 */
public final class Constant {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };
	
	public static final String SPARK_XML_PATH = "/spark.xml";

	public static final String SPARK_PROPERTIES_PATH = "/spark.properties";
	
	// 用户session key
    public static final String USER_SESSION = "cwl_user_session";
    
    // 管理员账户名
    public static final String USER_ADMIN = "admin";
    
    // 是否系统内置字段
    public static final String IS_SYSTEM = "is_system";

    //文件导出路径
	public static final String EXPORT_FILE_PATH = "export";

	//word模版路径
	public static final String WORD_TEMPLATE_PATH = "word";
    
    
    //--------------Model常量---------------------
    public static final boolean LOCKED = true;		//锁定
	public static final boolean UNLOCKED = false;	//未锁
    
	
    //--------------User常量-----------------------
    public static final boolean USER_ENABLED = true; 	// 启用
	public static final boolean USER_NOTENABLED = false;	//不启用
	
	public static final int USER_DEFAULT_FAIL_COUNT = 0;//默认账号登陆失败次数
    
	
	//--------------Role常量------------------------
	public static final boolean SYSTEM = true;		//内置
	public static final boolean NOTSYSTEM = false;	//非内置
	
	//---------------Office常量---------------------
	public static final int OFFICE_TYPE_COMPANY = 0;		//公司
	public static final int OFFICE_TYPE_DEPARTMENT = 1;		//部门

	//-------------学时常量 ---------------------
	public static final String THEORY_HOURS = "理论学时";
	public static final String EXPERIMENT_HOURS = "实验学时";
	public static final String PRACTICE_HOURS = "实践学时";
    public static final String OPERATE_COMPUTER_HOURS = "上机学时";
	public static final String DICUSS_HOURS = "研讨学时";
	public static final String EXERCISES_HOURS = "习题学时";

	//-----------------性别-----------------------
	public static final Integer SEX_MAN = 0;
	public static final Integer SEX_WOMEN = 1;

	/**
	 * 不可实例化
	 */
	private Constant() {}

}