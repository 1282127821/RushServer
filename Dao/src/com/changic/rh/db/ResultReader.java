package com.changic.rh.db;

import java.sql.ResultSet;

/**
 * 数据库查询结果集reader  建议使用时new一个新的实例，避免并发问题
 * @param <T> 查询返回类型
 */
public abstract class ResultReader<T>
{
	private T data;

	public final void readRs(ResultSet rs) throws Exception
	{
		data = read(rs);
	}

	public abstract T read(ResultSet rs) throws Exception;

	public T getResult()
	{
		return data;
	}
}
