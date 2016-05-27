package com.chenyg.wporter.a.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chenyg.wporter.Config;
import com.chenyg.wporter.InitException;
import com.chenyg.wporter.WPMain;
import com.chenyg.wporter.WPorterSearcher;
import com.chenyg.wporter.WPorterType;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.LogUtil;
import com.chenyg.wporter.simple.WebParamDealt;
import com.chenyg.wporter.util.FileTool;

public class WPMainServlet extends HttpServlet
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Config config;
    private ClassLoader classLoader;
    private WPMain wpMain;

    public void set(String config, ClassLoader classLoader)
    {
        this.config = Config.toConfig(config);
        this.classLoader = classLoader;
    }

    public void set(Config config, ClassLoader classLoader)
    {
        this.config = config;
        this.classLoader = classLoader;
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doRequest(req, resp, RequestMethod.TARCE);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doRequest(req, resp, RequestMethod.PUT);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doRequest(req, resp, RequestMethod.HEAD);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doRequest(req, resp, RequestMethod.DELETE);
    }

    @Override
    protected void doOptions(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        doRequest(request, response, RequestMethod.OPTIONS);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doRequest(request, response, RequestMethod.POST);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doRequest(request, response, RequestMethod.GET);
    }

    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param method
     * @throws IOException
     */
    private void doRequest(HttpServletRequest request, HttpServletResponse response,
            RequestMethod method) throws IOException
    {
        wpMain.doRequest(new WPServletRequest(request, wpMain), new WPServletResponse(response),
                method);
    }

    public WPMain getWpMain()
    {
        return wpMain;
    }

    private void newWpMain()
    {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
        {
            contextPath += "/";
        }
        wpMain = new WPMain(getClass().getName(), WPorterType.SERVLET, contextPath);
    }

    public void init(Config config, WPorterSearcher wPorterSearcher)
    {

        try
        {
            newWpMain();
            wpMain.init(new WebParamDealt(), config, wPorterSearcher, null);
        } catch (InitException e)
        {
            e.printStackTrace();
        }

    }


    public void init(Config config, ClassLoader classLoader)
    {

        try
        {
            newWpMain();
            wpMain.setClassLoader(classLoader);
            wpMain.init(new WebParamDealt(), config, null, false, null);
        } catch (InitException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 会从WEB-INF/wpconfig/WebPorter.json读取配置信息
     */
    public void init(ClassLoader classLoader) throws InitException
    {
        String config;
        try
        {
            String dir = getWebInfDir();
            LogUtil.printPosLn(dir);
            config = FileTool.getString(new FileInputStream(dir + "wpconfig"
                    + File.separator
                    + "WebPorter.json"));
            init(config, classLoader);
        } catch (Exception e)
        {
            throw new InitException(e);
        }

    }

    public void init(String config, ClassLoader classLoader) throws InitException
    {
        newWpMain();
        Config _config = Config.toConfig(config);
        init(_config, classLoader);
    }


    /**
     * <pre>
     * 1.会从WEB-INF/wpconfig/WebPorter.json读取配置信息
     * 2.若存在wpconfig参数(getInitParameter)，则从该文件夹下读取WebPorter.json配置信息.
     * </pre>
     */

    @Override
    public void init() throws ServletException
    {
        super.init();
        if (config != null)
        {
            init(config, classLoader);
            config = null;
            classLoader = null;
        } else
        {
            String wpconfig = getInitParameter("wpconfig");
            config = null;
            if (wpconfig != null)
            {
                try
                {
                    String config = FileTool.getString(new FileInputStream(wpconfig + File.separator
                            + "WebPorter.json"));
                    init(config, Thread.currentThread().getContextClassLoader());
                    classLoader = null;
                } catch (Exception e)
                {

                    throw new ServletException(e);

                }
            } else
            {
                try
                {
                    init(Thread.currentThread().getContextClassLoader());
                } catch (InitException e)
                {
                    throw new ServletException(e);
                }
            }
        }
    }

    /**
     * 获取WEB-INF目录的路径
     *
     * @return
     */
    public static String getWebInfDir()
    {
        // file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/
        String path = Thread.currentThread().getContextClassLoader().getResource("").getFile();// .toString();
        path = path.replace('/', File.separatorChar);
        path = path.replace("file:", ""); // 去掉file:
        path = path.replace("classes" + File.separator, ""); // 去掉class\
        // if (path.startsWith(File.separator) && path.indexOf(':') != -1)
        // {
        // return path.substring(1);
        // }
        // else
        // {
        // return path;
        // }
        return path;

    }

    /**
     * 获取Context所在的路径,以File.separatorChar结尾
     *
     * @return
     */
    public static String getContextDir()
    {
        // WEB-INF/
        String path = WPMainServlet.getWebInfDir();
        path = path.substring(0, path.length() - 8);
        return path;
    }

    @Override
    public void destroy()
    {
        wpMain.destroy();
        super.destroy();
    }

}
