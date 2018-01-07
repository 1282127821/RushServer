package com.webserver;

/**
 * 处理指令接口
 */
public interface IHandlerCmd
{
	/**
	 * 处理命令接口
	 */
	String execute(String jsonString);
}
