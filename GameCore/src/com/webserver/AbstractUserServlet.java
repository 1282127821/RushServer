package com.webserver;

/**
 * 玩家相关的Servlet抽象类
 * 
 * @author jiayi.wei
 */
public abstract class AbstractUserServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 1165405088429054519L;

    /**
     * 玩家相关的访问不需要限制访问IP。
     */
    @Override
    protected boolean checkRequestIP(String ip)
    {
        return true;
    }
}
