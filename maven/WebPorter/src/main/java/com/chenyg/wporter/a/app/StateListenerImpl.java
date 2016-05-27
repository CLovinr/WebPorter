package com.chenyg.wporter.a.app;

import com.chenyg.wporter.Config;
import com.chenyg.wporter.InitParamSource;
import com.chenyg.wporter.ParamSourceHandleManager;
import com.chenyg.wporter.StateListener;
import com.chenyg.wporter.base.RequestMethod;
import com.chenyg.wporter.log.LogUtil;
import com.chenyg.wporter.log.WPLog;
import com.chenyg.wporter.simple.QueryParamSourceHandle;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by 宇宙之灵 on 2016/5/18.
 */
class StateListenerImpl implements StateListener
{

    static Config addSelf(final Config config)
    {
        final InitParamSource sporter = config.getPorterParamGeter();
        final InitParamSource porter = new InitParamSource()
        {
            @Override
            public String getInitParameter(String name)
            {
                if (Config.NAME_STATE_LISTENERS.equals(name))
                {
                    String porterPaths = sporter.getInitParameter(name);
                    JSONArray jsonArray = null;

                    try
                    {
                        if (porterPaths != null)
                        {
                            jsonArray = new JSONArray(porterPaths);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        if (jsonArray == null)
                        {
                            jsonArray = new JSONArray();
                        }
                    }

                    jsonArray.put(StateListenerImpl.class.getName());

                    return jsonArray.toString();

                } else
                {
                    return sporter.getInitParameter(name);
                }
            }
        };

        Config config1 = new Config()
        {
            @Override
            public InitParamSource getPorterParamGeter()
            {
                return porter;
            }

            @Override
            public InitParamSource getUserParamGeter()
            {
                return config.getUserParamGeter();
            }
        };

        return config1;
    }

    @Override
    public void beforeInit(InitParamSource initParamSource, WPLog<?> wpLog)
    {

    }

    @Override
    public void afterInit(InitParamSource initParamSource, ParamSourceHandleManager paramSourceHandleManager,
            WPLog<?> wpLog)
    {
        LogUtil.printErrPosLn("add QueryParamSourceHandle");
        paramSourceHandleManager.addParamHandles(new QueryParamSourceHandle(), RequestMethod.values());
    }

    @Override
    public void onDestroy(WPLog<?> wpLog)
    {

    }
}
