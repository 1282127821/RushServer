package com.star.light.db;

/**
 * 每次有增加数据库操作都需要在这里定义一个常量，并且在alterdb脚本里面设置t_u_dbversion的版本为对应的版本好，主要是拥有起服验证是否有更新数据库脚本，
 * 必须保证COUNT是最后一个，另外需要注意同一个版本内不同人员有增加数据库操作只需要定义一个枚举即可，注释加上。
 */
public enum DBVersion {
	/**
	 * 默认的数据库版本，
	 */
	NONE,
	COUNT,
}
