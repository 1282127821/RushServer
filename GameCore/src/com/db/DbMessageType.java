package com.db;

public interface DbMessageType
{
	String Format3 = "[DataBase] 提示消息:%s; 错误描述:%s; 当前状态:%s";

	String Pool_Info = "当前连接池信息";
	String Pool_Del = "从连接也中删除一个无效连接";
	String Pool_Release = "无法撤销下列JDBC驱动程序的注册";

	String Sql_Error = "当前Sql语句出错";
	String Sql_Batch = "批处理Sql语句出错";
	String SQL_NoExec = "没有可用连接池，未执行Sql命令";
}