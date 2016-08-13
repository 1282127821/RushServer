package com.star.light.game;

public interface DbMessageType {

	String Format1 = "[DataBase] 提示消息:%s;";
	String Format2 = "[DataBase] 提示消息:%s; 错误描述:%s";
	String Format3 = "[DataBase] 提示消息:%s; 错误描述:%s; 当前状态:%s";

	String Config_Path = "不能读取属性文件.请确保db.properties在CLASSPATH指定的路径中";
	String Config_MaxConn = "配置文件中最大连接数配置错误";

	String Connect_Error = "连接数据库出错！";
	String Connect_None = "没有可用的数据库连接池";
	String Connect_Close = "关闭数据库出错";
	String Connect_TimeOut = "连接数据库超时";
	String Connect_Create = "创建连接数据库出错！请检查配置文件";

	String Pool_Info = "当前连接池信息";
	String Pool_Del = "从连接也中删除一个无效连接";
	String Pool_Release = "无法撤销下列JDBC驱动程序的注册";

	String Sql_Error = "当前Sql语句出错";
	String Sql_Batch = "批处理Sql语句出错";
	String SQL_NoExec = "没有可用连接池，未执行Sql命令";
	String Procude_Error = "执行存储过程出错";
	String Procude_Para = "当前存储过程仅支持输入参数，如需使用输出参数请用runProcedureAll。";
}
