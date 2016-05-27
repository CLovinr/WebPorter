package com.chenyg.wporter.a.app;

import java.util.ArrayList;
import java.util.Set;

import com.chenyg.wporter.Config;
import com.chenyg.wporter.InitException;
import com.chenyg.wporter.WPMain;
import com.chenyg.wporter.WPRequest;
import com.chenyg.wporter.WPorterType;
import com.chenyg.wporter.WebPorter;
import com.chenyg.wporter.base.RequestMethod;

public class AppPorterMain
{
    private WPMain wpMain;
    private boolean started;
    private AppPorterSender appPorterSender;

    public AppPorterMain(String contextPath)
    {
        initSender();
        wpMain = new WPMain(getClass().getName(), WPorterType.APP, contextPath);
    }


    public WPMain getWpMain()
    {
        return wpMain;
    }

    private void initSender()
    {
        appPorterSender = new AppPorterSender()
        {

            @Override
            public void send(AppCommunication communication, AppPorterListener listener) throws AppPorterException
            {
                WPRequest request = getRequest(communication);
                RequestMethod method = communication.getReqMethod();
                if (method == null)
                {
                    throw new AppPorterException("the request method is null");
                }
                AppResponse response = getResponse();

                try
                {
                    wpMain.doRequest(request, response, method);
                    if (listener != null)
                    {
                        listener.onResponse(response.getAppReciever());
                    }
                } catch (Exception e)
                {
                    if (listener != null)
                    {
                        AppReciever appReciever = response.getAppReciever();
                        appReciever.list = new ArrayList<Object>();
                        appReciever.list.add(e);
                        appReciever.errorCode = -1;
                        appReciever.statusCode = -1;
                        listener.onResponse(appReciever);
                    } else
                    {
                        e.printStackTrace();
                    }
                }
            }

            private AppResponse getResponse()
            {
                AppResponse response = null;
                response = new AppResponse();
                return response;
            }

            private WPRequest getRequest(AppCommunication communication)
            {
                AppRequest request = null;
                request = new AppRequest(communication, AppPorterMain.this);
                return request;
            }
        };
    }

    public AppPorterSender getAppPorterSender()
    {
        return appPorterSender;
    }

//    public String getContextPath()
//    {
//        return contextPath;
//    }

    /**
     * @param config
     * @param set
     * @param isSys
     * @param classLoader
     * @throws InitException
     */
    public void start(Config config, Set<WebPorter> set,
            boolean isSys, ClassLoader classLoader) throws InitException
    {
        if (!started)
        {
            wpMain.setClassLoader(classLoader);
            wpMain.init(new AppParamDealt(), StateListenerImpl.addSelf(config), set, isSys, null);
            started = true;
        }
    }


    /**
     * 是否已经开始运行。
     *
     * @return
     */
    public boolean isStarted()
    {
        return started;
    }

    public void stop()
    {
        if (started)
        {
            wpMain.destroy();

            started = false;
        }

    }
}
