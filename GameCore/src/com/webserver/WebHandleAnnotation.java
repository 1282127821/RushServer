package com.webserver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WebServer相应接口注释
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebHandleAnnotation
{
	public String cmdName();

	public String desc();
}
