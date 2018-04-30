package com.andy.gomoku.utils;

public class GoConstant {

	public static final String USER_SESSION_KEY = "USER_SESSION_KEY_";
	
	public static final int SUCC_CODE = 0; // 成功
	
	public static final int USER_STATUS_0 = 0; // 未准备
	public static final int USER_STATUS_1 = 1; // 准备
	public static final int USER_STATUS_2 = 2; // 匹配中
	
	
	public static final long ROBOT_ID_START = 10000000; // 机器人起始id
	
	public static final int STEP_TIMEOUT = 60*1000; // 单步超时时间,1分钟
	public static final int QUANPAN_TIMEOUT = 600*1000; // 全盘超时时间,10分钟
	
}
