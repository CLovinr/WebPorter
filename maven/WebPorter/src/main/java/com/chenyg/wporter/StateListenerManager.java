package com.chenyg.wporter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.chenyg.wporter.log.WPLog;
import org.json.JSONArray;

import com.chenyg.wporter.util.Foreach;
import com.chenyg.wporter.util.PackageUtil;

/**
 * 状态监听管理s
 */
class StateListenerManager implements StateListener
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<StateListener> list;

    public StateListenerManager(JSONArray listeners,
            WPLog<?> wpLog, ClassLoader classLoader)
    {
        list = new ArrayList<StateListener>(listeners.length());
        for (int i = 0; i < listeners.length(); i++)
        {
            try
            {

                Class<?> c = PackageUtil.newClass(listeners.getString(i), classLoader);

                Constructor constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);

                StateListener stateListener = (StateListener) constructor.newInstance();
                list.add(stateListener);
            } catch (Exception e)
            {
                wpLog.error(e.getMessage(), e);
            }
        }

    }

    private void forEach(Foreach<StateListener> foreach)
    {
        for (int i = 0; i < list.size(); i++)
        {
            StateListener stateListener = list.get(i);
            if (!foreach.each(stateListener))
            {
                break;
            }
        }

    }

    @Override
    public void beforeInit(final InitParamSource initParamSource, final WPLog<?> wpLog)
    {
        forEach(new Foreach<StateListener>()
        {

            @Override
            public boolean each(StateListener t)
            {
                t.beforeInit(initParamSource, wpLog);
                return true;
            }
        });
    }

    @Override
    public void afterInit(final InitParamSource initParamSource,
            final ParamSourceHandleManager paramSourceHandleManager,
            final WPLog<?> wpLog)
    {
        forEach(new Foreach<StateListener>()
        {

            @Override
            public boolean each(StateListener t)
            {
                t.afterInit(initParamSource, paramSourceHandleManager, wpLog);
                return true;
            }
        });
    }

    /**
     * {@linkplain WPMain#destroy()}时调用.
     */
    @Override
    public void onDestroy(final WPLog<?> wpLog)
    {
        forEach(new Foreach<StateListener>()
        {

            @Override
            public boolean each(StateListener t)
            {
                t.onDestroy(wpLog);
                return true;
            }
        });

    }
}
